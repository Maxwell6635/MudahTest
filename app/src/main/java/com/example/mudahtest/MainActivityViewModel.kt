package com.example.mudahtest

import android.content.Context
import androidx.lifecycle.*
import com.example.mudahtest.repository.data.model.entity.ChatMessage
import com.example.mudahtest.repository.data.model.entity.ResultWrapper
import com.example.mudahtest.repository.data.model.request.SendChatPayload
import com.example.mudahtest.repository.data.model.response.ChatResponse
import com.example.mudahtest.repository.data.repository.ChatRepository
import com.example.mudahtest.util.DateFormatterUtils
import com.example.mudahtest.util.Utils
import com.example.mudahtest.util.Utils.getJsonDataFromAsset
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private var mContext: Context,
    private var repository: ChatRepository,
    private var gson: Gson
) : ViewModel() {

    val localChatList: LiveData<List<ChatMessage>> = repository.allWords.asLiveData()
    val mutableShowToastMessage: MutableLiveData<String> = MutableLiveData("")
    val showToastMessage: LiveData<String> = mutableShowToastMessage

    fun sendChat(message: String?) {
        if (message.isNullOrEmpty()) {
            mutableShowToastMessage.postValue("Please type your messages")
            return
        }
        if (Utils.isNetworkAvailable(mContext)) {
            viewModelScope.launch {
                repository.postChatMessage(SendChatPayload(message)).collect {
                    when (it) {
                        is ResultWrapper.NetworkError -> {
                            mutableShowToastMessage.postValue("Unable to connect to server, please try again later.")
                        }
                        is ResultWrapper.GenericError -> {
                            mutableShowToastMessage.postValue("Opps, something wrong is happen. Please try again later")
                        }
                        is ResultWrapper.Success -> {
                            var chatMessage = it.value
                            chatMessage.direction = "OUTGOING"
                            insert(chatMessage)
                        }
                    }
                }
            }
        } else {
            mutableShowToastMessage.postValue("Network is unavailable, please check your network connectivity")
        }
    }

    fun getLocalChatList() = viewModelScope.launch {
        repository.countChatTable.collect {
            if (it <= 0) {
                val jsonFileString = getJsonDataFromAsset(mContext, "localChat.json")
                var messageList: ChatResponse = gson.fromJson(jsonFileString, ChatResponse::class.java)
                insertLocalChatList(messageList)
            }
        }
    }

    private fun insertLocalChatList(chatResponse: ChatResponse) = viewModelScope.launch {
        chatResponse.chatList?.forEach {
            repository.insert(it)
        }
    }

    fun insertAreYouThereMessage() {
        var chatMessage = ChatMessage(
            message = "Are you there?",
            direction = "INCOMING",
            timestamp = DateFormatterUtils.getFormattedCurrentDay()
        )
        insert(chatMessage)
    }

   private fun insert(chatMessage: ChatMessage) = viewModelScope.launch {
        repository.insert(chatMessage)
    }

}

