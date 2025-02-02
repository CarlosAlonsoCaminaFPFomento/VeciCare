package com.alonsocamina.vecicare.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alonsocamina.vecicare.domain.ChuckJoke
import com.alonsocamina.vecicare.domain.GetChuckJokeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChuckJokesViewModel @Inject constructor(
    private val getChuckJoke: GetChuckJokeUseCase
): ViewModel() {
    private val _state = MutableStateFlow<ChuckJokesState>(ChuckJokesState.Loading)
    val state = _state

    init {
        getChuckJokeAndChangeState()
    }

    fun onJokeClicked() {
        getChuckJokeAndChangeState()
    }

    private fun getChuckJokeAndChangeState() {
        viewModelScope.launch {
            _state.value = ChuckJokesState.Loading
            getChuckJoke()?.let { _state.value = ChuckJokesState.Success(it) }
        }
    }
}

sealed interface ChuckJokesState {
    object Loading: ChuckJokesState
    data class Success(val joke: ChuckJoke) :ChuckJokesState
}