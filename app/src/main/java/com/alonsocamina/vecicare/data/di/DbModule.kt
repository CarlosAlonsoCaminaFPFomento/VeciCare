package com.alonsocamina.vecicare.data.di

import android.content.Context
import androidx.room.Room
import com.alonsocamina.vecicare.data.local.chucknorrisjokes.ChuckJokeDao
import com.alonsocamina.vecicare.data.local.chucknorrisjokes.ChuckJokesRoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule{

    @Provides
    @Singleton
    fun provideChuckJokesRoomDb(@ApplicationContext context: Context): ChuckJokesRoomDb =
        Room.databaseBuilder(
        context, ChuckJokesRoomDb::class.java, "chuck_jokes.db"
    ).build()

    @Provides
    @Singleton
    fun provideChuckJokesDao(db:ChuckJokesRoomDb): ChuckJokeDao = db.chuckJokeDao()
}