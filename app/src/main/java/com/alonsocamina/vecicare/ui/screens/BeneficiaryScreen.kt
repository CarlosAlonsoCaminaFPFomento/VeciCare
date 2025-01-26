package com.alonsocamina.vecicare.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.TaskViewModel
import com.alonsocamina.vecicare.ui.shared.ActivityItem
import com.alonsocamina.vecicare.ui.shared.SharedMainScreen
import com.alonsocamina.vecicare.ui.shared.activities

@Composable
fun BeneficiaryScreen(
    taskViewModel: TaskViewModel = viewModel(),
    beneficiaryId: Int
) {
    val activitiesList = activities // Lista de actividades predefinidas
    var showDialog by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf<ActivityItem?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    // Estado para las tareas del beneficiario
    val beneficiaryTasks by taskViewModel.getTasksByBeneficiary(beneficiaryId).collectAsState(initial = emptyList())
    val pendingTaskTypes = beneficiaryTasks.filter { it.status == "PENDING" }.map { it.taskTypeCode }.toSet()

    Column(modifier = Modifier.fillMaxSize()) {
        // Mostrar tareas pendientes en la parte superior
        if (beneficiaryTasks.isNotEmpty()) {
            Text(
                text = "Peticiones de ayuda",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(beneficiaryTasks) { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tarea: ${task.name}")
                            Text("Estado: ${task.status}")
                            if (task.volunteerId != null) {
                                Text("Asignado a voluntario ID: ${task.volunteerId}")
                            }
                        }
                    }
                }
            }
        }

        // Pantalla principal con lista de actividades
        SharedMainScreen(
            title = "Solicitar Ayuda",
            tasks = activitiesList.filterNot { activity ->
                val taskTypeCode = TaskViewModel.TaskTypeCodes.codes[activity.name]
                taskTypeCode != null && taskTypeCode in pendingTaskTypes // Excluir actividades ya pendientes
            },
            onItemClick = { activity ->
                selectedActivity = activity
                showDialog = true
            }
        )
    }

    // Diálogo para confirmar solicitud de tarea
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Confirmar Acción") },
            text = { Text(text = "¿Estás seguro de que quieres solicitar ayuda con ${selectedActivity?.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            selectedActivity?.let { activity ->
                                val taskTypeCode = TaskViewModel.TaskTypeCodes.codes[activity.name]
                                if (taskTypeCode != null) {
                                    taskViewModel.createTask(
                                        taskName = activity.name,
                                        description = "Ayuda solicitada para ${activity.name}",
                                        status = "PENDING",
                                        beneficiaryId = beneficiaryId
                                    )
                                    message = "Solicitud creada con éxito para ${activity.name}."
                                } else {
                                    Log.e("BeneficiaryScreen", "Código de tipo de tarea no encontrado: ${activity.name}")
                                }
                            }
                            showDialog = false
                        } catch (e: Exception) {
                            Log.e("BeneficiaryScreen", "Error al solicitar ayuda: ${e.localizedMessage}")
                        }
                    }
                ) {
                    Text(text = "Sí")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "No")
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
