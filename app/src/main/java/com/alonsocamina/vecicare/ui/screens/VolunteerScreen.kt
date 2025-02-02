// VolunteerScreen.kt
package com.alonsocamina.vecicare.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alonsocamina.vecicare.data.local.tareas.dao.TaskDao
import com.alonsocamina.vecicare.data.local.tareas.entities.Task
import com.alonsocamina.vecicare.ui.shared.Section
import com.alonsocamina.vecicare.ui.shared.SharedMainScreen
import com.alonsocamina.vecicare.ui.shared.activities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun VolunteerScreen(
    volunteerId: Int,
    viewModel: VolunteerScreenViewModel
) {
    val tasksInProgress by viewModel.tasksInProgress.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()
    val pendingTasksByType by viewModel.pendingTasksByType.collectAsState()

    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var selectedActivity by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showPendingTasks by remember { mutableStateOf(false) }

    SharedMainScreen(
        title = "Responder Solicitudes",
        tasks = activities,
        onItemClick = { activity ->
            selectedActivity = activity.name
            viewModel.loadPendingTasksByType(viewModel.taskTypeCodes[activity.name]!!)
            showPendingTasks = true
        },
        contentSections = {
            if (tasksInProgress.isNotEmpty()) {
                Section(
                    title = "Solicitudes de Ayuda en Progreso",
                    tasks = tasksInProgress
                ) { task ->
                    viewModel.completeTask(task.id)
                }
            }

            if (showPendingTasks) {
                Section(
                    title = "Solicitudes Pendientes para $selectedActivity",
                    tasks = pendingTasksByType
                ) { task ->
                    selectedTask = task
                    showDialog = true
                }
            }

            if (completedTasks.isNotEmpty()) {
                Section(title = "Historial de Tareas Completadas", tasks = completedTasks)
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Acción") },
            text = { Text("¿Estás seguro de aceptar esta tarea?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.startTask(selectedTask!!.id, volunteerId)
                    showDialog = false
                    showPendingTasks = false
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

// VolunteerScreenViewModel
class VolunteerScreenViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val _tasksInProgress = MutableStateFlow<List<Task>>(emptyList())
    val tasksInProgress: StateFlow<List<Task>> get() = _tasksInProgress

    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> get() = _completedTasks

    private val _pendingTasksByType = MutableStateFlow<List<Task>>(emptyList())
    val pendingTasksByType: StateFlow<List<Task>> get() = _pendingTasksByType

    val taskTypeCodes = mapOf(
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

    fun loadPendingTasksByType(taskTypeCode: Int) {
        viewModelScope.launch {
            taskDao.getTasksByTypeAndStatus(taskTypeCode, "PENDING").collect {
                _pendingTasksByType.value = it
            }
        }
    }

    fun startTask(taskId: Int, volunteerId: Int) {
        viewModelScope.launch {
            val task = taskDao.getTaskById(taskId).firstOrNull()
            if (task != null) {
                val updatedTask = task.copy(volunteerId = volunteerId, status = "IN_PROGRESS")
                taskDao.updateTask(updatedTask)
                updateTasks()
            }
        }
    }

    fun completeTask(taskId: Int) {
        viewModelScope.launch {
            val task = taskDao.getTaskById(taskId).firstOrNull()
            if (task != null) {
                val updatedTask = task.copy(status = "AWAITING_CONFIRMATION")
                taskDao.updateTask(updatedTask)
                updateTasks()
            }
        }
    }

    fun updateTasks() {
        viewModelScope.launch {
            _tasksInProgress.value =
                taskDao.getTasksByStatus("IN_PROGRESS").firstOrNull() ?: emptyList()
            _completedTasks.value =
                taskDao.getTasksByStatus("COMPLETED").firstOrNull() ?: emptyList()
        }
    }
}

class VolunteerViewModelFactory(
    private val taskDao: TaskDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VolunteerScreenViewModel::class.java)) {
            return VolunteerScreenViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//Revision?