package com.alonsocamina.vecicare.data.local.chucknorrisjokes

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChuckJokeEntity::class], version = 1)
abstract class ChuckJokesRoomDb: RoomDatabase() {
    abstract fun chuckJokeDao(): ChuckJokeDao
}