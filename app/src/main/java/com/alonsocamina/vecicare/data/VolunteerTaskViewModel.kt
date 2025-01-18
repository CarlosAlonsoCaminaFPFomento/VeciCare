package com.alonsocamina.vecicare.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class VolunteerTaskViewModel(private val dao: VolunteerTaskDao) : ViewModel() {
    //Lista de tareas
    val tasks = mutableListOf<VolunteerTask>()

    //Cargamos todas las tareas desde la  base de datos
    fun loadTasks() {
        viewModelScope.launch {
            try{
                tasks.clear()
                tasks.addAll(dao.getAllTask())
                Log.d("VolunteerTaskViewModel", "Tareas cargadas: ${tasks.joinToString{it.name}}")
            }
            catch (e : Exception) {
                Log.e("VolunteerTaskViewModel", "Error al cargar tareas", e)
            }
        }
    }

    //Actualizamos una tarea
    fun updateTask(task : VolunteerTask) {
        viewModelScope.launch {
            try{
                dao.updateTask(task)
                Log.d("VolunteerTaskViewModel", "Tarea actualizada: ${task.name}")
                loadTasks()
            }
            catch (e : Exception){
                Log.e("VolunteerTaskViewModel", "Error al actualizar tarea: ${task.name}", e)
            }
        }
    }

    //Eliminar una tarea
    fun deleteTask(task : VolunteerTask) {
        viewModelScope.launch {
            try{
                dao.deleteTask(task)
                Log.d("VolunteerTaskViewModel", "Tarea eliminada: ${task.name}")
                loadTasks()
            }
            catch (e : Exception){
                Log.e("VolunteerTaskViewModel", "Error al eliminar tarea: ${task.name}", e)
            }
        }
    }

    //Insertar una nueva tarea
    fun insertTask(task : VolunteerTask) {
        viewModelScope.launch {
            try{
                dao.insertTask(task)
                Log.d("VolunteerTaskViewModel", "Tarea insertada: ${task.name}")
                loadTasks()
            }
            catch (e : Exception) {
                Log.e("VolunteerTaskViewModel","Error al insertar tarea: ${task.name}", e)
            }
        }
    }
}