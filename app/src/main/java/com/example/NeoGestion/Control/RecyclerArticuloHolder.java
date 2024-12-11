package com.example.NeoGestion.Control;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.R;

public class RecyclerArticuloHolder extends RecyclerView.ViewHolder {
    public TextView tvNombre, tvId, tvCantidad, tvReferencia;
    final RecyclerArticulo adapter;
    public RecyclerArticuloHolder(@NonNull View itemView, RecyclerArticulo adapter) {
        super(itemView);
        tvNombre = itemView.findViewById(R.id.tv_descripcion_escan);
        tvId = itemView.findViewById(R.id.tv_id_escan);
        tvCantidad = itemView.findViewById(R.id.tv_cbar_escan);
        tvReferencia = itemView.findViewById(R.id.tv_alm_escan);
        this.adapter = adapter;
    }
    public void setTvNombre(String nombre) {
        tvNombre.setText(nombre);
    }
    public void setTvId(String id) {
        tvId.setText(id);
    }
    public void setTvCantidad(String cantidad) {
            tvCantidad.setText(cantidad);
    }
    public void setTvReferencia(String referencia) {
        tvReferencia.setText(referencia);
    }
}
