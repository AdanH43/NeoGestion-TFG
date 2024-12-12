package com.example.NeoGestion.Control;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.NeoGestion.Model.Usuario;
import com.example.NeoGestion.View.MainTrabajadores;
import com.example.NeoGestion.View.UsuariosBajaFragment;
import com.example.NeoGestion.View.UsuariosFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStateAdapter {

    private List<Usuario> list = new ArrayList<>();
    private List<Usuario> listBaja = new ArrayList<>();
    private OnItemClickListener listener;
    private OnUserListChangedListener listener1;
    private boolean booleanFlag;
    private UsuariosFragment usuariosFragment;
    private UsuariosBajaFragment usuariosBajaFragment;

    public MyPagerAdapter(MainTrabajadores activity, List<Usuario> list, List<Usuario> listBaja, boolean booleanFlag, OnItemClickListener listener, OnUserListChangedListener listener1) {
        super(activity);
        this.list = list;
        this.listener = listener;
        this.listBaja = listBaja;
        this.booleanFlag = booleanFlag;
        this.listener1 = listener1;
        this.usuariosFragment = new UsuariosFragment(list, listener, listener1);
        this.usuariosBajaFragment = new UsuariosBajaFragment(listBaja, listener, listener1);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return usuariosFragment;
            case 1: return usuariosBajaFragment;
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void filterData(String query) {
        usuariosFragment.filterUsuarios(query);
        usuariosBajaFragment.filterUsuarios(query);
    }

    public void resetFilter() {
        usuariosBajaFragment.resetFiler();
        usuariosFragment.resetFiler();
    }
}
