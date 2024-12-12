package com.example.NeoGestion.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Control.OnItemClickListener;
import com.example.NeoGestion.Control.OnUserListChangedListener;
import com.example.NeoGestion.Control.UsuarioAdapter;
import com.example.NeoGestion.Control.UsuarioBajaAdapter;
import com.example.NeoGestion.Model.Usuario;
import com.example.NeoGestion.R;

import java.util.ArrayList;
import java.util.List;

public class UsuariosFragment extends Fragment {
    private RecyclerView recyclerUsuarios;
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> usuarioList;
    private List<Usuario> filteredList;
    private OnItemClickListener listener;
    private OnUserListChangedListener listener1;

    public UsuariosFragment(List<Usuario> usuarioList, OnItemClickListener listener, OnUserListChangedListener listener1) {
        this.usuarioList = usuarioList;
        this.listener = listener;
        this.listener1 = listener1;
        this.filteredList = new ArrayList<>(usuarioList);  // Inicializamos la lista filtrada
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_usuarios, container, false);
        recyclerUsuarios = view.findViewById(R.id.rv_usuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        if (usuarioAdapter == null) {
            usuarioAdapter = new UsuarioAdapter(requireContext(), filteredList, listener);
            usuarioAdapter.setOnUserListChangedListener(listener1);
            recyclerUsuarios.setAdapter(usuarioAdapter);
        }

        return view;
    }

    public void filterUsuarios(String query) {
        if (usuarioAdapter != null) {
        if (!query.isEmpty()) {
            usuarioAdapter.filtrarUsuario(query);
        }
        else {
            usuarioAdapter.ResetFiltro();
        }
        }
    }
    public void resetFiler() {
        if (usuarioAdapter != null) {
            usuarioAdapter.ResetFiltro();
        }
    }
}