package com.alonsocamina.vecicare.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.TaskViewModel
import com.alonsocamina.vecicare.data.local.tareas.entities.Task
import com.alonsocamina.vecicare.ui.shared.SharedMainScreen
import com.alonsocamina.vecicare.ui.shared.ActivityItem
import com.alonsocamina.vecicare.ui.shared.activities

@Composable
fun VolunteerScreen(
    taskViewModel: TaskViewModel = viewModel(),
    volunteerId: Int
) {
    val activitiesList = activities // Lista de actividades predefinidas
    var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }
    var showPendingTasks by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    // Escuchar las tareas pendientes desde el ViewModel
    val pendingTasks by taskViewModel.pendingTasks.collectAsState()

    // Pantalla principal con actividades
    SharedMainScreen(
        title = "Responder Solicitudes",
        tasks = activitiesList,
        onItemClick = { activity ->
            selectedActivity = activity
            val taskTypeCode = TaskViewModel.TaskTypeCodes.codes[activity.name]
            if (taskTypeCode != null) {
                taskViewModel.loadPendingTasksByType(taskTypeCode)
                showPendingTasks = true
            } else {
                message = "No se encontró el código para la actividad seleccionada."
            }
        }
    )

    // Mostrar lista de tareas pendientes
    if (showPendingTasks) {
        if (pendingTasks.isEmpty()) {
            AlertDialog(
                onDismissRequest = { showPendingTasks = false },
                title = { Text("Sin Solicitudes") },
                text = { Text("No hay solicitudes pendientes para ${selectedActivity?.name}.") },
                confirmButton = {
                    Button(onClick = { showPendingTasks = false }) {
                        Text("Aceptar")
                    }
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pendingTasks) { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            selectedTask = task
                            showDialog = true
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Beneficiario ID: ${task.beneficiaryId}, Tarea: ${task.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialog para confirmar asignación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Acción") },
            text = { Text("¿Estás seguro de que quieres aceptar esta tarea para ${selectedTask?.beneficiaryId}?") },
            confirmButton = {
                Button(
                    onClick = {
                        selectedTask?.let { task ->
                            taskViewModel.assignVolunteerToTask(task.id, volunteerId)
                            message = "Tarea asignada con éxito."
                        }
                        showDialog = false
                        showPendingTasks = false
                    }
                ) {
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

    // Mostrar mensajes
    message?.let {
        AlertDialog(
            onDismissRequest = { message = null },
            title = { Text("Mensaje") },
            text = { Text(it) },
            confirmButton = {
                Button(onClick = { message = null }) {
                    Text("Aceptar")
                }
            }
        )
    }
}