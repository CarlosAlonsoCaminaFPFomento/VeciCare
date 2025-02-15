package com.alonsocamina.vecicare.domain.taskusecase

import com.alonsocamina.vecicare.data.repository.TaskRepository
import javax.inject.Inject

class GetTasksByBeneficiaryUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(beneficiaryId: Int) = repository.getTasksByBeneficiary(beneficiaryId)
}
