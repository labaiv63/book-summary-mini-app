package com.test.book_summary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.design_system.theme.Blue
import com.test.utils.formatDuration

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
internal fun SeekBarComponent(
    modifier: Modifier = Modifier,
    currentPosition: Long,
    totalDuration: Long
) {
    val formattedPosition = remember(currentPosition) {
        derivedStateOf { formatDuration(currentPosition) }
    }

    val formattedDuration = remember(totalDuration) {
        derivedStateOf { formatDuration(totalDuration) }
    }

    val sliderValue = remember(currentPosition) {
        derivedStateOf {
            runCatching { (currentPosition.toFloat() / totalDuration.toFloat()) }.getOrNull() ?: 0f
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = TextStyle(
                color = Color.Gray.copy(alpha = .8f),
                fontSize = 14.sp,
                letterSpacing = (-.01).sp
            ),
            text = formattedPosition.value
        )

        Slider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 6.dp),
            enabled = false,
            state = SliderState(
                value = sliderValue.value
            ),
            thumb = { state ->
                CustomSliderThumb()
            },
            track = { state ->
                CustomSliderTrack(currentPosition = state.value)
            }
        )

        Text(
            style = TextStyle(
                color = Color.Gray.copy(alpha = .8f),
                fontSize = 14.sp,
                letterSpacing = (-.01).sp
            ),
            text = formattedDuration.value
        )
    }
}

@Composable
private fun CustomSliderThumb(
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = Blue
) {
    Box(
        modifier = modifier
            .size(size = size)
            .background(
                color = color,
                shape = CircleShape
            )
    )
}

@Composable
private fun CustomSliderTrack(
    modifier: Modifier = Modifier,
    currentPosition: Float,
    height: Dp = 6.dp,
    color: Color = Blue
) {
    Box(
        modifier = Modifier
            .height(height = height)
            .fillMaxWidth()
            .background(
                color = Color.Gray.copy(alpha = .3f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = modifier
                .height(height = height)
                .background(
                    color = color,
                    shape = CircleShape
                )
                .fillMaxWidth(currentPosition)
        )
    }
}