package com.example.NeoGestion.Control;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.NeoGestion.Model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    // Lista de mensajes que se compartirá con la vista
    private final MutableLiveData<List<Message>> messageList = new MutableLiveData<>(new ArrayList<>());

    // El contexto de la IA
    private static String contexto = "Eres un asistente de una aplicacion de gestion de usuarios y productos llamado NeoGestion, la cual funciona de que cada usuario registrado tiene su propia base de datos online en la que guardar usuarios y productos" +
            " si te preguntan como puedo añadir un producto o usuario, decirle que tiene que acceder al menu desplegable a la izquierda en la opcion de usuarios o productos y una vez dentro de ese menu pulsar el boton flotanto con el simbolo del +, si te pregunta como puedo eliminar un usuario o producto" +
            "dile que una vez dentro del apartado de productos o usuarios tiene que pulsar sobre uno de ellos y le saldra un menu para decidir igual para editar osea el mismo menu esta eliminar y editar, tambien decirte que en el apartado productos hay un icono de una camara al lado de la barra de busqueda que sirve para abrir un escaner codigo de barra y leer un producto"+
            "y asi poder buscar entre todos tus productos";

    public LiveData<List<Message>> getMessageList() {
        return messageList;
    }

    public void addMessage(Message message) {
        List<Message> currentMessages = messageList.getValue();
        if (currentMessages != null) {
            currentMessages.add(message);
            messageList.setValue(currentMessages);
        }
    }
    public void inicializarContexto(GeminiModel geminiModel) {
        if (contexto != null && !contexto.isEmpty()) {
            geminiModel.enviarContexto(contexto);
        }
    }
}
