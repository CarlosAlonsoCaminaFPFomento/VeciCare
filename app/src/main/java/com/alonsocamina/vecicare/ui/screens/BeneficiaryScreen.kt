package com.alonsocamina.vecicare.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.alonsocamina.vecicare.data.local.tareas.VolunteerTask
import com.alonsocamina.vecicare.data.local.tareas.VolunteerTaskViewModel
import com.alonsocamina.vecicare.ui.shared.MessageBar
import com.alonsocamina.vecicare.ui.shared.SharedMainScreen

@Composable
fun BeneficiaryScreen(viewModel: VolunteerTaskViewModel, beneficiaryId: Int) {
    var message by remember { mutableStateOf<String?>(null) } // Mensaje a mostrar en la UI

    SharedMainScreen(
        title = "Solicitar Ayuda",
        onItemClick = { activity ->
            // Crear una solicitud de ayuda
            val newTask = VolunteerTask(
                name = activity.name,
                description = "Solicitud creada por el beneficiario",
                status = "PENDING",
                beneficiaryID = beneficiaryId
            )
            viewModel.insertTask(newTask)
            message = "Solicitud creada para: ${activity.name}" // Mostrar mensaje en pantalla
        }
    )

    // Muestra el mensaje en pantalla si está disponible
    message?.let {
        MessageBar(message = it, onDismiss = { message = null })
    }
}




