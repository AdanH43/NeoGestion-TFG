package com.example.NeoGestion.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.NeoGestion.Control.FireBase;
import com.example.NeoGestion.Control.MainController;
import com.example.NeoGestion.Control.OnUserListChangedListener;
import com.example.NeoGestion.R;
import com.example.NeoGestion.Control.MyPagerAdapter;
import com.example.NeoGestion.Control.OnItemClickListener;
import com.example.NeoGestion.Model.Usuario;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.search.SearchBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainTrabajadores extends Fragment implements OnItemClickListener, OnUserListChangedListener {

    private ImageView bt_select;
    private List<Usuario> selectedUsers = new ArrayList<>();
    private List<Usuario> usuariosList = new ArrayList<>();
    private List<Usuario> usuariosBajaList = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;
    private boolean booleanFlag = false;
    private SearchView edt_buscar;
    private MainController mainController;
    private FloatingActionButton floatAdd;
    private TabLayout tab;
    private ViewPager2 viewPager;
    private FirebaseAuth mAuth;
    FireBase firebaseHelper;
    private ProgressDialog progressDialog;

    public MainTrabajadores() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_trabajadores, container, false);

        firebaseHelper = new FireBase();
        mAuth = FirebaseAuth.getInstance();

        tab = view.findViewById(R.id.tb_layout);
        viewPager = view.findViewById(R.id.viewPager);
        bt_select = view.findViewById(R.id.bt_select);
        floatAdd = view.findViewById(R.id.floatAdd);
        edt_buscar = view.findViewById(R.id.searchView);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando tus usuarios...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        loadUsuariosFromFirebase();

        bt_select.setOnClickListener(v -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.sheets_botom, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();

            TextView delete_select = bottomSheetView.findViewById(R.id.tv_delete);
            TextView add = bottomSheetView.findViewById(R.id.tv_añadir);

            delete_select.setOnClickListener(view1 -> {
                bottomSheetDialog.cancel();
                if (!selectedUsers.isEmpty()) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.safe_delete)
                            .setPositiveButton(R.string.aceptar, (dialog, which) -> {
                                for (Usuario usuario : selectedUsers) {
                                    firebaseHelper.deleteUser(usuario.getCorreo(),
                                            message -> showSuccessDialog(message),
                                            message -> showErrorDialog(message));
                                }
                            })
                            .show();
                } else {
                    Snackbar.make(viewPager, R.string.error_user, Snackbar.LENGTH_SHORT).show();
                }
            });

            add.setOnClickListener(view1 -> {
                bottomSheetDialog.cancel();
                Intent intent = new Intent(getActivity(), CrearUserActivity.class);
                intent.putExtra("editar", false);
                startActivity(intent);
            });
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (myPagerAdapter != null) {
                    myPagerAdapter.resetFilter();
                    edt_buscar.setQuery("", false);
                }
            }
        });

        floatAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CrearUserActivity.class);
            intent.putExtra("editar", false);
            startActivityForResult(intent, 1);
        });

        edt_buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (myPagerAdapter != null) {
                    myPagerAdapter.filterData(newText);
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onItemClick(Usuario usuario) {
        AlertDialog.Builder builder;
        Intent intent = new Intent(getActivity(), CrearUserActivity.class);
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.que_hacer);
        builder.setNegativeButton(R.string.eliminar, (dialogInterface, i) -> {
            String correoUser = usuario.getCorreo();
            firebaseHelper.deleteUser(correoUser,
                    message -> {
                        loadUsuariosFromFirebase();
                        showSuccessDialog(message);
                    },
                    message -> showErrorDialog(message));
        });
        builder.setPositiveButton(R.string.editar, (dialog, which) -> {
            intent.putExtra("editar", true);
            intent.putExtra("objeto", usuario);
            intent.putExtra("usuario_email", usuario.getCorreo());
            startActivityForResult(intent, 1);
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadUsuariosFromFirebase();
        if (myPagerAdapter != null) {
            myPagerAdapter.notifyDataSetChanged();
        }
    }

    private void loadUsuariosFromFirebase() {
        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        db.collection("Users")
                .document(email)
                .collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        usuariosList.clear();
                        usuariosBajaList.clear();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String correo = documentSnapshot.getString("correo");
                            String fechaNacimiento = documentSnapshot.getString("fecha");
                            String fechaBaja = documentSnapshot.getString("fecha_baja");
                            String telefono = documentSnapshot.getString("telefono");
                            String nombre = documentSnapshot.getString("nombre_apellidos");
                            String usuario = documentSnapshot.getString("usuario");
                            String tipoUsuario = documentSnapshot.getString("tipo");
                            String foto = documentSnapshot.getString("foto");

                            Bitmap bitmap = MainController.getSingleton().StringToBitMap(foto);

                            Usuario usuarioObj = new Usuario();
                            usuarioObj.setCorreo(correo);
                            usuarioObj.setFechaNacimiento(fechaNacimiento);
                            usuarioObj.setFechaBaja(fechaBaja);
                            usuarioObj.setTelefono(telefono);
                            usuarioObj.setNombreapellidos(nombre);
                            usuarioObj.setUsuario(usuario);
                            usuarioObj.setTipousuario(tipoUsuario);
                            usuarioObj.setFoto(bitmap);

                            if (fechaBaja != null && !fechaBaja.isEmpty()) {
                                booleanFlag = false;
                                usuariosBajaList.add(usuarioObj);
                            } else {
                                usuariosList.add(usuarioObj);
                            }
                        }

                        myPagerAdapter = new MyPagerAdapter(this, usuariosList, usuariosBajaList, booleanFlag, this, this);
                        viewPager.setAdapter(myPagerAdapter);
                        tab.setTabTextColors(
                                ContextCompat.getColor(requireContext(), R.color.blue_200),
                                ContextCompat.getColor(requireContext(), R.color.blue_400)
                        );
                        tab.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(), R.color.blue_600));

                        new TabLayoutMediator(tab, viewPager, (tab, position) -> {
                            switch (position) {
                                case 0:
                                    tab.setText("Activos");
                                    tab.setIcon(R.drawable.baseline_recent_actors_24);
                                    break;
                                case 1:
                                    tab.setText("Dados de Baja");
                                    tab.setIcon(R.drawable.baseline_no_accounts_24);
                                    break;
                                default:
                                    tab.setText(null);
                                    break;
                            }
                        }).attach();
                        progressDialog.dismiss();
                    } else {
                        Log.d("Firestore", "No hay usuarios en la subcolección");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error al obtener los documentos", e));
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

    @Override
    public void onUserListChanged(List<Usuario> userList) {
        selectedUsers = userList;
    }
}

