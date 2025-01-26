package com.alonsocamina.vecicare.data.local.tareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsocamina.vecicare.data.local.tareas.dao.BeneficiaryDao
import com.alonsocamina.vecicare.data.local.tareas.entities.Beneficiary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BeneficiaryViewModel(private val beneficiaryDao: BeneficiaryDao) : ViewModel() {

    // Obtener todos los beneficiarios como Flow
    val allBeneficiaries: Flow<List<Beneficiary>> = beneficiaryDao.getAllBeneficiaries()

    // Insertar un nuevo beneficiario
    fun insertBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            beneficiaryDao.insertBeneficiary(beneficiary)
        }
    }

    // Actualizar un beneficiario
    fun updateBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            beneficiaryDao.updateBeneficiary(beneficiary)
        }
    }

    // Eliminar un beneficiario
    fun deleteBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            beneficiaryDao.deleteBeneficiary(beneficiary)
        }
    }

    // Buscar un beneficiario por ID
    fun getBeneficiaryById(beneficiaryId: Int): Flow<Beneficiary?> {
        return beneficiaryDao.getBeneficiaryById(beneficiaryId)
    }
}