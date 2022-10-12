package com.freestar.android.sample;


import android.os.Build;

import com.confiant.sdk.Confiant;
import com.confiant.sdk.ConfiantError;
import com.confiant.sdk.Result;
import com.confiant.sdk.Settings;
import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.ChocolateLogger;
import com.freestar.android.ads.FreeStarAds;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static final String TAG = "MyApp";

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

        initializeConfiant();

        //Uncomment to utilize App Open Ads
        FreeStarAds.requestAppOpenAds("app_open_p2", true, null);

        //Turn off automated test mode just OMIT the next line
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.LIMITED_MEDIATION);
        //Also try:
        //FreeStarAds.setAutomatedTestMode(FreeStarAds.AutomatedTestMode.BYPASS_ALL_ADS);
        FreeStarAds.init(this, MainActivity.API_KEY, adRequest);
    }

    private void initializeConfiant() {

        Settings settings = null;

        final @NonNull Result<Settings, ConfiantError> settingsResult = Settings.with("Your Confiant Property ID here");
        if (settingsResult instanceof Result.Success) {
            settings = ((Result.Success<Settings, ConfiantError>) settingsResult).getValue();
            /* see below */
        } else if (settingsResult instanceof Result.Failure) {
            final @NonNull ConfiantError settingsError = ((Result.Failure<Settings, ConfiantError>) settingsResult).getError();
            ChocolateLogger.w(TAG, String.format("Failed to create Confiant Settings: %s %d%n", settingsError.getDescription(), settingsError.getCode()));
            assert false;
        } else {
            ChocolateLogger.w(TAG, String.format("Failed to create Confiant Settings%n"));
            assert false;
        }

        Confiant.initialize(
                settings,
                (final @NonNull Result<Confiant, ConfiantError> confiantInitResult) -> {
                    if (confiantInitResult instanceof Result.Success) {
                        ChocolateLogger.w(TAG, String.format("Successfully initialized Confiant SDK%n"));
                    } else if (confiantInitResult instanceof Result.Failure) {
                        final @NonNull ConfiantError initError = ((Result.Failure<Confiant, ConfiantError>) confiantInitResult).getError();
                        ChocolateLogger.w(TAG, String.format("Failed to initialize Confiant SDK: %s %d%n", initError.getDescription(), initError.getCode()));
                        assert false;
                    } else {
                        assert false;
                    }
                }
        );

    }
}
