package com.freestar.android.sample;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.AdSize;
import com.freestar.android.ads.BannerAd;
import com.freestar.android.ads.BannerAdListener;
import com.freestar.android.ads.FreeStarAds;
import com.freestar.android.ads.InterstitialAd;
import com.freestar.android.ads.InterstitialAdListener;
import com.freestar.android.ads.PrerollAd;
import com.freestar.android.ads.PrerollAdListener;
import com.freestar.android.ads.RewardedAd;
import com.freestar.android.ads.RewardedAdListener;
import com.freestar.android.sample.recyclerview.RecyclerViewActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements RewardedAdListener, InterstitialAdListener, PrerollAdListener {

    public static final String API_KEY = "XqjhRR"; //test key
    private static final String TAG = "MainActivity";
    private static final boolean DO_CHOOSE_PARTNERS = true; //purely for demonstration purposes.  set false later.

    private AdRequest adRequest;
    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    private BannerAd bannerAd;
    private BannerAd mrecBannerAd;
    private PrerollAd preRollVideoAd;
    private boolean doPrerollAdFragment;

    private VideoHelper videoHelper;
    private boolean isLargeLayout;
    private int pageNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLargeLayout = false;// getResources().getBoolean(R.bool.large_layout);
        adRequest = new AdRequest(this);
        FreeStarAds.enableLogging(true);  //don't set true for production
        FreeStarAds.enableTestAds(true);  //don't set true for production
        pageNum = savedInstanceState != null ? savedInstanceState.getInt("page",0) : 0;
        setTitle("FreeStar Page "+(pageNum+1));
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
        if (mrecBannerAd != null) {
            mrecBannerAd.onResume();
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
        if (mrecBannerAd != null) {
            mrecBannerAd.destroyView();
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
                        interstitialAd.loadAd(adRequest, "interstitial_p"+pageNum);
                    }
                }
            });
        } else {
            interstitialAd.loadAd(adRequest);
        }
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
                        rewardedAd.loadAd(adRequest, "rewarded_p"+pageNum);
                    }
                }
            });
        } else {
            rewardedAd.loadAd(adRequest);
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

        if (bannerAd != null)
            bannerAd.destroyView();

        bannerAd = new BannerAd(this);
        bannerAd.setAdSize(AdSize.BANNER_320_50);
        bannerAd.setBannerAdListener(new BannerAdListener() {
            @Override
            public void onBannerAdLoaded(View view, String placement) {
                ((ViewGroup) findViewById(R.id.banner_container)).removeAllViews();
                ((ViewGroup) findViewById(R.id.banner_container)).addView(view);
                ((TextView) findViewById(R.id.textView)).setText("Banner winner: " + bannerAd.getWinningPartnerName());
            }

            @Override
            public void onBannerAdFailed(View view, String placement, int errorCode) {
                ((TextView) findViewById(R.id.textView)).setText("Banner No-Fill");
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
                    bannerAd.loadAd(adRequest);
                    if (pageNum == 0) {
                        bannerAd.loadAd(adRequest);
                    } else {
                        bannerAd.loadAd(adRequest, "banner_p"+pageNum);
                    }
                }
            });
        } else {
            bannerAd.loadAd(adRequest);

        }
    }

    public void loadBannerAdMREC(View view) {

        if (mrecBannerAd != null)
            mrecBannerAd.destroyView();

        mrecBannerAd = new BannerAd(this);
        mrecBannerAd.setAdSize(AdSize.MEDIUM_RECTANGLE_300_250);
        mrecBannerAd.setBannerAdListener(new BannerAdListener() {
            @Override
            public void onBannerAdLoaded(View view, String placement) {

                if (preRollVideoAd != null)
                    preRollVideoAd.destroyView();

                ((ViewGroup) findViewById(R.id.mrec_container)).removeAllViews();
                ((ViewGroup) findViewById(R.id.mrec_container)).addView(view);
                ((TextView) findViewById(R.id.textView)).setText("MREC Banner winner: " + mrecBannerAd.getWinningPartnerName());
            }

            @Override
            public void onBannerAdFailed(View view, String placement, int errorCode) {
                ((TextView) findViewById(R.id.textView)).setText("MREC Banner No-Fill");
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
                        mrecBannerAd.loadAd(adRequest);
                    } else {
                        mrecBannerAd.loadAd(adRequest, "banner_p"+pageNum);
                    }
                }
            });
        } else {
            mrecBannerAd.loadAd(adRequest);
        }

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
                        preRollVideoAd.loadAd(adRequest, AdSize.PREROLL_320_480, "preroll_p"+pageNum, MainActivity.this);
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
        getSupportFragmentManager().popBackStack();
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

        if (doPrerollAdFragment) {
            showPrerollAdFragment();
        } else {
            ((ViewGroup) findViewById(R.id.mrec_container)).addView(view);
            preRollVideoAd.showAd();
        }
    }

    @Override
    public void onPrerollAdFailed(View view, String placement, int errorCode) {
        ((TextView) findViewById(R.id.textView)).setText("Preroll No-Fill");
    }

    @Override
    public void onPrerollAdShown(View view, String placement) {

    }

    @Override
    public void onPrerollAdShownError(View view, String placement) {

    }

    @Override
    public void onPrerollAdClicked(View view, String placement) {

    }

    @Override
    public void onPrerollAdCompleted(View view, String placement) {
        playUserContent();
    }

    private void playUserContent() {
        //Let's pretend you want to roll a movie/video when the preroll ad is completed.
        getSupportFragmentManager().popBackStack();
        videoHelper = new VideoHelper(this, (FrameLayout) findViewById(R.id.mrec_container));
        videoHelper.playContentVideo(0);
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
        }
    }

    public static class CustomDialogFragment extends DialogFragment {

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
            return frameLayout;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (getActivity() != null)
                ((MainActivity) getActivity()).hideSystemUI();

        }

        @Override
        public void onDetach() {
            super.onDetach();
            if (getActivity() != null)
                ((MainActivity) getActivity()).showSystemUI();
        }

        void setAdView(PrerollAd preRollVideoAd) {
            this.preRollVideoAd = preRollVideoAd;
        }

        /**
         * The system calls this only when creating the layout in a dialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        CustomDialogFragment newFragment = new CustomDialogFragment();

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
            transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
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
                setTitle("FreeStar Page 1");
                return true;
            case R.id.page2:
                setTitle("FreeStar Page 2");
                pageNum = 1;
                return true;
            case R.id.page3:
                setTitle("FreeStar Page 3");
                pageNum = 2;
                return true;
            case R.id.menu_recyclerview:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
