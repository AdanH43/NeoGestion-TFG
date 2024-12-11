package com.example.NeoGestion.Control;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.R;

public class UsuarioViewHolder extends RecyclerView.ViewHolder {
    final UsuarioAdapter myadapter;
    public TextView tvUsuario;
    public TextView tvCorreo;
    public TextView tvNombre;
    public TextView tvTelefono;
    public TextView tvTipo;
    public TextView tvFecha;
    public ImageView tvFoto;
    public CardView cardView;
    public TextView ldbNombre, lbTipo, lbTelefono, lbCorreo, lbFecha, lbFechaBaja;
    public CheckBox cbSelect;
    public TextView tvFechaBaja;

    public UsuarioViewHolder(@NonNull View itemView, UsuarioAdapter myadapter) {
        super(itemView);
        cbSelect = itemView.findViewById(R.id.check_delete);
        tvUsuario = itemView.findViewById(R.id.tv_usuario);
        tvTelefono = itemView.findViewById(R.id.tv_telefono);
        tvNombre = itemView.findViewById(R.id.tv_nombre_apellidos);
        tvCorreo = itemView.findViewById(R.id.tv_correo);
        tvTipo = itemView.findViewById(R.id.tv_tipo);
        tvFoto = itemView.findViewById(R.id.tv_foto);
        tvFecha = itemView.findViewById(R.id.tv_fecha);
        tvFechaBaja = itemView.findViewById(R.id.tv_fecha_baja);
        cardView = itemView.findViewById(R.id.card);
        ldbNombre = itemView.findViewById(R.id.lb_nombre);
        lbTipo = itemView.findViewById(R.id.lb_tipo);
        lbTelefono = itemView.findViewById(R.id.label_telefono);
        lbCorreo = itemView.findViewById(R.id.lb_correo);
        lbFecha = itemView.findViewById(R.id.lb_fecha);
        lbFechaBaja = itemView.findViewById(R.id.lb_fecha_baja);
        this.myadapter = myadapter;
    }

    public void setTvFecha(String Fecha) {
        tvFecha.setText(Fecha);
    }

    public void setTvUsuario(String Usuario) {
        tvUsuario.setText(Usuario);
    }

    public void setTvCorreo(String Correo) {
        tvCorreo.setText(Correo);
    }

    public void setTvNombre(String Nombre) {
        tvNombre.setText(Nombre);
    }

    public void setTvTelefono(String Telefono) {
        tvTelefono.setText(Telefono);
    }

    public void setTvTipo(String Tipo) {
        tvTipo.setText(Tipo);
    }

    public void setTvFoto(Bitmap Foto) {
        tvFoto.setImageBitmap(Foto);
    }

    public void setTvFechaBaja(String fechaBaja) {
        tvFechaBaja.setText(fechaBaja);
    }
}
