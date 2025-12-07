package com.chicken.dropper.audio

interface InteractiveAudioEngine {
    fun startMenuTrack()
    fun startGameplayTrack()
    fun haltAllTracks()
    fun suspendMusic()
    fun continueMusic()

    fun adjustMusicLevel(percent: Int)
    fun adjustEffectsLevel(percent: Int)

    fun emitDropSound()
    fun emitFailSound()
    fun emitCoinSound()
    fun emitImpactSound()
    fun emitMissSound()
    fun emitToggleSound()
    fun emitVictorySound()
}