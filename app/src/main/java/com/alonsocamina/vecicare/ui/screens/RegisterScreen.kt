@file:OptIn(ExperimentalMaterial3Api::class)

package com.alonsocamina.vecicare.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.alonsocamina.vecicare.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
    val coroutineScope = rememberCoroutineScope()

    // Estados de los campos
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+34") } // Prefijo predeterminado
    var address by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val roles = listOf("Voluntario/a", "Beneficiario/a")
    val countryCodes = listOf("+34", "+1", "+44", "+91", "+81") // Ejemplo de prefijos

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

        // Campos de texto
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

        // Selector de fecha
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
            // Selector de código de país
            Box(
                modifier = Modifier.weight(1.2f)
            ) {
                DropdownMenuField(
                    label = "Código",
                    options = countryCodes,
                    selectedOption = countryCode,
                    onOptionSelected = { countryCode = it }
                )
            }

            // Campo de texto para el número de teléfono
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    // Permitir solo números
                    if (it.all { char -> char.isDigit() }) {
                        phoneNumber = it
                    }
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

        // Selector de rol
        DropdownMenuField(
            label = "Rol",
            options = roles,
            selectedOption = role,
            onOptionSelected = { role = it }
        )

        // Contraseñas
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

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro
        Button(
            onClick = {
                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() ||
                    phoneNumber.isEmpty() || address.isEmpty() || description.isEmpty() ||
                    birthDate.isEmpty() || role.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    errorMessage = "Por favor, completa todos los campos."
                } else if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden."
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    errorMessage = "Correo electrónico no válido."
                } else if (phoneNumber.length < 8) {
                    errorMessage = "Número de teléfono no válido."
                } else {
                    errorMessage = ""
                    onRegisterSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Registrarse")
        }
    }
}


@Composable
fun DatePickerField(label: String, value: String, onValueChange: (String) -> Unit, context: Context) {
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

@Composable
fun DropdownMenuField(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(painter = painterResource(id = R.drawable.ic_arrow_down), contentDescription = "Desplegar")
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
