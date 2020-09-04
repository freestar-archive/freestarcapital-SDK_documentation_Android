import { requireNativeComponent, ViewPropTypes } from 'react-native';
import PropTypes from 'prop-types';

import {NativeModules, NativeEventEmitter, Platform} from 'react-native';
import type EmitterSubscription from 'EmitterSubscription';

const FreestarReactBridge = NativeModules.FreestarReactBridge;
const emitter = new NativeEventEmitter(FreestarReactBridge);

const INTERSTITIAL_CALLBACKS = [
  "onInterstitialLoaded",
  "onInterstitialClicked",
  "onInterstitialShown",
  "onInterstitialFailed",
  "onInterstitialDismissed"];
const REWARD_CALLBACKS_NONFINISHED = [
  "onRewardedLoaded",
  "onRewardedFailed",
  "onRewardedShowFailed",
  "onRewardedShown",
  "onRewardedDismissed"];
const REWARD_CALLBACK_FINISHED = "onRewardedCompleted";

module.exports = {
  initWithAdUnitID: (apiKey: string) => FreestarReactBridge.initWithAdUnitID(apiKey),
  setDemographics: (
    age: int,
    birthday: date,
    gender: string,
    maritalStatus: string,
    ethnicity: string
  ) => FreestarReactBridge.setDemographics(age,birthday.toISOString(),gender,maritalStatus,ethnicity),
  setLocation: (
    dmaCode: string,
    postalCode: string,
    currPostal: string,
    latitude: string,
    longitude: string
  ) => FreestarReactBridge.setLocation(dmaCode,postalCode,currPostal,latitude,longitude),
  setAppInfo: (
    appName: string,
    publisherName: string,
    appDomain: string,
    publisherDomain: string,
    storeURL: string,
    category: string
  ) => FreestarReactBridge.setAppInfo(appName,publisherName,appDomain,publisherDomain,storeURL,category),
  subjectToGDPR: (gdprApplies: boolean, gdprConsentString: string) =>
    FreestarReactBridge.subjectToGDPR(gdprApplies,gdprConsentString),
  setCoppaStatus: (coppa: boolean) => FreestarReactBridge.setCoppaStatus(coppa),
  loadInterstitialAd: (placement: string) => FreestarReactBridge.loadInterstitialAd(placement),
  showInterstitialAd: () => FreestarReactBridge.showInterstitialAd(),
  loadRewardAd: (placement: string) => FreestarReactBridge.loadRewardAd(placement),
  showRewardAd: (
    rewardName: string,
    rewardAmount: int,
    userID: string,
    secretKey: string
  ) => FreestarReactBridge.showRewardAd(rewardName,rewardAmount,userID,secretKey),
  subscribeToInterstitialCallbacks: (callback: Function) => {
    INTERSTITIAL_CALLBACKS.map((event) => {
      emitter.removeAllListeners(event);
      emitter.addListener(event, () => {
        callback(event);
      });
    });
  },
  unsubscribeFromInterstitialCallbacks: () => {
    INTERSTITIAL_CALLBACKS.map((event) => {
      emitter.removeAllListeners(event);
    });
  },
  subscribeToRewardCallbacks: (callback: Function) => {
    REWARD_CALLBACKS_NONFINISHED.map((event) => {
      emitter.removeAllListeners(event);
      emitter.addListener(event, () => {
        callback(event);
      });
    });
    emitter.removeAllListeners(REWARD_CALLBACK_FINISHED);
    emitter.addListener(REWARD_CALLBACK_FINISHED, (body) => {
      callback(REWARD_CALLBACK_FINISHED, body.rewardName, body.rewardAmount)
    });
  },
  unsubscribeFromRewardCallbacks: () => {
    REWARD_CALLBACKS_NONFINISHED.map((event) => {
      emitter.removeAllListeners(event);
    });
    emitter.removeAllListeners(REWARD_CALLBACK_FINISHED);
  },
  setCustomSegmentProperty: (key: string, value: string) =>
    FreestarReactBridge.setCustomSegmentProperty(key,value),
  getCustomSegmentProperty: (key: string, callback: Function) =>
    FreestarReactBridge.getCustomSegmentProperty(key, callback),
  getAllCustomSegmentProperties: (callback: Function) =>
    FreestarReactBridge.getAllCustomSegmentProperties(callback),
  deleteCustomSegmentProperty: (key: string) =>
    FreestarReactBridge.deleteCustomSegmentProperty(key),
  deleteAllCustomSegmentProperties: () =>
    FreestarReactBridge.deleteAllCustomSegmentProperties()

};

export { default as BannerAd } from './BannerAd';