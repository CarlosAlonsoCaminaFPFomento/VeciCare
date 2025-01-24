package com.alonsocamina.vecicare.data.local.tareas

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VolunteerTaskDao {

    // Beneficiario: Crear una nueva tarea
    @Insert
    suspend fun insertTask(task: VolunteerTask)

    // Obtener todas las tareas para administración/debugging
    @Query("SELECT * FROM volunteer_tasks")
    suspend fun getAllTasks(): List<VolunteerTask>

    // Actualizar una tarea
    @Update
    suspend fun updateTask(task: VolunteerTask)

    // Eliminar una tarea
    @Delete
    suspend fun deleteTask(task: VolunteerTask)

    // Beneficiario: Obtener tareas por estado
    @Query("SELECT * FROM volunteer_tasks WHERE beneficiary_id = :beneficiaryId AND status = :status")
    suspend fun getTasksByStatusForBeneficiary(beneficiaryId: Int, status: String): List<VolunteerTask>

    // Voluntario: Ver tareas disponibles (sin asignar)
    @Query("SELECT * FROM volunteer_tasks WHERE status = :status")
    suspend fun getAvailableTasks(status: String = TaskStatus.PENDING): List<VolunteerTask>

    // Voluntario: Tomar una tarea
    @Query("UPDATE volunteer_tasks SET volunteer_id = :volunteerId, status = :newStatus WHERE id = :taskId")
    suspend fun takeTask(taskId: Int, volunteerId: Int, newStatus: String = TaskStatus.IN_PROGRESS)

    // Voluntario: Ver tareas asignadas
    @Query("SELECT * FROM volunteer_tasks WHERE volunteer_id = :volunteerId")
    suspend fun getTasksForVolunteer(volunteerId: Int): List<VolunteerTask>

    // Cambiar estado de una tarea
    @Query("UPDATE volunteer_tasks SET status = :status WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Int, status: String)

    //Ver tareas por beneficiaro
    @Query("SELECT * FROM volunteer_tasks WHERE beneficiary_id = :beneficiaryId")
    suspend fun getTaskByBeneficiary(beneficiaryId: Int): List<VolunteerTask>

    //

}
