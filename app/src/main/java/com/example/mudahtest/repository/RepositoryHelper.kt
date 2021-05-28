package com.example.mudahtest.repository

import com.example.mudahtest.repository.data.model.entity.APIErrorResponse
import com.example.mudahtest.repository.data.model.entity.ResultWrapper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ResultWrapper.GenericError(code, errorResponse, throwable)
                }
                else -> {
                    ResultWrapper.GenericError(null, null, throwable)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): APIErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.let {
            val stringBuffer = it.string()
            return Gson().fromJson(stringBuffer, APIErrorResponse::class.java)
        }
    } catch (exception: Exception) {
        exception.printStackTrace()
        null
    }
}
