package com.test.audio.model

import androidx.media3.common.Player

sealed class AudioState {
    object Idle : AudioState()
    object Loading : AudioState()
    data class Ready(val duration: Long) : AudioState()
    data class Playing(val currentPosition: Long, val playbackSpeed: Float, val duration: Long) : AudioState()
    data class Paused(val currentPosition: Long, val playbackSpeed: Float, val duration: Long) : AudioState()
    data class Error(val message: String) : AudioState()
    object Ended : AudioState()
    object Stalled : AudioState()

    companion object {
        fun fromPlayerState(playWhenReady: Boolean, playbackState: Int, playbackSpeed: Float, currentPosition: Long, duration: Long): AudioState {
            return when (playbackState) {
                Player.STATE_IDLE -> Idle
                Player.STATE_BUFFERING -> if (duration > 0) Stalled else Loading
                Player.STATE_READY -> {
                    if (duration > 0) {
                        if (playWhenReady) {
                            Playing(
                                currentPosition = currentPosition,
                                playbackSpeed = playbackSpeed,
                                duration = duration
                            )
                        } else {
                            Paused(
                                currentPosition = currentPosition,
                                playbackSpeed = playbackSpeed,
                                duration = duration
                            )
                        }
                    } else {
                        Ready(duration)
                    }
                }
                Player.STATE_ENDED -> Ended
                else -> Error("Unknown state")
            }
        }
    }
}