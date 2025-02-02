package com.alonsocamina.vecicare.domain

import android.util.Log
import com.alonsocamina.vecicare.data.repository.ChuckJokesRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetChuckJokeUseCase @Inject constructor(
    private val repository: ChuckJokesRepository
){
    suspend operator fun invoke(): ChuckJoke? {
        return withContext(IO){
            Log.d("GetChuckJokeUseCase", "invoke")
            repository.getJoke()
        }
    }
}