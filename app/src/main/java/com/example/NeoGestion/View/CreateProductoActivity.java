package com.example.NeoGestion.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.NeoGestion.Control.FireBase;
import com.example.NeoGestion.Model.Producto;
import com.example.NeoGestion.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateProductoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean editar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_producto);
        FireBase firebaseHelper = new FireBase();

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        editar = intent.getBooleanExtra("editar", false);

        TextInputEditText nombreProducto = findViewById(R.id.et_nombre_producto);
        TextInputEditText idProducto = findViewById(R.id.et_id_producto);
        TextInputEditText referenciaProducto = findViewById(R.id.et_referencia_producto);
        TextInputEditText precioProducto = findViewById(R.id.et_precio_producto);
        TextInputEditText cantidadProducto = findViewById(R.id.et_cantidad_producto);
        TextInputLayout categoriaProducto = findViewById(R.id.edt_categoria_producto);
        TextInputEditText stockMin = findViewById(R.id.et_stock_min);
        TextInputEditText stockMax = findViewById(R.id.et_stock_max);
        AutoCompleteTextView spinner = findViewById(R.id.spinner_categoria);
        Button btnCrear = findViewById(R.id.btn_crear_producto);


        if (editar) {
            Producto productoEditar = intent.getParcelableExtra("objeto");
            nombreProducto.setText(productoEditar.getNombre());
            idProducto.setText(productoEditar.getId());
            referenciaProducto.setText(productoEditar.getReferencia());
            precioProducto.setText(String.valueOf(productoEditar.getPrecio()));
            spinner.setText(productoEditar.getCategoria(), false);
            cantidadProducto.setText(String.valueOf(productoEditar.getCantidad()));
            stockMax.setText(String.valueOf(productoEditar.getStockMax()));
            stockMin.setText(String.valueOf(productoEditar.getStockMin()));

            btnCrear.setText("Editar Producto");
            idProducto.setFocusable(false);
            idProducto.setClickable(false);
        } else  {
            btnCrear.setText("Crear Producto");
            idProducto.setFocusable(true);
            idProducto.setClickable(true);

        }

        String[] categorias = getResources().getStringArray(R.array.tipo_categoria);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categorias);
        spinner.setAdapter(adapter);

        btnCrear.setOnClickListener(v -> {
            String nombre = nombreProducto.getText().toString().trim();
            String id = idProducto.getText().toString().trim();
            String referencia = referenciaProducto.getText().toString().trim();
            String precioStr = precioProducto.getText().toString().trim();
            String cantidadStr = cantidadProducto.getText().toString().trim();
            String categoria = spinner.getText().toString().trim();
            String stockMinStr = stockMin.getText().toString().trim();
            String stockMaxStr = stockMax.getText().toString().trim();

            if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(referencia) ||
                    TextUtils.isEmpty(precioStr) || TextUtils.isEmpty(cantidadStr) ||
                    TextUtils.isEmpty(stockMinStr) || TextUtils.isEmpty(stockMaxStr)) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            double precio = Double.parseDouble(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);
            double min = Double.parseDouble(stockMinStr);
            double max = Double.parseDouble(stockMaxStr);

            if (editar) {
                firebaseHelper.updateProducto(id, nombre, referencia, precio, categoria , cantidad, min, max, this::showSuccessDialog,
                        this::showErrorDialog);

            } else {
                String emailUsuario = mAuth.getCurrentUser().getEmail();if (emailUsuario != null) {
                    Map<String, Object> producto = new HashMap<>();
                    producto.put("nombre", nombre);
                    producto.put("id", id);
                    producto.put("referencia", referencia);
                    producto.put("precio", precio);
                    producto.put("categoria", categoria);
                    producto.put("cantidad", cantidad);
                    producto.put("stockMin", min);
                    producto.put("stockMax", max);

                    db.collection("Users").document(emailUsuario)
                            .collection("productos").add(producto)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Producto añadido correctamente", Toast.LENGTH_LONG).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error al añadir el producto", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void showSuccessDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Aceptar", (dialog, which) -> finish())
                .show();
    }
}
