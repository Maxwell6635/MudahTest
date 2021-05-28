package com.example.mudahtest.repository.data.remote

import com.example.mudahtest.repository.data.remote.api.API
import com.example.mudahtest.repository.data.remote.errorhandling.RxErrorHandlingCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitService {

    companion object {
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().apply {
                retryOnConnectionFailure(true)
                readTimeout(30, TimeUnit.SECONDS)
                connectTimeout(30, TimeUnit.SECONDS)
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }.build()
        }

        fun provideAPI(retrofit: Retrofit): API {
            return retrofit.create(API::class.java)
        }
    }
}

