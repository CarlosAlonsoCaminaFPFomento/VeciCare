package com.alonsocamina.vecicare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alonsocamina.vecicare.data.local.tareas.VolunteerTaskViewModel
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosRepository
import com.alonsocamina.vecicare.ui.screens.*

@Composable
fun NavGraph(
    usuariosRepository: UsuariosRepository,
    taskViewModel: VolunteerTaskViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { userId, userRole ->
                    if (userRole == "Beneficiario/a") {
                        navController.navigate("beneficiary_screen/$userId")
                    } else {
                        navController.navigate("volunteer_screen/$userId")
                    }
                },
                onRegister = {
                    navController.navigate("register")
                },
                validateUser = { email, password ->
                    usuariosRepository.validateUser(email, password)
                },
                getUserDetails = { email ->
                    usuariosRepository.getUsuarioByEmail(email)?.let {
                        it.id to it.role
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(onRegisterSuccess = {
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
            BeneficiaryScreen(viewModel = taskViewModel, beneficiaryId = beneficiaryId)
        }

        composable(
            "volunteer_screen/{volunteerId}",
            arguments = listOf(navArgument("volunteerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val volunteerId = backStackEntry.arguments?.getInt("volunteerId") ?: 0
            VolunteerScreen(viewModel = taskViewModel, volunteerId = volunteerId)
        }
    }
}
