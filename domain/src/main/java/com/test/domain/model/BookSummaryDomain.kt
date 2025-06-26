package com.test.domain.model

data class BookSummaryDomain(
    val imageUrl: String,
    val chapters: List<ChapterDomain>
)

data class ChapterDomain(
    val title: String,
    val audioUrl: String,
    val text: String
)