package com.test.book_summary.screen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.test.audio.manager.AudioPlayerManager
import com.test.audio.model.AudioState
import com.test.audio.service.AudioPlayerService
import com.test.book_summary.model.BookSummaryTab
import com.test.book_summary.model.BookSummaryUiState
import com.test.domain.model.ChapterDomain
import com.test.domain.repository.BookSummaryRepository
import com.test.utils.extensions.finally
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@UnstableApi
class BookSummaryViewModel @Inject constructor(
    application: Application,
    private val audioPlayerManager: AudioPlayerManager,
    private val bookSummaryRepository: BookSummaryRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<BookSummaryUiState>(BookSummaryUiState())
    val uiState = _uiState.asStateFlow()

    private var chapters: List<ChapterDomain> = emptyList()

    init {
        getBookSummary()
        observeAudioState()
        observePlaybackPosition()
    }

    private fun getBookSummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(2000) // Simulate network delay
            bookSummaryRepository.getBookSummary().onSuccess { result ->
                chapters = result.chapters
                _uiState.update {
                    it.copy(
                        currentChapterIndex = 0,
                        totalChapters = chapters.size,
                        currentChapterTitle = chapters.takeIf { it.isNotEmpty() }?.get(0)?.title.orEmpty(),
                        currentChapterText = chapters.takeIf { it.isNotEmpty() }?.get(0)?.text.orEmpty(),
                        bookImageUrl = result.imageUrl,
                        error = null
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }.finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun observePlaybackPosition() {
        viewModelScope.launch {
            audioPlayerManager.playbackPosition.collect { position ->
                _uiState.update {
                    it.copy(playerState = it.playerState.copy(currentPosition = position))
                }
            }
        }
    }

    private fun observeAudioState() {
        audioPlayerManager.audioState
            .onEach { audioState ->
                _uiState.update { state ->
                    when (audioState) {
                        is AudioState.Playing -> state.copy(
                            playerState = state.playerState.copy(
                                isPlaying = true,
                                currentPlaybackSpeed = audioState.playbackSpeed,
                                currentPosition = audioState.currentPosition,
                                duration = audioState.duration
                            )
                        )

                        is AudioState.Paused -> state.copy(
                            playerState = state.playerState.copy(
                                isPlaying = false,
                                currentPlaybackSpeed = audioState.playbackSpeed,
                                currentPosition = audioState.currentPosition,
                                duration = audioState.duration
                            )
                        )

                        is AudioState.Error -> state.copy(
                            playerState = state.playerState.copy(
                                error = audioState.message
                            )
                        )

                        is AudioState.Ended -> state.copy(
                            playerState = state.playerState.copy(
                                isPlaying = false
                            )
                        )

                        else -> _uiState.value
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onBookTabClick(tab: BookSummaryTab) {
        _uiState.update {
            it.copy(
                currentTab = tab
            )
        }
    }

    fun onRetryToLoadBookSummaryClick() = getBookSummary()

    fun togglePlayPause() {
        if (!audioPlayerManager.isInitialized) {
            playAudio(chapters[uiState.value.currentChapterIndex].audioUrl)
            return
        }
        if (uiState.value.playerState.isPlaying) {
            audioPlayerManager.pause()
        } else {
            audioPlayerManager.play()
        }
    }

    fun onRewindClick() {
        seekTo { uiState.value.playerState.currentPosition - 5000}
    }

    fun onFastForwardClick() {
        seekTo { uiState.value.playerState.currentPosition + 10000}
    }

    private fun seekTo(calculation: () -> Long) {
        audioPlayerManager.seekTo(calculation().coerceIn(0, uiState.value.playerState.duration))
    }

    fun playPreviousChapter() {
        changeChapter { uiState.value.currentChapterIndex - 1 }
    }

    fun playNextChapter() {
        changeChapter { uiState.value.currentChapterIndex + 1 }
    }

    fun changePlaybackSpeed() {
        val currentPlaybackSpeed = uiState.value.playerState.currentPlaybackSpeed
        val newPlaybackSpeed = if (currentPlaybackSpeed >= 3f) 1f else currentPlaybackSpeed + 0.5f
        audioPlayerManager.setPlaybackSpeed(newPlaybackSpeed)
    }

    private fun changeChapter(calculation: () -> Int) {
        val maxValue = chapters.size.coerceAtLeast(1) - 1
        val newIndex = (calculation()).coerceIn(0, maxValue)
        _uiState.update {
            it.copy(
                currentChapterIndex = newIndex,
                currentChapterTitle = chapters[newIndex].title,
                currentChapterText = chapters[newIndex].text
            )
        }
        playAudio(chapters[newIndex].audioUrl)
    }

    private fun playAudio(audioUrl: String) {
        AudioPlayerService.playAudio(context = application.applicationContext, audioUrl = audioUrl)
    }
}