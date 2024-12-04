package com.robedev.museai.data.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.robedev.museai.data.model.Message;

import java.lang.reflect.Type;
import java.util.List;

public class MessageListConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static String fromMessageList(List<Message> messages) {
        return gson.toJson(messages);
    }

    @TypeConverter
    public static List<Message> toMessageList(String data) {
        Type listType = new TypeToken<List<Message>>() {}.getType();
        return gson.fromJson(data, listType);
    }
}
