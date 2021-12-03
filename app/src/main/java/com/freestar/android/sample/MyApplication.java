package com.freestar.android.sample;


import android.os.Build;
import android.util.Log;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.AppOpenAdListener;
import com.freestar.android.ads.FreeStarAds;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        String countryCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            countryCode = getResources().getConfiguration().locale.getCountry();
        }
        FreeStarAds.setCustomSegmentProperty(this, "CountryCode", countryCode);

        AdRequest adRequest = new AdRequest(this);
        adRequest.addCustomTargeting("some target", "my value");
        FreeStarAds.enableLogging(true);  //set false for production or don't call
        FreeStarAds.enableTestAds(true);  //set false for production or don't call

        //Turn off automated test mode just OMIT the next line
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.LIMITED_MEDIATION);
        //Also try:
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.BYPASS_ALL_ADS);
        FreeStarAds.requestAppOpenAds("app_open_ads_p2", true, appOpenAdListener);
        FreeStarAds.init(this, MainActivity.API_KEY, adRequest);

    }

    private final AppOpenAdListener appOpenAdListener = new AppOpenAdListener() {
        @Override
        public void onAppOpenAdLoading(String placement) {
            Log.i(TAG,"onAppOpenAdLoading " + placement);
        }

        @Override
        public void onAppOpenAdLoaded(String placement) {
            Log.i(TAG,"onAppOpenAdLoaded " + placement);
        }

        @Override
        public void onAppOpenAdShowing(String placement) {
            Log.i(TAG,"onAppOpenAdShowing " + placement);
        }

        @Override
        public void onAppOpenAdDismissed(String placement) {
            Log.i(TAG,"onAppOpenAdDismissed " + placement);
        }

        @Override
        public void onAppOpenAdFailed(String placement, int errorCode) {
            Log.i(TAG,"onAppOpenAdFailed " + placement);
        }
    };
}
