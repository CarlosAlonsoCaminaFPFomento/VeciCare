package com.alonsocamina.vecicare.data.local.tareas.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Beneficiary::class,
            parentColumns = ["id"],
            childColumns = ["beneficiaryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Volunteer::class,
            parentColumns = ["id"],
            childColumns = ["volunteerId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID único de la tarea.
    val taskTypeCode: Int, //Codigo del tipo de tarea
    val name: String, // Nombre de la tarea.
    val description: String, // Descripción detallada de la tarea.
    val status: String, // Estado de la tarea (por ejemplo: "Available", "Pending", "InProgress").
    val beneficiaryId: Int?, // ID del beneficiario que solicitó la tarea.
    val volunteerId: Int? = null// ID del voluntario asignado (puede ser null).
)
