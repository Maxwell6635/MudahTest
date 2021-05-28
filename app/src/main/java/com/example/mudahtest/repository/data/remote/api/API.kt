package com.example.mudahtest.repository.data.remote.api

import com.example.mudahtest.repository.data.model.entity.ChatMessage
import com.example.mudahtest.repository.data.model.request.SendChatPayload
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    @POST("/api/users")
    suspend fun postChatMessage(@Body sendChatPayload: SendChatPayload): ChatMessage
}