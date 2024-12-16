package com.example.NeoGestion.View;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.NeoGestion.Control.FireBase;
import com.example.NeoGestion.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout edt_confirmpass, edt_correo, edt_crear_contra, edt_crear_usu;
    private Button btcrea_rusuario;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    FireBase firebaseHelper;
    private static final String CHANNEL_ID = "my_channel_id";
    private static final int NOTIFICATION_ID = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseHelper = new FireBase();
        edt_confirmpass = findViewById(R.id.edt_confirmpass);
        edt_correo = findViewById(R.id.edtcrear_correo);
        edt_crear_contra = findViewById(R.id.edtcrear_contra);
        edt_crear_usu = findViewById(R.id.edtcrear_usu);
        btcrea_rusuario = findViewById(R.id.btcrea_rusuario);
        createNotificationChannel();


        btcrea_rusuario.setOnClickListener(view -> {
            String correo = edt_correo.getEditText().getText().toString().trim();
            String usuario = edt_crear_usu.getEditText().getText().toString().trim();
            String contraseña = edt_crear_contra.getEditText().getText().toString().trim();
            String confirmarContraseña = edt_confirmpass.getEditText().getText().toString().trim();

            if (correo.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Por favor, introduce un correo válido.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!contraseña.equals(confirmarContraseña)) {
                Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (contraseña.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            registrarUsuario(correo, contraseña, usuario);
        });
    }

    private void registrarUsuario(String correo, String contraseña, String usuario) {
        auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            actualizarNombreDeUsuario(user, usuario);


                            user.sendEmailVerification()
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            Toast.makeText(this, "Correo de verificación enviado. Verifícalo antes de iniciar sesión.", Toast.LENGTH_LONG).show();
                                            showVerificationNotification();
                                            auth.signOut();
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Error al enviar el correo de verificación.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {

                        String error = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                        Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void actualizarNombreDeUsuario(FirebaseUser user, String nombreDeUsuario) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nombreDeUsuario)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        crearUsuarioEnFirestore(user.getUid(), user.getEmail(), nombreDeUsuario);
                    } else {
                        Toast.makeText(this, "Error al actualizar el perfil de usuario.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void crearUsuarioEnFirestore(String uid, String email, String username) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", username);
        userData.put("createdAt", System.currentTimeMillis());
        userData.put("isVerified", true);

        firestore.collection("users")
                .document(uid)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Usuario creado.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar el usuario en Firestore.", Toast.LENGTH_SHORT).show();
                });
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showVerificationNotification() {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Bienvenido!")
                    .setContentText("Bienvenido a NeoGestion " + auth.getCurrentUser()+"! Por favor, verifica tu correo para iniciar sesión.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}

