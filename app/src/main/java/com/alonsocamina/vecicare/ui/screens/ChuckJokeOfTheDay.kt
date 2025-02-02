package com.alonsocamina.vecicare.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChuckJokeOfTheDay(viewModel: ChuckJokesViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        if (state is ChuckJokesState.Success) {
            isVisible = true
        }
    }

    when(val current = state){
        ChuckJokesState.Loading -> {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                LinearProgressIndicator(modifier = Modifier.padding(16.dp))
                Text ("Se viene la broma...", style = MaterialTheme.typography.bodyLarge)
            }
        }
        is ChuckJokesState.Success -> {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .clickable { viewModel.onJokeClicked() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ){
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEmotions,
                            contentDescription = "Joke Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Joke of the Day \uD83E\uDD23",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = current.joke.value,
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}