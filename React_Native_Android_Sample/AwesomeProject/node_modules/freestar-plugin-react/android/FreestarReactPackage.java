package com.awesomeproject; /***Rename package according to your own Android project***/

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FreestarReactPackage implements ReactPackage {

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        ArrayList<NativeModule> nativeModules = new ArrayList<>(1);
        nativeModules.add(new FreestarReactBridge(reactContext));
        return nativeModules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.asList(
                new FreestarBannerAdViewManager()
        );
    }

}