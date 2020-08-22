package com.awesomeproject; /***Rename package according to your own Android project***/

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.AdSize;
import com.freestar.android.ads.BannerAd;
import com.freestar.android.ads.BannerAdListener;
import com.freestar.android.ads.ChocolateLogger;
import com.freestar.android.ads.ErrorCodes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FreestarBannerAdViewManager extends ViewGroupManager<ReactViewGroup> {

    private static final String TAG = "FreestarBannerAdViewManager";

    private static final String EVENT_AD_LOADED = "onBannerAdLoaded";
    private static final String EVENT_AD_FAILED_TO_LOAD = "onBannerAdFailedToLoad";
    private static final String EVENT_AD_CLICKED = "onBannerAdClicked";

    private static final String PLACEMENT_UNDEFINED = "undefined";
    private static final String KEY_SIZE = "size";
    private static final String KEY_PLACEMENT = "placement";
    private static final String KEY_ERROR_DESCR = "errorDesc";

    private static final String AD_SIZE_BANNER = "BANNER";
    private static final String AD_SIZE_MEDIUM_RECTANGLE = "MREC";
    private static final String AD_SIZE_LEADERBOARD = "LEADERBOARD";

    private AdRequest adRequest;
    private AdSize adSize;
    private String placement;
    private BannerAd bannerAd;

    @Override
    public String getName() {
        return "BannerAd";  //maps to BannerAd.js
    }

    @ReactProp(name = "requestOptions")
    public void setRequestOptions(ReactViewGroup reactViewGroup, ReadableMap value) {
        adRequest = buildAdRequest(reactViewGroup.getContext(), value);
        if (value.hasKey(KEY_PLACEMENT)) {
            placement = value.getString(KEY_PLACEMENT);
        }
        ChocolateLogger.w(TAG, "setRequestOptions setSize " + value);
        String size = value.getString(KEY_SIZE); //will fail up to react if not found

        //map to native sdk
        if (size.equals(AD_SIZE_BANNER)) {
            adSize = AdSize.BANNER_320_50;
        } else if (size.equals(AD_SIZE_MEDIUM_RECTANGLE)) {
            adSize = AdSize.MEDIUM_RECTANGLE_300_250;
        } else if (size.equals(AD_SIZE_LEADERBOARD)) {
            adSize = AdSize.LEADERBOARD_728_90;
        } else {
            WritableMap payload = Arguments.createMap();
            payload.putString(KEY_ERROR_DESCR, "Invalid size: " + size);
            payload.putString(KEY_PLACEMENT, (placement == null ? PLACEMENT_UNDEFINED : placement));
            payload.putString(KEY_SIZE, getAdSizeString());
            sendEvent(reactViewGroup, EVENT_AD_FAILED_TO_LOAD, payload);
            ChocolateLogger.e(TAG, "setSize invalid size: " + value);
            return;
        }
        ChocolateLogger.w(TAG, "setRequestOptions " + value + " placement: " + placement);
        requestAd(reactViewGroup, bannerAd, adSize, adRequest, placement);
    }

    private void requestAd(ReactViewGroup reactViewGroup, BannerAd bannerAd, AdSize adSize,
                           AdRequest request, String placement) {
        ChocolateLogger.w(TAG, "requestAd.  adSize: " + adSize + "  request: " + request);
        int widthPixels = (int) PixelUtil.toPixelFromDIP(adSize.getWidth());
        int heightPixels =(int) PixelUtil.toPixelFromDIP(adSize.getHeight());
        FrameLayout.LayoutParams frameLayoutParams
                = new FrameLayout.LayoutParams(widthPixels, heightPixels);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(widthPixels, heightPixels);
        bannerAd.setLayoutParams(frameLayoutParams);
        reactViewGroup.setLayoutParams(layoutParams);
        bannerAd.setAdSize(adSize);
        bannerAd.loadAd(request, placement);
    }

    @Override
    protected ReactViewGroup createViewInstance(ThemedReactContext reactContext) {
        ChocolateLogger.w(TAG, "createViewInstance");
        ReactViewGroup viewGroup = new ReactViewGroup(reactContext);
        bannerAd = new BannerAd(reactContext);
        setOnHierarchyChangeListener(bannerAd);
        viewGroup.addView(bannerAd);
        setAdListener(viewGroup);
        return viewGroup;
    }

    private void layout(View view) {
        int top = view.getTop();
        int left = view.getLeft();
        int widthPixels = (int) PixelUtil.toPixelFromDIP(adSize.getWidth());
        int heightPixels = (int) PixelUtil.toPixelFromDIP(adSize.getHeight());
        view.measure(widthPixels, heightPixels);
        view.layout(left, top, left + widthPixels, top + heightPixels);
        ChocolateLogger.w(TAG, "layout. left: "
                + left + " top: " + top
                + " width: " + widthPixels
                + " height: " + heightPixels
                + " view: " + view);
        view.requestLayout();
    }

    /**
     * React native has some weird behavior where dynamically added views
     * do NOT show up unless re-measure and re-layout.  This is NOT
     * an issue with native android, only with react native.  This was tough.
     * It may seem like overkill, but these measures were necessary after
     * individually testing all the different ad partners banner ad views.
     */
    private void setOnHierarchyChangeListener(final ViewGroup viewGroup) {
        viewGroup.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                ChocolateLogger.w(TAG, "onChildViewAdded: " + child);
                layout(viewGroup);
                parent.post(new Runnable() {
                    @Override
                    public void run() {
                        layout(child);
                    }
                });
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
                ChocolateLogger.w(TAG, "onChildViewRemoved: " + child);
            }
        });
    }

    //for informational purposes only
    private String getAdSizeString() {
        if (adSize.getWidth() == AdSize.BANNER_320_50.getWidth()) {
            return AD_SIZE_BANNER;
        } else if (adSize.getWidth() == AdSize.MEDIUM_RECTANGLE_300_250.getWidth()) {
            return AD_SIZE_MEDIUM_RECTANGLE;
        } else if (adSize.getWidth() == AdSize.LEADERBOARD_728_90.getWidth()) {
            return AD_SIZE_LEADERBOARD;
        } else {
            return "unsupported banner ad size";
        }
    }

    private void setAdListener(final ReactViewGroup reactViewGroup) {

        bannerAd.setBannerAdListener(new BannerAdListener() {
            @Override
            public void onBannerAdLoaded(View view, String placement) {
                ChocolateLogger.w(TAG, "onBannerAdLoaded");
                /*
                 * React native has some weird behavior where dynamically added views
                 * do NOT show up unless re-measure and re-layout.  This is NOT
                 * an issue with native android, only with react native.  This was tough.
                 * It may seem like overkill, but these measures were necessary after
                 * individually testing all the different ad partners banner ad views.
                 */
                layout(bannerAd);
                bannerAd.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bannerAd.getChildCount() > 0) {
                            layout(bannerAd.getChildAt(0));
                        }
                    }
                });
                WritableMap payload = Arguments.createMap();
                payload.putString(KEY_PLACEMENT, (placement == null ? PLACEMENT_UNDEFINED : placement));
                payload.putString(KEY_SIZE, getAdSizeString());
                sendEvent(reactViewGroup, EVENT_AD_LOADED, payload);
            }

            @Override
            public void onBannerAdFailed(View view, String placement, int errorCode) {
                WritableMap payload = Arguments.createMap();
                payload.putString(KEY_ERROR_DESCR, ErrorCodes.getErrorDescription(errorCode));
                payload.putString(KEY_PLACEMENT, (placement == null ? PLACEMENT_UNDEFINED : placement));
                payload.putString(KEY_SIZE, getAdSizeString());
                sendEvent(reactViewGroup, EVENT_AD_FAILED_TO_LOAD, payload);
            }

            @Override
            public void onBannerAdClicked(View view, String placement) {
                WritableMap payload = Arguments.createMap();
                payload.putString(KEY_PLACEMENT, (placement == null ? PLACEMENT_UNDEFINED : placement));
                payload.putString(KEY_SIZE, getAdSizeString());
                sendEvent(reactViewGroup, EVENT_AD_CLICKED, payload);
            }

            @Override
            public void onBannerAdClosed(View view, String placement) {
                //not implemented
            }
        });
    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put(
                        EVENT_AD_LOADED,
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", EVENT_AD_LOADED)))
                .put(
                        EVENT_AD_FAILED_TO_LOAD,
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", EVENT_AD_FAILED_TO_LOAD)))
                .put(
                        EVENT_AD_CLICKED,
                        MapBuilder.of(
                                "phasedRegistrationNames",
                                MapBuilder.of("bubbled", EVENT_AD_CLICKED)))
                .build();
    }

    private void sendEvent(ReactViewGroup reactViewGroup, String eventName, WritableMap payload) {
        WritableMap event = Arguments.createMap();
        event.putString("type", eventName);

        if (payload != null) {
            event.merge(payload);
        }

        ChocolateLogger.w(TAG, "sendEvent: " + event.toString() + " instance: " + this
                + " id: " + reactViewGroup.getId());

        ((ThemedReactContext) reactViewGroup.getContext())
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(reactViewGroup.getId(), eventName, event);
    }

    static com.freestar.android.ads.AdRequest buildAdRequest(Context context, ReadableMap adRequestOptions) {
        AdRequest adRequest = new AdRequest(context);
        if (adRequestOptions.hasKey("targetingParams")) {
            try {
                ReadableMap targetingParams = adRequestOptions.getMap("targetingParams");
                ReadableMapKeySetIterator i = targetingParams.keySetIterator();
                while (i.hasNextKey()) {
                    String name = i.nextKey();
                    String value = targetingParams.getString(name);
                    adRequest.addCustomTargeting(name, value);
                    ChocolateLogger.w(TAG, "buildAdRequest " + name + '/' + value);
                }
            } catch (Exception e) {
                ChocolateLogger.e(TAG, "buildAdRequest. check targetingParams", e);
            }
        }
        if (adRequestOptions.hasKey("isCoppaEnabled")) {
            try {
                adRequest.setCOPPAEnabled(adRequestOptions.getBoolean("isCoppaEnabled"));
                ChocolateLogger.w(TAG, "isCoppaEnabled: " + adRequestOptions.getBoolean("isCoppaEnabled"));
            } catch (Exception e) {
                ChocolateLogger.e(TAG, "buildAdRequest. check isCoppaEnabled", e);
            }
        }
        if (adRequestOptions.hasKey("testDeviceIds")) {
            try {
                ReadableArray array = adRequestOptions.getArray("testDeviceIds");
                Set<String> testDevices = new HashSet<>();
                for (int i = 0; i < array.size(); i++) {
                    String testDeviceId = array.getString(i);
                    if (testDeviceId != null) {
                        testDevices.add(testDeviceId);
                        ChocolateLogger.w(TAG, "buildAdRequest. adding test device id: " + testDeviceId);
                    }
                }
                if (testDevices.size() > 0)
                    adRequest.setTestDevices(testDevices);
            } catch (Exception e) {
                ChocolateLogger.e(TAG, "buildAdRequest check testDeviceIds", e);
            }
        }
        return adRequest;
    }

}
