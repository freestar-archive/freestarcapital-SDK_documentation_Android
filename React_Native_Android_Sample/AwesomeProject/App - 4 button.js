import * as React from 'react';
import { Text, TextInput, View, Button, StyleSheet, Alert } from 'react-native';
import { NavigationContainer} from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import FreestarReactBridge from 'freestar-plugin-react';
import BannerAd from 'freestar-plugin-react/BannerAd';


function SettingsScreen({ navigation }) {
  return (
    <View style={styles.root}>
      <Text style={styles.text}>Click the below button</Text>
      <Button
        color="#009900"
        title="MREC banner ad 300x250"
        onPress={() => navigation.navigate('BigBanner',{name: 'MREC banner ad 300x250'})}
      />
      <Text style={styles.text}>Click the below button for placement</Text>
      <Button
        color="#009900"
        title="MREC banner ad 300x250"
        onPress={() => navigation.navigate('BigBannerPlacement',{name: 'MREC banner ad 300x250'})}
      />

    </View>
  );
}


function BannerScreen({ navigation }) {
  return (
    <View style={styles.root}>
     <Text style={styles.text}>Click the below button</Text>
      <Button
        color="#009900"
        title="Small banner ad 320x50"
        onPress={() => navigation.navigate('SmallBanner',{name: 'Small banner ad 320x50'})}
      />
     <Text style={styles.text}>Click the below button for Placement </Text>
      <Button
        color="#009900"
        title="Small Placement ad 320x50"
        onPress={() => navigation.navigate('SmallBanner',{name: 'Small banner ad 320x50'})}
      />
    </View>
  );
}


