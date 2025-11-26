package com.chicken.dropper.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun getMusicVolume(): Int = prefs.getInt(KEY_MUSIC_VOLUME, DEFAULT_VOLUME)

    fun setMusicVolume(volume: Int) {
        prefs.edit().putInt(KEY_MUSIC_VOLUME, volume.coerceIn(0, 100)).apply()
    }

    fun getSoundVolume(): Int = prefs.getInt(KEY_SOUND_VOLUME, DEFAULT_VOLUME)

    fun setSoundVolume(volume: Int) {
        prefs.edit().putInt(KEY_SOUND_VOLUME, volume.coerceIn(0, 100)).apply()
    }

    private companion object {
        const val DEFAULT_VOLUME = 80
        const val KEY_MUSIC_VOLUME = "music_volume"
        const val KEY_SOUND_VOLUME = "sound_volume"
    }
}
