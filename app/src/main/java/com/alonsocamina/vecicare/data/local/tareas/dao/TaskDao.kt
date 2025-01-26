package com.alonsocamina.vecicare.data.local.tareas.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alonsocamina.vecicare.data.local.tareas.entities.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): Flow<Task?>

    @Query("SELECT * FROM tasks WHERE status = :status")
    fun getTasksByStatus(status: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE taskTypeCode = :taskTypeCode AND status = :status")
    fun getTasksByTypeAndStatus(taskTypeCode: Int, status: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE beneficiaryId = :beneficiaryId")
    fun getTasksByBeneficiary(beneficiaryId: Int): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE volunteerId = :volunteerId")
    fun getTasksByVolunteer(volunteerId: Int): Flow<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>
}

