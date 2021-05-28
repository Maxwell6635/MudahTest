package com.example.mudahtest.repository.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mudahtest.repository.data.model.entity.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_message ORDER BY datetime(timestamp)")
    fun getChatMessages(): Flow<List<ChatMessage>>

    @Query("SELECT count(*) FROM chat_message")
    fun getChatTableSize(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: ChatMessage)

    @Query("DELETE FROM chat_message")
    suspend fun deleteAll()
}