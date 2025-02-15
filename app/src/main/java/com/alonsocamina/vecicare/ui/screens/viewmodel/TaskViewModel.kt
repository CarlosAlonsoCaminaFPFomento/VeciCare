package com.alonsocamina.vecicare.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsocamina.vecicare.data.local.tareas.entities.Task
import com.alonsocamina.vecicare.domain.taskusecase.ConfirmTaskCompletionUseCase
import com.alonsocamina.vecicare.domain.taskusecase.InsertTaskUseCase
import com.alonsocamina.vecicare.domain.taskusecase.UpdateTaskUseCase
import com.alonsocamina.vecicare.domain.taskusecase.DeleteTaskUseCase
import com.alonsocamina.vecicare.domain.taskusecase.GetTasksByBeneficiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val getTasksByBeneficiaryUseCase: GetTasksByBeneficiaryUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val confirmTaskCompletionUseCase: ConfirmTaskCompletionUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _tasksAwaitingConfirmation = MutableStateFlow<List<Task>>(emptyList())
    val tasksAwaitingConfirmation: StateFlow<List<Task>> = _tasksAwaitingConfirmation.asStateFlow()

    private val _taskHistory = MutableStateFlow<List<Task>>(emptyList())
    val taskHistory: StateFlow<List<Task>> = _taskHistory.asStateFlow()

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
            getTasksByBeneficiaryUseCase(beneficiaryId).collect { tasks ->
                _tasks.value = tasks.filter { it.status == "PENDING" || it.status == "IN_PROGRESS" }
                _tasksAwaitingConfirmation.value = tasks.filter { it.status == "AWAITING_CONFIRMATION" }
                _taskHistory.value = tasks.filter { it.status == "COMPLETED" }
            }
        }
    }

    fun confirmTaskCompletion(taskId: Int) {
        viewModelScope.launch {
            confirmTaskCompletionUseCase(taskId)
            _tasksAwaitingConfirmation.value = _tasksAwaitingConfirmation.value.filterNot { it.id == taskId }
            loadTasksForBeneficiary(_tasks.value.firstOrNull()?.beneficiaryId ?: return@launch)
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            insertTaskUseCase(task)
            loadTasksForBeneficiary(task.beneficiaryId ?: 0)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
            loadTasksForBeneficiary(task.beneficiaryId ?: 0)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
            loadTasksForBeneficiary(task.beneficiaryId ?: 0)
        }
    }
}

