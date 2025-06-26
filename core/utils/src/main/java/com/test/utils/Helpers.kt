package com.test.utils

import java.util.Locale

fun formatDuration(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(
        locale = Locale.getDefault(),
        format = "%d:%02d",
        minutes,
        remainingSeconds
    )
}