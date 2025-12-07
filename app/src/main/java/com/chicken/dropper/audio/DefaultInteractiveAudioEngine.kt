package com.chicken.dropper.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.chicken.dropper.data.SettingsRepository

@Singleton
class DefaultInteractiveAudioEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    settingsRepository: SettingsRepository
) : InteractiveAudioEngine {

    private enum class BgmChannel { HOME_SCREEN, SESSION }
    private enum class FxSignal { VICTORY, FAIL, IMPACT, FALL, MISS, TOGGLE }

    private val fxPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(6)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val fxResourceMap = mapOf(
        FxSignal.VICTORY to "sfx_win",
        FxSignal.FAIL to "sfx_lose",
        FxSignal.IMPACT to "sfx_hit",
        FxSignal.FALL to "sfx_drop",
        FxSignal.MISS to "sfx_miss",
        FxSignal.TOGGLE to "sfx_switch",
    ).mapValues { (_, name) -> resolveRaw(name) }

    private val fxInstanceCache = mutableMapOf<FxSignal, Int>()
    private val availableSamples = mutableSetOf<Int>()
    private val delayedPlaybackQueue = mutableSetOf<Int>()

    private val bgmMenuRes = resolveRaw("music_menu")
    private val bgmGameRes = resolveRaw("music_game")

    private val bgmPlayers = mutableMapOf<BgmChannel, MediaPlayer>()
    private var activeChannel: BgmChannel? = null

    private var bgmLevel: Float = settingsRepository.getMusicVolume().toVolume()
    private var fxLevel: Float = settingsRepository.getSoundVolume().toVolume()
    private var shouldResumeAfterPause: Boolean = false

    init {
        fxPool.setOnLoadCompleteListener { pool, sampleId, status ->
            if (status == 0) {
                availableSamples += sampleId
                if (delayedPlaybackQueue.remove(sampleId)) {
                    pool.play(sampleId, fxLevel, fxLevel, 1, 0, 1f)
                }
            } else {
                delayedPlaybackQueue.remove(sampleId)
            }
        }
    }

    override fun startMenuTrack() {
        shouldResumeAfterPause = false
        playMusic(BgmChannel.HOME_SCREEN)
    }

    override fun startGameplayTrack() {
        shouldResumeAfterPause = false
        playMusic(BgmChannel.SESSION)
    }

    override fun emitDropSound() {
        playEffect(FxSignal.FALL)
    }

    override fun emitMissSound() {
        playEffect(FxSignal.MISS)
    }

    override fun emitToggleSound() {
        playEffect(FxSignal.TOGGLE)
    }

    override fun haltAllTracks() {
        activeChannel?.let { channel ->
            bgmPlayers[channel]?.let { player ->
                if (player.isPlaying) player.pause()
                player.seekTo(0)
            }
        }
        activeChannel = null
        shouldResumeAfterPause = false
    }

    override fun suspendMusic() {
        activeChannel?.let { channel ->
            bgmPlayers[channel]?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    shouldResumeAfterPause = true
                }
            }
        }
    }

    override fun continueMusic() {
        if (!shouldResumeAfterPause) return
        shouldResumeAfterPause = false
        activeChannel?.let { channel ->
            val player = bgmPlayers[channel]
            if (player != null && !player.isPlaying) {
                try {
                    player.start()
                } catch (_: IllegalStateException) {
                    bgmPlayers.remove(channel)
                    activeChannel = null
                }
            } else if (player == null) {
                activeChannel = null
            }
        }
    }

    override fun adjustMusicLevel(percent: Int) {
        bgmLevel = percent.toVolume()
        bgmPlayers.values.forEach { player ->
            player.setVolume(bgmLevel, bgmLevel)
        }
    }

    override fun adjustEffectsLevel(percent: Int) {
        fxLevel = percent.toVolume()
    }

    override fun emitFailSound() {
        playEffect(FxSignal.FAIL)
    }

    override fun emitCoinSound() {

    }

    override fun emitImpactSound() {
        playEffect(FxSignal.IMPACT)
    }

    override fun emitVictorySound() {
        playEffect(FxSignal.VICTORY)
    }


    private fun playMusic(channel: BgmChannel) {
        if (activeChannel == channel && bgmPlayers[channel]?.isPlaying == true) {
            return
        }

        activeChannel?.let { active ->
            bgmPlayers[active]?.let { player ->
                if (player.isPlaying) player.pause()
                player.seekTo(0)
            }
        }

        val player = ensurePlayer(channel)
        activeChannel = if (player != null) channel else null
        player?.let {
            it.setVolume(bgmLevel, bgmLevel)
            it.isLooping = true
            try {
                it.start()
            } catch (_: IllegalStateException) {
                // player might have been released by the system, recreate lazily on next call
                bgmPlayers.remove(channel)
            }
        }
    }

    private fun ensurePlayer(channel: BgmChannel): MediaPlayer? {
        bgmPlayers[channel]?.let { existing ->
            return existing
        }

        val resId = when (channel) {
            BgmChannel.HOME_SCREEN -> bgmMenuRes
            BgmChannel.SESSION -> bgmGameRes
        }

        if (resId == 0) return null

        return runCatching { MediaPlayer.create(context, resId) }
            .getOrNull()
            ?.also { player ->
                player.isLooping = true
                player.setVolume(bgmLevel, bgmLevel)
                bgmPlayers[channel] = player
            }
    }

    private fun playEffect(effect: FxSignal) {
        if (fxLevel <= 0f) return

        val resId = fxResourceMap[effect] ?: 0
        if (resId == 0) return

        val sampleId = fxInstanceCache[effect]
        if (sampleId != null) {
            if (availableSamples.contains(sampleId)) {
                fxPool.play(sampleId, fxLevel, fxLevel, 1, 0, 1f)
            } else {
                delayedPlaybackQueue += sampleId
            }
            return
        }

        val loadId = fxPool.load(context, resId, 1)
        if (loadId != 0) {
            fxInstanceCache[effect] = loadId
            delayedPlaybackQueue += loadId
        }
    }

    private fun resolveRaw(name: String): Int {
        if (name.isBlank()) return 0
        return context.resources.getIdentifier(name, "raw", context.packageName)
    }

    private fun Int.toVolume(): Float = (this.coerceIn(0, 100) / 100f).coerceIn(0f, 1f)
}
