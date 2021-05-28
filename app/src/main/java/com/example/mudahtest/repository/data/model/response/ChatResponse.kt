package com.example.mudahtest.repository.data.model.response

import com.example.mudahtest.repository.data.model.entity.ChatMessage
import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("chat")
    var chatList: ArrayList<ChatMessage>? = arrayListOf()
)