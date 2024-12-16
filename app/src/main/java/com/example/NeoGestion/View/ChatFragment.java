package com.example.NeoGestion.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.NeoGestion.Control.GeminiModel;
import com.example.NeoGestion.Control.MessageAdapter;
import com.example.NeoGestion.Model.Message;
import com.example.NeoGestion.R;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private MessageAdapter adapter;
    private ChatViewModel chatViewModel;



    private GeminiModel geminiModel;

    public ChatFragment() {
    }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.chat_fragment, container, false);

            recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
            editTextMessage = view.findViewById(R.id.editTextMessage);
            buttonSend = view.findViewById(R.id.buttonSend);
            chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

            adapter = new MessageAdapter(chatViewModel.getMessageList().getValue());
            recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewMessages.setAdapter(adapter);

            chatViewModel.getMessageList().observe(getViewLifecycleOwner(), messages -> {
                adapter.notifyDataSetChanged();
                recyclerViewMessages.scrollToPosition(messages.size() - 1);
            });

            geminiModel = new GeminiModel();

            chatViewModel.inicializarContexto(geminiModel);

            if (chatViewModel.getMessageList().getValue().isEmpty()) {
                chatViewModel.addMessage(new Message("Hola! Soy Gemini el asistente de NeoGestion ¿En qué puedo ayudarte?", false));
            }

            buttonSend.setOnClickListener(v -> {
                String text = editTextMessage.getText().toString().trim();
                if (!text.isEmpty()) {
                    chatViewModel.addMessage(new Message(text, true));
                    editTextMessage.setText("");

                    geminiModel.generarRespuesta(text, new GeminiModel.Callback() {
                        @Override
                        public void onSuccess(String response) {
                            chatViewModel.addMessage(new Message(response, false));
                        }
                        @Override
                        public void onFailure(Throwable error) {
                            chatViewModel.addMessage(new Message("Error al generar respuesta", false));
                        }
                    });
                }
            });
            return view;
        }
}
