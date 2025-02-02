package com.alonsocamina.vecicare.data.local.chucknorrisjokes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alonsocamina.vecicare.domain.ChuckJoke

@Entity(tableName = "chuck_jokes")
class ChuckJokeEntity (
    @PrimaryKey val id: String,
    val value: String
)

fun ChuckJokeEntity.toDomain() = ChuckJoke(this.id, this.value)

fun ChuckJoke.toEntity() = ChuckJokeEntity(this.id, this.value)