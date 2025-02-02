// BeneficiaryScreen.kt
package com.alonsocamina.vecicare.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun BeneficiaryScreen(
    beneficiaryId: Int,
    viewModel: BeneficiaryScreenViewModel
) {
    val beneficiaryTasks by viewModel.beneficiaryTasks.collectAsState()
    val tasksAwaitingConfirmation by viewModel.tasksAwaitingConfirmation.collectAsState()
    val taskHistory by viewModel.taskHistory.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf<String?>(null) }

    // Cargar las tareas al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadTasksForBeneficiary(beneficiaryId)
    }

    SharedMainScreen(
        title = "Solicitar Ayuda",
        tasks = activities.filterNot { activity ->
            val taskTypeCode = viewModel.taskTypeCodes[activity.name]
            beneficiaryTasks.any { it.taskTypeCode == taskTypeCode && it.status != "COMPLETED" }
        },
        onItemClick = { activity ->
            selectedActivity = activity.name
            showDialog = true
        },
        contentSections = {
            if (tasksAwaitingConfirmation.isNotEmpty()) {
                Section(
                    title = "Tareas en Espera de Confirmación",
                    tasks = tasksAwaitingConfirmation
                ) { task ->
                    viewModel.confirmTaskCompletion(task.id)
                }
            }

            if (beneficiaryTasks.isNotEmpty()) {
                Section(title = "Estado de tus solicitudes de ayuda", tasks = beneficiaryTasks)
            }

            if (taskHistory.isNotEmpty()) {
                Section(title = "Historial de tareas", tasks = taskHistory)
            }
        }
    )

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Solicitud") },
            text = { Text("¿Estás seguro de que deseas solicitar ayuda con $selectedActivity?") },
            confirmButton = {
                Button(onClick = {
                    val taskTypeCode = viewModel.taskTypeCodes[selectedActivity!!]
                    if (taskTypeCode != null) {
                        viewModel.createTask(
                            Task(
                                taskTypeCode = taskTypeCode,
                                name = selectedActivity!!,
                                description = "Ayuda solicitada para $selectedActivity",
                                status = "PENDING",
                                beneficiaryId = beneficiaryId
                            )
                        )
                    }
                    showDialog = false
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

// BeneficiaryScreenViewModel
class BeneficiaryScreenViewModel(private val taskDao: TaskDao) : ViewModel() {

    private val _beneficiaryTasks = MutableStateFlow<List<Task>>(emptyList())
    val beneficiaryTasks: StateFlow<List<Task>> get() = _beneficiaryTasks

    private val _tasksAwaitingConfirmation = MutableStateFlow<List<Task>>(emptyList())
    val tasksAwaitingConfirmation: StateFlow<List<Task>> get() = _tasksAwaitingConfirmation

    private val _taskHistory = MutableStateFlow<List<Task>>(emptyList())
    val taskHistory: StateFlow<List<Task>> get() = _taskHistory

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

    fun loadTasksForBeneficiary(beneficiaryId: Int) {
        viewModelScope.launch {
            taskDao.getTasksByBeneficiary(beneficiaryId).collect { tasks ->
                _beneficiaryTasks.value =
                    tasks.filter { it.status == "PENDING" || it.status == "IN_PROGRESS" }
                _tasksAwaitingConfirmation.value =
                    tasks.filter { it.status == "AWAITING_CONFIRMATION" }
                _taskHistory.value = tasks.filter { it.status == "COMPLETED" }
            }
        }
    }

    fun confirmTaskCompletion(taskId: Int) {
        viewModelScope.launch {
            val task = taskDao.getTaskById(taskId).firstOrNull()
            if (task != null && task.status == "AWAITING_CONFIRMATION") {
                val updatedTask = task.copy(status = "COMPLETED")
                taskDao.updateTask(updatedTask)
                loadTasksForBeneficiary(task.beneficiaryId!!)
            }
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
            loadTasksForBeneficiary(task.beneficiaryId!!)
        }
    }
}

class BeneficiaryViewModelFactory(
    private val taskDao: TaskDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BeneficiaryScreenViewModel::class.java)) {
            return BeneficiaryScreenViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
