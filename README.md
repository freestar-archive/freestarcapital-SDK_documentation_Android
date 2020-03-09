# FreeStar Ads Mediation SDK
FreeStar provides an effective ad mediation solution.  The FreeStar mediation method is universal auction, not the traditional waterfall.  Universal auction is more sophisticated than waterfall and provides, by far, the best eCPM.
This document describes how to integrate the FreeStar SDK into your native Android app quickly and easily.  This repo is a fully integrated Android sample app.  Feel free to clone it, open with Android Studio and run it on a device.

<blockquote>
  Note: You can remotely toggle on/off any of the following ad providers as you see fit using our web dashboard.  
  <i>All</i> applicable providers are enabled by default.
</blockquote>

<table>
  <tr><td>Ad Provider</td><td>SDK Version</td><td>Ad Unit Types</td></tr>
  
  <tr>  <td>AdColony</td> <td>4.1.1</td>  <td>Fullscreen Interstitial & Rewarded</td> </tr>
  <tr>  <td>Amazon</td> <td>5.9.0</td>  <td>Fullscreen Interstitial, Banner 300x250, Banner 320x50</td> </tr>
  <tr>  <td>AppLovin</td> <td>9.8.4</td>  <td>Fullscreen Interstitial & Rewarded, Banner 300x250, Banner 320x50</td>  </tr>
  <tr>  <td>Criteo</td> <td>3.4.0</td>  <td>Fullscreen Interstitial, Banner 300x250, Banner 320x50</td> </tr>
  
  <tr>  <td>Facebook</td> <td>4.9.9</td>  <td>Fullscreen Interstitial & Rewarded, Banner 300x250, Banner 320x50</td> </tr>
  <tr>  <td>Admob</td> <td>17.2.2</td>  <td>Fullscreen Interstitial & Rewarded, Banner 300x250, Banner 320x50</td> </tr>
  <tr>  <td>Google Ads Manager</td> <td>17.2.2</td>  <td>Fullscreen Interstitial & Rewarded, Banner 300x250, Banner 320x50</td> </tr>
  
  <tr>  <td>MoPub</td> <td>5.8.4</td>  <td>Fullscreen Interstitial & Rewarded, Banner 300x250, Banner 320x50</td> </tr>
  <tr>  <td>Tapjoy</td> <td>12.2.1</td>  <td>Fullscreen Interstitial & Rewarded</td> </tr>
  <tr>  <td>Unity Ads</td> <td>3.4.0</td>  <td>Fullscreen Interstitial & Rewarded, Banner 320x50</td> </tr>
  <tr>  <td>Vungle</td> <td>6.4.11</td>  <td>Fullscreen Interstitial & Rewarded</td> </tr>
</table>

<h2>Project Setup</h2>

Your <a href="https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/build.gradle">app-level build.gradle</a> should have the following:


<pre>
android {

    defaultConfig {

        // ... existing settings ...

        multiDexEnabled true
    }

    compileOptions { 

        sourceCompatibility JavaVersion.VERSION_1_8 
        targetCompatibility JavaVersion.VERSION_1_8 

    }

}
</pre>

Specify repositories in your <a href="https://github.com/freestarcapital/SDK_documentation_Android/blob/master/build.gradle">top-level build.gradle</a>:

https://github.com/freestarcapital/SDK_documentation_Android/blob/master/build.gradle

<pre>
repositories {
    maven { url 'https://jitpack.io' }
    maven { url "https://dl.bintray.com/vdopiacorp/fsdk" }
    maven { url 'https://tapjoy.bintray.com/maven' }
    // If you're using a version of Gradle greater than or equal to 4.1, you must use:
    google()

    // If you're using a version of Gradle lower than 4.1, you must use:
    maven { url 'https://maven.google.com' }

    flatDir {
        dirs 'libs'
    }
}
</pre>

Add all the dependencies in your <a href="https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/build.gradle">app-level build.gradle</a>:

https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/build.gradle

