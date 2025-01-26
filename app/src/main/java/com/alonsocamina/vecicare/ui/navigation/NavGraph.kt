package com.alonsocamina.vecicare.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosRepository
import com.alonsocamina.vecicare.ui.screens.*
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.BeneficiaryViewModel
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.TaskHistoryViewModel
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.TaskViewModel
import com.alonsocamina.vecicare.data.local.tareas.viewmodel.VolunteerViewModel

@Composable
fun NavGraph(
    usuariosRepository: UsuariosRepository,
    taskViewModel: TaskViewModel,
    beneficiaryViewModel: BeneficiaryViewModel,
    volunteerViewModel: VolunteerViewModel,
    taskHistoryViewModel: TaskHistoryViewModel,
    navController: NavHostController
) {
    Log.d("NavGraph", "Configurando NavHost con startDestination 'login'.")

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            Log.d("NavGraph", "Navegando a la pantalla de inicio de sesión ('login').")
            LoginScreen(
                onLoginSuccess = { userId, userRole ->
                    Log.d("NavGraph", "Inicio de sesión exitoso: userId=$userId, userRole=$userRole")
                    if (userRole == "Beneficiario/a") {
                        navController.navigate("beneficiary_screen/$userId")
                        Log.d("NavGraph", "Navegando a BeneficiaryScreen con userId=$userId.")
                    } else {
                        navController.navigate("volunteer_screen/$userId")
                        Log.d("NavGraph", "Navegando a VolunteerScreen con userId=$userId.")
                    }
                },
                onRegister = {
                    Log.d("NavGraph", "Navegando a la pantalla de registro ('register').")
                    navController.navigate("register")
                },
                validateUser = { email, password ->
                    val isValid = usuariosRepository.validateUser(email, password)
                    Log.d("NavGraph", "Validando usuario: email=$email, resultado=${if (isValid) "válido" else "inválido"}")
                    isValid
                },
                getUserDetails = { email ->
                    usuariosRepository.getUsuarioByEmail(email)?.let {
                        Log.d("NavGraph", "Detalles del usuario obtenidos: id=${it.id}, role=${it.role}")
                        it.id to it.role
                    }
                }
            )
        }

        composable("register") {
            Log.d("NavGraph", "Navegando a la pantalla de registro ('register').")
            RegisterScreen(onRegisterSuccess = {
                Log.d("NavGraph", "Registro exitoso, volviendo a la pantalla de inicio de sesión ('login').")
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }

        composable(
            "beneficiary_screen/{beneficiaryId}",
            arguments = listOf(navArgument("beneficiaryId") { type = NavType.IntType })
        ) { backStackEntry ->
            val beneficiaryId = backStackEntry.arguments?.getInt("beneficiaryId") ?: 0
            Log.d("NavGraph", "Navegando a BeneficiaryScreen con beneficiaryId=$beneficiaryId.")
            BeneficiaryScreen(
                beneficiaryId = beneficiaryId,
                taskViewModel = taskViewModel
            )
        }

        composable(
            "volunteer_screen/{volunteerId}",
            arguments = listOf(navArgument("volunteerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val volunteerId = backStackEntry.arguments?.getInt("volunteerId") ?: 0
            Log.d("NavGraph", "Navegando a VolunteerScreen con volunteerId=$volunteerId.")
            VolunteerScreen(
                volunteerId = volunteerId,
                taskViewModel = taskViewModel
            )
        }
    }
}
