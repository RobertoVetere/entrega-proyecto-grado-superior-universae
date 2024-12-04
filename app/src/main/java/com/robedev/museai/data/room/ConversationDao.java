package com.robedev.museai.data.room;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.robedev.museai.data.model.Conversation;

import java.util.List;

@Dao
public interface ConversationDao {
    @Insert
    void insert(Conversation conversation);

    @Update
    void update(Conversation conversation);

    @Query("SELECT * FROM conversations")
    List<Conversation> getAllConversations();

    @Query("SELECT * FROM conversations WHERE id = :id")
    Conversation getConversationById(int id);
}
