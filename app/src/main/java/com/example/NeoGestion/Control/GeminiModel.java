package com.example.NeoGestion.Control;

import com.example.NeoGestion.Model.Message;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;

public class GeminiModel {
    private String apiKey = "AIzaSyBC0uTwB2CFN9Zf-M5nrH9JiMZ2bDS9eE4";
    private List<Message> chatHistory;
    GenerativeModel gm = new GenerativeModel("gemini-1.5-flash-001", apiKey );
    GenerativeModelFutures model = GenerativeModelFutures.from(gm);

    public GeminiModel() {
        this.chatHistory = new ArrayList<>();
    }

    public void generarRespuesta(String mensaje, Callback callback) {
        chatHistory.add(new Message(mensaje, true));

        StringBuilder contexto = new StringBuilder();
        for (Message msg : chatHistory) {
            if (msg.isRol()) {
                contexto.append("Usuario: ").append(msg.getMensaje()).append("\n");
            } else {
                contexto.append("Gemini: ").append(msg.getMensaje()).append("\n");
            }
        }
        Content content = new Content.Builder().addText(contexto.toString()).build();

        ListenableFuture<GenerateContentResponse> respuesta = model.generateContent(content);
        Futures.addCallback(respuesta, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String respuestaGenerada = result.getText();
                chatHistory.add(new Message(respuestaGenerada, false));
                callback.onSuccess(respuestaGenerada);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        }, MoreExecutors.directExecutor());
    }

    public void enviarContexto(String mensaje) {
        chatHistory.add(new Message(mensaje, true));

        StringBuilder contexto = new StringBuilder();
        for (Message msg : chatHistory) {
            if (msg.isRol()) {
                contexto.append("Usuario: ").append(msg.getMensaje()).append("\n");
            } else {
                contexto.append("Gemini: ").append(msg.getMensaje()).append("\n");
            }
        }

        Content content = new Content.Builder().addText(contexto.toString()).build();
        ListenableFuture<GenerateContentResponse> respuesta = model.generateContent(content);
        Futures.addCallback(respuesta, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String respuestaGenerada = result.getText();
                chatHistory.add(new Message(respuestaGenerada, false));
            }

            @Override
            public void onFailure(Throwable t) {
            }
        }, MoreExecutors.directExecutor());
    }

    public interface Callback {
        void onSuccess(String response);
        void onFailure(Throwable error);
    }
}