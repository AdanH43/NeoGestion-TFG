package com.example.NeoGestion.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Control.FireBase;
import com.example.NeoGestion.Control.OnItemClickProducto;
import com.example.NeoGestion.Control.RecyclerArticulo;
import com.example.NeoGestion.Model.Producto;
import com.example.NeoGestion.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

public class ProductosFragment extends Fragment implements OnItemClickProducto {
    private static final int SCAN_REQUEST_CODE = 1;
    private static final int OTHER_REQUEST_CODE = 2;
    private RecyclerView recyclerView;
    private FloatingActionButton floatAdd;
    private RecyclerArticulo productAdapter;
    private ImageButton btEscaner;
    private SearchView edtBuscar;
    private List<Producto> productList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FireBase firebaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productos_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        edtBuscar = view.findViewById(R.id.buscar);
        btEscaner = view.findViewById(R.id.bt_escaner);
        firebaseHelper = new FireBase();
        loadProducts();
        recyclerView = view.findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<>();



        FloatingActionButton fabAddProduct = view.findViewById(R.id.floatAdd);
        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateProductoActivity.class);
            startActivityForResult(intent, 1);
        });
        edtBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.filtrarProducto(newText);
                return false;
            }
        });

        btEscaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barScan();
            }
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
                            Producto product = document.toObject(Producto.class);
                            productList.add(product);
                        }
                        productAdapter = new RecyclerArticulo(productList, this);
                        recyclerView.setAdapter(productAdapter);
                    })
                    .addOnFailureListener(e -> {
                    });
        }
    }

    private void barScan() {
        IntentIntegrator integrator = new IntentIntegrator(requireActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setCaptureActivity(CustomCaptura.class);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == getActivity().RESULT_OK) {
            loadProducts();
            productAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(Producto producto) { AlertDialog.Builder builder;
        Intent intent = new Intent(getActivity(), CreateProductoActivity.class);
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.que_hacer);
        builder.setNegativeButton(R.string.eliminar, (dialogInterface, i) -> {
            String ID = producto.getId();
            firebaseHelper.deleteProducto(ID,
                    message -> {
                        loadProducts();
                        showSuccessDialog(message);
                    },
                    message -> showErrorDialog(message));
        });
        builder.setPositiveButton(R.string.editar, (dialog, which) -> {
            intent.putExtra("editar", true);
            intent.putExtra("objeto", producto);
            intent.putExtra("producto_Id", producto.getId());
            startActivityForResult(intent, 1);
        });
        builder.show();
    }

    @Override
    public void onItemLongClickListener(Producto producto) {
        ArticuloDetalleDialog dialog = new ArticuloDetalleDialog();
        Bundle args = new Bundle();
        args.putString("producto_id", producto.getId());

        dialog.setArguments(args);

        dialog.show(getFragmentManager(), "ArticuloDetalleDialog");

    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

    private void showSuccessDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();
    }

}