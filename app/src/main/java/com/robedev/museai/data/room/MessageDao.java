package com.robedev.museai.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.robedev.museai.data.model.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insert(Message message);

    @Update
    void update(Message message);

    @Query("SELECT * FROM messages")
    List<Message> getAllMessages();

    @Query("SELECT * FROM messages WHERE id = :id")
    Message getMessageById(int id);
}
