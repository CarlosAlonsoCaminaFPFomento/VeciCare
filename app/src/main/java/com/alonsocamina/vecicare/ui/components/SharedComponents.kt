package com.alonsocamina.vecicare.ui.shared

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alonsocamina.vecicare.R
import com.alonsocamina.vecicare.data.local.tareas.entities.Task
import com.alonsocamina.vecicare.ui.theme.VeciCareTheme
import com.alonsocamina.vecicare.ui.theme.gradientBrush

data class ActivityItem(val name: String, val iconRes: Int)

val activities = listOf(
    ActivityItem("Comprar alimentos", R.drawable.ic_supermarket),
    ActivityItem("Recoger medicinas", R.drawable.ic_medicine),
    ActivityItem("Acompañamiento virtual", R.drawable.ic_virtualmeeting),
    ActivityItem("Acompañamiento presencial", R.drawable.ic_handshake),
    ActivityItem("Trámites administrativos", R.drawable.ic_document),
    ActivityItem("Asistencia técnica", R.drawable.ic_support),
    ActivityItem("Paseo de mascotas", R.drawable.ic_pet),
    ActivityItem("Pequeñas reparaciones", R.drawable.ic_repair),
    ActivityItem("Eventos locales", R.drawable.ic_event)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedMainScreen(
    title: String,
    tasks: List<ActivityItem>,
    onItemClick: (ActivityItem) -> Unit,
    contentSections: @Composable (() -> Unit)? = null
) {
    Log.d("SharedMainScreen", "Mostrando la pantalla con título: $title")
    Log.d("SharedMainScreen", "Cantidad de actividades cargadas: ${tasks.size}")

    VeciCareTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "VeciCare",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
        ) { innerPadding ->
            GradientBackground(darkTheme = isSystemInDarkTheme()) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Conectando a la comunidad",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    ThemedImage(modifier = Modifier.align(Alignment.CenterHorizontally))

                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    contentSections?.invoke()

                    ActivityList(activities = tasks, onItemClick = onItemClick)
                }
            }
        }
    }
}

@Composable
fun Section(title: String, tasks: List<Task>, onClick: ((Task) -> Unit)? = null) {
    Text(text = title, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(vertical = 8.dp))
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tasks) { task ->
            TaskCard(task = task, buttonText = onClick?.let { "Aceptar" }, onClick = onClick?.let { { it(task) } })
        }
    }
}


@Composable
fun ActivityList(activities: List<ActivityItem>, onItemClick: (ActivityItem) -> Unit) {
    Log.d("ActivityList", "Mostrando lista de actividades. Total: ${activities.size}")
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(activities) { activity ->
            Log.d("ActivityList", "Mostrando actividad: ${activity.name}")
            ActivityCard(activity = activity, onClick = {
                try {
                    onItemClick(activity)
                    Log.d("ActivityList", "Actividad seleccionada correctamente: ${activity.name}")
                } catch (e: Exception) {
                    Log.e("ActivityList", "Error al seleccionar actividad: ${e.message}")
                }
            })
        }
    }
}

@Composable
fun ActivityCard(activity: ActivityItem, onClick: () -> Unit) {
    Log.d("ActivityCard", "Creando tarjeta para la actividad: ${activity.name}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
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
            Image(
                painter = painterResource(id = activity.iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = activity.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TaskCard(task: Task, buttonText: String? = null, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tarea: ${task.name}")
            Text("Descripción: ${task.description}")
            Text("Estado: ${task.status}")
            if (buttonText != null && onClick != null) {
                Button(onClick = onClick, modifier = Modifier.padding(top = 8.dp)) {
                    Text(buttonText)
                }
            }
        }
    }
}

@Composable
fun GradientBackground(darkTheme: Boolean, content: @Composable () -> Unit) {
    Log.d("GradientBackground", "Configurando fondo con tema oscuro: $darkTheme")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush(darkTheme))
    ) {
        content()
    }
}

@Composable
fun ThemedImage(modifier: Modifier = Modifier) {
    val imageRes = if (isSystemInDarkTheme()) {
        R.drawable.baseline_accessibility_new_24_black
    } else {
        R.drawable.baseline_accessibility_new_24_white
    }
    Log.d("ThemedImage", "Mostrando imagen con recurso: $imageRes")
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Imagen representativa",
        modifier = modifier.size(128.dp)
    )
}

@Composable
fun MessageBar(
    message: String,
    onDismiss: () -> Unit,
    durationMillis: Long = 5000 // Duración en milisegundos (5 segundos por defecto)
) {
    Log.d("MessageBar", "Mostrando barra de mensaje: $message")
    LaunchedEffect(message) {
        kotlinx.coroutines.delay(durationMillis)
        Log.d("MessageBar", "Ocultando barra de mensaje tras $durationMillis ms")
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier.widthIn(max = 300.dp), // Limita el ancho máximo
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Cerrar",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
