package com.example.NeoGestion.Control;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Model.Producto;
import com.example.NeoGestion.R;

import java.util.List;

public class RecyclerArticulo extends RecyclerView.Adapter<RecyclerArticuloHolder> {
    private List<Producto> productList;

    public RecyclerArticulo(List<Producto> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerArticuloHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articulo_list, parent, false);
        return new RecyclerArticuloHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerArticuloHolder holder, int position) {
        Producto producto = productList.get(position);
        holder.setTvNombre(producto.getNombre());
        holder.setTvId(producto.getId());
        holder.setTvCantidad(String.valueOf(producto.getCantidad()));
        holder.setTvReferencia(producto.getReferencia());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
