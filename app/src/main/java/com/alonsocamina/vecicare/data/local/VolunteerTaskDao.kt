package com.alonsocamina.vecicare.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface VolunteerTaskDao {
    @Insert
    suspend fun insertTask(task: VolunteerTask) //Insertar una tarea

    @Query ("SELECT * FROM volunteer_tasks")
    suspend fun getAllTask(): List<VolunteerTask> //Obtener todas las tareas

    @Update
    suspend fun updateTask(task: VolunteerTask)

    @Delete
    suspend fun deleteTask(task: VolunteerTask) //Eliminar una tarea
}