package com.alonsocamina.vecicare.data.local.tareas.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.alonsocamina.vecicare.data.local.tareas.entities.Task

@Entity(
    tableName = "task_history",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID único del historial.
    val taskId: Int, // ID de la tarea asociada.
    val timestamp: Long, // Marca de tiempo del evento.
    val action: String, // Acción realizada (por ejemplo: "Created", "Assigned", "Completed").
    val actor: String // Quién realizó la acción (por ejemplo: "Beneficiary", "Volunteer").
)
