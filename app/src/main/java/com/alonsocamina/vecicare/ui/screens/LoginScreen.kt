@file:OptIn(ExperimentalMaterial3Api::class)

package com.alonsocamina.vecicare.ui.screens

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosHelper
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosRepository
import com.alonsocamina.vecicare.ui.shared.GradientBackground
import com.alonsocamina.vecicare.ui.theme.VeciCareTheme

class LoginActivity : ComponentActivity() {

    private lateinit var usuariosRepository: UsuariosRepository
    private lateinit var dbHelper: UsuariosHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar UsuariosHelper y UsuariosRepository
        dbHelper = UsuariosHelper(this)
        usuariosRepository = UsuariosRepository(this)

        // Verificar si la base de datos está abierta
        val db = dbHelper.writableDatabase
        Log.d("DatabaseDebug", "Base de datos abierta: ${db.isOpen}")

        Log.d("LifecycleLoginActivity", "onCreate: Pantalla creada.")
        setContent {
            VeciCareTheme {
                LoginScreen(
                    onLoginSuccess = { userId, userRole ->
                        Log.d(
                            "LoginActivity",
                            "Inicio de sesión con éxito. Usuario ID: $userId, Role: $userRole"
                        )
                        finish()
                    },
                    onRegister = {
                        Log.d("LoginActivity", "Navegando a la pantalla de registro.")
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
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleLoginActivity", "onStart: Pantalla visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleLoginActivity", "onResume: Pantalla interactiva.")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleLoginActivity", "onPause: Pantalla no interactiva.")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleLoginActivity", "onStop: Pantalla no visible.")
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cerrar la base de datos al destruir la actividad
        dbHelper.close()
        Log.d("LifecycleLoginActivity", "Base de datos cerrada en onDestroy.")
        Log.d("LifecycleLoginActivity", "onDestroy: Pantalla destruida.")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("LifecycleLoginActivity", "onLowMemory: El sistema tiene poca memoria.")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Log.d(
                    "LifecycleLoginActivity",
                    "Screen Rotation: The screen is now in landscape mode."
                )
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                Log.d(
                    "LifecycleLoginActivity",
                    "Screen Rotation: The screen is now in portrait mode."
                )
            }

            else -> {
                Log.d(
                    "LifecycleLoginActivity",
                    "Screen Rotation: The screen orientation has changed to an undefined mode."
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (userId: Int, userRole: String) -> Unit,
    onRegister: () -> Unit,
    validateUser: (email: String, password: String) -> Boolean,
    getUserDetails: (email: String) -> Pair<Int, String>?
) {
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
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        GradientBackground(darkTheme = isSystemInDarkTheme()) {
            LoginContent(
                modifier = Modifier.padding(innerPadding),
                onLoginSuccess = onLoginSuccess,
                onRegister = onRegister,
                validateUser = validateUser,
                getUserDetails = getUserDetails
            )
        }
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    onLoginSuccess: (userId: Int, userRole: String) -> Unit,
    onRegister: () -> Unit,
    validateUser: (email: String, password: String) -> Boolean,
    getUserDetails: (email: String) -> Pair<Int, String>?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val trimmedEmail = email.trim()
                val trimmedPassword = password.trim()
                when {
                    trimmedEmail.isEmpty() || trimmedPassword.isEmpty() -> {
                        errorMessage = "Por favor, completa todos los campos."
                    }

                    validateUser(trimmedEmail, trimmedPassword) -> {
                        val userDetails = getUserDetails(trimmedEmail)
                        if (userDetails != null) {
                            val (userId, userRole) = userDetails
                            onLoginSuccess(userId, userRole)
                        } else {
                            errorMessage = "No se pudieron obtener los detalles del usuario."
                        }
                    }

                    else -> {
                        errorMessage = "Credenciales incorrectas. Inténtalo de nuevo."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Iniciar Sesión")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¿No tienes cuenta?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Button(
            onClick = { onRegister() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse")
        }
    }
}

