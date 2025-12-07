package com.chicken.dropper.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.chicken.dropper.audio.InteractiveAudioEngine
import com.chicken.dropper.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AudioSettingsViewModel @Inject constructor(
    private val interactiveAudioEngine: InteractiveAudioEngine,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private enum class MusicDestination { MENU, GAME }

    private var musicVolume = settingsRepository.getMusicVolume().coerceIn(0, 100)
    private var soundVolume = settingsRepository.getSoundVolume().coerceIn(0, 100)

    private val _state = MutableStateFlow(
        AudioSettingsState(
            isMusicEnabled = musicVolume > 0,
            isSoundEnabled = soundVolume > 0
        )
    )
    val state: StateFlow<AudioSettingsState> = _state

    private var lastMusicDestination: MusicDestination? = null

    init {
        applyAudioSettings()
    }

    fun toggleMusic() {
        _state.update { it.copy(isMusicEnabled = !it.isMusicEnabled) }
        musicVolume = if (_state.value.isMusicEnabled) DEFAULT_VOLUME else 0
        settingsRepository.setMusicVolume(musicVolume)
        applyMusicSettings()
        playSwitchIfSoundEnabled()
    }

    fun toggleSound() {
        val wasEnabled = _state.value.isSoundEnabled
        if (wasEnabled) {
            interactiveAudioEngine.emitToggleSound()
        }
        val newState = !wasEnabled
        _state.update { it.copy(isSoundEnabled = newState) }
        soundVolume = if (newState) DEFAULT_VOLUME else 0
        settingsRepository.setSoundVolume(soundVolume)
        applySoundSettings()
        if (newState) {
            interactiveAudioEngine.emitToggleSound()
        }
    }

    fun playMenuMusic() {
        lastMusicDestination = MusicDestination.MENU
        if (_state.value.isMusicEnabled) {
            interactiveAudioEngine.startMenuTrack()
        }
    }

    fun playGameMusic() {
        lastMusicDestination = MusicDestination.GAME
        if (_state.value.isMusicEnabled) {
            interactiveAudioEngine.startGameplayTrack()
        }
    }


    fun playDrop() {
        if (_state.value.isSoundEnabled) interactiveAudioEngine.emitDropSound()
    }

    fun playHit() {
        if (_state.value.isSoundEnabled) interactiveAudioEngine.emitImpactSound()
    }

    fun playMiss() {
        if (_state.value.isSoundEnabled) {
            interactiveAudioEngine.emitMissSound()
        }
    }

    fun playLose() {
        if (_state.value.isSoundEnabled) {
            interactiveAudioEngine.emitFailSound()
        }
    }

    fun playWin() {
        if (_state.value.isSoundEnabled) {
            interactiveAudioEngine.emitVictorySound()
        }
    }

    fun pauseMusic() {
        interactiveAudioEngine.suspendMusic()
    }

    fun resumeMusic() {
        if (_state.value.isMusicEnabled) {
            interactiveAudioEngine.continueMusic()
        }
    }

    private fun applyAudioSettings() {
        applyMusicSettings()
        applySoundSettings()
    }

    private fun applyMusicSettings() {
        interactiveAudioEngine.adjustMusicLevel(musicVolume)
        if (_state.value.isMusicEnabled && musicVolume > 0) {
            playLastRequestedMusic()
        } else {
            interactiveAudioEngine.suspendMusic()
        }
    }

    private fun applySoundSettings() {
        interactiveAudioEngine.adjustEffectsLevel(soundVolume)
    }

    private fun playLastRequestedMusic() {
        when (lastMusicDestination) {
            MusicDestination.MENU -> interactiveAudioEngine.startMenuTrack()
            MusicDestination.GAME -> interactiveAudioEngine.startGameplayTrack()
            null -> Unit
        }
    }

    private fun playSwitchIfSoundEnabled() {
        if (_state.value.isSoundEnabled) {
            interactiveAudioEngine.emitToggleSound()
        }
    }

    private companion object {
        const val DEFAULT_VOLUME = 80
    }
}

data class AudioSettingsState(
    val isMusicEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true
)
