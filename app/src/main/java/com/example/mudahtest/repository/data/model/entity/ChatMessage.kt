package com.example.mudahtest.repository.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chat_message")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "timestamp")
    @SerializedName(value = "timestamp", alternate = ["createdAt"])
    var timestamp: String? = "",

    @ColumnInfo(name = "direction")
    @SerializedName("direction")
    var direction: String? = "",

    @ColumnInfo(name = "message")
    @SerializedName("message")
    var message: String? = ""
) {
    val isOutBoundMessage: Boolean
        get() = direction.toString().equals("OUTGOING", ignoreCase = true)
}