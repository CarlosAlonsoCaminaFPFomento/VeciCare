package com.alonsocamina.vecicare.data.local.tareas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class VolunteerTaskViewModel(private val dao: VolunteerTaskDao) : ViewModel() {

    // Lista de tareas para la IU
    val tasks = mutableListOf<VolunteerTask>()

    // Cargar tareas de un beneficiario (por estado)
    fun loadTasksForBeneficiary(beneficiaryId: Int, status: String = TaskStatus.PENDING) {
        viewModelScope.launch {
            try {
                tasks.clear()
                tasks.addAll(dao.getTasksByStatusForBeneficiary(beneficiaryId, status))
                Log.d("VolunteerTaskViewModel", "Tareas para beneficiario $beneficiaryId cargadas: ${tasks.size}")
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al cargar tareas para beneficiario", e)
            }
        }
    }

    //Actualizar tareas en base de datos y notificar cambios en lista de tareas
    fun updateTask(task: VolunteerTask, contextType: String, userId: Int) {
        viewModelScope.launch {
            try {
                dao.updateTask(task)
                when (contextType) {
                    "BENEFICIARY" -> loadTasksForBeneficiary(userId, TaskStatus.PENDING)
                    "VOLUNTEER" -> loadAvailableTasks()
                }
                Log.d("VolunteerTaskViewModel", "Tarea actualizada: ${task.name}")
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al actualizar tarea: ${task.name}", e)
            }
        }
    }

    // Cargar tareas disponibles para voluntarios
    fun loadAvailableTasks() {
        viewModelScope.launch {
            try {
                tasks.clear()
                tasks.addAll(dao.getAvailableTasks())
                Log.d("VolunteerTaskViewModel", "Tareas disponibles cargadas: ${tasks.size}")
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al cargar tareas disponibles", e)
            }
        }
    }

    // Seleccionar una tarea como voluntario
    fun takeTask(taskId: Int, volunteerId: Int) {
        viewModelScope.launch {
            try {
                dao.takeTask(taskId, volunteerId)
                Log.d("VolunteerTaskViewModel", "Tarea con ID $taskId seleccionada por voluntario $volunteerId")
                loadAvailableTasks()
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al seleccionar tarea con ID $taskId", e)
            }
        }
    }

    // Cargar tareas asignadas a un voluntario
    fun loadTasksForVolunteer(volunteerId: Int) {
        viewModelScope.launch {
            try {
                tasks.clear()
                tasks.addAll(dao.getTasksForVolunteer(volunteerId))
                Log.d("VolunteerTaskViewModel", "Tareas para voluntario $volunteerId cargadas: ${tasks.size}")
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al cargar tareas para voluntario", e)
            }
        }
    }

    // Cambiar el estado de una tarea
    fun updateTaskStatus(taskId: Int, status: String) {
        viewModelScope.launch {
            try {
                dao.updateTaskStatus(taskId, status)
                Log.d("VolunteerTaskViewModel", "Estado de tarea $taskId actualizado a $status")
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al actualizar estado de tarea $taskId", e)
            }
        }
    }

    // Insertar una tarea (Beneficiario)
    fun insertTask(task: VolunteerTask) {
        viewModelScope.launch {
            try {
                dao.insertTask(task)
                Log.d("VolunteerTaskViewModel", "Tarea insertada: ${task.name}")
                loadTasksForBeneficiary(task.beneficiaryID ?: -1, TaskStatus.PENDING)
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al insertar tarea: ${task.name}", e)
            }
        }
    }

    // Eliminar una tarea (Beneficiario)
    fun deleteTask(task: VolunteerTask) {
        viewModelScope.launch {
            try {
                dao.deleteTask(task)
                Log.d("VolunteerTaskViewModel", "Tarea eliminada: ${task.name}")
                loadTasksForBeneficiary(task.beneficiaryID ?: -1, TaskStatus.PENDING)
            } catch (e: Exception) {
                Log.e("VolunteerTaskViewModel", "Error al eliminar tarea: ${task.name}", e)
            }
        }
    }
}
