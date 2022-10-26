import React, { useState, useRef, useEffect } from 'react';
import { StyleSheet, Text, View, Alert, ScrollView } from 'react-native';
import { AppRegistry, Button } from 'react-native';
import { Dimensions } from 'react-native';
import FreestarReactBridge from '@freestar/freestar-plugin-react-native';
import BannerAd from '@freestar/freestar-plugin-react-native/BannerAd';

import MrecBannerAd from '@freestar/freestar-plugin-react-native/MrecBannerAd';
import SmallNativeAd from '@freestar/freestar-plugin-react-native/SmallNativeAd';
import MediumNativeAd from '@freestar/freestar-plugin-react-native/MediumNativeAd';

export default function App(props) {

  const [isHungry, setIsHungry] = useState(true);
  const window = Dimensions.get("window");
  const screen = Dimensions.get("screen");
  const windowWidth = Dimensions.get('window').width;

  return (

   <View style={styles.container}>

   <ScrollView style={styles.scrollView}>

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

      <Button
        onPress={() => {
          FreestarReactBridge.loadThumbnailAd(null);
        }}
        title={"Load Thumbnail Ad"}
      />

       <View style={{ flex: 1, flexDirection: 'row', justifyContent: 'center', alignItems: 'center' }}>

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
               onBannerAdLoaded={handleDisplayAdLoaded}
               onBannerAdAdFailedToLoad={handleDisplayAdFailed}
               onBannerAdClicked={handleDisplayAdClicked}
            />

      </View>


      <SmallNativeAd
         style={{width: 360, height: 100}}
         requestOptions={
            {
               //placement: 'home_page_p1' //NOTE: if this placement has not been setup in the back-end, then do NOT specify placement
               targetingParams: {
                     'someparam1': 'somevalue1',
                     'someparam2': 'somevalue2',
                     'someparam3': 'somevalue3',
               },
               testDeviceIds: ['deviceId1','deviceId2', 'deviceId3']
            }
         }
         onNativeAdLoaded={handleDisplayAdLoaded}
         onNativeAdFailedToLoad={handleDisplayAdFailed}
         onNativeAdClicked={handleDisplayAdClicked}
      />

    </ScrollView>

    </View>

  );
}

function handleDisplayAdClicked({nativeEvent}) {
   Alert.alert('display ad clicked');
}

function handleDisplayAdLoaded({ nativeEvent }) {
   console.log('loaded display ad. placement: ' + nativeEvent.placement + " window width: "
   + Dimensions.get('window').width
   + ' winningBidder: ' + nativeEvent.winningBidder);
}

function handleDisplayAdFailed({ nativeEvent }) {
   //console.log('failed ' + nativeEvent.errorDesc + ' ' + nativeEvent.size + ' placement: ' + nativeEvent.placement);
   Alert.alert('failed to load display ad. error: ' + nativeEvent.errorDesc + ' placement: ' + nativeEvent.placement);
}

const styles = StyleSheet.create({

  container: {
  flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',

  },
});

FreestarReactBridge.enablePartnerChooserForTesting(true);
FreestarReactBridge.initWithAdUnitID('XqjhRR');

FreestarReactBridge.subscribeToInterstitialCallbacks2((eventName, placement, eventMap) => {

    if(eventName === "onInterstitialLoaded") {

      console.log("Interstitial: " + eventMap.winningBidder);

      if (placement == 'not defined')
         placement = null;
      FreestarReactBridge.showInterstitialAd(placement);

      } else if (eventName === "onInterstitialClicked") {

      Alert.alert('Interstitial Ad clicked');

      } else if (eventName === "onInterstitialShown") {

      } else if (eventName === "onInterstitialFailed") {

        Alert.alert('Interstitial Ad not available');

      } else if (eventName === "onInterstitialDismissed") {

      } else {
       console.log("unknown event");
      }
     });


FreestarReactBridge.subscribeToRewardCallbacks2((eventName, placement, rewardName = '', rewardAmount = 0, eventMap) => {

     if (eventName === "onRewardedFailed") {

          Alert.alert('Reward Ad not available');

     } else if (eventName === "onRewardedDismissed") {

     } else if(eventName === "onRewardedLoaded") {

         console.log("Rewarded: " + eventMap.winningBidder);

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

FreestarReactBridge.subscribeToThumbnailAdCallbacks((eventName, placement, eventMap) => {

    if(eventName === "onThumbnailAdLoaded") {

      console.log("Thumbnail Ad: " + eventMap.winningBidder);

      if (placement == 'not defined')
         placement = null;

      FreestarReactBridge.showThumbnailAd(placement, 'bottomLeft', 0, 0);

    } else if (eventName === "onThumbnailAdClicked") {

      Alert.alert('Thumbnail Ad clicked');

    } else if (eventName === "onThumbnailAdShown") {

    } else if (eventName === "onThumbnailAdFailed") {

        Alert.alert('Thumbnail Ad not available');

    } else if (eventName === "onThumbnailAdDismissed") {

    } else {
       console.log("unknown event");
    }
});