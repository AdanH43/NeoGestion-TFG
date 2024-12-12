package com.example.NeoGestion.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Producto implements Parcelable {
    private String nombre;
    private String id;
    private String referencia;
    private double precio;
    private int cantidad;
    private String categoria;
    private double stockMax;
    private double stockMin;

    public Producto() {
    }

    public Producto(String nombre, String id, double precio, int cantidad, String categoria, double stockMax, double stockMin, String referencia) {
        this.nombre = nombre;
        this.id = id;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.stockMax = stockMax;
        this.stockMin = stockMin;
        this.referencia = referencia;
    }

    protected Producto(Parcel in) {
        nombre = in.readString();
        id = in.readString();
        referencia = in.readString();
        precio = in.readDouble();
        cantidad = in.readInt();
        categoria = in.readString();
        stockMax = in.readDouble();
        stockMin = in.readDouble();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public String getNombre() {
        return nombre;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getId() {
        return id;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getStockMax() {
        return stockMax;
    }

    public double getStockMin() {
        return stockMin;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setStockMax(double stockMax) {
        this.stockMax = stockMax;
    }

    public void setStockMin(double stockMin) {
        this.stockMin = stockMin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(id);
        dest.writeString(referencia);
        dest.writeDouble(precio);
        dest.writeInt(cantidad);
        dest.writeString(categoria);
        dest.writeDouble(stockMax);
        dest.writeDouble(stockMin);
    }
}

