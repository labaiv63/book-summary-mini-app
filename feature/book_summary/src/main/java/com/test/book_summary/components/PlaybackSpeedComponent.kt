package com.test.book_summary.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
internal fun PlaybackSpeedComponent(
    modifier: Modifier = Modifier,
    playbackSpeed: Float,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray.copy(alpha = .5f),
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(
            horizontal = 10.dp
        )
    ) {
        Text("Speed x${String.format(locale = Locale.ROOT, format = "%.1f", playbackSpeed)}")
    }
}