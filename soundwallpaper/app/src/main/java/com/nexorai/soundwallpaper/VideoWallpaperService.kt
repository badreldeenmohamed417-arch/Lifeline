package com.nexorai.soundwallpaper

import android.app.KeyguardManager
import android.service.wallpaper.WallpaperService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Build
import android.view.SurfaceHolder
import java.io.File

class VideoWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = VideoEngine()

    private inner class VideoEngine : Engine() {
        private var mediaPlayer: MediaPlayer? = null
        private var prepared = false
        private var surfaceReady = false
        private var receiverRegistered = false
        private val videoFile = File(filesDir, "wallpaper.mp4")
        private val keyguard by lazy { getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager }

        private val screenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_OFF -> pausePlayer()
                    Intent.ACTION_SCREEN_ON, Intent.ACTION_USER_PRESENT -> updatePlayback()
                }
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            val filter = IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_SCREEN_ON)
                addAction(Intent.ACTION_USER_PRESENT)
            }
            registerReceiver(screenReceiver, filter)
            receiverRegistered = true
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            super.onSurfaceCreated(holder)
            surfaceReady = true
            updatePlayback()
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            surfaceReady = true
            updatePlayback()
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            surfaceReady = false
            releasePlayer()
            super.onSurfaceDestroyed(holder)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            updatePlayback()
        }

        override fun onDestroy() {
            if (receiverRegistered) {
                try { unregisterReceiver(screenReceiver) } catch (_: Exception) { }
                receiverRegistered = false
            }
            releasePlayer()
            super.onDestroy()
        }

        private fun shouldPlay(): Boolean {
            return isVisible && !isPreview && surfaceReady && videoFile.exists() &&
                videoFile.length() > 0L && !keyguard.isKeyguardLocked
        }

        private fun updatePlayback() {
            if (!shouldPlay()) {
                pausePlayer()
                return
            }
            if (mediaPlayer == null) createPlayer() else if (prepared) startPlayer()
        }

        private fun createPlayer() {
            if (!shouldPlay() || mediaPlayer != null) return
            val player = MediaPlayer()
            mediaPlayer = player
            prepared = false
            try {
                player.setDataSource(videoFile.absolutePath)
                player.setSurface(surfaceHolder.surface)
                player.isLooping = true
                player.setVolume(1.0f, 1.0f)
                player.setOnPreparedListener { readyPlayer ->
                    prepared = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        try { readyPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING) }
                        catch (_: Exception) { }
                    }
                    if (shouldPlay()) startPlayer()
                }
                player.setOnCompletionListener { completed ->
                    if (shouldPlay()) {
                        try { completed.seekTo(0); completed.start() } catch (_: Exception) { }
                    }
                }
                player.setOnErrorListener { _, _, _ ->
                    releasePlayer()
                    true
                }
                player.prepareAsync()
            } catch (_: Exception) {
                releasePlayer()
            }
        }

        private fun startPlayer() {
            val player = mediaPlayer ?: return
            if (!shouldPlay() || !prepared) return
            try { if (!player.isPlaying) player.start() } catch (_: Exception) { }
        }

        private fun pausePlayer() {
            val player = mediaPlayer ?: return
            try { if (prepared && player.isPlaying) player.pause() } catch (_: Exception) { }
        }

        private fun releasePlayer() {
            val player = mediaPlayer
            mediaPlayer = null
            prepared = false
            try {
                player?.setOnPreparedListener(null)
                player?.setOnCompletionListener(null)
                player?.setOnErrorListener(null)
                player?.reset()
                player?.release()
            } catch (_: Exception) { }
        }
    }
}
