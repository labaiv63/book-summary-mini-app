package com.test.book_summary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ShortText
import androidx.compose.material.icons.rounded.Headset
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.test.book_summary.model.BookSummaryTab
import com.test.design_system.theme.Blue

@Composable
internal fun BookSummaryTabsComponent(
    modifier: Modifier = Modifier,
    bookSummaryTab: BookSummaryTab,
    onClick: (BookSummaryTab) -> Unit
) {
    Row(
        modifier = modifier
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = Color.Gray.copy(alpha = .3f),
                shape = CircleShape
            ),
    ) {
        IconButton(
            modifier = Modifier
                .padding(all = 4.dp)
                .background(
                    color = if (bookSummaryTab == BookSummaryTab.Audio) {
                        Blue
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                ),
            onClick = {
                onClick(BookSummaryTab.Audio)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Headset,
                contentDescription = "Audio",
                tint = if (bookSummaryTab == BookSummaryTab.Audio) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        }

        IconButton(
            modifier = Modifier
                .padding(all = 4.dp)
                .background(
                    color = if (bookSummaryTab == BookSummaryTab.Text) {
                        Blue
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                ),
            onClick = {
                onClick(BookSummaryTab.Text)
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ShortText,
                contentDescription = "Text",
                tint = if (bookSummaryTab == BookSummaryTab.Text) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        }
    }
}