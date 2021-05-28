package com.example.mudahtest.repository.data.repository

import androidx.annotation.WorkerThread
import com.example.mudahtest.repository.APIHelper
import com.example.mudahtest.repository.data.local.dao.ChatMessageDao
import com.example.mudahtest.repository.data.model.entity.ChatMessage
import com.example.mudahtest.repository.data.model.entity.ResultWrapper
import com.example.mudahtest.repository.data.model.request.SendChatPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ChatRepository(private var apiHelper: APIHelper, private val chatMessageDao: ChatMessageDao) {

    suspend fun postChatMessage(payload: SendChatPayload): Flow<ResultWrapper<ChatMessage>> {
        return flow {
            emit(apiHelper.postChatMessage(payload))
        }.flowOn(Dispatchers.IO)
    }

    val allWords: Flow<List<ChatMessage>> = chatMessageDao.getChatMessages()

    val countChatTable: Flow<Int> = chatMessageDao.getChatTableSize()

    @WorkerThread
    suspend fun insert(chatMessage: ChatMessage) {
        chatMessageDao.insert(chatMessage)
    }
}