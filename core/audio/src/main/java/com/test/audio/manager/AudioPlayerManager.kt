package com.test.audio.manager

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.test.audio.model.AudioState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@UnstableApi
@Singleton
class AudioPlayerManager @Inject constructor(
    private val context: Context
) : Player.Listener {
    private var player: ExoPlayer? = null
    private val _audioState = MutableStateFlow<AudioState>(AudioState.Idle)
    val audioState: StateFlow<AudioState> = _audioState

    private val _playbackPosition = MutableStateFlow(0L)
    val playbackPosition: StateFlow<Long> = _playbackPosition.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var updateJob: Job? = null

    val isInitialized: Boolean
        get() = player?.currentMediaItem != null

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(context).build().apply {
            addListener(this@AudioPlayerManager)
            playWhenReady = false
        }
    }

    fun playAudio(audioUrl: String) {
        if (player == null) initializePlayer()
        player?.let { exoPlayer ->
            val mediaItem = MediaItem.fromUri(audioUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    fun play() {
        player?.play()
    }

    fun pause() {
        player?.pause()
    }

    fun stop() {
        player?.stop()
        _audioState.value = AudioState.Idle
    }

    fun seekTo(position: Long) {
        player?.seekTo(position)
    }

    fun setPlaybackSpeed(speed: Float) {
        player?.playbackParameters = player?.playbackParameters?.withSpeed(speed) ?: PlaybackParameters(speed)
        updateAudioState()
    }

    private fun startPositionUpdates() {
        updateJob?.cancel()
        updateJob = player?.let { exoPlayer ->
            scope.launch {
                while (isActive) {
                    _playbackPosition.value = exoPlayer.currentPosition
                    delay(16)
                }
            }
        }
    }

    private fun stopPositionUpdates() {
        updateJob?.cancel()
        updateJob = null
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        super.onPlaybackParametersChanged(playbackParameters)
        updateAudioState()
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_READY -> startPositionUpdates()
            Player.STATE_ENDED,
            Player.STATE_IDLE -> stopPositionUpdates()
            else -> { /* Do nothing */ }
        }
        updateAudioState()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying) {
            startPositionUpdates()
        }
        updateAudioState()
    }

    private fun updateAudioState() {
        player?.let { exoPlayer ->
            _audioState.value = AudioState.fromPlayerState(
                playWhenReady = exoPlayer.isPlaying,
                playbackState = exoPlayer.playbackState,
                playbackSpeed = exoPlayer.playbackParameters.speed,
                currentPosition = exoPlayer.currentPosition,
                duration = exoPlayer.duration
            )
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        _audioState.value = AudioState.Error(error.message ?: "Unknown error occurred")
    }

    fun release() {
        player?.let { exoPlayer ->
            exoPlayer.stop()
            exoPlayer.release()
        }
        player = null
        _audioState.value = AudioState.Idle
    }
}