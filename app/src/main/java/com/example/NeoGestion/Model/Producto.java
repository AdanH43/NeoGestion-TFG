package com.example.NeoGestion.Model;

public class Producto {
    private String nombre;
    private String id;
    private String referencia;
    private double precio;
    private int cantidad;
    private String categoria;
    private double stocMax;
    private double stocMin;

    public Producto() {
    }

    public Producto(String nombre, String id, double precio, int cantidad, String categoria, double stocMax, double stocMin,  String referencia) {
        this.nombre = nombre;
        this.id = id;
        this.precio = precio;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.stocMax = stocMax;
        this.stocMin = stocMin;
        this.referencia = referencia;
    }

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

    public double getStocMax() {
        return stocMax;
    }

    public double getStocMin() {
        return stocMin;
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

    public void setStocMax(double stocMax) {
        this.stocMax = stocMax;
    }

    public void setStocMin(double stocMin) {
        this.stocMin = stocMin;
    }
}

