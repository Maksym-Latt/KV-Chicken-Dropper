package com.chicken.dropper.audio

interface AudioController {
    fun playMenuMusic()
    fun playGameMusic()
    fun stopMusic()
    fun pauseMusic()
    fun resumeMusic()

    fun setMusicVolume(percent: Int)
    fun setSoundVolume(percent: Int)

    fun playDrop()
    fun playGameLose()
    fun playCoinPickup()
    fun playHit()
    fun playMiss()
    fun playSwitch()
    fun playGameWin()
}