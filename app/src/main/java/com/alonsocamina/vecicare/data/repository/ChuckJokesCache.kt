package com.alonsocamina.vecicare.data.repository

import android.content.SharedPreferences
import com.alonsocamina.vecicare.data.local.chucknorrisjokes.ChuckJokeDao
import com.alonsocamina.vecicare.data.local.chucknorrisjokes.toDomain
import com.alonsocamina.vecicare.data.local.chucknorrisjokes.toEntity
import com.alonsocamina.vecicare.domain.ChuckJoke
import javax.inject.Inject

class ChuckJokesCache @Inject constructor(
    //private val sharedPreferences: SharedPreferences,
    private val dao: ChuckJokeDao
){

    fun getJoke(): ChuckJoke? {
       // return if(sharedPreferences.getString("joke", null) != null) {
         return dao.getAll()?.shuffled()?.first()?.toDomain()
        //} else {
         //   null
        //}
    }

    fun saveJoke(joke: ChuckJoke) {
        //sharedPreferences.edit().putString("joke", joke.value).apply()
        dao.insertAll(joke.toEntity())
    }
}