<pre>
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])

    //begin FreeStar
    implementation 'com.freestar.android.ads:freestar:3.2.0'
    implementation 'com.freestar.android.ads.admob:freestar-admob-adapter:17.2.0'
    implementation 'com.freestar.android.ads.applovin:freestar-applovin-adapter:9.8.4'
    implementation 'com.freestar.android.ads.amazon:freestar-amazon-adapter:5.9.0'
    implementation 'com.freestar.android.ads.adcolony:freestar-adcolony-adapter:4.1.1'
    implementation 'com.freestar.android.ads.google:freestar-google-adapter:17.2.0'
    implementation 'com.freestar.android.ads.criteo:freestar-criteo-adapter:3.4.0'
    implementation 'com.freestar.android.ads.unityads:freestar-unity-adapter:3.4.0'
    implementation 'com.freestar.android.ads.vungle:freestar-vungle-adapter:6.4.11'
    implementation 'com.freestar.android.ads.facebook:freestar-facebook-adapter:1.0.7'
    implementation 'com.freestar.android.ads.tapjoy:freestar-tapjoy-adapter:12.2.1'
    implementation 'com.freestar.android.ads.mopub:freestar-mopub-adapter:5.8.0'

    implementation 'com.applovin:applovin-sdk:9.8.4'
    implementation 'com.google.android.gms:play-services-ads:17.2.1' //11.0.4 minimum is required, 16.0.0 or newer recommended!
    implementation('com.google.android.ads.consent:consent-library:1.0.7') {
        exclude group: 'com.android.support'
        exclude group: 'com.google.code.gson', module: 'gson'
    }
    implementation('com.facebook.android:audience-network-sdk:4.99.3') {
        exclude group: 'com.google.android.exoplayer'
        exclude group: 'com.google.android.gms'
        exclude group: 'com.android.support'
    }
    implementation 'com.tapjoy:tapjoy-android-sdk:12.2.1@aar'
    implementation('com.github.vungle:vungle-android-sdk:6.4.11') {
        exclude group: 'com.android.support'
    }
    implementation('com.mopub:mopub-sdk:5.8.0@aar') {
        transitive = true
        exclude group: 'com.android.support'
    }

    //Note: if you are using Pre-roll, un-comment the following line:
    implementation 'com.google.ads.interactivemedia.v3:interactivemedia:3.11.2'
    //end, FreeStar
    
</pre>


<h3>AndroidManifest.xml</h3>

<pre>
Within the application tag of your AndroidManifest.xml, make sure to add

   &lt;application
        
       &lt;meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version"/&gt;

       &lt;meta-data
            tools:replace="android:value"
            android:name="com.google.android.gms.ads.APPLICATION_ID"
            android:value="ca-app-pub-3940256099942544~3347511713"/&gt;
            
   &lt;/application&gt;

</pre>

See the <a href="https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/src/main/AndroidManifest.xml">AndroidManifest.xml</a>:

https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/src/main/AndroidManifest.xml

<h3>GDPR Support</h3>

FreeStar is GDPR-ready and supports the IAB Standards for GDPR compliance.

Use the following simple api in conjunction with your existing Consent Management Provider.  If you do not have a CMP solution, that’s ok, too!  Our mediation sdk will detect if the user is in the EU and automatically apply GDPR actions to the ad request.  So, by default, you do not have to do any extra work to use our sdk in a GDPR-compliant fashion.

<pre>
// Save GDPR consent string
FreeStarAds.setGDPR(Context context, boolean isSubjectToGDPR, String iabConsentString);

// Indicates if the device is subject to GDPR by checking device locale.  No network required!
FreeStarAds.isSubjectToGDPR(Context context);
</pre>

<h2>Initialize FreeStar</h2>

FreeStar must be initialized in the <b>onCreate</b> of your Activity and must be initialized with
the <strong>Activity Context</strong> since some of the ad providers require it.

<pre>
public class MyActivity extends AppCompatActivity {

    private static final String API_KEY = "XqjhRR";   //Feel free to use this test key for now!
    private LVDOAdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adRequest = new LVDOAdRequest(this);
        FreeStarAds.init(this, API_KEY);
    }   

}
</pre>

<h2>Interstitial Ad</h2>

<pre>
InterstitialAd interstitialAd = new InterstitialAd(this, this); //Requires Activity for context
interstitialAd.loadAd(adRequest);

//You can also load associated to a placement as follows
//interstitialAd.loadAd(adRequest, "my_placement_p1"); 
</pre>

<blockquote>
If you plan to use more than one placement in your app, please adhere to the placement naming convention
as follows:  
<br> 
  
  "my_placement_name_pN", where N is the number of your placement.  
  
  
For example, let us 
assume you are using 2 interstitial ad placements in your game or app.  The first placement would be
the default placement; simply do not specifiy a placement name by using the <strong>loadAd()</strong> method without 
the placement parameter.  The second placement would be, for example,  "my_search_screen_p1".  The ending 
"p1" tells the SDK to use the second placement you created in our web dashboard for the interstitial ad unit.
  
This process is the same for all the other ad units, such as rewarded ads and banner ads.
</blockquote>

When the interstitial ad is ready, the <strong>onInterstitialLoaded</strong> callback will occur.

<pre>
    @Override
    public void onInterstitialLoaded(String placement) {
        interstitialAd.show();  //You can display the ad now OR show it later; your choice.
        
        //Note: Placement will be null if not specified in the original loadAd request.
        
    }
