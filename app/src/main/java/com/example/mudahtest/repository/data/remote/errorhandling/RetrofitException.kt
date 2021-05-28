package com.example.mudahtest.repository.data.remote.errorhandling

import com.example.mudahtest.repository.data.model.entity.APIErrorResponse
import com.google.gson.Gson
import io.reactivex.exceptions.CompositeException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class RetrofitException
private constructor(
    message: String?,
    /**
         * The request URL which produced the error.
         */
        val url: String?,
    /**
         * Response object containing status code, headers, body, etc.
         */
        val response: Response<*>?,

    val errorResponse: APIErrorResponse?,
    /**
         * The event kind which triggered this error.
         */
        val kind: Kind,

    exception: Throwable
) : RuntimeException(message, exception) {

    override fun toString(): String {
        return super.toString().plus(" : $kind : $url :").plus(response?.errorBody()?.string())
    }

    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Kind {
        /**
         * An [CompositeException] occurred when converting the response to json come along with HTTP 404  : UNEXPECTED
         */
        SERVER_UNEXPECTED,

        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,

        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED,

        /**
         * An [SocketTimeoutException] occurred while communicating to the server.
         */
        TIMEOUT,

        /**
         * An [UnknownHostException] occurred while communicating to the server.
         */
        NO_NETWORK
    }

    companion object {
        fun httpError(
                url: String,
                response: Response<*>,
                errorResponse: APIErrorResponse,
                httpException: HttpException
        ): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            return RetrofitException(
                    message,
                    url,
                    response,
                    errorResponse,
                    Kind.HTTP,
                    httpException
            )
        }

        // Usually is server is returning HTML format
        fun serverReturnUnexceptedError(exception: CompositeException): RetrofitException {
            return RetrofitException(
                    exception.message,
                    null,
                    null,
                    null,
                    Kind.SERVER_UNEXPECTED,
                    exception
            )
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(exception.message, null, null, null, Kind.NETWORK, exception)
        }

        fun noNetworkError(exception: UnknownHostException): RetrofitException {
            return RetrofitException(
                    exception.message,
                    null,
                    null,
                    APIErrorResponse("0", exception.message),
                    Kind.NO_NETWORK,
                    exception
            )
        }

        fun timeOutError(exception: SocketTimeoutException): RetrofitException {
            return RetrofitException(
                    exception.message,
                    null,
                    null,
                    APIErrorResponse("408", exception.message),
                    Kind.TIMEOUT,
                    exception
            )
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(exception.message, null, null, null, Kind.UNEXPECTED, exception)
        }

        fun asRetrofitException(throwable: Throwable): RetrofitException {
            if (throwable is RetrofitException) {
                return throwable
            }
            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response()
                val data = response?.errorBody()?.string().toString()
                try {
                    val errorResponse: APIErrorResponse = Gson().fromJson(
                            data,
                            APIErrorResponse::class.java
                    )
                    return httpError(
                            response?.raw()?.request()?.url().toString(),
                            response!!,
                            errorResponse,
                            throwable
                    )
                } catch (ex: Exception) {
                    return when (throwable) {
                        is CompositeException -> {
                            serverReturnUnexceptedError(throwable)
                        }
                        else -> unexpectedError(throwable)
                    }
                }
            }
            // A network error happened
            return when (throwable) {
                is UnknownHostException -> {
                    noNetworkError(throwable)
                }
                is SocketTimeoutException -> {
                    timeOutError(throwable)
                }
                is IOException -> {
                    networkError(throwable)
                }
                else -> unexpectedError(throwable)
            }
            // We don't know what happened. We need to simply convert to an unknown error
        }
    }
}