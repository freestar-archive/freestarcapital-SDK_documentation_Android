package com.freestar.android.sample;


import android.os.Build;

import com.confiant.sdk.Confiant;
import com.confiant.sdk.PropertyId;
import com.confiant.sdk.Result;
import com.confiant.sdk.Settings;
import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.ChocolateLogger;
import com.freestar.android.ads.FreeStarAds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    public static final String TAG = "MyApplication";

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
        FreeStarAds.init(this, MainActivity.API_KEY, adRequest);

        setupConfiant();
    }

    private void setupConfiant() {
        //https://freestar.atlassian.net/browse/ENG-8593
        final @NonNull Result<PropertyId> propertyIdResult = PropertyId.from("x0Jm1cYDEbEK1qln_DqWfmhw8zk");
        final @Nullable Settings settings;
        switch(propertyIdResult.kind)
        {
            case Success:
                assert propertyIdResult.value != null;
                final @NonNull PropertyId propertyId = propertyIdResult.value;

                /**
                 * Passing true in the last param will force all ads to be blocked.
                 * For testing purposes only.
                 */
                settings = //new Settings(propertyId);
                new Settings
                        (
                                null, // enableRate, null for default
                                propertyId, // propertyId
                                null, // enableAdReporter, null for default
                                true // forceBlockOnce, null for default
                        );
                break;
            case Failure:
                // TODO: handle error
                assert propertyIdResult.error != null;
                final @NonNull String message = "Failed to create Confiant Property Id: " + propertyIdResult.error.description();
                ChocolateLogger.e(TAG, message);
                settings = null;
                assert false;
                break;
            default:
                settings = null;
                assert false;
                break;
        }

        Confiant.initialize
                (
                        settings,
                        (final @NonNull Result<Confiant> result) ->
                        {
                            switch(result.kind)
                            {
                                case Success:
                                    assert result.value != null;
                                    ChocolateLogger.i(TAG, "Successfully initialized Confiant SDK");
                                    break;
                                case Failure:
                                    // TODO: handle error
                                    assert result.error != null;
                                    final @NonNull String message = "Failed to initialize Confiant SDK: " + result.error.description();
                                    ChocolateLogger.e(TAG, message);
                                    assert false;
                                    break;
                                default:
                                    assert false;
                                    break;
                            }
                        }
                );

    }
}
