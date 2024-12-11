package com.example.NeoGestion.Control;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.graphics.Bitmap;

import com.example.NeoGestion.Model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FireBase  {
    private final FirebaseFirestore db;
    private final FirebaseAuth mAuth;

    public FireBase() {
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();;
    }

    public void createUser(String nombreUsuario, String correo, String nombreApellidos,
                           String telefono, String tipo, String fecha, Bitmap foto,
                           FirebaseCallback successCallback, FirebaseCallback errorCallback) {
        String emailId = mAuth.getCurrentUser().getEmail();
        String fotoString = MainController.getSingleton().BitMapToString(foto);

        if (emailId == null || nombreUsuario.isEmpty() || correo.isEmpty()) {
            errorCallback.onCallback("Campos obligatorios vacíos");
            return;
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("usuario", nombreUsuario);
        userMap.put("correo", correo);
        userMap.put("nombre_apellidos", nombreApellidos);
        userMap.put("telefono", telefono);
        userMap.put("fecha", fecha);
        userMap.put("tipo", tipo);
        userMap.put("foto", fotoString);
        userMap.put("fecha_baja", "");

        db.collection("Users").document(emailId).collection("usuarios")
                .add(userMap)
                .addOnSuccessListener(documentReference -> successCallback.onCallback("Usuario creado correctamente"))
                .addOnFailureListener(e -> errorCallback.onCallback("Error al crear usuario: " + e.getMessage()));
    }

    // Método para actualizar un usuario
    public void updateUser(String emailUser, String nombreUsuario, String correo, String nombreApellidos,
                           String telefono, String tipo, String fecha, Bitmap foto, String fechaBaja,
                           FirebaseCallback successCallback, FirebaseCallback errorCallback) {
        String emailId = mAuth.getCurrentUser().getEmail();
        String fotoString = MainController.getSingleton().BitMapToString(foto);

        if (emailId == null || nombreUsuario.isEmpty() || correo.isEmpty()) {
            errorCallback.onCallback("Campos obligatorios vacíos");
            return;
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("usuario", nombreUsuario);
        userMap.put("correo", correo);
        userMap.put("nombre_apellidos", nombreApellidos);
        userMap.put("telefono", telefono);
        userMap.put("fecha", fecha);
        userMap.put("tipo", tipo);
        userMap.put("foto", fotoString);
        userMap.put("fecha_baja", fechaBaja);

        db.collection("Users").document(emailId).collection("usuarios")
                .whereEqualTo("correo", emailUser)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String documentId = document.getId();
                            db.collection("Users").document(emailId).collection("usuarios").document(documentId)
                                    .update(userMap)
                                    .addOnSuccessListener(aVoid -> successCallback.onCallback("Usuario actualizado correctamente"))
                                    .addOnFailureListener(e -> errorCallback.onCallback("Error al actualizar usuario: " + e.getMessage()));
                        }
                    } else {
                        errorCallback.onCallback("No se encontró ningún usuario con el correo especificado.");
                    }
                })
                .addOnFailureListener(e -> errorCallback.onCallback("Error al buscar usuario: " + e.getMessage()));
    }

    // Método para eliminar un usuario
    public void deleteUser(String emailUserToDelete, FirebaseCallback successCallback, FirebaseCallback errorCallback) {
        String emailId = mAuth.getCurrentUser().getEmail();

        if (emailId == null) {
            errorCallback.onCallback("No se pudo obtener el usuario autenticado.");
            return;
        }

        db.collection("Users").document(emailId).collection("usuarios")
                .whereEqualTo("correo", emailUserToDelete)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String documentId = document.getId();
                            db.collection("Users").document(emailId).collection("usuarios").document(documentId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> successCallback.onCallback("Usuario eliminado correctamente"))
                                    .addOnFailureListener(e -> errorCallback.onCallback("Error al eliminar usuario: " + e.getMessage()));
                        }
                    } else {
                        errorCallback.onCallback("No se encontró ningún usuario con el correo especificado.");
                    }
                })
                .addOnFailureListener(e -> errorCallback.onCallback("Error al buscar usuario: " + e.getMessage()));
    }

    public interface FirebaseCallback {
        void onCallback(String message);
    }
}



