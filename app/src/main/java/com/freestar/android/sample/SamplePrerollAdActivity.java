package com.freestar.android.sample;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.AdSize;
import com.freestar.android.ads.PrerollAd;
import com.freestar.android.ads.PrerollAdListener;

import androidx.annotation.NonNull;

/**
 *
 * This is a sample Activity of how to run fullscreen preroll ads.
 * Since preroll ads in 320x480 (LVDOAdSize.PREROLL_320_480) are just a View you can implement
 * however you wish, e.g. in a fragment, dialog, etc.
 *
 * FYI: LVDOAdSize.PREROLL_FULLSCREEN is just a wrapper around a transparent Activity like this
 * and internally embedded in the FreeStar SDK for your convenience.
 *
 *
 */
public class SamplePrerollAdActivity extends Activity implements PrerollAdListener {

    private static final String TAG = "SamplePrerollAdActivity";

    static final int RESULT_NO_FILL = 100;
    static final int RESULT_AD_COMPLETED = 101;
    static final int RESULT_AD_ERROR = 102;

    private PrerollAd preRollVideoAd;
    private FrameLayout frameLayout;
    private boolean isLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideNavigation();
        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        frameLayout.setBackgroundColor(Color.TRANSPARENT);
        setContentView(frameLayout);
        preRollVideoAd = new PrerollAd(this);
        AdRequest adRequest = new AdRequest(this);
        preRollVideoAd.loadAd(adRequest, AdSize.PREROLL_320_480, this);
    }

    private void hideNavigation() {
        if (Build.VERSION.SDK_INT >= 19)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        else
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPrerollAdLoaded(View prerollAd, String placement) {
        Log.i(TAG, "onPrerollAdLoaded");
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int height = (displayMetrics.widthPixels * 9) / 16;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayMetrics.widthPixels, height);
        params.gravity = Gravity.CENTER;
        preRollVideoAd.getAdView().setLayoutParams(params);
        frameLayout.addView(preRollVideoAd.getAdView());
        preRollVideoAd.showAd();
        isLoaded = true;
        frameLayout.setBackgroundColor(Color.BLACK);
    }

    @Override
    public void onPrerollAdFailed(View prerollAd, String placement, int errorCode) {
        Log.i(TAG, "onPrerollAdFailed");
        finishWithResult(RESULT_NO_FILL);
    }

    @Override
    public void onPrerollAdShownError(View prerollAd, String placement) {
        Log.i(TAG, "onPrerollAdShownError");
        finishWithResult(RESULT_AD_ERROR);
    }

    @Override
    public void onPrerollAdShown(View prerollAd, String placement) {
        Log.i(TAG, "onPrerollAdShown");
    }

    @Override
    public void onPrerollAdClicked(View prerollAd, String placement) {
        Log.i(TAG, "onPrerollAdClicked");
    }

    @Override
    public void onPrerollAdCompleted(View prerollAd, String placement) {
        Log.i(TAG, "onPrerollAdCompleted");
        finishWithResult(RESULT_AD_COMPLETED);
    }

    private boolean afterFirst = false;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "  onResume");

        if (!afterFirst) {
            afterFirst = true;
        } else {
            if (isLoaded) {
                preRollVideoAd.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "  onPause");
        if (isLoaded) {
            preRollVideoAd.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "  onDestroy");
        if (isLoaded) {
            preRollVideoAd.destroyView();
        }
    }

    @Override
    public void onBackPressed() {
        //don't allow user to go back until ad is finished or no-fill
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        hideNavigation();

        if (isLoaded) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.i(TAG, "  onConfigurationChanged PORTRAIT");
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                //x = 1024 * 9 / 16   (1024 is the physical width)
                int height = (displayMetrics.widthPixels * 9) / 16;
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayMetrics.widthPixels, height);
                params.gravity = Gravity.CENTER;
                preRollVideoAd.getAdView().getLayoutParams().width = displayMetrics.widthPixels;
                preRollVideoAd.getAdView().getLayoutParams().height = height;
            } else {
                Log.i(TAG, "  onConfigurationChanged LANDSCAPE");
                preRollVideoAd.getAdView().getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
                preRollVideoAd.getAdView().getLayoutParams().height = FrameLayout.LayoutParams.MATCH_PARENT;
            }
        }
    }

    private void finishWithResult(int result) {
        setResult(result);
        finish();
    }
}
