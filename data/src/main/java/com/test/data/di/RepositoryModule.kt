package com.test.data.di

import com.test.data.repository.BookSummaryRepositoryImpl
import com.test.domain.repository.BookSummaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {

    @Binds
    @Singleton
    fun bindBookSummaryRepository(impl: BookSummaryRepositoryImpl): BookSummaryRepository
}