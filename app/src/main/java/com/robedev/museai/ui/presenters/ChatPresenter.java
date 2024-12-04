package com.robedev.museai.ui.presenters;

import android.os.Message;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.leanback.widget.Presenter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robedev.museai.data.model.Artwork;
import com.robedev.museai.interfaces.ChatView;

import java.util.ArrayList;
import java.util.List;

public class ChatPresenter {
    private ChatView view;
    private List<Message> messages;

    public ChatPresenter(ChatView view) {
        this.view = view;
        this.messages = new ArrayList<>();
    }

    public void loadMessages() {
        // Lógica para cargar mensajes (por ejemplo, desde una base de datos o API)
        // Luego actualizar la vista
        view.updateMessageList(messages);
    }

    public void addMessage(Message message) {
        messages.add(message);
        view.updateMessageList(messages);
    }

}
