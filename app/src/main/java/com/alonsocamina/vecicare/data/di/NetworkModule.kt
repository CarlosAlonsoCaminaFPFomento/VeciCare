package com.alonsocamina.vecicare.data.di

import com.alonsocamina.vecicare.data.network.ChuckJokesService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideChuckJokesService(json: Json): ChuckJokesService =
        Retrofit
            .Builder()
            .baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
            .build()
            .create(ChuckJokesService::class.java)
}