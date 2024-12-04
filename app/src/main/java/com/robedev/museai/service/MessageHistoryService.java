package com.robedev.museai.service;



import com.robedev.museai.data.model.Conversation;
import com.robedev.museai.data.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageHistoryService {

    private final List<Message> messages = new ArrayList<>();

    public synchronized void addMessage(String role, String content) {
        messages.add(new Message(role, content));
    }

    public synchronized List<Message> getRecentMessages(int count) {
        int startIndex = Math.max(messages.size() - count, 0);
        return new ArrayList<>(messages.subList(startIndex, messages.size()));
    }

    public synchronized void clearMessages() {
        messages.clear();
    }

    public synchronized void updateConversationFromUser(Conversation conversation) {
        // Implementar la lógica para actualizar la conversación desde el usuario
    }
}
