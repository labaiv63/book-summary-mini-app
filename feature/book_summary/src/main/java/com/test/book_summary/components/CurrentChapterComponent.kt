package com.test.book_summary.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
internal fun CurrentChapterComponent(
    modifier: Modifier = Modifier,
    currentChapter: Int,
    totalChapters: Int
) {
    Text(
        modifier = modifier,
        style = TextStyle(
            color = Color.Gray.copy(alpha = .8f),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.5.sp
        ),
        text = "KEY POINT $currentChapter OF $totalChapters"
    )
}