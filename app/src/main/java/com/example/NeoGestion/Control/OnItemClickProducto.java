package com.example.NeoGestion.Control;

import android.widget.AdapterView;

import com.example.NeoGestion.Model.Producto;

public interface OnItemClickProducto {
    void onItemClick(Producto producto);
    void onItemLongClickListener(Producto producto);
}
