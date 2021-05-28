package com.example.mudahtest.di

import android.app.Application
import com.example.mudahtest.MainActivityViewModel
import com.example.mudahtest.repository.APIHelper
import com.example.mudahtest.repository.data.local.ChatRoomDatabase
import com.example.mudahtest.repository.data.local.dao.ChatMessageDao
import com.example.mudahtest.repository.data.remote.RetrofitService
import com.example.mudahtest.repository.data.repository.ChatRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): ChatRoomDatabase {
        return ChatRoomDatabase.getDatabase(application)
    }

    fun provideCountriesDao(database: ChatRoomDatabase): ChatMessageDao {
        return database.chatMessageDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }
}

val repositoryModule = module {
    factory { RetrofitService.provideOkHttpClient() }
    factory { RetrofitService.provideAPI(get()) }
    single { RetrofitService.provideRetrofit(get()) }
    single { APIHelper(get()) }

    //Repository
    single { ChatRepository(get(), get()) }
}

val appModule = module {
    //Gson
    single { Gson() }
}

val viewModelModule = module {
    //Home
    viewModel { MainActivityViewModel(get(), get(), get()) }
}

val allModules = listOf(appModule, databaseModule, repositoryModule, viewModelModule)