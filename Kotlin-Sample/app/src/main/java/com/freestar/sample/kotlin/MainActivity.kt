package com.freestar.sample.kotlin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import com.freestar.sample.kotlin.MediationPartners.choosePartners
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.freestar.android.ads.*

class MainActivity : AppCompatActivity(), RewardedAdListener, InterstitialAdListener,
    PrerollAdListener {
    private var adRequest: AdRequest? = null
    private var rewardedAd: RewardedAd? = null
    private var interstitialAd: InterstitialAd? = null
    private var bannerAd: BannerAd? = null
    private var nativeAd: NativeAd? = null
    private var preRollVideoAd: PrerollAd? = null
    private var doPrerollAdFragment = false
    private var videoHelper: VideoHelper? = null
    private var isLargeLayout = false
    private var pageNum = 0
    private var bannerAdContainer = BannerAdContainer.wrapXwrap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        isLargeLayout = false // getResources().getBoolean(R.bool.large_layout);
        adRequest = AdRequest(this)
        adRequest!!.addCustomTargeting("my custom target", "value")
        pageNum = savedInstanceState?.getInt("page", 0) ?: 0
        title = "Freestar Page " + (pageNum + 1)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        (findViewById<View>(R.id.radioBannerWrapWrap) as RadioButton).isChecked =
            true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("page", pageNum)
    }

    override fun onResume() {
        super.onResume()
        if (interstitialAd != null) {
            interstitialAd!!.onResume()
        }
        if (rewardedAd != null) {
            rewardedAd!!.onResume()
        }
        if (bannerAd != null) {
            bannerAd!!.onResume()
        }
        if (preRollVideoAd != null) {
            preRollVideoAd!!.onResume()
        }
        if (videoHelper != null) {
            videoHelper!!.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (interstitialAd != null) {
            interstitialAd!!.onPause()
        }
        if (rewardedAd != null) {
            rewardedAd!!.onPause()
        }
        if (bannerAd != null) {
            bannerAd!!.onPause()
        }
        if (preRollVideoAd != null) {
            preRollVideoAd!!.onPause()
        }
        if (videoHelper != null) {
            videoHelper!!.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (interstitialAd != null) {
            interstitialAd!!.destroyView()
        }
        if (rewardedAd != null) {
            rewardedAd!!.destroyView()
        }
        if (bannerAd != null) {
            bannerAd!!.destroyView()
        }
        if (preRollVideoAd != null) {
            preRollVideoAd!!.destroyView()
        }
        if (videoHelper != null) videoHelper!!.cleanUp()
    }

    /**
     * Note: In production release, simply call:
     *
     *
     * interstitialAd.loadAd( adRequest )
     *
     *
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view - onclick view
     */
    fun loadInterstitialAd(view: View?) {
        interstitialAd = InterstitialAd(this, this)
        if (MainActivity.Companion.DO_CHOOSE_PARTNERS) {
            choosePartners(
                this,
                adRequest!!,
                MediationPartners.ADTYPE_INTERSTITIAL
            ) { dialog, which ->
                if (pageNum == 0) {
                    interstitialAd!!.loadAd(adRequest)
                } else {
                    interstitialAd!!.loadAd(adRequest, "interstitial_p$pageNum")
                }
            }
        } else {
            interstitialAd!!.loadAd(adRequest)
        }
    }

    private fun loadNativeAd(template: Int) {
        nativeAd = NativeAd(this)
        nativeAd!!.template = template
        nativeAd!!.setNativeAdListener(object : NativeAdListener {
            override fun onNativeAdLoaded(nativeAdView: View?, placement: String?) {
                (findViewById<View>(R.id.native_container) as ViewGroup).removeAllViews()
                (findViewById<View>(R.id.native_container) as ViewGroup).addView(nativeAdView)
            }

            override fun onNativeAdFailed(placement: String?, errorCode: Int) {
                Toast.makeText(this@MainActivity, "native ad failed", Toast.LENGTH_SHORT).show()
            }

            override fun onNativeAdClicked(placement: String?) {}
        })
        if (MainActivity.Companion.DO_CHOOSE_PARTNERS) {
            choosePartners(
                this,
                adRequest!!,
                MediationPartners.ADTYPE_BANNER
            ) { dialog, which -> nativeAd!!.loadAd(adRequest) }
        } else {
            nativeAd!!.loadAd(adRequest)
        }
    }

    fun loadNativeMedium(view: View?) {
        loadNativeAd(NativeAd.TEMPLATE_MEDIUM)
    }

    fun loadNativeSmall(view: View?) {
        loadNativeAd(NativeAd.TEMPLATE_SMALL)
    }

    /**
     * Note: In production release, simply call:
     *
     *
     * rewardedAd.loadAd( adRequest )
     *
     *
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view - onclick view
     */
    fun loadRewardedAd(view: View?) {
        rewardedAd = RewardedAd(this, this)
        if (MainActivity.Companion.DO_CHOOSE_PARTNERS) {
            choosePartners(this, adRequest!!, MediationPartners.ADTYPE_REWARDED) { dialog, which ->
                if (pageNum == 0) {
                    rewardedAd!!.loadAd(adRequest)
                } else {
                    rewardedAd!!.loadAd(adRequest, "rewarded_p$pageNum")
                }
            }
        } else {
            rewardedAd!!.loadAd(adRequest)
        }
    }

    private fun clearStandardBannerAds() {
        val standardWidth = resources.getDimensionPixelSize(R.dimen.fs_default_banner_width)
        val standardHeight = resources.getDimensionPixelSize(R.dimen.fs_default_banner_height)
        var params = LinearLayout.LayoutParams(standardWidth, standardHeight)
        (findViewById<View>(R.id.banner_320_50) as FrameLayout).removeAllViews()
        (findViewById<View>(R.id.banner_320_50) as FrameLayout).layoutParams = params
        (findViewById<View>(R.id.banner_320_50) as FrameLayout).forceLayout()
        params = LinearLayout.LayoutParams(-1, -2)
        (findViewById<View>(R.id.banner_fullwidth_wrap) as FrameLayout).removeAllViews()
        (findViewById<View>(R.id.banner_fullwidth_wrap) as FrameLayout).layoutParams =
            params
        (findViewById<View>(R.id.banner_fullwidth_wrap) as FrameLayout).forceLayout()
        params = LinearLayout.LayoutParams(-2, -2)
        (findViewById<View>(R.id.banner_wrap_wrap) as FrameLayout).removeAllViews()
        (findViewById<View>(R.id.banner_wrap_wrap) as FrameLayout).layoutParams =
            params
        (findViewById<View>(R.id.banner_wrap_wrap) as FrameLayout).forceLayout()
    }

    private fun loadBannerAd(adSize: AdSize, resBannerContainer: Int) {
        bannerAd = BannerAd(this)
        bannerAd!!.adSize = adSize
        bannerAd!!.setBannerAdListener(object : BannerAdListener {
            override fun onBannerAdLoaded(view: View, placement: String?) {
                clearStandardBannerAds()
                (findViewById<View>(resBannerContainer) as ViewGroup).addView(view)
                (findViewById<View>(R.id.textView) as TextView).text =
                    "Banner " + adSize + " winner: " + bannerAd!!.winningPartnerName + " isAdaptive: " + bannerAd!!.isAdaptiveBannerAd
                ChocolateLogger.i(
                    MainActivity.Companion.TAG,
                    "onBannerAdLoaded() " + view.width + '/' + view.height
                )
            }

            override fun onBannerAdFailed(view: View?, placement: String?, errorCode: Int) {
                (findViewById<View>(R.id.textView) as TextView).text =
                    "Banner " + adSize + ": " + ErrorCodes.getErrorDescription(errorCode)
            }

            override fun onBannerAdClicked(view: View, placement: String?) {}
            override fun onBannerAdClosed(view: View, placement: String?) {}
        })
        if (MainActivity.Companion.DO_CHOOSE_PARTNERS) {
            choosePartners(this, adRequest!!, MediationPartners.ADTYPE_BANNER) { dialog, which ->
                if (pageNum == 0) {
                    bannerAd!!.loadAd(adRequest)
                } else {
                    bannerAd!!.loadAd(adRequest, "banner_p$pageNum")
                }
            }
        } else {
            bannerAd!!.loadAd(adRequest)
        }
    }

    /**
     * Note: In production release, simply call:
     *
     *
     * bannerAd.loadAd( adRequest )
     *
     *
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view - onclick view
     */
    fun loadBannerAd(view: View?) {
        var containerRes = R.id.banner_wrap_wrap
        when (bannerAdContainer) {
            BannerAdContainer.fullXwrap -> containerRes = R.id.banner_fullwidth_wrap
            BannerAdContainer.wrapXwrap -> containerRes = R.id.banner_wrap_wrap
            BannerAdContainer.standard -> containerRes = R.id.banner_320_50
            else -> {
            }
        }
        loadBannerAd(AdSize.BANNER_320_50, containerRes)
    }

    fun loadBannerAdMREC(view: View?) {
        loadBannerAd(AdSize.MEDIUM_RECTANGLE_300_250, R.id.mrec_container)
    }

    fun loadBannerAdLEADERBOARD(view: View?) {
        loadBannerAd(AdSize.LEADERBOARD_728_90, R.id.leaderboard_container)
    }

    private fun loadPrerollAd() {
        cleanupPreroll()
        preRollVideoAd = PrerollAd(this)
        if (MainActivity.Companion.DO_CHOOSE_PARTNERS) {
            choosePartners(this, adRequest!!, MediationPartners.ADTYPE_PREROLL) { dialog, which ->
                if (pageNum == 0) {
                    preRollVideoAd!!.loadAd(adRequest, AdSize.PREROLL_320_480, this@MainActivity)
                } else {
                    preRollVideoAd!!.loadAd(
                        adRequest,
                        AdSize.PREROLL_320_480,
                        "preroll_p$pageNum",
                        this@MainActivity
                    )
                }
            }
        } else {
            preRollVideoAd!!.loadAd(adRequest, AdSize.PREROLL_320_480, this@MainActivity)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            when (resultCode) {
                SamplePrerollAdActivity.RESULT_AD_COMPLETED -> playUserContent()
                SamplePrerollAdActivity.RESULT_AD_ERROR -> Toast.makeText(
                    this,
                    "ad error: could not play ad",
                    Toast.LENGTH_SHORT
                ).show()
                SamplePrerollAdActivity.RESULT_NO_FILL -> Toast.makeText(
                    this,
                    "full screen preroll NO-FILL",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                }
            }
        }
    }

    fun loadPrerollAdAsView(view: View?) {
        doPrerollAdFragment = false
        loadPrerollAd()
    }

    fun loadPrerollAdAsFragment(view: View?) {
        doPrerollAdFragment = true
        loadPrerollAd()
    }

    fun loadPrerollAdAsActivity(view: View?) {
        cleanupPreroll()
        startActivityForResult(Intent(this, SamplePrerollAdActivity::class.java), 10)
    }

    private fun cleanupPreroll() {
        if (preRollVideoAd != null) preRollVideoAd!!.destroyView()
        supportFragmentManager.popBackStackImmediate()
    }

    override fun onInterstitialLoaded(placement: String?) {
        interstitialAd!!.show()
        (findViewById<View>(R.id.textView) as TextView).text =
            "Interstitial Ad winner: " + interstitialAd!!.winningPartnerName
    }

    override fun onInterstitialFailed(placement: String?, errorCode: Int) {
        (findViewById<View>(R.id.textView) as TextView).text =
            "Interstitial No-Fill"
    }

    override fun onInterstitialShown(placement: String?) {}
    override fun onInterstitialClicked(placement: String?) {}
    override fun onInterstitialDismissed(placement: String?) {}

    /**
     * view - The preroll video ad view
     */
    override fun onPrerollAdLoaded(view: View, placement: String?) {
        if (bannerAd != null) bannerAd!!.destroyView()
        (findViewById<View>(R.id.textView) as TextView).text =
            "PreRoll Ad winner: " + preRollVideoAd!!.winningPartnerName
        (findViewById<View>(R.id.mrec_container) as ViewGroup).removeAllViews()
        ChocolateLogger.i(MainActivity.Companion.TAG, "onPrerollAdLoaded")
        if (doPrerollAdFragment) {
            showPrerollAdFragment()
        } else {
            (findViewById<View>(R.id.mrec_container) as ViewGroup).addView(view)
            preRollVideoAd!!.showAd()
        }
    }

    override fun onPrerollAdFailed(view: View, placement: String?, errorCode: Int) {
        (findViewById<View>(R.id.textView) as TextView).text =
            "Preroll: " + ErrorCodes.getErrorDescription(errorCode)
    }

    override fun onPrerollAdShown(view: View, placement: String?) {
        ChocolateLogger.i(MainActivity.Companion.TAG, "onPrerollAdShown")
    }

    override fun onPrerollAdShownError(view: View, placement: String?) {}
    override fun onPrerollAdClicked(view: View, placement: String?) {}
    override fun onPrerollAdCompleted(view: View, placement: String?) {
        ChocolateLogger.i(MainActivity.Companion.TAG, "onPrerollAdCompleted")
        playUserContent()
    }

    private fun playUserContent() {
        //Let's pretend you want to roll a movie/video when the preroll ad is completed.
        cleanupPreroll()
        videoHelper = VideoHelper(this, (findViewById<View>(R.id.mrec_container) as FrameLayout))
        videoHelper!!.playContentVideo(0)
        ChocolateLogger.i(MainActivity.Companion.TAG, "playUserContent")
    }

    override fun onRewardedVideoLoaded(placement: String?) {
        rewardedAd!!.showRewardAd("my secret code", "my userid", "V-BUCKS", "5000")
        (findViewById<View>(R.id.textView) as TextView).text =
            "Rewarded Ad winner: " + rewardedAd!!.winningPartnerName
    }

    override fun onRewardedVideoFailed(placement: String?, errorCode: Int) {
        (findViewById<View>(R.id.textView) as TextView).text = "Rewarded No-Fill"
    }

    override fun onRewardedVideoShown(placement: String?) {}
    override fun onRewardedVideoShownError(placement: String?, errorCode: Int) {
        (findViewById<View>(R.id.textView) as TextView).text =
            "Rewarded Got Fill, but Error Showing"
    }

    override fun onRewardedVideoDismissed(placement: String?) {}
    override fun onRewardedVideoCompleted(placement: String?) {}
    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is CustomDialogFragment) {
            fragment.setAdView(preRollVideoAd)
            ChocolateLogger.i(MainActivity.Companion.TAG, "onAttachFragment")
        }
    }

    class CustomDialogFragment : DialogFragment() {
        private var preRollVideoAd: PrerollAd? = null

        /**
         * The system calls this to get the DialogFragment's layout, regardless
         * of whether it's being displayed as a dialog or an embedded fragment.
         */
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout to use as dialog or embedded fragment
            val frameLayout = FrameLayout(container!!.context)
            frameLayout.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            frameLayout.setBackgroundColor(Color.TRANSPARENT)
            val displayMetrics = resources.displayMetrics
            val height = displayMetrics.widthPixels * 9 / 16
            val params = FrameLayout.LayoutParams(displayMetrics.widthPixels, height)
            params.gravity = Gravity.CENTER
            preRollVideoAd!!.adView.layoutParams = params
            frameLayout.addView(preRollVideoAd!!.adView)
            preRollVideoAd!!.showAd()
            frameLayout.setBackgroundColor(Color.BLACK)
            ChocolateLogger.i(MainActivity.Companion.TAG, "CustomDialogFragment onCreateView")
            return frameLayout
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            ChocolateLogger.i(MainActivity.Companion.TAG, "CustomDialogFragment onActivityCreated")
            super.onActivityCreated(savedInstanceState)
            (activity as MainActivity?)!!.hideSystemUI()
        }

        override fun onDetach() {
            ChocolateLogger.i(MainActivity.Companion.TAG, "CustomDialogFragment onDetach")
            super.onDetach()
            (activity as MainActivity?)!!.showSystemUI()
        }

        fun setAdView(preRollVideoAd: PrerollAd?) {
            ChocolateLogger.i(MainActivity.Companion.TAG, "setAdView")
            this.preRollVideoAd = preRollVideoAd
        }

        /**
         * The system calls this only when creating the layout in a dialog.
         */
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            ChocolateLogger.i(MainActivity.Companion.TAG, "  onCreateDialog")
            // to modify any dialog characteristics. For example, the dialog includes a
            // title by default, but your custom layout might not need it. So here you can
            // remove the dialog title, but you must call the superclass to get the Dialog.
            val dialog = super.onCreateDialog(savedInstanceState)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            return dialog
        }

        override fun onConfigurationChanged(newConfig: Configuration) {
            super.onConfigurationChanged(newConfig)
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.i(MainActivity.Companion.TAG, "  onConfigurationChanged PORTRAIT")
                val displayMetrics = resources.displayMetrics
                //x = 1024 * 9 / 16   (1024 is the physical width)
                val height = displayMetrics.widthPixels * 9 / 16
                val params = FrameLayout.LayoutParams(displayMetrics.widthPixels, height)
                params.gravity = Gravity.CENTER
                preRollVideoAd!!.adView.layoutParams.width = displayMetrics.widthPixels
                preRollVideoAd!!.adView.layoutParams.height = height
            } else {
                Log.i(MainActivity.Companion.TAG, "  onConfigurationChanged LANDSCAPE")
                preRollVideoAd!!.adView.layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT
                preRollVideoAd!!.adView.layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
            }
        }

        companion object {
            fun newInstance(): CustomDialogFragment {
                return CustomDialogFragment()
            }
        }
    }

    private fun showPrerollAdFragment() {
        ChocolateLogger.i(
            MainActivity.Companion.TAG,
            "showPrerollAdFragment UI thread? " + (Looper.myLooper() == Looper.getMainLooper())
        )
        val fragmentManager = supportFragmentManager
        val newFragment = CustomDialogFragment.newInstance()
        ChocolateLogger.i(MainActivity.Companion.TAG, "showPrerollAdFragment 2")
        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog")
        } else {
            // The device is smaller, so show the fragment fullscreen
            val transaction = fragmentManager.beginTransaction()
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            ChocolateLogger.i(MainActivity.Companion.TAG, "showPrerollAdFragment 3")
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
            supportFragmentManager.executePendingTransactions()
            ChocolateLogger.i(MainActivity.Companion.TAG, "showPrerollAdFragment 4")
        }
    }

    fun hideSystemUI() {

        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE or  // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        showActionBar(false)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    fun showSystemUI() {
        showActionBar(true)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    private fun showActionBar(show: Boolean) {
        if (supportActionBar == null) return
        if (show) supportActionBar!!.show() else supportActionBar!!.hide()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 0) {
            return
        }
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.choose_pages, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.page1 -> {
                pageNum = 0
                title = "Freestar Page 1"
                true
            }
            R.id.page2 -> {
                title = "Freestar Page 2"
                pageNum = 1
                true
            }
            R.id.page3 -> {
                title = "Freestar Page 3"
                pageNum = 2
                true
            }
            R.id.menu_recyclerview -> {
                startActivity(Intent(this, RecyclerViewInfiniteAdsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onShowAdaptiveCheck(view: View) {
        val checkBox = view as CheckBox
        FreeStarAds.showAdaptiveBannerAdsWhenAvailable(checkBox.isChecked)
        ChocolateLogger.i(MainActivity.Companion.TAG, "onShowAdaptiveCheck: " + checkBox.isChecked)
        showAlertDialog("For this it is recommended to restart app and then try; not intended to toggle back-n-forth within same session for small banner.")
    }

    fun onBanner320x50Checked(view: View) {
        val radioButton = view as RadioButton
        if (radioButton.isChecked) {
            bannerAdContainer = BannerAdContainer.standard
        }
    }

    fun onBannerWrapWrapChecked(view: View) {
        val radioButton = view as RadioButton
        if (radioButton.isChecked) {
            bannerAdContainer = BannerAdContainer.wrapXwrap
        }
    }

    fun onBannerFullWrapChecked(view: View) {
        val radioButton = view as RadioButton
        if (radioButton.isChecked) {
            bannerAdContainer = BannerAdContainer.fullXwrap
        }
    }

    internal enum class BannerAdContainer {
        standard, wrapXwrap, fullXwrap
    }

    private fun showAlertDialog(msg: String) {
        AlertDialog.Builder(this).setMessage(msg).show()
    }

    companion object {
        const val API_KEY = "XqjhRR" //test key
        private const val TAG = "FsMainActivity"
        private const val DO_CHOOSE_PARTNERS =
            true //purely for demonstration purposes.  set false later.
    }
}