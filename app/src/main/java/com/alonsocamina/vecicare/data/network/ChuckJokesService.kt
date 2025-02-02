package com.alonsocamina.vecicare.data.network

import com.alonsocamina.vecicare.domain.ChuckJoke
import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface ChuckJokesService {
    @GET("jokes/random")
    suspend fun getJoke(): ResponseChuckJoke
}

@Serializable
data class ResponseChuckJoke(
    val id: String,
    val value: String
)

fun ResponseChuckJoke.toDomain() = ChuckJoke(id = id, value = value)

