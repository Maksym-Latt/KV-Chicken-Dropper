package com.chicken.dropper.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor() {
    fun getMusicVolume(): Int = 80
    fun getSoundVolume(): Int = 80
    fun isVibrationEnabled(): Boolean = true
}
