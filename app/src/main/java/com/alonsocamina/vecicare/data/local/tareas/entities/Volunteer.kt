package com.alonsocamina.vecicare.data.local.tareas.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volunteers")
data class Volunteer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID único del voluntario.
    val name: String, // Nombre del voluntario.
    val surname: String, // Apellido del voluntario.
    val email: String, // Correo electrónico del voluntario.
    val phoneNumber: String, // Número de teléfono.
    val address: String, // Dirección del voluntario.
    val skills: String // Descripción de habilidades específicas.
)
