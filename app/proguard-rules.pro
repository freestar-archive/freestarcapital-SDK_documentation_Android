# ProGuard rules for FreeStar Ads Mediation SDK

-dontwarn android.app.Activity

# For communication with AdColony's WebView
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Keep filenames and line numbers for stack traces
-keepattributes SourceFile,LineNumberTable

# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface

# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}

-keep class androidx.** {*;}
-keep interface androidx.** {*;}
-dontwarn androidx.**

-keep class android.** {*;}
-keep interface android.** {*;}
-dontwarn android.**

-keep class com.adcolony.** { *; }
-keep interface com.iab.omid.** { *; }
-dontwarn com.iab.omid.**
-dontwarn com.adcolony.**

-keep class com.amazon.** {*;}
-keep interface com.amazon.** {*;}

-keep class com.applovin.** {*;}
-keep interface com.applovin.** {*;}

-keep class com.criteo.** {*;}
-keep interface com.criteo.** {*;}

-keep class com.danikula.** {*;}
-keep interface com.danikula.** {*;}

-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}

-keep interface com.freestar.** {*;}
-keep class com.freestar.** { *; }

-keep interface com.iab.** {*;}
-keep class com.iab.** { *; }

-keep class com.google.** { *; }
-keep interface com.google.** { *; }
-dontwarn com.google.**

-keep class com.mopub.** {*;}
-keep interface com.mopub.** {*;}

-keep class com.tapjoy.** { *; }
-keep interface com.tapjoy.** { *; }
-keep class com.moat.** { *; }
-keep interface com.moat.** { *; }

-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**

-keep class com.unity3d.** {*;}
-keep interface com.unity3d.** {*;}

-keep class com.vungle.** {*;}
-keep interface com.vungle.** {*;}

-keep class okio.** {*;}
-keep interface okio.** {*;}
-dontwarn okio.**

-keep class retrofit2.** {*;}
-keep interface retrofit2.** {*;}
-dontwarn retrofit2.**