</pre>

There are other callbacks that will occur in other events, such as in the rare event where a load ad request
does not result in a fill.  Please see the <a href="https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/src/main/java/com/freestar/android/sample/MainActivity.java">MainActivity</a> on this sample for those details.

<blockquote>
❗⚠Warning: Attempting to load a new ad from the onInterstitialFailed() method is <strong>strongly</strong> discouraged. If you must load an ad from onInterstitialFailed(), limit ad load retries to avoid continuous failed ad requests in situations such as limited network connectivity.
</blockquote>


<h2>Banner Ad</h2>

FreeStar supports <strong>300x250</strong> and <strong>320x50</strong> banner ad formats and allows you to
control the refresh intervals remotely.

<pre>
        BannerAd bannerAd = new BannerAd(this);  //in Activity
        bannerAd.setAdSize(AdSize.BANNER_320_50);
        bannerAd.loadAd(adRequest);
        
        //Note: bannerAd.loadAd(adRequest, "my_banner_placement_p1") //'placement' is OPTIONAL and only if 
                                                       //you plan to have more than one Banner placement.
        
</pre>

When the banner ad is ready, the <strong>onBannerAdLoaded</strong> callback will occur.

<pre>
    @Override
    public void onBannerAdLoaded(View bannerAd, String placement) {
        //Note: Placement will be null if not specified in the original loadAd request.
        
        if (view.getParent() != null) {
            if (view.getParent() instanceof ViewGroup) {
                ((ViewGroup)view.getParent()).removeView(view);
            }
        }

        ((ViewGroup) findViewById(R.id.banner_container)).removeAllViews();
        ((ViewGroup) findViewById(R.id.banner_container)).addView(view);        
    }
</pre>

Banner ads can also be specified in XML layout and will be automatically loaded.
See <strong>com.freestar.android.ads.BannerAd</strong> in the <a href="https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/src/main/res/layout/activity_main.xml">sample layout</a>:

https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/src/main/res/layout/activity_main.xml

<pre>
            &lt;com.freestar.android.ads.BannerAd xmlns:ads="http://schemas.android.com/apk/res-auto"
            android:id="@+id/freestarBannerAd_2"
            android:layout_width="320dp"
            android:layout_height="50dp"
            android:layout_gravity="center"
            ads:FreeStarAdSize="BANNER"/&gt;
</pre>


<h2>Rewarded Ad</h2>

<blockquote>
  A common myth regarding Rewarded Ads is publishers are required to <i>give something</i> to the user.
  But, that's not true.  You can simply tell the user they must watch the ad in order to be able to 
  proceed to the next level or proceed to content.
</blockquote>

<pre>
  RewardedAd rewardedAd = new RewardedAd(this, this); //Must use Activity context
  rewardedAd.loadAd(adRequest);

//You can also load an ad tied to an Interstitial 'placement' as follows
//rewardedAd.loadAd(adRequest, "my_placement_p1"); //'placement' is OPTIONAL and only if 
                                                       //you plan to have more than one Rewarded
                                                       //placement
</pre>

When the rewarded ad is ready, the <strong>onRewardedVideoLoaded</strong> callback will occur.

<pre>
    @Override
    public void onRewardedVideoLoaded(String placement) {
        rewardedAd.show();  //You can display the rewarded ad now.
        
        //Note: Placement will be null if not specified in the original <strong>loadAd</strong> request.
    }
</pre>

When the user has fully watched the rewarded ad, the following callback will occur:

<pre>
    @Override
    public void onRewardedVideoCompleted(String placement) {
       //allow user to proceed to app content or next level in app/game
    }
</pre>


When the user has closed the rewarded ad, the following callback will occur:

<pre>
    @Override
    public void onRewardedVideoDismissed(String placement) {

    }
</pre>

<blockquote>
  If the user does not watch the rewarded ad thru to completion, <strong>onRewardedVideoCompleted</strong> will not occur.
  However, the <strong>onRewardedVideoDismissed</strong> will always occur when the rewarded ad is dismissed
  regardless if the user watched the entire rewarded ad or not.
</blockquote>

<blockquote>
❗⚠ Please assume that ads will expire in about 1 hour after the loaded callback.  Meaning, you may <i>cache</i> an ad 
  in your app or game, but must be displayed with the alloted hour.
</blockquote>
 
<h2>Sample Project</h2>
<b>
All of this and more, such as <i>Preroll Ads</i> can be seen in the sample <a href="https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/src/main/java/com/freestar/android/sample/MainActivity.java">MainActivity</a>:
</b>

https://github.com/freestarcapital/SDK_documentation_Android/blob/master/app/src/main/java/com/freestar/android/sample/MainActivity.java



