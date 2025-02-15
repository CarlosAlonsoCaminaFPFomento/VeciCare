package com.alonsocamina.vecicare.domain.beneficiaryusecase

import com.alonsocamina.vecicare.data.repository.TaskRepository
import com.alonsocamina.vecicare.data.local.tareas.entities.Task
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) = repository.insertTask(task)
}
