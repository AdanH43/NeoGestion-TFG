package com.example.NeoGestion.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.NeoGestion.Model.Producto;
import com.example.NeoGestion.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ArticuloDetalleDialog extends DialogFragment {
    private TextView tvDescripcion, tvId, tvCantidad, tvReferencia, tvPrecio, tvCategoria, tvStockMax, tvStockMin;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.articulo_detalle, container, false);
        tvDescripcion = view.findViewById(R.id.tv_descripcion);
        tvId = view.findViewById(R.id.tv_id);
        tvCantidad = view.findViewById(R.id.tv_cant);
        tvReferencia = view.findViewById(R.id.tv_alm);
        tvPrecio = view.findViewById(R.id.tv_precio);
        tvCategoria = view.findViewById(R.id.tv_categoria);
        tvStockMax = view.findViewById(R.id.tv_stockmax);
        tvStockMin = view.findViewById(R.id.tv_clasi);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Bundle arguments = getArguments();
        if (arguments != null) {
            String productoId = arguments.getString("producto_id");
            if (productoId != null) {
                cargarProducto(productoId);
            }
        }

        return view;
    }
    private void cargarProducto(String productoId) {
        String email = mAuth.getCurrentUser().getEmail();
        db.collection("Users")
                .document(email)
                .collection("productos")
                .whereEqualTo("id", productoId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Producto producto = document.toObject(Producto.class);
                            if (producto != null) {
                                tvDescripcion.setText(producto.getNombre());
                                tvId.setText(producto.getId());
                                tvCantidad.setText(String.valueOf(producto.getCantidad()));
                                tvReferencia.setText(producto.getReferencia());
                                tvPrecio.setText(String.valueOf(producto.getPrecio()));
                                tvCategoria.setText(producto.getCategoria());
                                tvStockMax.setText(String.valueOf(producto.getStockMax()));
                                tvStockMin.setText(String.valueOf(producto.getStockMin()));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    tvDescripcion.setText("Error al obtener el producto");
                });
    }
}
