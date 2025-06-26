package com.test.book_summary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay5
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun PlayerControlsComponent(
    modifier: Modifier = Modifier,
    playPauseImageVector: ImageVector = Icons.Rounded.PlayArrow,
    onPreviousClick: () -> Unit,
    onRewindClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onForwardClick: () -> Unit,
    onNextClick: () -> Unit,
    iconSize: Dp = 48.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                modifier = Modifier.size(size = iconSize),
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = "Previous"
            )
        }
        IconButton(onClick = onRewindClick) {
            Icon(
                modifier = Modifier.size(size = iconSize),
                imageVector = Icons.Rounded.Replay5,
                contentDescription = "Rewind"
            )
        }
        IconButton(onClick = onPlayPauseClick) {
            Icon(
                modifier = Modifier.size(size = iconSize),
                imageVector = playPauseImageVector,
                contentDescription = "Play/Pause"
            )
        }
        IconButton(onClick = onForwardClick) {
            Icon(
                modifier = Modifier.size(size = iconSize),
                imageVector = Icons.Rounded.Forward10,
                contentDescription = "Forward"
            )
        }
        IconButton(onClick = onNextClick) {
            Icon(
                modifier = Modifier
                    .size(size = iconSize)
                    .rotate(degrees = 180f),
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = "Next"
            )
        }
    }
}