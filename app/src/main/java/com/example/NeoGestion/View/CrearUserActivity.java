package com.example.NeoGestion.View;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.NeoGestion.Control.FireBase;
import com.example.NeoGestion.Model.Usuario;
import com.example.NeoGestion.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class CrearUserActivity extends AppCompatActivity {
    int idNotificacion = 0;
    private final static String CHANNEL_ID = "NOTIFICATION";
    private Button btcrearusuario;
    private ImageView imagen;
    private TextInputLayout edtcrearusu, edtcrearcorreo, edtnombreapellidos, edtcreartlf, tipo;
    private AutoCompleteTextView spinnertipo;
    private TextView text, lb_fecha;
    private boolean editar = false;
    private SwitchMaterial stw_baja;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        FireBase firebaseHelper = new FireBase();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        btcrearusuario = findViewById(R.id.btcrea_rusuario);
        spinnertipo = findViewById(R.id.spinner_tipo);
        edtcrearusu = findViewById(R.id.edtcrear_usu);
        edtcrearcorreo = findViewById(R.id.edtcrear_correo);
        edtcreartlf = findViewById(R.id.edtcrear_tlf);
        edtnombreapellidos = findViewById(R.id.edtnombre_apellidos);
        imagen = findViewById(R.id.imagen_perfil);
        text = findViewById(R.id.fecha);
        lb_fecha = findViewById(R.id.lb_fecha);
        tipo = findViewById(R.id.tipo);
        stw_baja = findViewById(R.id.swt_baja);

        stw_baja.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        editar = intent.getBooleanExtra("editar", false);

        if (editar) {
            Usuario usuarioeditar = intent.getParcelableExtra("objeto");
            edtcrearusu.getEditText().setText(usuarioeditar.getUsuario());
            edtcrearcorreo.getEditText().setText(usuarioeditar.getCorreo());
            edtnombreapellidos.getEditText().setText(usuarioeditar.getNombreapellidos());
            edtcreartlf.getEditText().setText(usuarioeditar.getTelefono());
            imagen.setImageBitmap(usuarioeditar.getFoto());
            text.setText(usuarioeditar.getFechaNacimiento());
            spinnertipo.setText(usuarioeditar.getTipousuario(), false);
            stw_baja.setVisibility(View.VISIBLE);
            edtcrearcorreo.setFocusable(false);
            edtcrearcorreo.setClickable(false);
            if (!usuarioeditar.getFechaBaja().isEmpty()) {
                stw_baja.setChecked(true);
            }

        } else {
            edtcrearcorreo.setFocusable(true);
            edtcrearcorreo.setClickable(true);
            btcrearusuario.setText(getString(R.string.crear_user));
            stw_baja.setVisibility(View.GONE);
        }

        String[] tipos = getResources().getStringArray(R.array.tipo_usuario_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tipos);
        spinnertipo.setAdapter(adapter);

        text.setOnClickListener(view -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.selecciona_fech)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(selection));
                text.setText(MessageFormat.format("{0}", date));
            });
            datePicker.show(getSupportFragmentManager(), "tag");
        });
        imagen.setOnClickListener(view -> {
            requestPermissions();
        });

        btcrearusuario.setOnClickListener(view -> {
            if (editar) {
                String fecha_baja = "";
                if (stw_baja.isChecked()) {
                    LocalDate now = LocalDate.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    fecha_baja = now.format(format);
                }
                if (!stw_baja.isChecked()) {
                    fecha_baja = "";
                }
                String nombreUsuario = edtcrearusu.getEditText().getText().toString().trim();
                String correo = edtcrearcorreo.getEditText().getText().toString().trim();
                edtcrearcorreo.setFocusable(false);
                edtcrearcorreo.setClickable(false);
                String nombreApellidos = edtnombreapellidos.getEditText().getText().toString().trim();
                String telefono = edtcreartlf.getEditText().getText().toString().trim();
                String tipo = spinnertipo.getText().toString().trim();
                Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
                firebaseHelper.updateUser(correo, nombreUsuario, correo , nombreApellidos, telefono, tipo, text.getText().toString(), bitmap, fecha_baja , message -> showSuccessDialog(message),
                        message -> showErrorDialog(message));
            } else {
                edtcrearcorreo.setFocusable(true);
                edtcrearcorreo.setClickable(true);
                String nombreUsuario = edtcrearusu.getEditText().getText().toString().trim();
                String correo = edtcrearcorreo.getEditText().getText().toString().trim();
                String nombreApellidos = edtnombreapellidos.getEditText().getText().toString().trim();
                String telefono = edtcreartlf.getEditText().getText().toString().trim();
                String tipo = spinnertipo.getText().toString().trim();
                Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
                firebaseHelper.createUser(nombreUsuario, correo, nombreApellidos, telefono, tipo, text.getText().toString(), bitmap, message -> showSuccessDialog(message),
                        message -> showErrorDialog(message));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imagen.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imagen.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                imagen.performClick();
            } else {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearUserActivity.this);
        builder.setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearUserActivity.this);
        builder.setMessage(message)
                .setPositiveButton("Aceptar", (dialog, which) -> finish())
                .show();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            startImagenChoose();
        }
    }
    private void startImagenChoose() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Seleccionar Imagen");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });
        startActivityForResult(chooserIntent, 1);
    }

}

