package com.alonsocamina.vecicare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosRepository
import com.alonsocamina.vecicare.ui.screens.LoginScreen
import com.alonsocamina.vecicare.ui.screens.MainScreen
import com.alonsocamina.vecicare.ui.screens.RegisterScreen

@Composable
fun NavGraph(
    usuariosRepository: UsuariosRepository,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login" //Pantalla inicial
    ) {
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    // Redirige al MainActivity después de iniciar sesión
                    navController.navigate("main_screen")
                },
                onRegister = {
                    // Redirige a la pantalla de registro si no tiene cuenta
                    navController.navigate("register")
                },
                validateUser = { email, password ->
                    usuariosRepository.validateUser(email, password)
                }
            )
        }
        // Pantalla de registro
        composable("register") {
            RegisterScreen(onRegisterSuccess = {
                // Regresa al login tras el registro exitoso
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        // Pantalla principal
        composable("main_screen") {
            MainScreen()
        }
    }
}
