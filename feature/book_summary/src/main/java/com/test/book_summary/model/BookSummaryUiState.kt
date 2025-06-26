package com.test.book_summary.model

data class BookSummaryUiState(
    val playerState: PlayerState = PlayerState.Empty,
    val isLoading: Boolean = true,
    val bookImageUrl: String = "",
    val currentChapterIndex: Int = 0,
    val totalChapters: Int = 0,
    val currentChapterTitle: String = "",
    val currentChapterText: String = "",
    val currentTab: BookSummaryTab = BookSummaryTab.Audio,
    val error: String? = null
)