package com.alonsocamina.vecicare.data.local.tareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsocamina.vecicare.data.local.tareas.dao.TaskDao
import com.alonsocamina.vecicare.data.local.tareas.entities.Task
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {

    // Código de tipos de tareas
    object TaskTypeCodes {
        val codes = mapOf(
            "Comprar alimentos" to 1,
            "Recoger medicinas" to 2,
            "Acompañamiento virtual" to 3,
            "Acompañamiento presencial" to 4,
            "Trámites administrativos" to 5,
            "Asistencia técnica" to 6,
            "Paseo de mascotas" to 7,
            "Pequeñas reparaciones" to 8,
            "Eventos locales" to 9
        )
    }

    // Obtener todas las tareas disponibles como Flow
    val allTasks = taskDao.getAllTasks()

    // Estado para las tareas pendientes por tipo
    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTasks: StateFlow<List<Task>> = _pendingTasks

    init {
        viewModelScope.launch {
            try {
                val tasks = taskDao.getAllTasks().firstOrNull()
                Log.d("TaskViewModel", "Tareas iniciales en la base de datos: $tasks")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al recuperar tareas iniciales: ${e.message}")
            }
        }
    }

    // Insertar una nueva tarea
    fun insertTask(task: Task) {
        viewModelScope.launch {
            try {
                taskDao.insertTask(task)
                Log.d("TaskViewModel", "Tarea insertada: $task")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al insertar tarea: ${e.localizedMessage}")
            }
        }
    }

    // Crear una nueva tarea
    fun createTask(taskName: String, description: String, status: String, beneficiaryId: Int) {
        viewModelScope.launch {
            try {
                // Obtener el código de tipo de tarea desde el mapeo
                val taskTypeCode = TaskTypeCodes.codes[taskName]
                    ?: throw IllegalArgumentException("Nombre de tarea no reconocido: $taskName")

                // Validar el ID del beneficiario
                if (beneficiaryId <= 0) {
                    Log.e("TaskViewModel", "ID de beneficiario inválido: $beneficiaryId")
                    return@launch
                }

                // Crear la tarea
                val newTask = Task(
                    taskTypeCode = taskTypeCode,
                    name = taskName,
                    description = description,
                    status = status,
                    beneficiaryId = beneficiaryId
                )

                // Insertar la tarea en la base de datos
                taskDao.insertTask(newTask)
                Log.d("TaskViewModel", "Tarea creada: $newTask")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al crear la tarea: ${e.localizedMessage}")
            }
        }
    }

    // Asignar un voluntario a una tarea
    fun assignVolunteerToTask(taskId: Int, volunteerId: Int) {
        viewModelScope.launch {
            try {
                val task = taskDao.getTaskById(taskId).firstOrNull()
                if (task != null && task.status == "PENDING") {
                    val updatedTask = task.copy(
                        volunteerId = volunteerId,
                        status = "IN_PROGRESS"
                    )
                    taskDao.updateTask(updatedTask)
                    Log.d("TaskViewModel", "Voluntario asignado a la tarea: $updatedTask")
                } else {
                    Log.e("TaskViewModel", "Tarea no encontrada o no está en estado PENDING.")
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al asignar voluntario a la tarea: ${e.localizedMessage}")
            }
        }
    }

    // Cargar tareas pendientes por tipo
    fun loadPendingTasksByType(taskTypeCode: Int) {
        viewModelScope.launch {
            try {
                val tasks = taskDao.getTasksByTypeAndStatus(taskTypeCode, "PENDING").firstOrNull() ?: emptyList()
                _pendingTasks.value = tasks
                Log.d("TaskViewModel", "Tareas pendientes cargadas para el tipo $taskTypeCode: $tasks")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al cargar tareas pendientes: ${e.localizedMessage}")
            }
        }
    }

    // Obtener tareas por tipo y estado como Flow
    fun getTasksByTypeAndStatus(taskTypeCode: Int, status: String): Flow<List<Task>> {
        return taskDao.getTasksByTypeAndStatus(taskTypeCode, status)
    }

    // Actualizar una tarea
    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskDao.updateTask(task)
                Log.d("TaskViewModel", "Tarea actualizada: $task")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al actualizar tarea: ${e.localizedMessage}")
            }
        }
    }

    // Eliminar una tarea
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                taskDao.deleteTask(task)
                Log.d("TaskViewModel", "Tarea eliminada: $task")
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error al eliminar tarea: ${e.localizedMessage}")
            }
        }
    }

    // Obtener tareas por estado como Flow
    fun getTasksByStatus(status: String): Flow<List<Task>> {
        return taskDao.getTasksByStatus(status)
    }

    // Buscar una tarea por ID como Flow
    fun getTaskById(taskId: Int): Flow<Task?> {
        return taskDao.getTaskById(taskId)
    }

    // Obtener tareas asignadas a un beneficiario como Flow
    fun getTasksByBeneficiary(beneficiaryId: Int): Flow<List<Task>> {
        return taskDao.getTasksByBeneficiary(beneficiaryId)
    }

    // Obtener tareas asignadas a un voluntario como Flow
    fun getTasksByVolunteer(volunteerId: Int): Flow<List<Task>> {
        return taskDao.getTasksByVolunteer(volunteerId)
    }

    // Obtener tareas según el usuario y su rol
    fun getTasksForUser(userId: Int, role: String): Flow<List<Task>> {
        return if (role == "Beneficiario/a") {
            getTasksByBeneficiary(userId)
        } else {
            getTasksByVolunteer(userId)
        }
    }
}
