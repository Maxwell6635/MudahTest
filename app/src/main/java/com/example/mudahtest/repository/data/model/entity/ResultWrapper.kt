package com.example.mudahtest.repository.data.model.entity

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class GenericError(
        val code: Int? = 0,
        val error: APIErrorResponse? = null,
        val throwable: Throwable
    ) : ResultWrapper<Nothing>()

    object NetworkError : ResultWrapper<Nothing>()
}