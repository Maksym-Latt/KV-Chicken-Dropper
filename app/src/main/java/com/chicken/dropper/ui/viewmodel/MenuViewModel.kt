package com.chicken.dropper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.dropper.domain.model.PlayerState
import com.chicken.dropper.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MenuViewModel @Inject constructor(
    playerRepository: PlayerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state

    init {
        viewModelScope.launch {
            playerRepository.playerState.collectLatest { _state.value = it }
        }
    }
}
