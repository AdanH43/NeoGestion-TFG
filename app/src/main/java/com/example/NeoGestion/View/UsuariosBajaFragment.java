package com.example.NeoGestion.View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class UsuariosBajaFragment  extends Fragment {
    private RecyclerView recyclerUsuarios;
    private TextView tvVacio;
    private ImageView imgVacio;
    private UsuarioBajaAdapter usuarioBajaAdapter;
    private List<Usuario> usuarioList;
    private List<Usuario> filteredListBaja;
    private OnItemClickListener listener;
    private OnUserListChangedListener userListChangedListener;

    public UsuariosBajaFragment(List<Usuario> usuarioList, OnItemClickListener listener, OnUserListChangedListener listener1) {
        this.usuarioList = usuarioList;
        this.listener = listener;
        this.userListChangedListener = listener1;
        this.filteredListBaja = new ArrayList<>(usuarioList);
    }
    public UsuariosBajaFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_usuarios, container, false);
        tvVacio = view.findViewById(R.id.tv_vacio);
        imgVacio = view.findViewById(R.id.img_vacio);
        recyclerUsuarios = view.findViewById(R.id.rv_usuarios);
        recyclerUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
        usuarioBajaAdapter = new UsuarioBajaAdapter(requireContext(), filteredListBaja, listener);
        recyclerUsuarios.setAdapter(usuarioBajaAdapter);
        usuarioBajaAdapter.setOnUserListChangedListener(userListChangedListener);
        if (filteredListBaja.isEmpty() || filteredListBaja == null ) {
            recyclerUsuarios.setVisibility(View.GONE);
            tvVacio.setVisibility(View.VISIBLE);
            imgVacio.setVisibility(View.VISIBLE);
        }
        else {
            recyclerUsuarios.setVisibility(View.VISIBLE);
            tvVacio.setVisibility(View.GONE);
            imgVacio.setVisibility(View.GONE);
        }
        return view;
    }
    public void filterUsuarios(String query) {
        if (usuarioBajaAdapter != null) {
            if (!query.isEmpty()) {
                usuarioBajaAdapter.filtrarUsuarioBaja(query);
            } else {
                usuarioBajaAdapter.ResetFiltroBaja();
            }
        }
    }
    public void resetFiler() {
        if (usuarioBajaAdapter != null) {
            usuarioBajaAdapter.ResetFiltroBaja();
        }
    }
}

