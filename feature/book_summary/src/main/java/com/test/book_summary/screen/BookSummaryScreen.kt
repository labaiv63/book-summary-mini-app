package com.test.book_summary.screen

import android.os.Build
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.test.book_summary.components.AudioTabComponent
import com.test.book_summary.components.BookSummaryTabsComponent
import com.test.book_summary.components.CurrentChapterComponent
import com.test.book_summary.model.BookSummaryTab
import com.test.book_summary.model.BookSummaryUiState
import com.test.design_system.components.CustomImageComponent
import com.test.design_system.models.CustomImage
import com.test.design_system.theme.Blue

@kotlin.OptIn(ExperimentalPermissionsApi::class)
@Composable
@UnstableApi
fun BookSummaryScreen() {
    val viewModel: BookSummaryViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState = rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS
        )

        LaunchedEffect(Unit) {
            if (notificationPermissionState.status.isGranted) return@LaunchedEffect
            notificationPermissionState.launchPermissionRequest()
        }
    }

    when {
        uiState.value.error != null -> {
            BookSummaryErrorScreen(
                error = uiState.value.error.orEmpty(),
                onRetryClick = viewModel::onRetryToLoadBookSummaryClick
            )
        }

        uiState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Blue
                )
            }
        }

        else -> {
            BookSummarySuccessScreen(
                viewModel = viewModel,
                uiState = uiState.value
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun BookSummarySuccessScreen(
    modifier: Modifier = Modifier,
    viewModel: BookSummaryViewModel,
    uiState: BookSummaryUiState
) {
    val playerState = uiState.playerState

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        CustomImageComponent(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(if (LocalConfiguration.current.screenHeightDp <= 700) 1.5f else 1f)
                .padding(horizontal = 86.dp)
                .clip(RoundedCornerShape(12.dp)),
            image = CustomImage.Url(uiState.bookImageUrl),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(40.dp))

        CurrentChapterComponent(
            currentChapter = uiState.currentChapterIndex + 1,
            totalChapters = uiState.totalChapters
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = uiState.currentChapterTitle,
        )

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            modifier = Modifier.weight(1f),
            targetState = uiState.currentTab,
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(animationSpec = tween(200)),
                    initialContentExit = fadeOut(animationSpec = tween(200))
                )
            }
        ) {
            when (it) {
                BookSummaryTab.Audio -> {
                    AudioTabComponent(
                        playerState = playerState,
                        onChangePlaybackSpeed = viewModel::changePlaybackSpeed,
                        onPlayPreviousChapter = viewModel::playPreviousChapter,
                        onPlayNextChapter = viewModel::playNextChapter,
                        onTogglePlayPause = viewModel::togglePlayPause,
                        onRewindClick = viewModel::onRewindClick,
                        onFastForwardClick = viewModel::onFastForwardClick
                    )
                }

                BookSummaryTab.Text -> {
                    Text(
                        text = uiState.currentChapterText
                    )
                }
            }
        }

        BookSummaryTabsComponent(
            modifier = Modifier.navigationBarsPadding(),
            bookSummaryTab = uiState.currentTab,
            onClick = viewModel::onBookTabClick
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun BookSummaryErrorScreen(
    modifier: Modifier = Modifier,
    error: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(error)
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onRetryClick,
        ) {
            Text(text = "Retry")
        }
    }
}

@OptIn(UnstableApi::class)
@Preview(showBackground = true)
@Composable
private fun BookSummaryPreview() {
    MaterialTheme {
        BookSummaryScreen()
    }
}




