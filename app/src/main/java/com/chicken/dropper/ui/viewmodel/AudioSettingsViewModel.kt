package com.chicken.dropper.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.chicken.dropper.audio.AudioController
import com.chicken.dropper.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AudioSettingsViewModel @Inject constructor(
    private val audioController: AudioController,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private enum class MusicDestination { MENU, GAME }

    private val defaultMusicVolume = settingsRepository.getMusicVolume().coerceIn(0, 100)
    private val defaultSoundVolume = settingsRepository.getSoundVolume().coerceIn(0, 100)

    private val _state = MutableStateFlow(
        AudioSettingsState(
            isMusicEnabled = defaultMusicVolume > 0,
            isSoundEnabled = defaultSoundVolume > 0,
            isVibrationEnabled = settingsRepository.isVibrationEnabled()
        )
    )
    val state: StateFlow<AudioSettingsState> = _state

    private var lastMusicDestination: MusicDestination? = null

    init {
        applyAudioSettings()
    }

    fun toggleMusic() {
        _state.update { it.copy(isMusicEnabled = !it.isMusicEnabled) }
        applyMusicSettings()
    }

    fun toggleSound() {
        _state.update { it.copy(isSoundEnabled = !it.isSoundEnabled) }
        applySoundSettings()
    }

    fun toggleVibration() {
        _state.update { it.copy(isVibrationEnabled = !it.isVibrationEnabled) }
        audioController.setVibrationEnabled(_state.value.isVibrationEnabled)
    }

    fun playMenuMusic() {
        lastMusicDestination = MusicDestination.MENU
        if (_state.value.isMusicEnabled) {
            audioController.playMenuMusic()
        }
    }

    fun playGameMusic() {
        lastMusicDestination = MusicDestination.GAME
        if (_state.value.isMusicEnabled) {
            audioController.playGameMusic()
        }
    }

    fun pauseMusic() {
        audioController.pauseMusic()
    }

    fun resumeMusic() {
        if (_state.value.isMusicEnabled) {
            audioController.resumeMusic()
        }
    }

    private fun applyAudioSettings() {
        applyMusicSettings()
        applySoundSettings()
        audioController.setVibrationEnabled(_state.value.isVibrationEnabled)
    }

    private fun applyMusicSettings() {
        val volume = if (_state.value.isMusicEnabled) defaultMusicVolume else 0
        audioController.setMusicVolume(volume)
        if (_state.value.isMusicEnabled) {
            playLastRequestedMusic()
        } else {
            audioController.pauseMusic()
        }
    }

    private fun applySoundSettings() {
        val volume = if (_state.value.isSoundEnabled) defaultSoundVolume else 0
        audioController.setSoundVolume(volume)
    }

    private fun playLastRequestedMusic() {
        when (lastMusicDestination) {
            MusicDestination.MENU -> audioController.playMenuMusic()
            MusicDestination.GAME -> audioController.playGameMusic()
            null -> Unit
        }
    }
}

data class AudioSettingsState(
    val isMusicEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true
)
