package com.alonsocamina.vecicare.data.local.tareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsocamina.vecicare.data.local.tareas.dao.TaskHistoryDao
import com.alonsocamina.vecicare.data.local.tareas.entities.TaskHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskHistoryViewModel(private val taskHistoryDao: TaskHistoryDao) : ViewModel() {

    // Obtener todo el historial de tareas
    val allTaskHistory: Flow<List<TaskHistory>> = taskHistoryDao.getAllTaskHistory()

    // Insertar un nuevo historial
    fun insertTaskHistory(taskHistory: TaskHistory) {
        viewModelScope.launch {
            taskHistoryDao.insertTaskHistory(taskHistory)
        }
    }

    // Eliminar un historial
    fun deleteTaskHistory(taskHistory: TaskHistory) {
        viewModelScope.launch {
            taskHistoryDao.deleteTaskHistory(taskHistory)
        }
    }

    // Buscar un historial por ID
    fun getTaskHistoryById(historyId: Int): Flow<TaskHistory?> {
        return taskHistoryDao.getTaskHistoryById(historyId)
    }

}