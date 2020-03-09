package com.freestar.android.sample;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;

class VideoHelper {

    private static final String VIDEO_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

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
        HttpProxyCacheServer proxy = getProxy(videoView.getContext());
        String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
        parentWindow.addView(videoView);
        videoView.setVideoURI(Uri.parse(proxyUrl));
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

    private static HttpProxyCacheServer proxy;

    private static HttpProxyCacheServer getProxy(Context context) {
        if (proxy == null) {
            proxy = newProxy(context);
        }
        return proxy;
    }

    private static HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheSize(1024 * 1024 * 100)       // 100 mb for cache
                .build();
    }
}
