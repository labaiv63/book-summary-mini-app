package com.test.book_summary.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.book_summary.model.PlayerState

@Composable
internal fun AudioTabComponent(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onChangePlaybackSpeed: () -> Unit,
    onPlayPreviousChapter: () -> Unit,
    onPlayNextChapter: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onRewindClick: () -> Unit,
    onFastForwardClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {

        SeekBarComponent(
            modifier = Modifier.padding(horizontal = 12.dp),
            currentPosition = playerState.currentPosition,
            totalDuration = playerState.duration.takeIf { it > 0 } ?: 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        PlaybackSpeedComponent(
            playbackSpeed = playerState.currentPlaybackSpeed,
            onClick = onChangePlaybackSpeed
        )

        PlayerControlsComponent(
            modifier = Modifier.padding(horizontal = 32.dp).weight(1f),
            playPauseImageVector = if (playerState.isPlaying) {
                Icons.Rounded.Pause
            } else {
                Icons.Rounded.PlayArrow
            },
            onPreviousClick = onPlayPreviousChapter,
            onNextClick = onPlayNextChapter,
            onPlayPauseClick = onTogglePlayPause,
            onRewindClick = onRewindClick,
            onForwardClick = onFastForwardClick
        )
    }
}