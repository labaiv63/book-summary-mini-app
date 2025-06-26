package com.test.audio.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import com.test.audio.manager.AudioPlayerManager
import com.test.audio.model.AudioState
import com.test.bool_summary_mini_app.audio.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@UnstableApi
class AudioPlayerService : LifecycleService() {

    @Inject
    lateinit var audioPlayerManager: AudioPlayerManager

    private val notificationId = 1
    private val channelId = "audio_player_channel"
    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            ACTION_PLAY -> handlePlayAction(intent)
            ACTION_PAUSE -> audioPlayerManager.pause()
            ACTION_STOP -> stopSelf()
        }

        return START_STICKY
    }

    private fun handlePlayAction(intent: Intent) {
        val audioUrl = intent.getStringExtra(EXTRA_AUDIO_URL)
        audioUrl?.let {
            audioPlayerManager.playAudio(audioUrl)
            startForegroundService()
            return
        }
        audioPlayerManager.play()
    }

    private fun startForegroundService() {
        createNotificationChannel()
        val notification = buildNotification()
        startForeground(notificationId, notification)
        observeAudioState()
    }

    private fun observeAudioState() {
        lifecycleScope.launch {
            audioPlayerManager.audioState.collectLatest { state ->
                when (state) {
                    is AudioState.Playing, is AudioState.Paused -> {
                        updateNotification(state)
                    }
                    is AudioState.Ended, is AudioState.Error -> {
                        stopSelf()
                    }
                    else -> { /* Do nothing */ }
                }
            }
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Audio Player",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Audio player controls"
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildNotification(state: AudioState? = null): Notification {
        val playPauseAction = if (state is AudioState.Playing) {
            NotificationCompat.Action(
                R.drawable.outline_pause,
                "Pause",
                getPendingAction(ACTION_PAUSE)
            )
        } else {
            NotificationCompat.Action(
                R.drawable.play_arrow,
                "Play",
                getPendingAction(ACTION_PLAY)
            )
        }

        val stopAction = NotificationCompat.Action(
            R.drawable.baseline_stop_24,
            "Stop",
            getPendingAction(ACTION_STOP)
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Audio Player")
            .setContentText(state?.let { "Playing audio" } ?: "Preparing...")
            .setSmallIcon(R.drawable.baseline_audiotrack_24)
            .addAction(playPauseAction)
            .addAction(stopAction)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun updateNotification(state: AudioState) {
        val notification = buildNotification(state)
        notificationManager.notify(notificationId, notification)
    }

    private fun getPendingAction(action: String): PendingIntent {
        val intent = Intent(this, AudioPlayerService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        audioPlayerManager.release()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    companion object {
        private const val ACTION_PLAY = "ACTION_PLAY"
        private const val ACTION_PAUSE = "ACTION_PAUSE"
        private const val ACTION_STOP = "ACTION_STOP"
        private const val EXTRA_AUDIO_URL = "extra_audio_url"

        fun playAudio(context: android.content.Context, audioUrl: String) {
            val intent = Intent(context, AudioPlayerService::class.java).apply {
                action = ACTION_PLAY
                putExtra(EXTRA_AUDIO_URL, audioUrl)
            }
            context.startService(intent)
        }

        fun stopService(context: android.content.Context) {
            val intent = Intent(context, AudioPlayerService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
}
