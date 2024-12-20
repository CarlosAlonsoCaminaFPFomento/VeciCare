@file:OptIn(ExperimentalMaterial3Api::class)

package com.alonsocamina.vecicare

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VeciCareTheme {
                MainScreen()
            }
        }
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
fun PreviewLightTheme() {
    VeciCareTheme(darkTheme = false) {
        MainScreen()
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun PreviewDarkTheme() {
    VeciCareTheme(darkTheme = true) {
        MainScreen()
    }
}


