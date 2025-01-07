package com.alonsocamina.vecicare.ui.theme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VolunteerTaskDao {
    @Insert
    suspend fun insertTask(task: VolunteerTask) //Insertar una tarea

    @Query ("SELECT * FROM VolunteerTask")
    suspend fun getAllTask(): List<VolunteerTask> //Obtener todas las tareas
}