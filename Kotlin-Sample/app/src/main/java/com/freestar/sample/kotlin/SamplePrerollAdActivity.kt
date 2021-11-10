package com.freestar.sample.kotlin

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import com.freestar.android.ads.PrerollAdListener
import com.freestar.android.ads.PrerollAd
import android.widget.FrameLayout
import android.os.Bundle
import android.view.WindowManager
import android.os.Build
import com.freestar.sample.kotlin.SamplePrerollAdActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import com.freestar.android.ads.AdRequest
import com.freestar.android.ads.AdSize

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
class SamplePrerollAdActivity : Activity(), PrerollAdListener {
    private var preRollVideoAd: PrerollAd? = null
    private var frameLayout: FrameLayout? = null
    private var isLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        hideNavigation()
        frameLayout = FrameLayout(this)
        frameLayout!!.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        frameLayout!!.setBackgroundColor(Color.TRANSPARENT)
        setContentView(frameLayout)
        preRollVideoAd = PrerollAd(this)
        val adRequest = AdRequest(this)
        preRollVideoAd!!.loadAd(adRequest, AdSize.PREROLL_320_480, this)
    }

    private fun hideNavigation() {
        if (Build.VERSION.SDK_INT >= 19) window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY else window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    override fun onPrerollAdLoaded(prerollAd: View, placement: String?) {
        Log.i(TAG, "onPrerollAdLoaded")
        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.widthPixels * 9 / 16
        val params = FrameLayout.LayoutParams(displayMetrics.widthPixels, height)
        params.gravity = Gravity.CENTER
        preRollVideoAd!!.adView.layoutParams = params
        frameLayout!!.addView(preRollVideoAd!!.adView)
        preRollVideoAd!!.showAd()
        isLoaded = true
        frameLayout!!.setBackgroundColor(Color.BLACK)
    }

    override fun onPrerollAdFailed(prerollAd: View, placement: String?, errorCode: Int) {
        Log.i(TAG, "onPrerollAdFailed")
        finishWithResult(RESULT_NO_FILL)
    }

    override fun onPrerollAdShownError(prerollAd: View, placement: String?) {
        Log.i(TAG, "onPrerollAdShownError")
        finishWithResult(RESULT_AD_ERROR)
    }

    override fun onPrerollAdShown(prerollAd: View, placement: String?) {
        Log.i(TAG, "onPrerollAdShown")
    }

    override fun onPrerollAdClicked(prerollAd: View, placement: String?) {
        Log.i(TAG, "onPrerollAdClicked")
    }

    override fun onPrerollAdCompleted(prerollAd: View, placement: String?) {
        Log.i(TAG, "onPrerollAdCompleted")
        finishWithResult(RESULT_AD_COMPLETED)
    }

    private var afterFirst = false
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "  onResume")
        if (!afterFirst) {
            afterFirst = true
        } else {
            if (isLoaded) {
                preRollVideoAd!!.onResume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "  onPause")
        if (isLoaded) {
            preRollVideoAd!!.onPause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "  onDestroy")
        if (isLoaded) {
            preRollVideoAd!!.destroyView()
        }
    }

    override fun onBackPressed() {
        //don't allow user to go back until ad is finished or no-fill
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        hideNavigation()
        if (isLoaded) {
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.i(TAG, "  onConfigurationChanged PORTRAIT")
                val displayMetrics = resources.displayMetrics
                //x = 1024 * 9 / 16   (1024 is the physical width)
                val height = displayMetrics.widthPixels * 9 / 16
                val params = FrameLayout.LayoutParams(displayMetrics.widthPixels, height)
                params.gravity = Gravity.CENTER
                preRollVideoAd!!.adView.layoutParams.width = displayMetrics.widthPixels
                preRollVideoAd!!.adView.layoutParams.height = height
            } else {
                Log.i(TAG, "  onConfigurationChanged LANDSCAPE")
                preRollVideoAd!!.adView.layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
                preRollVideoAd!!.adView.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            }
        }
    }

    private fun finishWithResult(result: Int) {
        setResult(result)
        finish()
    }

    companion object {
        private const val TAG = "SamplePrerollAdActivity"
        const val RESULT_NO_FILL = 100
        const val RESULT_AD_COMPLETED = 101
        const val RESULT_AD_ERROR = 102
    }
}