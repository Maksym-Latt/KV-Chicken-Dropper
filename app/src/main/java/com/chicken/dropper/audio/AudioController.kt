package com.chicken.dropper.audio

interface AudioController {
    fun playMenuMusic()
    fun playGameMusic()
    fun stopMusic()
    fun pauseMusic()
    fun resumeMusic()

    fun setMusicVolume(percent: Int)
    fun setSoundVolume(percent: Int)
    fun setVibrationEnabled(enabled: Boolean)

    fun playGameLose()
    fun playCoinPickup()
    fun playHit()
    fun playGameWin()
}
