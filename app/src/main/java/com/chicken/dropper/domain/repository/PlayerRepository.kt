package com.chicken.dropper.domain.repository

import com.chicken.dropper.domain.model.ChickenSkin
import com.chicken.dropper.domain.model.PlayerState
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val playerState: Flow<PlayerState>
    val skins: List<ChickenSkin>

    suspend fun addEggs(delta: Int)
    suspend fun spendEggs(amount: Int): Boolean
    suspend fun selectSkin(id: String)
    suspend fun unlockSkin(id: String)
}
