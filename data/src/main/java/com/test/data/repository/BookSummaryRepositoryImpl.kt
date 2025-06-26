package com.test.data.repository

import com.test.domain.model.BookSummaryDomain
import com.test.domain.model.ChapterDomain
import com.test.domain.repository.BookSummaryRepository
import javax.inject.Inject

internal class BookSummaryRepositoryImpl @Inject constructor(): BookSummaryRepository {

    override suspend fun getBookSummary(): Result<BookSummaryDomain> {
        return Result.success(dummyBookSummary)
    }
}

private val dummyBookSummary = BookSummaryDomain(
    imageUrl = "https://picsum.photos/1200/1000",
    chapters = listOf(
        ChapterDomain(
            title = "Chapter 1",
            audioUrl = "https://webaudioapi.com/samples/audio-tag/chrono.mp3",
            text = "Chapter 1 description"
        ),
        ChapterDomain(
            title = "Chapter 2",
            audioUrl = "https://webaudioapi.com/samples/audio-tag/chrono.mp3",
            text = "Chapter 2 description"
        ),
        ChapterDomain(
            title = "Chapter 3",
            audioUrl = "https://webaudioapi.com/samples/audio-tag/chrono.mp3",
            text = "Chapter 3 description"
        ),
        ChapterDomain(
            title = "Chapter 4",
            audioUrl = "https://webaudioapi.com/samples/audio-tag/chrono.mp3",
            text = "Chapter 4 description"
        ),
        ChapterDomain(
            title = "Chapter 5",
            audioUrl = "https://webaudioapi.com/samples/audio-tag/chrono.mp3",
            text = "Chapter 5 description"
        ),
        ChapterDomain(
            title = "Chapter 6",
            audioUrl = "https://webaudioapi.com/samples/audio-tag/chrono.mp3",
            text = "Chapter 6 description"
        )
    )
)