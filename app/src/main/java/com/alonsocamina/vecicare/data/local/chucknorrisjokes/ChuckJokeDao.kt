package com.alonsocamina.vecicare.data.local.chucknorrisjokes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChuckJokeDao {
    @Query("SELECT * FROM chuck_jokes")
    fun getAll(): List<ChuckJokeEntity>

    @Query("SELECT * FROM chuck_jokes WHERE id IN (:jokeIds)")
    fun loadAllByIds(jokeIds: IntArray): List<ChuckJokeEntity>

    @Insert
    fun insertAll(vararg jokes: ChuckJokeEntity)

    @Delete
    fun delete(joke: ChuckJokeEntity)
}