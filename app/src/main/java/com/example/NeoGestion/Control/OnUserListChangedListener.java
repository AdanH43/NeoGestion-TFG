package com.example.NeoGestion.Control;

import com.example.NeoGestion.Model.Usuario;

import java.util.List;

public interface OnUserListChangedListener {
    void onUserListChanged(List<Usuario> userList);
}
