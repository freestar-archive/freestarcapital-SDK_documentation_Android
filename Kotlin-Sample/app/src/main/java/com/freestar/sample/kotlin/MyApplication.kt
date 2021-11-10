package com.freestar.sample.kotlin

import androidx.multidex.MultiDexApplication
import android.os.Build
import com.freestar.android.ads.AdRequest
import com.freestar.android.ads.FreeStarAds
import com.freestar.sample.kotlin.MainActivity

class MyApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        val countryCode: String
        countryCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].country
        } else {
            resources.configuration.locale.country
        }
        FreeStarAds.setCustomSegmentProperty(this, "CountryCode", countryCode)
        val adRequest = AdRequest(this)
        adRequest.addCustomTargeting("some target", "my value")
        FreeStarAds.enableLogging(true) //set false for production or don't call
        FreeStarAds.enableTestAds(true) //set false for production or don't call

        //Turn off automated test mode just OMIT the next line
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.LIMITED_MEDIATION);
        //Also try:
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.BYPASS_ALL_ADS);
        FreeStarAds.init(this, MainActivity.API_KEY, adRequest)
    }
}