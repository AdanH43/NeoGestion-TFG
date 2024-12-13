package com.example.NeoGestion.View;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.NeoGestion.R;
import com.google.firebase.auth.FirebaseAuth;


public class SimpleFragment extends Fragment {
    private FirebaseAuth mAuth;
    private TextView tvBienvenido;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().getDisplayName();
        View view = inflater.inflate(R.layout.simple_fragmet, container, false);
        tvBienvenido = view.findViewById(R.id.tv_bienvenido);
        tvBienvenido.setText("Bienvenido a NeoGestion, " + mAuth.getCurrentUser().getDisplayName());
        return view;
    }
}