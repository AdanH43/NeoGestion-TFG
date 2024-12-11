package com.example.NeoGestion.Model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Usuario implements Parcelable {

    private String tipousuario;
    private String telefono;
    private String nombreapellidos;
    private String correo;
    private String usuario;
    private String fechaNacimiento;
    private Bitmap foto;
    private String fechaBaja;

    public Usuario(String tipousuario, String telefono, String nombreapellidos, String correo, String usuario,  String fechaNacimiento, Bitmap foto, String fechaBaja) {
        this.tipousuario = tipousuario;
        this.telefono = telefono;
        this.nombreapellidos = nombreapellidos;
        this.correo = correo;
        this.usuario = usuario;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
        this.fechaBaja = fechaBaja;
    }
    public Usuario() {
    }

    protected Usuario(Parcel in) {
        tipousuario = in.readString();
        telefono = in.readString();
        nombreapellidos = in.readString();
        correo = in.readString();
        usuario = in.readString();
        fechaNacimiento = in.readString();
        foto = in.readParcelable(Bitmap.class.getClassLoader());
        fechaBaja = in.readString();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getTipousuario() {
        return tipousuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNombreapellidos() {
        return nombreapellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setTipousuario(String tipousuario) {
        this.tipousuario = tipousuario;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setNombreapellidos(String nombreapellidos) {
        this.nombreapellidos = nombreapellidos;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public void setFechaBaja(String fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public String getFechaBaja() {
        return fechaBaja;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(tipousuario);
        parcel.writeString(telefono);
        parcel.writeString(nombreapellidos);
        parcel.writeString(correo);
        parcel.writeString(usuario);
        parcel.writeString(fechaNacimiento);
        parcel.writeParcelable(foto, i);
        parcel.writeString(fechaBaja);
    }
}
