package com.example.NeoGestion.Control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.example.NeoGestion.Model.Message;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;

public class GeminiModel {
    private String apiKey = "AIzaSyBC0uTwB2CFN9Zf-M5nrH9JiMZ2bDS9eE4";
    GenerativeModel gm = new GenerativeModel("gemini-1.5-flash-001", apiKey );
    GenerativeModelFutures model = GenerativeModelFutures.from(gm);
    public void generarRespuesta(String mensaje, Callback callback) {
        Content content = new Content.Builder().addText(mensaje).build();
        ListenableFuture<GenerateContentResponse> respuesta = model.generateContent(content);
        Futures.addCallback(respuesta, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String respuestaGenerada = result.getText();
                callback.onSuccess(respuestaGenerada);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        }, MoreExecutors.directExecutor());
    }


    public interface Callback {
        void onSuccess(String response);
        void onFailure(Throwable error);
    }
}