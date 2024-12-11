package com.example.NeoGestion.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.NeoGestion.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateProductoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_producto);

        // Referencias a Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Referencias a los campos de entrada
        TextInputEditText nombreProducto = findViewById(R.id.et_nombre_producto);
        TextInputEditText idProducto = findViewById(R.id.et_id_producto);
        TextInputEditText referenciaProducto = findViewById(R.id.et_referencia_producto);
        TextInputEditText precioProducto = findViewById(R.id.et_precio_producto);
        TextInputEditText cantidadProducto = findViewById(R.id.et_cantidad_producto);
        TextInputEditText stockMin = findViewById(R.id.et_stock_min);
        TextInputEditText stockMax = findViewById(R.id.et_stock_max);

        Button btnCrear = findViewById(R.id.btn_crear_producto);

        btnCrear.setOnClickListener(v -> {

            String nombre = nombreProducto.getText().toString().trim();
            String id = idProducto.getText().toString().trim();
            String referencia = referenciaProducto.getText().toString().trim();
            String precioStr = precioProducto.getText().toString().trim();
            String cantidadStr = cantidadProducto.getText().toString().trim();
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
            String emailUsuario = mAuth.getCurrentUser().getEmail();if (emailUsuario != null) {
                Map<String, Object> producto = new HashMap<>();
                producto.put("nombre", nombre);
                producto.put("id", id);
                producto.put("referencia", referencia);
                producto.put("precio", precio);
                producto.put("cantidad", cantidad);
                producto.put("stockMin", min);
                producto.put("stockMax", max);

                db.collection("Users").document(emailUsuario)
                        .collection("productos").add(producto)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Producto añadido correctamente", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Error al añadir el producto", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
