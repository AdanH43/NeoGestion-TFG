package com.example.NeoGestion.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Control.RecyclerArticulo;
import com.example.NeoGestion.Model.Producto;
import com.example.NeoGestion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductosFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatAdd;
    private RecyclerArticulo productAdapter;
    private List<Producto> productList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productos_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        loadProducts();
        recyclerView = view.findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();
        productAdapter = new RecyclerArticulo(productList);
        recyclerView.setAdapter(productAdapter);


        FloatingActionButton fabAddProduct = view.findViewById(R.id.floatAdd);
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateProductoActivity.class);
            startActivityForResult(intent, 1);
        });

        return view;
    }
    private void loadProducts() {
        String emailUsuario = mAuth.getCurrentUser().getEmail();
        if (emailUsuario != null) {
            db.collection("Users").document(emailUsuario)
                    .collection("productos")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        productList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Convertir los datos del producto
                            Producto product = document.toObject(Producto.class);
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == getActivity().RESULT_OK) {
            loadProducts();
            productAdapter.notifyDataSetChanged();
        }
    }
}