function BigBannerScreen({ navigation }) {
  return (
    <View style={styles.root}>

    <Button
        color="#009900"
        title="Reload MREC banner ad 320x250"
        onPress={() => navigation.push('BigBanner')}
      />

     <Text style={styles.text}> {'\n'} MREC banner ad 300x250 should show below {'\n'} </Text>

      <BannerAd
         style={{width: 300, height: 250}}
         requestOptions={
            {
               size:'MREC',
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

     <Text style={styles.text}> {'\n'}  {'\n'} </Text>
      <Button
        color="#009900"
        title="Go Back"
        onPress={() => navigation.navigate('Settings')}
      />
    </View>
  );
}



function BigBannerPlacementScreen({ navigation }) {
  return (
    <View style={styles.root}>

    <Button
        color="#009900"
        title="Reload MREC Placement ad 320x250"
        onPress={() => navigation.push('BigBannerPlacement')}
      />

     <Text style={styles.text}> {'\n'} MREC Placement banner ad 300x250 should show below {'\n'} </Text>

      <BannerAd
         style={{width: 300, height: 250}}
         requestOptions={
            {
               size:'MREC',
               placement: 'MREC_p1', 
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

      <Text style={styles.text}> {'\n'}  {'\n'} </Text>
      <Button
        color="#009900"
        title="Go Back"
        onPress={() => navigation.navigate('Settings')}
      />
    </View>
  );
}



function SmallBannerScreen({ navigation }) {
  return (
    <View style={styles.root}>

    <Button
       color="#009900"
        title="Reload Small banner ad 320x50"
        onPress={() => navigation.push('SmallBanner')}
      />

     <Text style={styles.text}> {'\n'} Small banner ad 320x50 should show below {'\n'} </Text>

      <BannerAd
         style={{width: 320, height: 50}}
         requestOptions={
            {
               size:'BANNER',
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

      <Text style={styles.text}> {'\n'}{'\n'} </Text>
      <Button
        color="#009900"
        title="Go Back"
        onPress={() => navigation.navigate('Banner')}
      />
    </View>
  );
}


function SmallBannerPlacementScreen({ navigation }) {
  return (
    <View style={styles.root}>

    <Button
       color="#009900"
        title="Reload Small banner ad 320x50"
        onPress={() => navigation.push('SmallBannerPlacement')}
      />

     <Text style={styles.text}> {'\n'} Small banner ad 320x50 should show below {'\n'} </Text>

      <BannerAd
         style={{width: 320, height: 50}}
         requestOptions={
            {
               size:'BANNER',
               placement: 'banner_p1',
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

      <Text style={styles.text}> {'\n'}{'\n'} </Text>
      <Button
        color="#009900"
        title="Go Back"
        onPress={() => navigation.navigate('Banner')}
      />
    </View>
  );
}


function VideoAdScreen({ navigation }) {
  return (
    <View style={styles.root}>
      <Text style={styles.text}>Rewarded/Interstitial Ad</Text>
      <View style={styles.ButtonStyle}>
      <Button
      color="#009900"
        onPress={() => {
          FreestarReactBridge.loadRewardAd("what");
        }}
        title={"Load Rewarded Ad"}
        style={{marginHorizontal:200}}
      />
      <Text style={styles.text}>Click the below button for Placement Ad</Text>
     
      <Button
      color="#009900"
        onPress={() => {
          FreestarReactBridge.loadRewardAd('rewarded_p1');
        }}
        title={"Load Placement Rewarded Ad"}
        style={{marginHorizontal:200}}
      />
      </View>

     <View style={styles.ButtonStyle}>
      <Button
      color="#009900"
        onPress={() => {
          FreestarReactBridge.loadInterstitialAd('what');
        }}
        title={"Load Interstitial Ad"}
        raised={true}
        />

      <Text style={styles.text}>Click the below button for Placement Ad</Text>
        <Button
      color="#009900"
        onPress={() => {
          FreestarReactBridge.loadInterstitialAd('interstitial_p1');
        }}
        title={"Load Placement Interstitial Ad"}
        raised={true}
        />
        </View>
    </View>
  );
}


function bannerLoaded({ nativeEvent }) {
   console.log('loaded ' + nativeEvent.size + ' placement: ' + nativeEvent.placement);
}

function bannerFailed({ nativeEvent }) {
   console.log('failed ' + nativeEvent.errorDesc + ' ' + nativeEvent.size + ' placement: ' + nativeEvent.placement);
}

FreestarReactBridge.initWithAdUnitID('dweT4z');

FreestarReactBridge.subscribeToInterstitialCallbacks((eventName, placement) => {
       if(eventName === "onInterstitialLoaded") {

         if (placement == 'not defined') {  //Important: Check if the placement comes in as the literal 'not defined'
             placement = null;
         }
         FreestarReactBridge.showInterstitialAd(placement);  //placement cannot be empty string '', but can be null or any string.

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

         if (placement == 'not defined') {  //Important: Check if the placement comes in as the literal 'not defined'
             placement = null;  
         }
         //placement cannot be empty string '', but can be null or any string.
         FreestarReactBridge.showRewardAd(placement, "Coins", 50, "myuserId", "12345678");

     } else if (eventName === "onRewardedCompleted") {

         console.log("reward ad completed: awarded " + rewardAmount + ' ' + rewardName);

     } else if (eventName === "onRewardedShown") {

     } else if (eventName === "onRewardedShowFailed") {

          Alert.alert('Reward Ad was available but failed to show');

     } else {
        console.log("unknown event");
     }
   });



const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'whitesmoke'
  },
  text: {
    color: '#101010',
    fontSize: 14,
    fontWeight: 'bold',
    padding: 10,
    textAlign: "center"
  },
  ButtonStyle: {
    margin: 20,
    width: 300
  },
});


const Tab = createBottomTabNavigator();
const SettingsStack = createStackNavigator();
const BannerStack = createStackNavigator();
const VideoAdStack = createStackNavigator();

function App() {
  return (
    <NavigationContainer>
      <Tab.Navigator>
        <Tab.Screen name="Banner300x250">
          {() => (
            <SettingsStack.Navigator>
              <SettingsStack.Screen
                name="Settings"
                component={SettingsScreen}
                options={{ title: 'MREC banner ad 300x250' }}
              />
              <SettingsStack.Screen 
              name="BigBanner" 
              component={BigBannerScreen} 
              options={{ title: 'MREC banner ad 300x250' }}
              />

              <SettingsStack.Screen 
              name="BigBannerPlacement" 
              component={BigBannerPlacementScreen} 
              options={{ title: 'MREC placement ad 300x250' }}
              />

            </SettingsStack.Navigator>
          )}
        </Tab.Screen>

         <Tab.Screen name="Banner320x50">
          {() => (
            <BannerStack.Navigator>
              <BannerStack.Screen
                name="Banner"
                component={BannerScreen}
                options={{ title: 'Small banner ad 320x50' }}
              />
              <BannerStack.Screen 
              name="SmallBanner" 
              component={SmallBannerScreen} 
              options={{ title: 'Small banner ad 320x50' }}
              />
             <BannerStack.Screen 
              name="SmallBannerPlacement" 
              component={SmallBannerPlacementScreen} 
              options={{ title: 'Small banner ad 320x50' }}
              />
            </BannerStack.Navigator>
          )}
        </Tab.Screen>

        <Tab.Screen name="Rewarded/Interstitial Ad">
          {() => (
            <VideoAdStack.Navigator>
              <VideoAdStack.Screen name="VideoAd" component={VideoAdScreen} 
              options={{ title: 'Rewarded/Interstitial Ad' }}
            />
            </VideoAdStack.Navigator>
          )}
        </Tab.Screen>
      </Tab.Navigator>
    </NavigationContainer>
  );
}
export default App;