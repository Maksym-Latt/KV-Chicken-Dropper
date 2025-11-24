package com.chicken.dropper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.dropper.domain.model.ChickenSkin
import com.chicken.dropper.domain.model.PlayerState
import com.chicken.dropper.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState

    val skins: List<ChickenSkin> = playerRepository.skins

    init {
        viewModelScope.launch {
            playerRepository.playerState.collectLatest { state ->
                _playerState.value = state
            }
        }
    }

    fun onSelectSkin(id: String) {
        viewModelScope.launch { playerRepository.selectSkin(id) }
    }

    fun onBuySkin(skin: ChickenSkin) {
        viewModelScope.launch {
            if (playerRepository.spendEggs(skin.price)) {
                playerRepository.unlockSkin(skin.id)
                playerRepository.selectSkin(skin.id)
            }
        }
    }
}
