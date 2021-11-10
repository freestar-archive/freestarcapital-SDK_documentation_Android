using Android.App;
using Android.Content;
using Android.OS;
using Android.Support.V7.App;
using Android.Runtime;
using Android.Widget;
using Com.Freestar.Android.Ads;
using Android.Views;

namespace fs_sample
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity, IRewardedAdListener, IBannerAdListener, IInterstitialAdListener, IDialogInterfaceOnClickListener
    {
        //const string FREESTAR_API_KEY = "dweT4z";
        const string FREESTAR_API_KEY = "XqjhRR";
        protected Button loadInterstitialButton, loadRewardedButton, bannerButton, mrecButton;
        private ViewGroup bannerAdContainer, mrecAdContainer;
        private InterstitialAd interstitialAd;
        private RewardedAd rewardedAd;
        private BannerAd bannerAd;
        private BannerAd banner_MREC_Ad;
        private AdRequest adRequest;

        public const int ADTYPE_INTERSTITIAL = 0;
        public const int ADTYPE_REWARDED = 1;
        public const int ADTYPE_BANNER = 2;
        public const int ADTYPE_MREC_BANNER = 3;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.activity_main);
            FreeStarAds.EnableTestAds(true);
            FreeStarAds.EnableLogging(true);
            FreeStarAds.Init(this, FREESTAR_API_KEY);
            adRequest = new AdRequest(this);

            bannerAdContainer = FindViewById<ViewGroup>(Resource.Id.bannerAdContainer);
            mrecAdContainer = FindViewById<ViewGroup>(Resource.Id.mrecAdContainer);

            loadInterstitialButton = FindViewById<Button>(Resource.Id.load_interstitial_button);
            loadInterstitialButton.SetOnClickListener(new OnClickListener(this, ADTYPE_INTERSTITIAL, adRequest));
            loadRewardedButton = FindViewById<Button>(Resource.Id.load_rewarded_button);
            loadRewardedButton.SetOnClickListener(new OnClickListener(this, ADTYPE_REWARDED, adRequest));
            bannerButton = FindViewById<Button>(Resource.Id.load_banner_button);
            bannerButton.SetOnClickListener(new OnClickListener(this, ADTYPE_BANNER, adRequest));
            mrecButton = FindViewById<Button>(Resource.Id.load_mrec_button);
            mrecButton.SetOnClickListener(new OnClickListener(this, ADTYPE_MREC_BANNER, adRequest));
            
            rewardedAd = new RewardedAd(this, this);
            interstitialAd = new InterstitialAd(this, this);
            bannerAd = new BannerAd(this);
            bannerAd.AdSize = AdSize.Banner32050;
            bannerAd.SetBannerAdListener(this);

            banner_MREC_Ad = new BannerAd(this);
            banner_MREC_Ad.AdSize = AdSize.MediumRectangle300250;
            banner_MREC_Ad.SetBannerAdListener(this);
   
        }
        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }


        public void OnRewardedVideoLoaded(string placement)
        {
            rewardedAd.ShowRewardAd("qj5ebyZ0F0vzW6yg", "SomeUser", "coin", "30");
        }

        public void OnRewardedVideoFailed(string placement, int errorCode)
        {

        }

        public void OnRewardedVideoShown(string placement)
        {

        }

        public void OnRewardedVideoShownError(string placement, int errorcode)
        {

        }

        public void OnRewardedVideoDismissed(string placement)
        {

        }

        public void OnRewardedVideoCompleted(string placement)
        {

        }

        // The interstitial has been cached and is ready to be shown.
        public void OnInterstitialLoaded(string placement)
        {
            interstitialAd.Show();
        }

        // The interstitial has failed to load. Inspect errorCode for additional information.
        public void OnInterstitialFailed(string placement, int errorCode)
        {

        }

        // The interstitial has been shown. Pause / save state accordingly.
        public void OnInterstitialShown(string placement)
        {

        }

        // The interstitial has been clicked and take actions accordingly.
        public void OnInterstitialClicked(string placement)
        {

        }

        // The interstitial has being dismissed. Resume / load state accordingly.
        public void OnInterstitialDismissed(string placement)
        {

        }

        // Banner has successfully loaded.
        public void OnBannerAdLoaded(View banner, string placement)
        {
            BannerAd ad = (BannerAd)banner;
            if (ad.AdSize == AdSize.Banner32050)
            {
                bannerAdContainer.RemoveAllViews();
                bannerAdContainer.AddView(banner);
            } else
            {
                mrecAdContainer.RemoveAllViews();
                mrecAdContainer.AddView(banner);
            }
        }

        // Banner has failed to retrieve an ad.
        public void OnBannerAdFailed(View banner, string placement, int errorCode)
        {

        }

        // The user has tapped on the banner.
        public void OnBannerAdClicked(View banner, string placement)
        {

        }

        // Banner is closed back.
        public void OnBannerAdClosed(View banner, string placement)
        {

        }

        public void OnClick(IDialogInterface var1, int which)
        {
            if (which == ADTYPE_INTERSTITIAL)
            {
                interstitialAd.LoadAd(adRequest);
            }
            else if (which == ADTYPE_REWARDED)
            {
                rewardedAd.LoadAd(adRequest);
            }
            else if (which == ADTYPE_BANNER)
            {
                bannerAd.LoadAd(adRequest);
            } 
            else if (which == ADTYPE_MREC_BANNER)
            {
                banner_MREC_Ad.LoadAd(adRequest);
            }
        }

        protected override void OnPause()
        {
            bannerAd.OnPause();
            banner_MREC_Ad.OnPause();
            base.OnPause();
        }

        protected override void OnResume()
        {
            bannerAd.OnResume();
            banner_MREC_Ad.OnResume();
            base.OnResume();
        }

        protected override void OnDestroy()
        {
            bannerAd.DestroyView();
            banner_MREC_Ad.DestroyView();
            base.OnDestroy();
        }

        class OnClickListener : Java.Lang.Object, View.IOnClickListener
        {
            MainActivity that;
            int adType;
            AdRequest adRequest;

            public OnClickListener(MainActivity t, int adType, AdRequest adRequest)
            {
                that = t;
                this.adType = adType;
                this.adRequest = adRequest;
            }

            public void OnClick(View v)
            {
                MediationPartners.choosePartners(that, adRequest, adType, that);
            }
        }
    }

}