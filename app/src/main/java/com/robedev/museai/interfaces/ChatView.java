package com.robedev.museai.interfaces;

import android.os.Message;

import java.util.List;

public interface ChatView {
    void updateMessageList(List<Message> messages);
}
