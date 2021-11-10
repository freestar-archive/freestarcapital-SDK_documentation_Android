package com.freestar.android.sample;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

class VideoHelper {

    private VideoView videoView;
    private Context context;
    private ViewGroup parentWindow;
    private int currentPosition;

    VideoHelper(Context context, ViewGroup parentWindow) {
        this.context = context;
        this.parentWindow = parentWindow;
    }

    void playContentVideo(int currentPosition) {
        parentWindow.removeAllViews();
        MediaController mediaController = new MediaController(context);
        videoView = new VideoView(context);
        parentWindow.addView(videoView);
        videoView.setVideoPath("android.resource://" + context.getPackageName() + "/" + R.raw.sample5s);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.seekTo(currentPosition);
        videoView.start();
    }

    void pause() {
        //surface view of videoView is destroyed by Android on pause activity lifecycle
        if (videoView != null) {
            currentPosition = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    void resume() {
        if (videoView != null && currentPosition > 0) {
            playContentVideo(currentPosition);
        }
    }

    void cleanUp() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
