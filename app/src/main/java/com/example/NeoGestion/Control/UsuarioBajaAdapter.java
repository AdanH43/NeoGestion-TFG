package com.example.NeoGestion.Control;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.R;
import com.example.NeoGestion.Model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioBajaAdapter extends RecyclerView.Adapter<UsuarioBajaViewHolder> {
    private LayoutInflater myInflater;
    private Context context;
    private OnItemClickListener listener;
    private List<Usuario> userlist = new ArrayList<>();
    private List<Usuario> userListFiltrada = new ArrayList<>();

    private List<Usuario> selectedUsers = new ArrayList<>();
    private OnUserListChangedListener userListChangedListener;


    public UsuarioBajaAdapter(Context context, List<Usuario> list, OnItemClickListener listener) {
        this.context = context;
        myInflater = LayoutInflater.from(context);
        userlist = new ArrayList<>(list);
        userListFiltrada = new ArrayList<>(userlist);
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsuarioBajaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = myInflater.inflate(R.layout.usuarios_list, parent, false);
        return new UsuarioBajaViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioBajaViewHolder holder, int position) {
        Usuario usuario1 =userListFiltrada.get(position);
        String fechaNacimiento = usuario1.getFechaNacimiento();
        Integer edad = MainController.getSingleton().getEdad(fechaNacimiento);
        if (!usuario1.getFechaBaja().isEmpty()) {
            holder.setTvUsuario(usuario1.getUsuario());
            holder.setTvNombre(usuario1.getNombreapellidos());
            holder.setTvTelefono(usuario1.getTelefono());
            holder.setTvCorreo(usuario1.getCorreo());
            holder.setTvTipo(usuario1.getTipousuario());
            holder.setTvFoto(usuario1.getFoto());
            holder.setTvFecha(usuario1.getFechaNacimiento());
            holder.setTvFechaBaja(usuario1.getFechaBaja());
        }
        else {
            holder.cardView.setVisibility(View.GONE);
        }

        if(!usuario1.getNombreapellidos().isEmpty()) {
            holder.tvNombre.setVisibility(View.VISIBLE);
            holder.ldbNombre.setVisibility(View.VISIBLE);
        }
        if (!usuario1.getTelefono().isEmpty()){
            holder.tvTelefono.setVisibility(View.VISIBLE);
            holder.lbTelefono.setVisibility(View.VISIBLE);
        }
        if (!usuario1.getFechaBaja().isEmpty()) {
            holder.tvFechaBaja.setVisibility(View.VISIBLE);
            holder.lbFechaBaja.setVisibility(View.VISIBLE);
        }

        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (!selectedUsers.contains(usuario1)) {
                        selectedUsers.add(usuario1);
                    }
                } else {
                    selectedUsers.remove(usuario1);
                }
                userListChangedListener.onUserListChanged(selectedUsers);
            }
        });

        if(!usuario1.getFechaBaja().isEmpty() && edad >=18) {
            holder.cardView.setCardBackgroundColor(Color.RED);
            holder.tvUsuario.setTextColor(Color.WHITE);
            holder.tvNombre.setTextColor(Color.WHITE);
            holder.tvFecha.setTextColor(Color.WHITE);
            holder.tvTipo.setTextColor(Color.WHITE);
            holder.tvTelefono.setTextColor(Color.WHITE);
            holder.tvCorreo.setTextColor(Color.WHITE);
            holder.ldbNombre.setTextColor(Color.WHITE);
            holder.lbTipo.setTextColor(Color.WHITE);
            holder.lbTelefono.setTextColor(Color.WHITE);
            holder.lbCorreo.setTextColor(Color.WHITE);
            holder.lbFecha.setTextColor(Color.WHITE);
            holder.lbFechaBaja.setTextColor(Color.WHITE);
            holder.tvFechaBaja.setTextColor(Color.WHITE);
        }
         else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(usuario1);
            }
        });

    }

    @Override
    public int getItemCount () {
        return userListFiltrada.size();
    }

    public List<Usuario> filtrarUsuarioBaja(String filtro) {
        userListFiltrada.clear();
        for(Usuario usuario : userlist) {
            if (usuario.getUsuario().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getCorreo().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getTelefono().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getNombreapellidos().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getTipousuario().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getFechaNacimiento().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getFechaBaja().toLowerCase().startsWith(filtro.toLowerCase())) {
                userListFiltrada.add(usuario);
            }
        }
        notifyDataSetChanged();
        return userListFiltrada;
    }

    public List<Usuario> ResetFiltroBaja() {
        userListFiltrada.clear();
        userListFiltrada.addAll(userlist);
        notifyDataSetChanged();
        return userListFiltrada;

    }
    public List<Usuario> getFiltList() {
        return userListFiltrada;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        userListFiltrada = usuarios;
        userlist = usuarios;
        notifyDataSetChanged();
    }

    public void setOnUserListChangedListener(OnUserListChangedListener listener) {
        this.userListChangedListener = listener;
    }
}
