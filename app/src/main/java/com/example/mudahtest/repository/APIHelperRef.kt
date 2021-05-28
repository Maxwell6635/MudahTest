package com.example.mudahtest.repository

import com.example.mudahtest.repository.data.model.entity.ChatMessage
import com.example.mudahtest.repository.data.model.entity.ResultWrapper
import com.example.mudahtest.repository.data.model.request.SendChatPayload

interface APIHelperRef {
    suspend fun postChatMessage(sendChatPayload: SendChatPayload): ResultWrapper<ChatMessage>
}