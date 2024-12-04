package com.robedev.museai.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.robedev.museai.data.room.MessageListConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "conversations")
public class Conversation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters({MessageListConverter.class})
    private List<Message> messages;

    public Conversation() {
        // Inicializa la lista de mensajes
        messages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message); // Agrega un nuevo mensaje a la lista
    }

    public void clearMessages() {
        messages.clear(); // Limpia la lista de mensajes
    }
}
