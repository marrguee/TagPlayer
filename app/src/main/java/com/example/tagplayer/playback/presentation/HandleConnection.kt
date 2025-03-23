package com.example.tagplayer.playback.presentation

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.tagplayer.core.media_service.MediaService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

interface HandleConnection : PlayerCommands{
    fun onDisconnected(block: () -> Unit)
    fun onConnected(block: (controller: MediaController) -> Unit)
    fun build()
    fun connect(create: CreatePlayerListener)
    fun disconnect()

    class Base(context: Context) : HandleConnection {
        private lateinit var future: ListenableFuture<MediaController>
        private var controller: MediaController? = null
        private var connected: (controller: MediaController) -> Unit = {}
        private var playerListener: Listener = PlayerListener.Empty
        private val builder: MediaController.Builder = MediaController.Builder(
            context,
            SessionToken(context, ComponentName(context, MediaService::class.java))
        )

        override fun onDisconnected(block: () -> Unit) {
            builder.setListener(object : MediaController.Listener {
                override fun onDisconnected(controller: MediaController) {
                    block.invoke()
                    super.onDisconnected(controller)
                }
            })
        }

        override fun onConnected(block: (controller: MediaController) -> Unit) {
            connected = block
        }

        override fun build() {
            future = builder.buildAsync()
        }

        override fun connect(create: CreatePlayerListener) = future.addListener({
            controller = future.get().also { connected.invoke(it) }
            controller?.let {
                playerListener = create.listener(it)
                it.addListener(playerListener)
            }
        }, MoreExecutors.directExecutor())

        override fun disconnect() {
            controller?.removeListener(playerListener)
            playerListener = PlayerListener.Empty
        }

        override fun playPause() {
            controller?.run {
                if (playWhenReady) pause() else play()
            }
        }

        override fun rewind() {
            controller?.run {
                seekToPrevious()
                play()
            }
        }

        override fun clearMediaQueue() {
            controller?.run {
                pause()
                stop()
                clearMediaItems()
            }
        }

        override fun seek(pos: Long) {
            controller?.seekTo(pos)
        }
    }
}