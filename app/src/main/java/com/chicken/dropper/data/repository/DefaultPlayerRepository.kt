package com.chicken.dropper.data.repository

import com.chicken.dropper.R
import com.chicken.dropper.data.local.PlayerPreferencesDataSource
import com.chicken.dropper.domain.model.ChickenSkin
import com.chicken.dropper.domain.model.PlayerState
import com.chicken.dropper.domain.repository.PlayerRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@Singleton
class DefaultPlayerRepository @Inject constructor(
    private val preferences: PlayerPreferencesDataSource
) : PlayerRepository {

    override val skins: List<ChickenSkin> = listOf(
        ChickenSkin(
            id = "classic",
            name = "Classic Chicken",
            titleTop = "CHICKEN",
            titleBottom = "CLASSIC",
            description = "Farm Friend",
            price = 0,
            eggSprite = R.drawable.chicken_1_egg,
            dropSprite = R.drawable.chicken_1_drop
        ),
        ChickenSkin(
            id = "cooker",
            name = "Fire Chef",
            titleTop = "CHICKEN",
            titleBottom = "COOKER",
            description = "Kitchen Fury",
            price = 500,
            eggSprite = R.drawable.chicken_2_egg,
            dropSprite = R.drawable.chicken_2_drop
        ),
        ChickenSkin(
            id = "hero",
            name = "Sky Guardian",
            titleTop = "CHICKEN",
            titleBottom = "HERO",
            description = "Brave Flyer",
            price = 1500,
            eggSprite = R.drawable.chicken_3_egg,
            dropSprite = R.drawable.chicken_3_drop
        )
    )

    override val playerState: Flow<PlayerState> = preferences.snapshot.map { snap ->
        PlayerState(
            eggs = snap.eggs,
            selectedSkinId = snap.selectedSkinId.takeIf { id -> skins.any { it.id == id } } ?: "classic",
            ownedSkins = snap.ownedSkins.ifEmpty { setOf("classic") }
        )
    }

    override suspend fun addEggs(delta: Int) {
        preferences.snapshot.map { it.eggs }.firstOrNull()?.let { current ->
            preferences.updateEggs((current + delta).coerceAtLeast(0))
        }
    }

    override suspend fun spendEggs(amount: Int): Boolean {
        val current = preferences.snapshot.map { it.eggs }.firstOrNull() ?: return false
        if (current < amount) return false
        preferences.updateEggs(current - amount)
        return true
    }

    override suspend fun selectSkin(id: String) {
        preferences.updateSelectedSkin(id)
    }

    override suspend fun unlockSkin(id: String) {
        val currentOwned = preferences.snapshot.map { it.ownedSkins }.firstOrNull() ?: emptySet()
        preferences.updateOwnedSkins(currentOwned + id)
    }
}
