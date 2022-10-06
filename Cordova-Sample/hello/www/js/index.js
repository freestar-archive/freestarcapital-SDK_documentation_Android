/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Wait for the deviceready event before using any of Cordova's device APIs.
// See https://cordova.apache.org/docs/en/latest/cordova/events/events.html#deviceready
document.addEventListener('deviceready', onDeviceReady, false);

document.getElementById("interstitialAd").addEventListener('click', onInterstitialAdButtonClicked);
document.getElementById("rewardedAd").addEventListener('click', onRewardedAdButtonClicked);
document.getElementById("bannerAd").addEventListener('click', onBannerAdButtonClicked);
document.getElementById("closeBannerAd").addEventListener('click', onCloseBannerAdButtonClicked);

document.addEventListener('onInterstitialLoaded', onInterstitialLoaded, false);
document.addEventListener('onRewardedVideoLoaded', onRewardedVideoLoaded, false);
document.addEventListener('onBannerAdFailed', onBannerAdFailed, false);

function onBannerAdButtonClicked() {
   //FreestarAds.showBannerAd(null, FreestarAds.BANNER_AD_SIZE_300x250, FreestarAds.BANNER_AD_POSITION_TOP);
   window.plugins.freestarPlugin.showBannerAd(null,
      window.plugins.freestarPlugin.BANNER_AD_SIZE_300x250,
      window.plugins.freestarPlugin.BANNER_AD_POSITION_TOP);
}

function onCloseBannerAdButtonClicked() {
   window.plugins.freestarPlugin.closeBannerAd(null, window.plugins.freestarPlugin.BANNER_AD_SIZE_300x250);
}

function onInterstitialAdButtonClicked() {
   window.plugins.freestarPlugin.loadInterstitialAd(null);
}

function onRewardedAdButtonClicked() {
   window.plugins.freestarPlugin.loadRewardedAd(null);
}

function onRewardedVideoLoaded(data) {
   console.log("FreestarPlugin.js: onRewardedVideoLoaded: "+ data.placement);
   window.plugins.freestarPlugin.showRewardedAd(data.placement, "MySecret1234", "MyUserId", "Gold Coins", "100");
}

function onInterstitialLoaded(data) {
   console.log("FreestarPlugin.js: onInterstitialLoaded: "+ data.placement);
   window.plugins.freestarPlugin.showInterstitialAd(data.placement);
}

function onBannerAdFailed(data) {
   console.log("FreestarPlugin.js: onBannerAdFailed: placement: "+ data.placement
                  + " ad size: " + data.banner_ad_size + " "
                  + " error: " + data.error);
   Alert.alert('onBannerAdFailed');
}

function onDeviceReady() {
    // Cordova is now initialized. Have fun!
    console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);
    document.getElementById('deviceready').classList.add('ready');
    window.plugins.freestarPlugin.setTestModeEnabled(true, 'optional hash');
    //window.plugins.freestarPlugin.setPartnerChooserEnabled(true);
}
