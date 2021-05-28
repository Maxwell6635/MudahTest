package com.example.mudahtest.repository.data.model.entity

import com.google.gson.annotations.SerializedName

data class APIErrorResponse(

        @SerializedName(value = "statusCode", alternate = ["status"])
        var statusCode: String? = "",

        @SerializedName(value = "message", alternate = ["errorMessage"])
        var message: String? = "",

        @SerializedName("errorCode")
        var errorCode: Int? = 0

)
