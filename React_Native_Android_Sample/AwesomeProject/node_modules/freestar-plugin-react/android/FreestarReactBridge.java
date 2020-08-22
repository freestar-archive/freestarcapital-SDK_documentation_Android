package com.awesomeproject;  /***Rename package according to your own Android project***/

import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.ChocolateLogger;
import com.freestar.android.ads.ErrorCodes;
import com.freestar.android.ads.FreeStarAds;
import com.freestar.android.ads.InterstitialAd;
import com.freestar.android.ads.InterstitialAdListener;
import com.freestar.android.ads.LVDOAdUtil;
import com.freestar.android.ads.RewardedAd;
import com.freestar.android.ads.RewardedAdListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FreestarReactBridge extends ReactContextBaseJavaModule implements InterstitialAdListener, RewardedAdListener {

    private static final String TAG = "FreestarReactBridge";

    public static final String EVENT_INTERSTITIAL_LOADED    = "onInterstitialLoaded";
    public static final String EVENT_INTERSTITIAL_FAILED    = "onInterstitialFailed";
    public static final String EVENT_INTERSTITIAL_CLICKED   = "onInterstitialClicked";
    public static final String EVENT_INTERSTITIAL_SHOWN     = "onInterstitialShown";
    public static final String EVENT_INTERSTITIAL_DISMISSED = "onInterstitialDismissed";

    public static final String EVENT_REWARDED_LOADED    = "onRewardedLoaded";
    public static final String EVENT_REWARDED_FAILED    = "onRewardedFailed";
    public static final String EVENT_REWARDED_SHOW_FAILED    = "onRewardedShowFailed";
    public static final String EVENT_REWARDED_SHOWN     = "onRewardedShown";
    public static final String EVENT_REWARDED_DISMISSED = "onRewardedDismissed";
    public static final String EVENT_REWARDED_COMPLETED = "onRewardedCompleted";

    public static final String NOT_DEFINED = "not defined";

    private RewardedAd rewardedAd;
    private InterstitialAd interstitialAd;
    private AdRequest adRequest;

    public FreestarReactBridge(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public void initWithAdUnitID(final String appKey){
        /*
        This will be documented to go into MainApplication; not here anymore.

        FreeStarAds.enableTestAds(BuildConfig.DEBUG);
        FreeStarAds.enableLogging(BuildConfig.DEBUG);
        if (!FreeStarAds.isInitialized()) {
            FreeStarAds.init(getCurrentActivity(), appKey, getAdRequest());
        }
        */
    }

    private AdRequest getAdRequest() {
        if (adRequest == null) {
            adRequest = new AdRequest(getCurrentActivity());
        }
        return adRequest;
    }

    @ReactMethod
    public void setDemographics(int age, String birthdayISO, String gender, String maritalStatus, String ethnicity) {
        AdRequest adRequest = getAdRequest();
        try {
            adRequest.setAge(""+age);
            adRequest.setBirthday(convert(birthdayISO));
            adRequest.setGender(gender);
            adRequest.setMaritalStatus(maritalStatus);
            adRequest.setEthnicity(convertEthnicity(ethnicity));
        }catch (Throwable t) {
            ChocolateLogger.e(TAG, "", t);
        }
    }

    @ReactMethod
    public void setLocation(String dmaCode, String postalCode, String currPostalCode, String latitude, String longitude) {
        AdRequest adRequest = getAdRequest();
        try {
            adRequest.setDmaCode(dmaCode);
            adRequest.setPostalCode(postalCode);
            adRequest.setCurrPostal(currPostalCode);
        }catch (Throwable t) {
            ChocolateLogger.e(TAG, "", t);
        }
    }

    @ReactMethod
    public void setAppInfo(String appName, String publisherName, String appDomain, String publisherDomain, String storeUrl, String category) {
        AdRequest adRequest = getAdRequest();
        adRequest.setAppName(appName);
        adRequest.setPublisherDomain(publisherDomain);
        adRequest.setAppDomain(appDomain);
        adRequest.setAppStoreUrl(storeUrl);
        adRequest.setCategory(category);
    }

    @ReactMethod
    public void subjectToGDPR(boolean isSubjectToGDPR, String gdprConsentString) {
        try {
            FreeStarAds.setGDPR(getCurrentActivity(), isSubjectToGDPR, gdprConsentString);
        }catch(Throwable t) {
            ChocolateLogger.e(TAG, "", t);
        }
    }

    @ReactMethod
    public void setCoppaStatus(boolean isCoppa) {
        AdRequest adRequest = getAdRequest();
        adRequest.setCOPPAEnabled(false);
    }

    private String convertEthnicity(String ethnicity) {
        if (ethnicity == null || ethnicity == "") {
            return null;
        }
        return ethnicity;
    }

    private Date convert(String dateISO) {
        try {
            //yyyy-MM-dd
            if (dateISO != null && dateISO.length() >= "yyyy-MM-dd".length()) {
                dateISO = dateISO.substring(0,"yyyy-MM-dd".length());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(dateISO);
            }

        }catch(Throwable t) {
            ChocolateLogger.e(TAG, "", t);
        }
        return null;
    }

    @ReactMethod
    public void loadInterstitialAd(String placement) {
        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(getCurrentActivity(), this);
        }
        interstitialAd.loadAd(getAdRequest(), placement);
    }

    @ReactMethod
    public void showInterstitialAd(){

        LVDOAdUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null){
                    try {

                        interstitialAd.show();
                    }catch (Exception e) {
                        onInterstitialFailed(null, ErrorCodes.NO_FILL);
                    }
                }
            }
        });
    }

    @ReactMethod
    public void loadRewardAd(String placement) {
        if (rewardedAd == null) {
            rewardedAd = new RewardedAd(getCurrentActivity(), this);
        }
        rewardedAd.loadAd(getAdRequest(), placement);
    }

    private String rewardName;
    private int rewardAmount;

    @ReactMethod
    public void showRewardAd(final String rewardName, final int rewardAmount, final String userId, final String secret) {

        LVDOAdUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardedAd != null){
                    try {
                        FreestarReactBridge.this.rewardName = rewardName;
                        FreestarReactBridge.this.rewardAmount = rewardAmount;
                        rewardedAd.showRewardAd(secret, userId, rewardName, rewardAmount+"");
                    }catch (Exception e) {
                        onRewardedVideoShownError(null, ErrorCodes.VIDEO_PLAYBACK_ERROR);
                    }
                }
            }
        });

    }

    @Override
    public String getName() {
        return "FreestarReactBridge";
    }

    @Override
    public void onInterstitialLoaded(String placement) {
        WritableMap event = makeEvent(placement, null);
        sendEvent(EVENT_INTERSTITIAL_LOADED, event);
    }

    @Override
    public void onInterstitialFailed(String placement, int errorCode) {
        WritableMap event = makeEvent(placement, errorCode);
        sendEvent(EVENT_INTERSTITIAL_FAILED, event);
    }

    @Override
    public void onInterstitialShown(String placement) {
        WritableMap event = makeEvent(placement, null);
        sendEvent(EVENT_INTERSTITIAL_SHOWN, event);
    }

    @Override
    public void onInterstitialClicked(String placement) {
        WritableMap event = makeEvent(placement, null);
        sendEvent(EVENT_INTERSTITIAL_CLICKED, event);
    }

    @Override
    public void onInterstitialDismissed(String placement) {
        WritableMap event = makeEvent(placement, null);
        sendEvent(EVENT_INTERSTITIAL_DISMISSED, event);
    }

    private void sendEvent(String eventName, WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @Override
    public void onRewardedVideoLoaded(String placement) {
        WritableMap event = makeEvent(placement, null);
        sendEvent(EVENT_REWARDED_LOADED, event);
    }

    @Override
    public void onRewardedVideoFailed(String placement, int errorCode) {
        WritableMap event = makeEvent(placement, errorCode);
        sendEvent(EVENT_REWARDED_FAILED, event);
    }

    @Override
    public void onRewardedVideoShown(String placement) {
        WritableMap event = makeEvent(placement, null);
        sendEvent(EVENT_REWARDED_SHOWN, event);
    }

    @Override
    public void onRewardedVideoShownError(String placement, int errorCode) {
        WritableMap event = makeEvent(placement, errorCode);
        sendEvent(EVENT_REWARDED_SHOW_FAILED, event);
    }

    @Override
    public void onRewardedVideoDismissed(String placement) {
        WritableMap event = makeEvent(placement, null);
        sendEvent(EVENT_REWARDED_DISMISSED, event);
    }

    @Override
    public void onRewardedVideoCompleted(String placement) {
        WritableMap event = makeEvent(placement, null);
        event.putString("rewardName", this.rewardName);
        event.putInt("rewardAmount", this.rewardAmount);
        sendEvent(EVENT_REWARDED_COMPLETED, event);
    }

    private WritableMap makeEvent(String placement, Integer errorCode) {
        WritableMap event = Arguments.createMap();
        if (errorCode != null) {
            event.putString("errorDesc", ErrorCodes.getErrorDescription(errorCode));
        }
        if (TextUtils.isEmpty(placement)) {
            event.putString("placement", NOT_DEFINED);
        } else {
            event.putString("placement", placement);
        }
        return event;
    }

}
