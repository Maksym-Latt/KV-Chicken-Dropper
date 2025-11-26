package com.chicken.dropper.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chicken.dropper.domain.repository.PlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val playerRepository: PlayerRepository
) : ViewModel() {

    private var hasRewarded = false

    fun rewardScore(score: Int) {
        if (hasRewarded || score <= 0) return
        hasRewarded = true
        viewModelScope.launch { playerRepository.addEggs(score) }
    }
}
