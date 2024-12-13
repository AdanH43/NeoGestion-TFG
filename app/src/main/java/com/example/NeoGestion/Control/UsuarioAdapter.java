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

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioViewHolder> {

    private LayoutInflater myInflater;
    private Context context;
    private OnItemClickListener listener;
    private List<Usuario> userlist = new ArrayList<>();
    private List<Usuario> userListFilstrada = new ArrayList<>();
    private List<Usuario> selectedUsers = new ArrayList<>();
    private OnUserListChangedListener userListChangedListener;


    public UsuarioAdapter(Context context, List<Usuario> list, OnItemClickListener listener) {
        this.context = context;
        myInflater = LayoutInflater.from(context);
        userlist= new ArrayList<>(list);
        userListFilstrada = new ArrayList<>(userlist);
        this.listener = listener;

    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = myInflater.inflate(R.layout.usuarios_list, parent, false);
        return new UsuarioViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario1 = userListFilstrada.get(position);
        String fechaNacimiento = usuario1.getFechaNacimiento();
        Integer edad = MainController.getSingleton().getEdad(fechaNacimiento);
        holder.setTvUsuario(usuario1.getUsuario());
        holder.setTvNombre(usuario1.getNombreapellidos());
        holder.setTvTelefono(usuario1.getTelefono());
        holder.setTvCorreo(usuario1.getCorreo());
        holder.setTvTipo(usuario1.getTipousuario());
        holder.setTvFoto(usuario1.getFoto());
        holder.setTvFecha(usuario1.getFechaNacimiento());
        holder.setTvFechaBaja(usuario1.getFechaBaja());
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
        if(!usuario1.getNombreapellidos().isEmpty()) {
            holder.tvNombre.setVisibility(View.VISIBLE);
            holder.ldbNombre.setVisibility(View.VISIBLE);
        }
        if (!usuario1.getTelefono().isEmpty()){
            holder.tvTelefono.setVisibility(View.VISIBLE);
            holder.lbTelefono.setVisibility(View.VISIBLE);
        }
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

        else if (edad >= 18 && usuario1.getFechaBaja().isEmpty()) {
            holder.cardView.setCardBackgroundColor(Color.BLACK);
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

        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        if (!usuario1.getFechaBaja().isEmpty()) {
            holder.tvFechaBaja.setVisibility(View.VISIBLE);
            holder.lbFechaBaja.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvFechaBaja.setVisibility(View.GONE);
            holder.lbFechaBaja.setVisibility(View.GONE);
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
            return userListFilstrada.size();
        }

    public List<Usuario> filtrarUsuario(String filtro) {
        userListFilstrada.clear();
        for(Usuario usuario : userlist) {
            if (usuario.getUsuario().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getCorreo().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getTelefono().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getNombreapellidos().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getTipousuario().toLowerCase().startsWith(filtro.toLowerCase())
                    || usuario.getFechaNacimiento().toLowerCase().startsWith(filtro.toLowerCase())) {
                userListFilstrada.add(usuario);
            }
        }
        notifyDataSetChanged();
        return userListFilstrada;
    }

    public List<Usuario> ResetFiltro() {
        userListFilstrada.clear();
        userListFilstrada.addAll(userlist);
        notifyDataSetChanged();
        return userListFilstrada;

    }
    public void setOnUserListChangedListener(OnUserListChangedListener listener) {
        this.userListChangedListener = listener;
    }

}

