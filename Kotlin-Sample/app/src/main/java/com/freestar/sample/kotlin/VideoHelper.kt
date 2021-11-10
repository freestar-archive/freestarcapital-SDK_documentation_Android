package com.freestar.sample.kotlin

import android.content.Context
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView

internal class VideoHelper(private val context: Context, private val parentWindow: ViewGroup) {
    private var videoView: VideoView? = null
    private var currentPosition = 0
    fun playContentVideo(currentPosition: Int) {
        parentWindow.removeAllViews()
        val mediaController = MediaController(
            context
        )
        videoView = VideoView(context)
        parentWindow.addView(videoView)
        videoView!!.setVideoPath("android.resource://" + context.packageName + "/" + R.raw.sample5s)
        videoView!!.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)
        mediaController.setMediaPlayer(videoView)
        videoView!!.seekTo(currentPosition)
        videoView!!.start()
    }

    fun pause() {
        //surface view of videoView is destroyed by Android on pause activity lifecycle
        if (videoView != null) {
            currentPosition = videoView!!.currentPosition
            videoView!!.pause()
        }
    }

    fun resume() {
        if (videoView != null && currentPosition > 0) {
            playContentVideo(currentPosition)
        }
    }

    fun cleanUp() {
        if (videoView != null) {
            videoView!!.stopPlayback()
        }
    }
}