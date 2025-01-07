package com.alonsocamina.vecicare.ui.theme

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VolunteerTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID autogenerate
    val name: String, // Nombre de la actividad
    val description: String // Descripción de la actividad
)