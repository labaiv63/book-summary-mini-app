package com.test.audio.di

import android.content.Context
import com.test.audio.manager.AudioPlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AudioModule {

    @Singleton
    @Provides
    fun provideAudioPlayerManager(
        @ApplicationContext context: Context
    ): AudioPlayerManager = AudioPlayerManager(context)
}