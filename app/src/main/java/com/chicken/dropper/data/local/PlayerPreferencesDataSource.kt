package com.chicken.dropper.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATA_STORE_NAME = "player_prefs"

private val Context.playerDataStore by preferencesDataStore(DATA_STORE_NAME)

@Singleton
class PlayerPreferencesDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.playerDataStore

    private val eggsKey = intPreferencesKey("eggs")
    private val selectedSkinKey = stringPreferencesKey("selected_skin")
    private val ownedSkinsKey = stringSetPreferencesKey("owned_skins")

    data class Snapshot(
        val eggs: Int = 0,
        val selectedSkinId: String = "classic",
        val ownedSkins: Set<String> = setOf("classic")
    )

    val snapshot: Flow<Snapshot> = dataStore.data.map { prefs ->
        Snapshot(
            eggs = prefs[eggsKey] ?: 0,
            selectedSkinId = prefs[selectedSkinKey] ?: "classic",
            ownedSkins = prefs[ownedSkinsKey] ?: setOf("classic")
        )
    }

    suspend fun updateEggs(newValue: Int) {
        dataStore.edit { prefs ->
            prefs[eggsKey] = newValue
        }
    }

    suspend fun updateSelectedSkin(id: String) {
        dataStore.edit { prefs ->
            prefs[selectedSkinKey] = id
        }
    }

    suspend fun updateOwnedSkins(ids: Set<String>) {
        dataStore.edit { prefs ->
            prefs[ownedSkinsKey] = ids
        }
    }
}
