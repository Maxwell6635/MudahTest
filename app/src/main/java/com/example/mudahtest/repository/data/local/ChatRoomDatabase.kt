package com.example.mudahtest.repository.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mudahtest.repository.data.local.dao.ChatMessageDao
import com.example.mudahtest.repository.data.model.entity.ChatMessage

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [ChatMessage::class], version = 1, exportSchema = false)
public abstract class ChatRoomDatabase : RoomDatabase() {

    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ChatRoomDatabase? = null

        fun getDatabase(context: Context): ChatRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatRoomDatabase::class.java,
                    "chat_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}