@file:OptIn(ExperimentalMaterial3Api::class)

package com.alonsocamina.vecicare.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.alonsocamina.vecicare.R
import com.alonsocamina.vecicare.data.local.usuarios.Usuario
import com.alonsocamina.vecicare.data.local.usuarios.UsuariosHelper
import com.alonsocamina.vecicare.ui.shared.GradientBackground
import com.alonsocamina.vecicare.ui.theme.VeciCareTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : ComponentActivity() {
    private lateinit var dbHelper: UsuariosHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = UsuariosHelper(this)

        Log.d("LifecycleRegisterActivity", "onCreate: Pantalla creada.")
        setContent {
            VeciCareTheme {
                RegisterScreen(
                    onRegisterSuccess = {
                        Log.d("RegisterActivity", "Registro exitoso.")
                        finish()
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifecycleRegisterActivity", "onStart: Pantalla visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleRegisterActivity", "onResume: Pantalla interactiva.")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleRegisterActivity", "onPause: Pantalla no interactiva.")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleRegisterActivity", "onStop: Pantalla no visible.")
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
        Log.d("LifecycleRegisterActivity", "onDestroy: Pantalla destruida y base de datos cerrada.")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.d("LifecycleRegisterActivity", "onLowMemory: El sistema tiene poca memoria.")
        //Implementación de lógica para liberar recursos no críticos, como limpiar cache por ejemplo
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Log.d(
                    "LifecycleRegisterActivity",
                    "Screen Rotation: The screen is now in landscape mode."
                )
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                Log.d(
                    "LifecycleRegisterActivity",
                    "Screen Rotation: The screen is now in portrait mode."
                )
            }

            else -> {
                Log.d(
                    "LifecycleRegisterActivity",
                    "Screen Rotation: The screen orientation has changed to an undefined mode."
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Registro de Usuario",
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
            RegisterContent(
                modifier = Modifier.padding(innerPadding),
                onRegisterSuccess = onRegisterSuccess
            )
        }
    }
}

@Composable
fun RegisterContent(modifier: Modifier = Modifier, onRegisterSuccess: () -> Unit) {
    val context = LocalContext.current
    val dbHelper = UsuariosHelper(context)
    val coroutineScope = rememberCoroutineScope()

    // Estados de los campos
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+34") }
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val roles = listOf("Voluntario/a", "Beneficiario/a")
    val countryCodes = listOf("+34", "+1", "+44", "+91", "+81")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_register_user),
            contentDescription = "Icono de registro",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(120.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        DatePickerField(
            label = "Fecha de Nacimiento",
            value = birthDate,
            onValueChange = { birthDate = it },
            context = context
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuField(
                label = "Código",
                options = countryCodes,
                selectedOption = countryCode,
                onOptionSelected = { countryCode = it },
                modifier = Modifier.weight(1.2f)
            )
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) phoneNumber = it
                },
                label = { Text("Teléfono") },
                modifier = Modifier.weight(3f)
            )
        }
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenuField(
            label = "Rol",
            options = roles,
            selectedOption = role,
            onOptionSelected = { role = it }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() ||
                    address.isEmpty() || description.isEmpty() || birthDate.isEmpty() || role.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty()
                ) {
                    errorMessage = "Por favor, completa todos los campos."
                } else if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden."
                } else {
                    coroutineScope.launch {
                        val user = Usuario(
                            name = name,
                            surname = surname,
                            email = email.trim(),
                            countryCode = countryCode,
                            phoneNumber = phoneNumber.trim(),
                            address = address.trim(),
                            description = description.trim(),
                            birthDate = birthDate.trim(),
                            role = role.trim(),
                            password = password.trim()
                        )
                        val result = dbHelper.insertUser(user)
                        Log.d("RegisterDebug", "Usuario registrado: $user")
                        if (result > 0) {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            onRegisterSuccess()
                        } else {
                            errorMessage = "Error al registrar. Inténtalo de nuevo."
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse")
        }
    }
}

// Composable personalizado para el selector de fecha
@Composable
fun DatePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    context: Context
) {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, day)
            onValueChange(dateFormat.format(selectedDate.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Seleccionar fecha"
                )
            }
        }
    )
}

// Composable personalizado para el menú desplegable
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Desplegar"
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
