package com.alonsocamina.vecicare.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "volunteer_tasks")
data class VolunteerTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID autogenerate
    val name: String, // Nombre de la actividad
    val description: String // Descripción de la actividad
)