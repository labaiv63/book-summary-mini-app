package com.test.book_summary.model

data class PlayerState(
    val isPlaying: Boolean = false,
    val currentPlaybackSpeed: Float = 1f,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val error: String? = null
) {
    companion object {
        val Empty = PlayerState()
    }
}