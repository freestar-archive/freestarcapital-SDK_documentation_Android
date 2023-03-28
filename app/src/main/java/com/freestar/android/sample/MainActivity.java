package com.freestar.android.sample;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.AdSize;
import com.freestar.android.ads.BannerAd;
import com.freestar.android.ads.BannerAdListener;
import com.freestar.android.ads.OnBannerAdSizeChangedListener;
import com.freestar.android.ads.ChocolateLogger;
import com.freestar.android.ads.ErrorCodes;
import com.freestar.android.ads.FreeStarAds;
import com.freestar.android.ads.InterstitialAd;
import com.freestar.android.ads.InterstitialAdListener;
import com.freestar.android.ads.NativeAd;
import com.freestar.android.ads.NativeAdListener;
import com.freestar.android.ads.OnPaidEventListener;
import com.freestar.android.ads.PaidEvent;
import com.freestar.android.ads.PrerollAd;
import com.freestar.android.ads.PrerollAdListener;
import com.freestar.android.ads.RewardedAd;
import com.freestar.android.ads.RewardedAdListener;
import com.freestar.android.ads.ThumbnailAd;
import com.freestar.android.ads.ThumbnailAdGravity;
import com.freestar.android.ads.ThumbnailAdListener;
import com.freestar.android.sample.recyclerview.RecyclerViewFixedAdsActivity;
import com.freestar.android.sample.recyclerview.RecyclerViewInfiniteAdsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements RewardedAdListener,
        InterstitialAdListener, PrerollAdListener, ThumbnailAdListener,
        OnPaidEventListener, OnBannerAdSizeChangedListener {

    public static final String API_KEY = "XqjhRR"; //"37f63777-6e63-42f2-89b7-4b67689c2493";// "ef8d3a3f-3516-4386-85a5-01e2e86f3499"; //"XqjhRR"; //"3QpLSQ" mopub/fb

    private static final String TAG = "FsMainActivity";
    private static final boolean DO_CHOOSE_PARTNERS = true; //purely for demonstration purposes.  set false later.

    private AdRequest adRequest;
    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    private BannerAd bannerAd;
    private NativeAd nativeAd;
    private PrerollAd preRollVideoAd;
    private ThumbnailAd thumbnailAd;
    private boolean doPrerollAdFragment;

    private VideoHelper videoHelper;
    private boolean isLargeLayout;
    private int pageNum = 0;

    private BannerAdContainer bannerAdContainer = BannerAdContainer.wrapXwrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLargeLayout = false;// getResources().getBoolean(R.bool.large_layout);
        adRequest = new AdRequest(this);
        adRequest.addCustomTargeting("my custom target", "value");
        pageNum = savedInstanceState != null ? savedInstanceState.getInt("page", 0) : 0;
        setTitle("Freestar Page " + (pageNum + 1));
        FreeStarAds.setOnPaidEventListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ((RadioButton) findViewById(R.id.radioBannerWrapWrap)).setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", pageNum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (interstitialAd != null) {
            interstitialAd.onResume();
        }
        if (rewardedAd != null) {
            rewardedAd.onResume();
        }
        if (bannerAd != null) {
            bannerAd.onResume();
        }
        if (preRollVideoAd != null) {
            preRollVideoAd.onResume();
        }
        if (videoHelper != null) {
            videoHelper.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (interstitialAd != null) {
            interstitialAd.onPause();
        }
        if (rewardedAd != null) {
            rewardedAd.onPause();
        }
        if (bannerAd != null) {
            bannerAd.onPause();
        }
        if (preRollVideoAd != null) {
            preRollVideoAd.onPause();
        }
        if (videoHelper != null) {
            videoHelper.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (interstitialAd != null) {
            interstitialAd.destroyView();
        }
        if (rewardedAd != null) {
            rewardedAd.destroyView();
        }
        if (bannerAd != null) {
            bannerAd.destroyView();
        }
        if (preRollVideoAd != null) {
            preRollVideoAd.destroyView();
        }
        if (videoHelper != null)
            videoHelper.cleanUp();
    }

    /**
     * Note: In production release, simply call:
     * <p>
     * interstitialAd.loadAd( adRequest )
     * <p>
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view - onclick view
     */
    public void loadInterstitialAd(View view) {

        interstitialAd = new InterstitialAd(this, this);

        if (DO_CHOOSE_PARTNERS) {
            MediationPartners.choosePartners(this, adRequest, MediationPartners.ADTYPE_INTERSTITIAL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (pageNum == 0) {
                        interstitialAd.loadAd(adRequest);
                    } else {
                        interstitialAd.loadAd(adRequest, "interstitial_p" + pageNum);
                    }
                }
            });
        } else {
            interstitialAd.loadAd(adRequest);
        }
    }

    private void loadNativeAd(int template) {
        if (nativeAd != null) {
            nativeAd.destroyView();
        }
        nativeAd = new NativeAd(this);
        nativeAd.setTemplate(template);
        nativeAd.setNativeAdListener(new NativeAdListener() {
            @Override
            public void onNativeAdLoaded(View nativeAdView, String placement) {
                ((ViewGroup) findViewById(R.id.native_container)).removeAllViews();
                ((ViewGroup) findViewById(R.id.native_container)).addView(nativeAdView);
            }

            @Override
            public void onNativeAdFailed(String placement, int errorCode) {
                Toast.makeText(MainActivity.this, "native ad failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNativeAdClicked(String placement) {

            }
        });

        if (DO_CHOOSE_PARTNERS) {
            MediationPartners.choosePartners(this, adRequest, MediationPartners.ADTYPE_BANNER, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nativeAd.loadAd(adRequest);
                }
            });
        } else {
            nativeAd.loadAd(adRequest);
        }
    }

    public void loadNativeMedium(View view) {
        loadNativeAd(NativeAd.TEMPLATE_MEDIUM);
    }

    public void loadNativeSmall(View view) {
        loadNativeAd(NativeAd.TEMPLATE_SMALL);
    }

    /**
     * Note: In production release, simply call:
     * <p>
     * rewardedAd.loadAd( adRequest )
     * <p>
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view - onclick view
     */
    public void loadRewardedAd(View view) {

        rewardedAd = new RewardedAd(this, this);

        if (DO_CHOOSE_PARTNERS) {
            MediationPartners.choosePartners(this, adRequest, MediationPartners.ADTYPE_REWARDED, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (pageNum == 0) {
                        rewardedAd.loadAd(adRequest);
                    } else {
                        rewardedAd.loadAd(adRequest, "rewarded_p" + pageNum);
                    }
                }
            });
        } else {
            rewardedAd.loadAd(adRequest);
        }
    }

    private void clearStandardBannerAds() {
        int standardWidth = getResources().getDimensionPixelSize(R.dimen.fs_default_banner_width);
        int standardHeight = getResources().getDimensionPixelSize(R.dimen.fs_default_banner_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(standardWidth, standardHeight);
        ((FrameLayout) findViewById(R.id.banner_320_50)).removeAllViews();
        ((FrameLayout) findViewById(R.id.banner_320_50)).setLayoutParams(params);
        ((FrameLayout) findViewById(R.id.banner_320_50)).forceLayout();

        params = new LinearLayout.LayoutParams(-1, -2);
        ((FrameLayout) findViewById(R.id.banner_fullwidth_wrap)).removeAllViews();
        ((FrameLayout) findViewById(R.id.banner_fullwidth_wrap)).setLayoutParams(params);
        ((FrameLayout) findViewById(R.id.banner_fullwidth_wrap)).forceLayout();

        params = new LinearLayout.LayoutParams(-2, -2);
        ((FrameLayout) findViewById(R.id.banner_wrap_wrap)).removeAllViews();
        ((FrameLayout) findViewById(R.id.banner_wrap_wrap)).setLayoutParams(params);
        ((FrameLayout) findViewById(R.id.banner_wrap_wrap)).forceLayout();
    }

    @Override
    public void onBannerAdSizeChanged(AdSize adSize, boolean isNativeAd) {
        ChocolateLogger.i(TAG,"onBannerAdSizeChanged: " + adSize + " isNative: " + isNativeAd);
        if (adSize.equals(AdSize.NATIVE)) {
            findViewById(R.id.mrec_container)
                    .setLayoutParams(new LinearLayout.LayoutParams(dpToPx(350),dpToPx(280)));
        } else if (adSize.equals(AdSize.BANNER_320_50)) {
            findViewById(R.id.mrec_container)
                    .setLayoutParams(new LinearLayout.LayoutParams(dpToPx(320),dpToPx(50)));
        } else if (adSize.equals(AdSize.MEDIUM_RECTANGLE_300_250)) { //mrec
            findViewById(R.id.mrec_container)
                    .setLayoutParams(new LinearLayout.LayoutParams(dpToPx(300),dpToPx(250)));
        }
    }

    private void loadBannerAd(final AdSize adSize, final int resBannerContainer) {
        if (bannerAd != null) {
            bannerAd.destroyView();
        }
        ((ViewGroup) findViewById(resBannerContainer)).removeAllViews();
        bannerAd = new BannerAd(this);
        bannerAd.setOnBannerAdSizeChangedListener(this);
        bannerAd.setAdSize(adSize);
        bannerAd.setBannerAdListener(new BannerAdListener() {
            @Override
            public void onBannerAdLoaded(View view, String placement) {
                clearStandardBannerAds();
                ((ViewGroup) findViewById(resBannerContainer)).addView(view);
                ((TextView) findViewById(R.id.textView)).setText("Banner " + adSize + " winner: " + bannerAd.getWinningPartnerName() + " isAdaptive: " + bannerAd.isAdaptiveBannerAd());
                ChocolateLogger.i(TAG, "onBannerAdLoaded() " + view.getWidth() + '/' + view.getHeight());
            }

            @Override
            public void onBannerAdFailed(View view, String placement, int errorCode) {
                ((TextView) findViewById(R.id.textView)).setText("Banner " + adSize + ": " + ErrorCodes.getErrorDescription(errorCode));
                ChocolateLogger.i(TAG, "onBannerAdFailed(). " + ErrorCodes.getErrorDescription(errorCode));
            }

            @Override
            public void onBannerAdClicked(View view, String placement) {

            }

            @Override
            public void onBannerAdClosed(View view, String placement) {

            }
        });

        if (DO_CHOOSE_PARTNERS) {
            MediationPartners.choosePartners(this, adRequest, MediationPartners.ADTYPE_BANNER, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (pageNum == 0) {
                        bannerAd.loadAd(adRequest);
                    } else {
                        bannerAd.loadAd(adRequest, "banner_p" + pageNum);
                    }
                }
            });
        } else {
            bannerAd.loadAd(adRequest);
        }
    }

    /**
     * Note: In production release, simply call:
     * <p>
     * bannerAd.loadAd( adRequest )
     * <p>
     * The selective mediation 'partner chooser' is only for developer entertainment purposes.
     *
     * @param view - onclick view
     */
    public void loadBannerAd(View view) {
        int containerRes = R.id.banner_wrap_wrap;
        switch (bannerAdContainer) {
            case fullXwrap:
                containerRes = R.id.banner_fullwidth_wrap;
                break;
            case wrapXwrap:
                containerRes = R.id.banner_wrap_wrap;
                break;
            case standard:
                containerRes = R.id.banner_320_50;
                break;
            default:
                break;
        }
        loadBannerAd(AdSize.BANNER_320_50, containerRes);
    }

    public void loadBannerAdMREC(View view) {
        loadBannerAd(AdSize.MEDIUM_RECTANGLE_300_250, R.id.mrec_container);
    }

    public void loadBannerAdLEADERBOARD(View view) {
        loadBannerAd(AdSize.LEADERBOARD_728_90, R.id.leaderboard_container);
    }

    private void loadPrerollAd() {
        cleanupPreroll();

        preRollVideoAd = new PrerollAd(this);

        if (DO_CHOOSE_PARTNERS) {
            MediationPartners.choosePartners(this, adRequest, MediationPartners.ADTYPE_PREROLL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (pageNum == 0) {
                        preRollVideoAd.loadAd(adRequest, AdSize.PREROLL_320_480, MainActivity.this);
                    } else {
                        preRollVideoAd.loadAd(adRequest, AdSize.PREROLL_320_480, "preroll_p" + pageNum, MainActivity.this);
                    }
                }
            });
        } else {
            preRollVideoAd.loadAd(adRequest, AdSize.PREROLL_320_480, MainActivity.this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            switch (resultCode) {
                case SamplePrerollAdActivity.RESULT_AD_COMPLETED:
                    playUserContent();
                    break;
                case SamplePrerollAdActivity.RESULT_AD_ERROR:
                    Toast.makeText(this, "ad error: could not play ad", Toast.LENGTH_SHORT).show();
                    break;
                case SamplePrerollAdActivity.RESULT_NO_FILL:
                    Toast.makeText(this, "full screen preroll NO-FILL", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    public void loadPrerollAdAsView(View view) {

        doPrerollAdFragment = false;
        loadPrerollAd();
    }

    public void loadPrerollAdAsFragment(View view) {

        doPrerollAdFragment = true;
        loadPrerollAd();
    }

    public void loadPrerollAdAsActivity(View view) {
        cleanupPreroll();
        startActivityForResult(new Intent(this, SamplePrerollAdActivity.class), 10);
    }

    private void cleanupPreroll() {
        if (preRollVideoAd != null)
            preRollVideoAd.destroyView();
        getSupportFragmentManager().popBackStackImmediate();
    }


    @Override
    public void onInterstitialLoaded(String placement) {
        interstitialAd.show();
        ((TextView) findViewById(R.id.textView)).setText("Interstitial Ad winner: " + interstitialAd.getWinningPartnerName());
    }

    @Override
    public void onInterstitialFailed(String placement, int errorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Interstitial No-Fill");
    }

    @Override
    public void onInterstitialShown(String placement) {

    }

    @Override
    public void onInterstitialClicked(String placement) {

    }

    @Override
    public void onInterstitialDismissed(String placement) {

    }

    /**
     * view - The preroll video ad view
     */
    @Override
    public void onPrerollAdLoaded(View view, String placement) {

        if (bannerAd != null)
            bannerAd.destroyView();

        ((TextView) findViewById(R.id.textView)).setText("PreRoll Ad winner: " + preRollVideoAd.getWinningPartnerName());
        ((ViewGroup) findViewById(R.id.mrec_container)).removeAllViews();

        ChocolateLogger.i(TAG, "onPrerollAdLoaded");

        if (doPrerollAdFragment) {
            showPrerollAdFragment();
        } else {
            ((ViewGroup) findViewById(R.id.mrec_container)).addView(view);
            preRollVideoAd.showAd();
        }
    }

    @Override
    public void onPrerollAdFailed(View view, String placement, int errorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Preroll: " + ErrorCodes.getErrorDescription(errorCode));
    }

    @Override
    public void onPrerollAdShown(View view, String placement) {
        ChocolateLogger.i(TAG, "onPrerollAdShown");
    }

    @Override
    public void onPrerollAdShownError(View view, String placement) {

    }

    @Override
    public void onPrerollAdClicked(View view, String placement) {

    }

    @Override
    public void onPrerollAdCompleted(View view, String placement) {
        ChocolateLogger.i(TAG, "onPrerollAdCompleted");
        playUserContent();
    }

    private void playUserContent() {
        //Let's pretend you want to roll a movie/video when the preroll ad is completed.
        cleanupPreroll();
        videoHelper = new VideoHelper(this, (FrameLayout) findViewById(R.id.mrec_container));
        videoHelper.playContentVideo(0);
        ChocolateLogger.i(TAG, "playUserContent");
    }

    @Override
    public void onRewardedVideoLoaded(String placement) {
        rewardedAd.showRewardAd("my secret code", "my userid", "V-BUCKS", "5000");
        ((TextView) findViewById(R.id.textView)).setText("Rewarded Ad winner: " + rewardedAd.getWinningPartnerName());
    }

    @Override
    public void onRewardedVideoFailed(String placement, int errorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Rewarded No-Fill");
    }

    @Override
    public void onRewardedVideoShown(String placement) {

    }

    @Override
    public void onRewardedVideoShownError(String placement, int errorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Rewarded Got Fill, but Error Showing");
    }

    @Override
    public void onRewardedVideoDismissed(String placement) {

    }

    @Override
    public void onRewardedVideoCompleted(String placement) {

    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof CustomDialogFragment) {
            CustomDialogFragment frag = (CustomDialogFragment) fragment;
            frag.setAdView(preRollVideoAd);
            ChocolateLogger.i(TAG, "onAttachFragment");
        }
    }

    public static class CustomDialogFragment extends DialogFragment {

        static CustomDialogFragment newInstance() {
            return new CustomDialogFragment();
        }

        private PrerollAd preRollVideoAd;

        /**
         * The system calls this to get the DialogFragment's layout, regardless
         * of whether it's being displayed as a dialog or an embedded fragment.
         */
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout to use as dialog or embedded fragment
            FrameLayout frameLayout = new FrameLayout(container.getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            frameLayout.setBackgroundColor(Color.TRANSPARENT);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int height = (displayMetrics.widthPixels * 9) / 16;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(displayMetrics.widthPixels, height);
            params.gravity = Gravity.CENTER;
            preRollVideoAd.getAdView().setLayoutParams(params);
            frameLayout.addView(preRollVideoAd.getAdView());
            preRollVideoAd.showAd();
            frameLayout.setBackgroundColor(Color.BLACK);
            ChocolateLogger.i(TAG, "CustomDialogFragment onCreateView");
            return frameLayout;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            ChocolateLogger.i(TAG, "CustomDialogFragment onActivityCreated");
            super.onActivityCreated(savedInstanceState);
            if (getActivity() != null)
                ((MainActivity) getActivity()).hideSystemUI();

        }

        @Override
        public void onDetach() {
            ChocolateLogger.i(TAG, "CustomDialogFragment onDetach");
            super.onDetach();
            if (getActivity() != null)
                ((MainActivity) getActivity()).showSystemUI();
        }

        void setAdView(PrerollAd preRollVideoAd) {
            ChocolateLogger.i(TAG, "setAdView");
            this.preRollVideoAd = preRollVideoAd;
        }

        /**
         * The system calls this only when creating the layout in a dialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ChocolateLogger.i(TAG, "  onCreateDialog");
            // to modify any dialog characteristics. For example, the dialog includes a
            // title by default, but your custom layout might not need it. So here you can
            // remove the dialog title, but you must call the superclass to get the Dialog.
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return dialog;
        }

        @Override
        public void onConfigurationChanged(@NonNull Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
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

    private void showPrerollAdFragment() {

        ChocolateLogger.i(TAG, "showPrerollAdFragment UI thread? " + (Looper.myLooper() == Looper.getMainLooper()));

        FragmentManager fragmentManager = getSupportFragmentManager();
        CustomDialogFragment newFragment = CustomDialogFragment.newInstance();

        ChocolateLogger.i(TAG, "showPrerollAdFragment 2");

        if (isLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity

            ChocolateLogger.i(TAG, "showPrerollAdFragment 3");

            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
            getSupportFragmentManager().executePendingTransactions();

            ChocolateLogger.i(TAG, "showPrerollAdFragment 4");
        }
    }

    void hideSystemUI() {

        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();

        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE |
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);

        showActionBar(false);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    void showSystemUI() {
        showActionBar(true);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void showActionBar(boolean show) {
        if (getSupportActionBar() == null) return;
        if (show)
            getSupportActionBar().show();
        else
            getSupportActionBar().hide();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments().size() > 0) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_pages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.page1:
                pageNum = 0;
                setTitle("Freestar Page 1");
                return true;
            case R.id.page2:
                setTitle("Freestar Page 2");
                pageNum = 1;
                return true;
            case R.id.page3:
                setTitle("Freestar Page 3");
                pageNum = 2;
                return true;
            case R.id.menu_recyclerview:
                startActivity(new Intent(this, RecyclerViewInfiniteAdsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onShowAdaptiveCheck(View view) {
        CheckBox checkBox = (CheckBox) view;
        FreeStarAds.showAdaptiveBannerAdsWhenAvailable(checkBox.isChecked());
        ChocolateLogger.i(TAG, "onShowAdaptiveCheck: " + checkBox.isChecked());
    }

    public void onBanner320x50Checked(View view) {
        RadioButton radioButton = (RadioButton) view;
        if (radioButton.isChecked()) {
            bannerAdContainer = BannerAdContainer.standard;
        }
    }

    public void onBannerWrapWrapChecked(View view) {
        RadioButton radioButton = (RadioButton) view;
        if (radioButton.isChecked()) {
            bannerAdContainer = BannerAdContainer.wrapXwrap;
        }
    }

    public void onBannerFullWrapChecked(View view) {
        RadioButton radioButton = (RadioButton) view;
        if (radioButton.isChecked()) {
            bannerAdContainer = BannerAdContainer.fullXwrap;
        }
    }

    enum BannerAdContainer {
        standard, wrapXwrap, fullXwrap
    }

    public void loadThumbnailAd(View view) {
        thumbnailAd = new ThumbnailAd(this);
        thumbnailAd.setListener(this);
        thumbnailAd.loadAd(adRequest);
    }

    @Override
    public void onThumbnailAdLoaded(String placement) {
        ((TextView) findViewById(R.id.textView)).setText("ThumbnailAd winner: " + thumbnailAd.getWinningPartnerName());
        thumbnailAd.setGravity(ThumbnailAdGravity.topLeft);
        //thumbnailAd.setMargins(40,40);
        thumbnailAd.setBlackListActivities(RecyclerViewFixedAdsActivity.class, RecyclerViewInfiniteAdsActivity.class);
        thumbnailAd.show();
    }

    @Override
    public void onThumbnailAdFailed(String placement, int errorCode) {
        ((TextView) findViewById(R.id.textView)).setText("ThumbnailAd failed: " + ErrorCodes.getErrorDescription(errorCode));
    }

    @Override
    public void onThumbnailAdShown(String placement) {

    }

    @Override
    public void onThumbnailAdClicked(String placement) {
        ((TextView) findViewById(R.id.textView)).setText("ThumbnailAd clicked");
    }

    @Override
    public void onThumbnailAdDismissed(String placement) {
        ((TextView) findViewById(R.id.textView)).setText("ThumbnailAd dismissed");
    }

    @Override
    public void onPaidEvent(PaidEvent paidEvent) {
        ChocolateLogger.i(TAG,"onPaidEvent: " + paidEvent);
    }

    private int dpToPx(int dp) {
        return (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
