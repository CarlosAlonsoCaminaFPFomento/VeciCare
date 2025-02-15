package com.alonsocamina.vecicare.domain.taskusecase

import com.alonsocamina.vecicare.data.repository.TaskRepository
import javax.inject.Inject

class ConfirmTaskCompletionUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Int) = repository.confirmTaskCompletion(taskId)
}
