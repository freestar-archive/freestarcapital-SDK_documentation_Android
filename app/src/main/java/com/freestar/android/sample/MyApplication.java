package com.freestar.android.sample;

import android.app.Application;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.FreeStarAds;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AdRequest adRequest = new AdRequest(this);
        adRequest.addCustomTargeting("some target", "my value");
        FreeStarAds.enableLogging(true);  //set false for production or don't call
        FreeStarAds.enableTestAds(true);  //set false for production or don't call
        FreeStarAds.init(this, MainActivity.API_KEY, adRequest);

    }
}
