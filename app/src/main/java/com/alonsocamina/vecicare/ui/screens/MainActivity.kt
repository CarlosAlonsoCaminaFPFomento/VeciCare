package com.alonsocamina.vecicare.ui.screens

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.alonsocamina.vecicare.data.local.tareas.VolunteerDatabase
import com.alonsocamina.vecicare.data.local.tareas.VolunteerTaskViewModel
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosRepository
import com.alonsocamina.vecicare.ui.navigation.NavGraph
import com.alonsocamina.vecicare.ui.theme.VeciCareTheme

class MainActivity : ComponentActivity() {
    private lateinit var usuariosRepository: UsuariosRepository //Base de datos de UsuariosVeciCare
    private lateinit var volunteerTaskViewModel: VolunteerTaskViewModel //Base de datos VolunteerTask (Room)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleMainActivity", "onCreate llamado: Configuración inicial de la app.")

        // Inicialización de la base de datos `UsuariosVeciCare`
        usuariosRepository = UsuariosRepository(this) // Repositorio que usa el helper

        // Inicializar la base de datos Room
        val database = Room.databaseBuilder(
            applicationContext,
            VolunteerDatabase::class.java,
            "volunteer_database"
        ).build()
        Log.d("DatabaseDebugRoom", "Base de datos inicializada")

        // Inicializar el ViewModel
        volunteerTaskViewModel = VolunteerTaskViewModel(database.volunteerTaskDao())

        //Creación de la interfaz del usuario
        setContent {
            VeciCareTheme {
                val navController = rememberNavController()
                NavGraph(
                    usuariosRepository = usuariosRepository,
                    taskViewModel = volunteerTaskViewModel,
                    navController = navController
                )
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
        Log.d(
            "LifecycleMainActivity",
            "onDestroy llamado: Liberando recursos antes de que la Activity se destruya."
        )
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("LifecycleMainActivity", "onLowMemory llamado: El sistema tiene poca memoria.")
        //Implementación de lógica para liberar recursos no críticos, como limpiar cache por ejemplo
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Log.d(
                    "LifecycleMainActivity",
                    "Screen Rotation: The screen is now in landscape mode."
                )
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                Log.d(
                    "LifecycleMainActivity",
                    "Screen Rotation: The screen is now in portrait mode."
                )
            }

            else -> {
                Log.d(
                    "LifecycleMainActivity",
                    "Screen Rotation: The screen orientation has changed to an undefined mode."
                )
            }
        }
    }
}




