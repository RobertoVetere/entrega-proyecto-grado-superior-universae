package com.robedev.museai.service;

import android.os.Handler;
import android.util.Log;

import com.robedev.museai.data.model.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ChatService {

    private static final String TAG = "ChatService";
    private static final String BASE_URL = "URL_A_TU_BACKEND";
    private final OkHttpClient client;
    private final MessageHistoryService messageHistoryService;
    private StringBuilder accumulatedResponse;
    private ChatServiceCallback callback;
    private Handler mainHandler;

    public ChatService(MessageHistoryService messageHistoryService, Handler mainHandler) {
        this.client = new OkHttpClient();
        this.messageHistoryService = messageHistoryService;
        this.accumulatedResponse = new StringBuilder();
        this.mainHandler = mainHandler;
    }

    public void setCallback(ChatServiceCallback callback) {
        this.callback = callback;
    }

    public void sendMessage(String language, String instructions, String userMessage) {
        List<Message> recentMessages = messageHistoryService.getRecentMessages(10);
        messageHistoryService.addMessage("user", userMessage);

        JSONObject requestPayload = new JSONObject();
        try {
            requestPayload.put("language", language != null ? language : "");
            requestPayload.put("instructions", instructions != null ? instructions : "");

            // Crear una copia de la lista de mensajes para evitar ConcurrentModificationException
            List<Message> messageCopy = new ArrayList<>(recentMessages);
            JSONArray messageArray = new JSONArray();
            for (Message message : messageCopy) {
                JSONObject messageObject = new JSONObject();
                messageObject.put("role", message.getRole() != null ? message.getRole() : "");
                messageObject.put("content", message.getContent() != null ? message.getContent() : "");
                messageArray.put(messageObject);
            }
            requestPayload.put("historyMessages", messageArray);
            requestPayload.put("userMessage", userMessage != null ? userMessage : "");

            // Registrar la solicitud enviada
            Log.d(TAG, "Request payload: " + requestPayload.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error creating request payload: " + e.getMessage(), e);
            return;
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(requestPayload.toString(), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + "/message")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "Error sending message: " + e.getMessage());
                if (callback != null) {
                    mainHandler.post(() -> callback.onFailure(e.getMessage()));
                }
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Error in response: " + response.code());
                    if (callback != null) {
                        mainHandler.post(() -> callback.onFailure("Error in response: " + response.code()));
                    }
                    return;
                }

                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    Log.e(TAG, "Response body is null");
                    if (callback != null) {
                        mainHandler.post(() -> callback.onFailure("Response body is null"));
                    }
                    return;
                }

                String responseString = responseBody.string();

                // Registrar la respuesta recibida
                Log.d(TAG, "Response received: " + responseString);

                // Añadir la respuesta completa al historial como 'assistant'
                messageHistoryService.addMessage("assistant", responseString);

                // Notificar al callback
                if (callback != null) {
                    mainHandler.post(() -> callback.onSuccess(responseString));
                }
            }
        });
    }

    public String getAccumulatedResponse() {
        return accumulatedResponse.toString();
    }

    public interface ChatServiceCallback {
        void onSuccess(String response);
        void onFailure(String errorMessage);
    }
}
