package com.chicken.dropper.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.dropper.domain.model.ChickenSkin
import com.chicken.dropper.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

private const val BASE_BUCKET_SPEED = 0.35f
private const val SPEED_STEP = 0.08f
private const val FALL_SPEED = 1.1f

@HiltViewModel
class GameViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GameUiState(showIntro = savedStateHandle.get<Boolean>("showIntro") ?: false)
    )
    val uiState: StateFlow<GameUiState> = _uiState

    private var direction = 1f
    private var hitsToSpeedUp = 5

    init {
        observePlayer()
        startLoop()
    }

    fun dropEgg() {
        val state = _uiState.value
        if (state.isPaused || state.isGameOver || state.eggY != null || state.showIntro) return
        _uiState.value = state.copy(
            brokenEggVisible = false,
            eggY = 0f,
            isDropping = true,
            chickenLookingDown = true
        )
    }

    fun dismissIntro() {
        _uiState.value = _uiState.value.copy(showIntro = false)
    }

    fun togglePause() {
        _uiState.value = _uiState.value.copy(isPaused = !_uiState.value.isPaused)
    }

    fun resume() {
        _uiState.value = _uiState.value.copy(isPaused = false)
    }

    fun restart() {
        direction = 1f
        hitsToSpeedUp = 5
        val currentState = _uiState.value
        _uiState.value = GameUiState(
            bucketSpeed = BASE_BUCKET_SPEED,
            selectedSkin = currentState.selectedSkin,
            eggs = currentState.eggs,
            showIntro = false
        )
    }

    private fun startLoop() {
        viewModelScope.launch {
            while (true) {
                delay(16L)
                tick(0.016f)
            }
        }
    }

    fun pause() {
        _uiState.value = _uiState.value.copy(isPaused = true)
    }

    private fun tick(delta: Float) {
        val state = _uiState.value
        if (state.isPaused || state.isGameOver) return

        moveBucket(delta, state)
        updateEgg(delta)
    }

    private fun moveBucket(delta: Float, state: GameUiState) {
        var x = state.bucketX + direction * state.bucketSpeed * delta
        if (x < 0.07f) {
            x = 0.07f
            direction = 1f
        } else if (x > 0.93f) {
            x = 0.93f
            direction = -1f
        }
        _uiState.value = state.copy(bucketX = x)
    }

    private fun updateEgg(delta: Float) {
        val state = _uiState.value
        val currentY = state.eggY ?: return
        val newY = currentY + FALL_SPEED * delta
        if (newY < 1f) {
            _uiState.value = state.copy(eggY = newY)
            return
        }

        val hit = abs(state.bucketX - state.chickenX) < 0.10f
        if (hit) {
            val add = if (state.goldenBucket) 5 else 1
            val newScore = state.score + add
            val nextHitsLeft = if (hitsToSpeedUp > 1) hitsToSpeedUp - 1 else 5
            val speedBoost = if (hitsToSpeedUp == 1) SPEED_STEP else 0f
            if (hitsToSpeedUp == 1) hitsToSpeedUp = 5 else hitsToSpeedUp -= 1
            viewModelScope.launch { playerRepository.addEggs(add) }
            _uiState.value = state.copy(
                score = newScore,
                eggY = null,
                isDropping = false,
                chickenLookingDown = false,
                bucketSpeed = state.bucketSpeed + speedBoost,
                goldenBucket = rollGoldenBucket()
            )
        } else {
            val remaining = state.lives - 1
            val gameOver = remaining <= 0

            _uiState.value = state.copy(
                eggY = null,
                isDropping = false,
                chickenLookingDown = false,
                lives = remaining,
                isGameOver = gameOver,
                brokenEggVisible = true
            )
        }
    }

    private fun rollGoldenBucket(): Boolean = Random.nextFloat() > 0.82f

    private fun observePlayer() {
        viewModelScope.launch {
            playerRepository.playerState.collectLatest { player ->
                val skin = playerRepository.skins.firstOrNull { it.id == player.selectedSkinId }
                    ?: playerRepository.skins.first()
                _uiState.value = _uiState.value.copy(
                    selectedSkin = skin,
                    eggs = player.eggs
                )
            }
        }
    }
}

data class GameUiState(
    val score: Int = 0,
    val lives: Int = 3,
    val bucketX: Float = 0.5f,
    val bucketSpeed: Float = BASE_BUCKET_SPEED,
    val eggY: Float? = null,
    val isDropping: Boolean = false,
    val chickenLookingDown: Boolean = false,
    val goldenBucket: Boolean = false,
    val isPaused: Boolean = false,
    val isGameOver: Boolean = false,
    val chickenX: Float = 0.5f,
    val selectedSkin: ChickenSkin? = null,
    val eggs: Int = 0,
    val brokenEggVisible: Boolean = false,
    val showIntro: Boolean = false
)
