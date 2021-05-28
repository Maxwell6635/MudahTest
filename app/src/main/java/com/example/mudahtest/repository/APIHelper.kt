package com.example.mudahtest.repository

import com.example.mudahtest.repository.data.model.entity.ChatMessage
import com.example.mudahtest.repository.data.model.entity.ResultWrapper
import com.example.mudahtest.repository.data.model.request.SendChatPayload
import com.example.mudahtest.repository.data.remote.api.API
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class APIHelper(private val api: API) : APIHelperRef {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun postChatMessage(sendChatPayload: SendChatPayload): ResultWrapper<ChatMessage> {
        return safeApiCall(dispatcher) { api.postChatMessage(sendChatPayload) }
    }
}
