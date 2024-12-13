package com.example.NeoGestion.View;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.NeoGestion.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private Button btiniciar;
    private TextView btregistrar, bt_forgot;
    private TextInputLayout edtusuemail, edtcontraseña;
    private static final String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int currentPermissionIndex = 0;
    private final static String CHANNEL_ID = "NOTIFICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        createNotificationChannel();
        FirebaseApp.initializeApp(this);

        btiniciar = findViewById(R.id.bt_iniciar);
        bt_forgot = findViewById(R.id.bt_cambiarpass);
        btregistrar = findViewById(R.id.bt_registrar);
        edtusuemail = findViewById(R.id.edt_usuemail);
        edtcontraseña = findViewById(R.id.edt_contraseña);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        requestNextPermission();

        btregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                edtusuemail.getEditText().setText("");
                edtcontraseña.getEditText().setText("");
            }
        });

        bt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Restablecer Contraseña");
                final EditText inputCorreo = new EditText(LoginActivity.this);
                inputCorreo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                inputCorreo.setHint("Ingresa tu correo electrónico");

                builder.setView(inputCorreo);

                builder.setPositiveButton("Restablecer", (dialog, which) -> {
                    String correo = inputCorreo.getText().toString().trim();

                    if (correo.isEmpty()) {
                        Snackbar snackbar = Snackbar.make(view, "Por favor, ingresa tu correo.", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                        Snackbar snackbar = Snackbar.make(view, "Por favor, ingresa un correo electrónico válido.", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        return;
                    }

                    mAuth.fetchSignInMethodsForEmail(correo).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<String> signInMethods = task.getResult().getSignInMethods();

                            if (signInMethods != null && !signInMethods.isEmpty()) {
                                mAuth.sendPasswordResetEmail(correo)
                                        .addOnCompleteListener(resetTask -> {
                                            if (resetTask.isSuccessful()) {
                                                mostrarMensaje("Se ha enviado un correo de restablecimiento de contraseña. Revisa tu bandeja de entrada.");
                                            } else {
                                                String error = resetTask.getException() != null ? resetTask.getException().getMessage() : "Error desconocido";
                                                mostrarMensaje("Error: " + error);
                                            }
                                        });
                            } else {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                builder1.setMessage("Este correo no está registrado en Neogestion. Si deseas, puedes crear una cuenta.");
                                builder1.setPositiveButton("Aceptar", null);
                                builder1.setNegativeButton("Crear Cuenta", (dialog1, which1) -> {
                                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                            startActivity(intent);
                                });
                                builder1.show();
                            }
                        }
                    });
                });
                builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());


                builder.show();
            }
        });
        btiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= edtusuemail.getEditText().getText().toString().trim();
                String contraseña = edtcontraseña.getEditText().getText().toString().trim();


                if(!email.isEmpty() || !contraseña.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, contraseña)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        if (user.isEmailVerified()) {
                                            Intent intent = new Intent(LoginActivity.this, ActivityMain.class);
                                            intent.putExtra("usuario_actual", user.getDisplayName());
                                            startActivity(intent);

                                            edtusuemail.getEditText().setText("");
                                            edtcontraseña.getEditText().setText("");
                                        } else {
                                            mostrarMensaje("Por favor, verifica tu correo antes de iniciar sesión.");
                                            mAuth.signOut();
                                        }
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Contraseña o Email incorrecto");
                                builder.setPositiveButton("Aceptar", null);
                                builder.show();
                            });
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Credenciales incompletas");
                    builder.setPositiveButton("Aceptar", null);
                    builder.show();
                }
            }
        });
    }


    private void requestNextPermission() {
        if (currentPermissionIndex < PERMISSIONS.length) {
            String permission = PERMISSIONS[currentPermissionIndex];
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, currentPermissionIndex);
            } else {
                currentPermissionIndex++;
                requestNextPermission();
            }
        }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        } else {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                currentPermissionIndex++;
                requestNextPermission();
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My Notification Channel");
            channel.enableLights(true);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", null);
        builder.show();
    }


}


