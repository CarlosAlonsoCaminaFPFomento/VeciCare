package com.alonsocamina.vecicare.data.local.tareas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "volunteer_tasks")
data class VolunteerTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID autogenerate
    val name: String, // Nombre de la actividad
    val description: String, // Descripción de la actividad
    val status: String = TaskStatus.PENDING, //Estado de la tarea
    @ColumnInfo(name = "beneficiary_id") val beneficiaryID: Int? = null, //ID del beneficiario que solicitó ayuda con la tarea
    @ColumnInfo(name = "volunteer_id") val volunteerID: Int? = null //ID del voluntario asignado
)