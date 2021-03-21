package com.freestar.android.sample;


import android.os.Build;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.FreeStarAds;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        String countyCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countyCode = getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            countyCode = getResources().getConfiguration().locale.getCountry();
        }
        FreeStarAds.setCustomSegmentProperty(this, countyCode, "true");

        AdRequest adRequest = new AdRequest(this);
        adRequest.addCustomTargeting("some target", "my value");
        FreeStarAds.enableLogging(true);  //set false for production or don't call
        FreeStarAds.enableTestAds(true);  //set false for production or don't call
        FreeStarAds.init(this, MainActivity.API_KEY, adRequest);

    }
}
