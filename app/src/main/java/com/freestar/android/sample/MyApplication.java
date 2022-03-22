package com.freestar.android.sample;


import android.os.Build;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.FreeStarAds;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {
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

        //Uncomment to utilize App Open Ads
        FreeStarAds.requestAppOpenAds("app_open_p2", true, null);

        //Turn off automated test mode just OMIT the next line
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.LIMITED_MEDIATION);
        //Also try:
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.BYPASS_ALL_ADS);


        /*
         * Google Family Policy Compliance
         *
         * FreeStarAds.GoogleFamilyPolicyMode.app       -- this app is child-directed
         *
         * FreeStarAds.GoogleFamilyPolicyMode.mixed     -- this app is directed at both children
         * as well as adults, such as parents.
         *
         * FreeStarAds.GoogleFamilyPolicyMode.none      -- (default) this app is not being forced
         * by Google to comply with the Google Family Policy.
         *
         * The second boolean parameter 'onlyNonPersonalizedAds' -- if true will only serve
         * non-personalized or non-contextual ads.  This can affect revenue.
         * NOTE: This parameter only has effect under 'mixed' mode.
         */
        FreeStarAds.setGoogleFamilyPolicyMode(FreeStarAds.GoogleFamilyPolicyMode.app, true);
        //FreeStarAds.setGoogleFamilyPolicyMode(FreeStarAds.GoogleFamilyPolicyMode.mixed, false);
        //FreeStarAds.setGoogleFamilyPolicyMode(FreeStarAds.GoogleFamilyPolicyMode.none, false);
        FreeStarAds.init(this, MainActivity.API_KEY, adRequest);

    }
}
