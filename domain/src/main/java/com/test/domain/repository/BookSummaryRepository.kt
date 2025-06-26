package com.test.domain.repository

import com.test.domain.model.BookSummaryDomain

interface BookSummaryRepository {
    suspend fun getBookSummary(): Result<BookSummaryDomain>
}