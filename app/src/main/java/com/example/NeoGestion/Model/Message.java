package com.example.NeoGestion.Model;

public class Message {
    private String mensaje;
    private boolean rol;

    public Message(String mensaje, boolean rol) {
        this.mensaje = mensaje;
        this.rol = rol;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isRol() {
        return rol;
    }

}
