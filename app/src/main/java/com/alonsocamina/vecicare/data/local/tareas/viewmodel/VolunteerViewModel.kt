package com.alonsocamina.vecicare.data.local.tareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsocamina.vecicare.data.local.tareas.dao.VolunteerDao
import com.alonsocamina.vecicare.data.local.tareas.entities.Volunteer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class VolunteerViewModel(private val volunteerDao: VolunteerDao) : ViewModel() {

    // Obtener todos los voluntarios como Flow
    val allVolunteers: Flow<List<Volunteer>> = volunteerDao.getAllVolunteers()

    // Insertar un nuevo voluntario
    fun insertVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            volunteerDao.insertVolunteer(volunteer)
        }
    }

    // Actualizar un voluntario
    fun updateVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            volunteerDao.updateVolunteer(volunteer)
        }
    }

    // Eliminar un voluntario
    fun deleteVolunteer(volunteer: Volunteer) {
        viewModelScope.launch {
            volunteerDao.deleteVolunteer(volunteer)
        }
    }

    // Buscar un voluntario por ID (manejo con una corutina para evitar el problema de tipo)
    fun getVolunteerById(volunteerId: Int): Flow<Volunteer?> {
        return flow {
            val volunteer = volunteerDao.getVolunteerById(volunteerId) // Suspend function
            emit(volunteer)
        }
    }
}