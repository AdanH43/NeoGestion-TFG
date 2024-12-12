package com.example.NeoGestion.Control;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.PluralsRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Model.Producto;
import com.example.NeoGestion.Model.Usuario;
import com.example.NeoGestion.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerArticulo extends RecyclerView.Adapter<RecyclerArticuloHolder> {
    private List<Producto> productList;
    private List<Producto> productoListFiltrada;
    private OnItemClickProducto onItemClickProducto;

    public RecyclerArticulo(List<Producto> productList, OnItemClickProducto listener) {
        this.productList = productList;
        this.onItemClickProducto = listener;
        productoListFiltrada = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public RecyclerArticuloHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articulo_list, parent, false);
        return new RecyclerArticuloHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerArticuloHolder holder, int position) {
        Producto producto = productoListFiltrada.get(position);
        holder.setTvNombre(producto.getNombre());
        holder.setTvId(producto.getId());
        holder.setTvCantidad(String.valueOf(producto.getCantidad()));
        holder.setTvReferencia(producto.getReferencia());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickProducto.onItemClick(producto);
            }
        });

        holder.itemView.setLongClickable(true);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickProducto.onItemLongClickListener(producto);
                return true;
            }
        });
    }

    public List<Producto> filtrarProducto(String filtro) {
        productoListFiltrada.clear();
        for (Producto producto : productList) {
            if(producto.getId().startsWith(filtro)) {
                productoListFiltrada.add(producto);
            }
        }
        notifyDataSetChanged();
        return productoListFiltrada;
    }

    public List<Producto> ResetFiltro() {
        productoListFiltrada.clear();
        productoListFiltrada.addAll(productList);
        notifyDataSetChanged();
        return productoListFiltrada;
    }

    @Override
    public int getItemCount() {
        return productoListFiltrada.size();
    }
}
