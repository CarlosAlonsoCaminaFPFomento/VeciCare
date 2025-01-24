package com.alonsocamina.vecicare.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.alonsocamina.vecicare.data.local.tareas.VolunteerTaskViewModel
import com.alonsocamina.vecicare.ui.shared.MessageBar
import com.alonsocamina.vecicare.ui.shared.SharedMainScreen

@Composable
fun VolunteerScreen(viewModel: VolunteerTaskViewModel, volunteerId: Int) {
    var message by remember { mutableStateOf<String?>(null) } // Mensaje a mostrar en la UI

    SharedMainScreen(
        title = "Responder Solicitudes",
        onItemClick = { activity ->
            // Buscar solicitudes pendientes
            val relatedTasks = viewModel.tasks.filter {
                it.name == activity.name && it.status == "PENDING"
            }

            if (relatedTasks.isNotEmpty()) {
                val taskToTake = relatedTasks.first()
                val updatedTask = taskToTake.copy(
                    status = "IN_PROGRESS",
                    volunteerID = volunteerId
                )
                viewModel.updateTask(
                    task = updatedTask,
                    contextType = "VOLUNTEER",
                    userId = volunteerId
                )
                message = "Solicitud aceptada para: ${activity.name}" // Mostrar mensaje en pantalla
            } else {
                message =
                    "No hay solicitudes pendientes para: ${activity.name}" // Mostrar mensaje en pantalla
            }
        }
    )

    // Muestra el mensaje en pantalla si está disponible
    message?.let {
        MessageBar(message = it, onDismiss = { message = null })
    }
}


