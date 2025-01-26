package com.alonsocamina.vecicare.ui.screens

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.alonsocamina.vecicare.data.local.VeciCareDatabase
import com.alonsocamina.vecicare.data.local.tareas.entities.Beneficiary
import com.alonsocamina.vecicare.data.local.tareas.entities.Volunteer
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.BeneficiaryViewModel
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.TaskHistoryViewModel
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.TaskViewModel
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.VolunteerViewModel
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosRepository
import com.alonsocamina.vecicare.ui.navigation.NavGraph
import com.alonsocamina.vecicare.ui.theme.VeciCareTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var usuariosRepository: UsuariosRepository // Base de datos de UsuariosVeciCare
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var beneficiaryViewModel: BeneficiaryViewModel
    private lateinit var volunteerViewModel: VolunteerViewModel
    private lateinit var taskHistoryViewModel: TaskHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleMainActivity", "onCreate llamado: Configuración inicial de la app.")

        // Inicialización de la base de datos `UsuariosVeciCare`
        usuariosRepository = UsuariosRepository(this)

        // Inicialización de la base de datos Room
        val database = VeciCareDatabase.getDatabase(this)

        // Sincronizar usuarios
        syncUsersToRoom()

        // Inicialización de los ViewModels directamente
        taskViewModel = TaskViewModel(database.taskDao())
        beneficiaryViewModel = BeneficiaryViewModel(database.beneficiaryDao())
        volunteerViewModel = VolunteerViewModel(database.volunteerDao())
        taskHistoryViewModel = TaskHistoryViewModel(database.taskHistoryDao())

        // Creación de la interfaz del usuario
        setContent {
            VeciCareTheme {
                val navController = rememberNavController()

                NavGraph(
                    usuariosRepository = usuariosRepository,
                    taskViewModel = taskViewModel,
                    beneficiaryViewModel = beneficiaryViewModel,
                    volunteerViewModel = volunteerViewModel,
                    taskHistoryViewModel = taskHistoryViewModel,
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
        // Se podría guardar el progreso de tareas en curso
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
        // Implementación de lógica para liberar recursos no críticos, como limpiar caché por ejemplo
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

    private fun syncUsersToRoom() {
        val usuarios = usuariosRepository.getAllUsuarios() // Obtener todos los usuarios de SQLite
        val database = VeciCareDatabase.getDatabase(this)

        // Iniciar una corrutina para realizar la sincronización
        lifecycleScope.launch(Dispatchers.IO) {
            usuarios.forEach { usuario ->
                when (usuario.role) {
                    "Beneficiario/a" -> {
                        val beneficiary = Beneficiary(
                            id = usuario.id,
                            name = usuario.name,
                            surname = usuario.surname,
                            email = usuario.email,
                            phoneNumber = usuario.phoneNumber,
                            address = usuario.address ?: "Dirección no especificada",
                            description = usuario.description ?: "Descripción no proporcionada"
                        )
                        database.beneficiaryDao().insertBeneficiary(beneficiary)
                    }
                    "Voluntario/a" -> {
                        val volunteer = Volunteer(
                            id = usuario.id,
                            name = usuario.name,
                            surname = usuario.surname,
                            email = usuario.email,
                            phoneNumber = usuario.phoneNumber,
                            address = usuario.address,
                            skills = usuario.description ?: "Habilidades no especificadas"
                        )
                        database.volunteerDao().insertVolunteer(volunteer)
                    }
                }
            }
            Log.d("SyncUsers", "Sincronización de usuarios completada.")
        }
    }
}
