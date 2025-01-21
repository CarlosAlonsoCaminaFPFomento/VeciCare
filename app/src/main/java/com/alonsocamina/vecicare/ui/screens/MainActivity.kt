@file:OptIn(ExperimentalMaterial3Api::class)

package com.alonsocamina.vecicare.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alonsocamina.vecicare.ui.theme.VeciCareTheme
import com.alonsocamina.vecicare.ui.theme.gradientBrush
import android.util.Log
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.alonsocamina.vecicare.R
import com.alonsocamina.vecicare.data.local.SQLiteHelper
import com.alonsocamina.vecicare.data.local.VolunteerDatabase
import com.alonsocamina.vecicare.data.local.VolunteerTask
import com.alonsocamina.vecicare.data.local.VolunteerTaskViewModel
import com.alonsocamina.vecicare.ui.navigation.NavGraph


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

class MainActivity : ComponentActivity() {
    private lateinit var database: VolunteerDatabase //Base de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleMainActivity", "onCreate llamado: Configuración inicial de la app.")

        //Inicialización de Room
        val database = Room.databaseBuilder(
            applicationContext,
            VolunteerDatabase::class.java,
            "volunteer_database" //Nombre de la base de datos
        ).build()
        Log.d("Database", "Base de datos inicializada")

        //Inicializamos el ViewModel
        val viewModel = VolunteerTaskViewModel(database.volunteerTaskDao())

        //Prueba verificación de funcionalidad de Room
        testDatabaseOperations(viewModel)

        //Creación de la interfaz del usuario
        setContent {
            VeciCareTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleMainActivity", "onStart llamado: La app está visible pero no interactiva.")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleMainActivity", "onResume llamado: La app está visible e interactiva.")

        //EJemplo de interacción con SQLiteHelper
        val dbHelper = SQLiteHelper(this)

        // Limpiar la tabla
        val deletedRowsTable = dbHelper.clearTable()
        Log.d("SQLiteHelper", "Limpieza completada. Filas eliminadas: $deletedRowsTable")

        //Insertamos un dato de prueba
        dbHelper.instertTask("Tarea ejemplo2")

        //Obtenemos todas las tareas y las mostramos en el log
        val tasks = dbHelper.getAllTasks()
        Log.d("SQLiteHelper", "Tareas obtenidas: $tasks")

        // Insertamos datos iniciales para pruebas
        val taskId1 = dbHelper.instertTask("Tarea de prueba 1")
        val taskId2 = dbHelper.instertTask("Tarea de prueba 2")
        Log.d("SQLiteHelper", "Tareas iniciales insertadas: $taskId1, $taskId2")

        // Probamos actualizar una tarea
        val updatedRows = dbHelper.updateTask(taskId1.toInt(), "Tarea actualizada")
        if (updatedRows > 0) {
            Log.d("SQLiteHelper", "Tarea con ID $taskId1 actualizada exitosamente.")
        } else {
            Log.d("SQLiteHelper", "Error al actualizar la tarea con ID $taskId1.")
        }

        // Probamos eliminar una tarea
        val deletedRows = dbHelper.deleteTask(taskId2.toInt())
        if (deletedRows > 0) {
            Log.d("SQLiteHelper", "Tarea con ID $taskId2 eliminada exitosamente.")
        } else {
            Log.d("SQLiteHelper", "Error al eliminar la tarea con ID $taskId2.")
        }

        // Verificamos el estado actual de las tareas
        Log.d("SQLiteHelper", "Tareas actuales en la base de datos: $tasks")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleMainActivity", "onPause llamado: La app está perdiendo el foco.")
        //Se podría guardar el progreso de tareas en curso
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleMainActivity", "onStop llamado: La app ya no está visible.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifecycleMainActivity", "onDestroy llamado: Liberando recursos antes de que la Activity se destruya.")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("LifecycleMainActivity", "onLowMemory llamado: El sistema tiene poca memoria.")
        //Implementación de lógica para liberar recursos no críticos, como limpiar cache por ejemplo
    }

    private fun testDatabaseOperations(viewModel: VolunteerTaskViewModel) {
        //Insertamos nueva tarea
        viewModel.insertTask(VolunteerTask(name = "Tarea de prueba 1", description = "test 1"))
        viewModel.insertTask(VolunteerTask(name = "Tarea de prueba 2", description = "test 2"))

        //Cargamos las tareas
        viewModel.loadTasks()

        //Actualizamos una tarea
        val taskToUpdate = VolunteerTask(id = 1, name = "Tarea de prueba 1 actualizada", description = "test 3")
        viewModel.updateTask(taskToUpdate)

        //Eliminamos una tarea
        val taskToDelete = VolunteerTask(id = 2, name = "Tarea de prueba 2 actualizada", description = "test 4")
        viewModel.deleteTask(taskToDelete)
    }
}

@Composable
fun MainScreen() {
    val darkTheme = isSystemInDarkTheme() //Detecta si el sistema está en modo oscuro o no
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background), //Color de fondo del tema
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "VeciCare",
                            color = MaterialTheme.colorScheme.onPrimary, //Color del texto
                            style = MaterialTheme.typography.headlineLarge //Tipografía personalizada
                            )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary //Color de fondo
                )
            )
        }
    ) { innerPadding ->
        GradientBackground (darkTheme = darkTheme) {
            MainContent(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun MainContent (modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Descripción funcional
        Text(
            text = "Conectando a la comunidad",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground, //Texto sobre el fondo
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        //Imagen representativa
        ThemedImage(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        //Lista de datos de prueba
        Text(
            text = "Actividades disponibles",
            color = MaterialTheme.colorScheme.onBackground, //Texto sobre el fondo
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(activities) { activity ->
                ActivityCard(activity)
            }
        }
    }
}

@Composable
fun ActivityCard(activity: ActivityItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // Fondo de la tarjeta
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
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun GradientBackground(darkTheme: Boolean, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush(darkTheme))
    ){
        content()
    }
}

@Composable
fun ThemedImage(modifier: Modifier = Modifier) {
    // Determinar recurso de la imagen según el tema
    val imageRes = if (isSystemInDarkTheme()) {
        R.drawable.baseline_accessibility_new_24_black // Imagen negra
    } else {
        R.drawable.baseline_accessibility_new_24_white // Imagen blanca
    }

    // Renderizar imagen seleccionada
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Imagen representativa",
        modifier = modifier.size(128.dp)
    )
}


@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun PreviewLightTheme() {
    VeciCareTheme(darkTheme = false) {
        MainScreen()
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun PreviewDarkTheme() {
    VeciCareTheme(darkTheme = true) {
        MainScreen()
    }
}


