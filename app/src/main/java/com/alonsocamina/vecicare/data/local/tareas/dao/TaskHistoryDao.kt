package com.alonsocamina.vecicare.data.local.tareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alonsocamina.vecicare.data.local.tareas.entities.TaskHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskHistoryDao {

    // Insertar un nuevo historial
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskHistory(taskHistory: TaskHistory)

    // Eliminar un historial
    @Delete
    suspend fun deleteTaskHistory(taskHistory: TaskHistory)

    // Obtener el historial de tareas como Flow
    @Query("SELECT * FROM task_history")
    fun getAllTaskHistory(): Flow<List<TaskHistory>>

    // Buscar un historial por ID
    @Query("SELECT * FROM task_history WHERE id = :historyId")
    fun getTaskHistoryById(historyId: Int): Flow<TaskHistory?>
}
