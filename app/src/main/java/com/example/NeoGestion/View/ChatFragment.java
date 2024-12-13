package com.example.NeoGestion.View;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Control.GeminiModel;
import com.example.NeoGestion.Control.MessageAdapter;
import com.example.NeoGestion.Model.Message;
import com.example.NeoGestion.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private List<Message> messageList;
    private MessageAdapter adapter;

    private GeminiModel geminiModel;

    public ChatFragment() {
    }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.chat_fragment, container, false);

            recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
            editTextMessage = view.findViewById(R.id.editTextMessage);
            buttonSend = view.findViewById(R.id.buttonSend);

            messageList = new ArrayList<>();
            adapter = new MessageAdapter(messageList);
            recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewMessages.setAdapter(adapter);

            geminiModel = new GeminiModel();


            messageList.add(new Message("Hola! Soy Gemini el asistente de NeoGestion ¿En qué puedo ayudarte?", false));
            adapter.notifyDataSetChanged();

            buttonSend.setOnClickListener(v -> {
                String text = editTextMessage.getText().toString().trim();
                if (!text.isEmpty()) {
                    messageList.add(new Message(text, true));
                    adapter.notifyDataSetChanged();
                    editTextMessage.setText("");
                    geminiModel.generarRespuesta(text, new GeminiModel.Callback() {
                        @Override
                        public void onSuccess(String response) {
                            messageList.add(new Message(response, false));
                            adapter.notifyDataSetChanged();
                            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            messageList.add(new Message("Error al generar respuesta", false));
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });

            return view;
        }
}
