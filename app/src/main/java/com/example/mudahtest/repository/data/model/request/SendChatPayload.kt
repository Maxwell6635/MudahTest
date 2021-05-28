package com.example.mudahtest.repository.data.model.request

import com.google.gson.annotations.SerializedName

data class SendChatPayload(
    @SerializedName("message")
    var message: String? = ""
)