package com.alonsocamina.vecicare.data.repository

import android.util.Log
import com.alonsocamina.vecicare.data.network.ChuckJokesService
import com.alonsocamina.vecicare.data.network.toDomain
import com.alonsocamina.vecicare.domain.ChuckJoke
import javax.inject.Inject

class ChuckJokesRepository @Inject constructor(
    private val api: ChuckJokesService,
    private val cache: ChuckJokesCache
) {
    suspend fun getJoke(): ChuckJoke? = try {
        val response = api.getJoke()
        val joke = response.toDomain()
        cache.saveJoke(joke)
        joke
    } catch (e: Exception) {
        Log.e("ChuckJokesRepository", "Error getting joke", e)
        cache.getJoke()
    }
}