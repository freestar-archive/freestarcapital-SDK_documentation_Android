import React, { useState } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { Alert, AppRegistry, Button } from 'react-native';
import FreestarReactBridge from 'freestar-plugin-react';
import BannerAd from 'freestar-plugin-react/BannerAd';

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

      <BannerAd
         style={{width: 300, height: 250}}
         requestOptions={
            {
               size:'MREC',
               isCoppaEnabled: false,
               targetingParams: {
                     'someparam1': 'somevalue1',
                     'someparam2': 'somevalue2',
                     'someparam3': 'somevalue3',
               },
               testDeviceIds: ['deviceId1','deviceId2', 'deviceId3']
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
   Alert.alert('failed ' + nativeEvent.errorDesc + ' ' + nativeEvent.size + ' placement: ' + nativeEvent.placement);
}

const styles = StyleSheet.create({
  container: {
  flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

FreestarReactBridge.initWithAdUnitID();

FreestarReactBridge.subscribeToInterstitialCallbacks((eventName) => {
       if(eventName === "onInterstitialLoaded") {

         FreestarReactBridge.showInterstitialAd();

       } else if (eventName === "onInterstitialClicked") {

       } else if (eventName === "onInterstitialShown") {

       } else if (eventName === "onInterstitialFailed") {

           Alert.alert('Interstitial Ad not available');

       } else if (eventName === "onInterstitialDismissed") {

       } else {
          console.log("unknown event");
       }
     });


FreestarReactBridge.subscribeToRewardCallbacks((eventName, rewardName = '', rewardAmount = 0) => {
     if (eventName === "onRewardedFailed") {

          Alert.alert('Reward Ad not available');

     } else if (eventName === "onRewardedDismissed") {

     } else if(eventName === "onRewardedLoaded") {

         FreestarReactBridge.showRewardAd("Coins", 50, "myuserId", "12345678");

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