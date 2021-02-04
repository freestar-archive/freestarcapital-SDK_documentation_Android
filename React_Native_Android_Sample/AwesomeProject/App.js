import React, { useState } from 'react';
import { StyleSheet, Text, View, Alert } from 'react-native';
import { AppRegistry, Button } from 'react-native';  //may need to include Alert in production builds
import FreestarReactBridge from '@freestar/freestar-plugin-react-native';
import BannerAd from '@freestar/freestar-plugin-react-native/BannerAd';
import MrecBannerAd from '@freestar/freestar-plugin-react-native/MrecBannerAd';

export default function App(props) {

  const [isHungry, setIsHungry] = useState(true);

  return (


   <View style={styles.container}>

      <Text> {'\n'} Freestar Ads - React Native Sample! {'\n'} </Text>

      <Button
        onPress={() => {
          FreestarReactBridge.loadInterstitialAd(null);
        }}
        title={"Load Interstitial Ad"}
      />


      <Button
        onPress={() => {
          FreestarReactBridge.loadRewardAd(null);
        }}
        title={"Load Rewarded Ad"}
      />

      <Text> {'\n'} Small banner ad 320x50 should show below {'\n'} </Text>

      <BannerAd
         style={{width: 320, height: 50}}
         requestOptions={
            {
               size: 'BANNER',
               targetingParams: {
                     'someparam1': 'somevalue1',
                     'someparam2': 'somevalue2',
                     'someparam3': 'somevalue3',
               }
            }
         }
         onBannerAdLoaded={bannerLoaded}
         onBannerAdFailedToLoad={bannerFailed}
      />

      <Text> {'\n'} MREC banner ad 300x250 should show below {'\n'} </Text>

      <BannerAd
         style={{width: 300, height: 250}}
         requestOptions={
            {
               size: 'MREC',
               targetingParams: {
                     'someparam1': 'somevalue1',
                     'someparam2': 'somevalue2',
                     'someparam3': 'somevalue3',
               }
            }
         }
         onBannerAdLoaded={bannerLoaded}
         onBannerAdFailedToLoad={bannerFailed}
      />

    </View>

  );
}

function bannerLoaded({ nativeEvent }) {
   console.log('loaded ' + nativeEvent.size + ' placement: ' + nativeEvent.placement);
}

function bannerFailed({ nativeEvent }) {
   console.log('failed ' + nativeEvent.errorDesc + ' ' + nativeEvent.size + ' placement: ' + nativeEvent.placement);
   //Alert.alert('failed ' + nativeEvent.errorDesc + ' ' + nativeEvent.size + ' placement: ' + nativeEvent.placement);
}

const styles = StyleSheet.create({
  container: {
  flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

FreestarReactBridge.initWithAdUnitID('XqjhRR');

FreestarReactBridge.subscribeToInterstitialCallbacks((eventName, placement) => {
       if(eventName === "onInterstitialLoaded") {

         if (placement == 'not defined')
            placement = null;
         FreestarReactBridge.showInterstitialAd(placement);

       } else if (eventName === "onInterstitialClicked") {

       } else if (eventName === "onInterstitialShown") {

       } else if (eventName === "onInterstitialFailed") {

           Alert.alert('Interstitial Ad not available');

       } else if (eventName === "onInterstitialDismissed") {

       } else {
          console.log("unknown event");
       }
     });


FreestarReactBridge.subscribeToRewardCallbacks((eventName, placement, rewardName = '', rewardAmount = 0) => {
     if (eventName === "onRewardedFailed") {

          Alert.alert('Reward Ad not available');

     } else if (eventName === "onRewardedDismissed") {

     } else if(eventName === "onRewardedLoaded") {

         if (placement == 'not defined')
            placement = null;
         FreestarReactBridge.showRewardAd(placement, "Coins", 50, "myuserId", "12345678");

     } else if (eventName === "onRewardedCompleted") {

         //Alert.alert("reward ad completed: awarded " + rewardAmount + ' ' + rewardName );
         console.log("reward ad completed: awarded " + rewardAmount + ' ' + rewardName);

     } else if (eventName === "onRewardedShown") {

     } else if (eventName === "onRewardedShowFailed") {

          Alert.alert('Reward Ad was available but failed to show');

     } else {
        console.log("unknown event");
     }
   });
