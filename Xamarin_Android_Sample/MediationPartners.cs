using Android.Content;
using Com.Freestar.Android.Ads;

namespace fs_sample
{
    public class MediationPartners
    {
        private static int NUM_INTERSTITIAL = 9;
        private static int NUM_REWARDED = 9;
        private static int NUM_BANNER = 8;

        private static string[] rewarded_partners = new string[NUM_REWARDED];
        private static string[] interstitial_partners = new string[NUM_INTERSTITIAL];
        private static string[] banner_partners = new string[NUM_BANNER];

        private static bool[] rewardedSelected = new bool[NUM_REWARDED];
        private static bool[] interstitialSelected = new bool[NUM_INTERSTITIAL];
        private static bool[] bannerSelected = new bool[NUM_BANNER];

        private static bool isInitialized;

        public static void init()
        {
            rewarded_partners[0] = LVDOConstants.PARTNER.Amazon.Name();
            rewarded_partners[1] = LVDOConstants.PARTNER.Adcolony.Name();
            rewarded_partners[2] = LVDOConstants.PARTNER.Applovin.Name();
            rewarded_partners[3] = LVDOConstants.PARTNER.Criteo.Name();
            rewarded_partners[4] = LVDOConstants.PARTNER.Chocolate.Name();
            rewarded_partners[5] = LVDOConstants.PARTNER.Facebook.Name();
            rewarded_partners[6] = LVDOConstants.PARTNER.Googleadmob.Name();
            rewarded_partners[7] = LVDOConstants.PARTNER.Google.Name();
            //rewarded_partners[8] = LVDOConstants.PARTNER.Mopub.Name();
            //rewarded_partners[9] = LVDOConstants.PARTNER.Tapjoy.Name();
            rewarded_partners[8] = LVDOConstants.PARTNER.Unity.Name();
            //rewarded_partners[11] = LVDOConstants.PARTNER.Vungle.Name();

            interstitial_partners[0] = LVDOConstants.PARTNER.Amazon.Name();
            interstitial_partners[1] = LVDOConstants.PARTNER.Adcolony.Name();
            interstitial_partners[2] = LVDOConstants.PARTNER.Applovin.Name();
            interstitial_partners[3] = LVDOConstants.PARTNER.Criteo.Name();
            interstitial_partners[4] = LVDOConstants.PARTNER.Chocolate.Name();
            interstitial_partners[5] = LVDOConstants.PARTNER.Facebook.Name();
            interstitial_partners[6] = LVDOConstants.PARTNER.Googleadmob.Name();
            interstitial_partners[7] = LVDOConstants.PARTNER.Google.Name();
            //interstitial_partners[8] = LVDOConstants.PARTNER.Mopub.Name();
            //interstitial_partners[9] = LVDOConstants.PARTNER.Tapjoy.Name();
            interstitial_partners[8] = LVDOConstants.PARTNER.Unity.Name();
            //interstitial_partners[11] = LVDOConstants.PARTNER.Vungle.Name();

            banner_partners[0] = LVDOConstants.PARTNER.Amazon.Name();
            banner_partners[1] = LVDOConstants.PARTNER.Applovin.Name();
            banner_partners[2] = LVDOConstants.PARTNER.Criteo.Name();
            banner_partners[3] = LVDOConstants.PARTNER.Chocolate.Name();
            banner_partners[4] = LVDOConstants.PARTNER.Facebook.Name();
            banner_partners[5] = LVDOConstants.PARTNER.Googleadmob.Name();
            banner_partners[6] = LVDOConstants.PARTNER.Google.Name();
            //banner_partners[7] = LVDOConstants.PARTNER.Mopub.Name();
            banner_partners[7] = LVDOConstants.PARTNER.Unity.Name();

            for (int i=0; i < NUM_BANNER;i++)
            {
                bannerSelected[i] = true;
            }
            for (int i=0; i < NUM_REWARDED;i++)
            {
                rewardedSelected[i] = true;
            }
            for (int i = 0; i < NUM_INTERSTITIAL; i++)
            {
                interstitialSelected[i] = true;
            }
        }

        public static void setInterstitialPartners(AdRequest adRequest)
        {
            if (adRequest.PartnerNames != null)
            {
                adRequest.PartnerNames.Clear();
            }
            for (int i=0; i < NUM_INTERSTITIAL;i++)
            {
                if (interstitialSelected[i])
                {
                    adRequest.AddPartnerName(LVDOConstants.PARTNER.ValueOf(interstitial_partners[i]));
                }
            }
        }

        public static void setRewardedPartners(AdRequest adRequest)
        {
            if (adRequest.PartnerNames != null)
            {
                adRequest.PartnerNames.Clear();
            }
            for (int i = 0; i < NUM_REWARDED; i++)
            {
                if (rewardedSelected[i])
                {
                    adRequest.AddPartnerName(LVDOConstants.PARTNER.ValueOf(rewarded_partners[i]));
                }
            }
        }

        public static void setBannerPartners(AdRequest adRequest)
        {
            if (adRequest.PartnerNames != null)
            {
                adRequest.PartnerNames.Clear();
            }
            for (int i = 0; i < NUM_BANNER; i++)
            {
                if (bannerSelected[i])
                {
                    adRequest.AddPartnerName(LVDOConstants.PARTNER.ValueOf(banner_partners[i]));
                }
            }
        }

        public static void choosePartners(Context context, AdRequest adRequest, int adUnitType, IDialogInterfaceOnClickListener listener)
        {

            if (!isInitialized)
            {
                isInitialized = true;
                init();
            }
            string[] partners;
            bool[] selected;
            string title;
            if (adUnitType == MainActivity.ADTYPE_INTERSTITIAL)
            {
                partners = interstitial_partners;
                selected = interstitialSelected;
                title = "Interstitial";
            }
            else if (adUnitType == MainActivity.ADTYPE_REWARDED)
            {
                partners = rewarded_partners;
                selected = rewardedSelected;
                title = "Rewarded";
            }
            else if (adUnitType == MainActivity.ADTYPE_BANNER)
            {
                partners = banner_partners;
                selected = bannerSelected;
                title = "Banner";
            } else
            {
                partners = banner_partners;
                selected = bannerSelected;
                title = "MREC";
            }
            Android.App.AlertDialog.Builder dialog = new Android.App.AlertDialog.Builder(context);
            OnMultiClickListener multiListener = new OnMultiClickListener(selected);
            dialog.SetTitle(title);
            dialog.SetMultiChoiceItems(partners, selected, multiListener);
            dialog.SetNegativeButton("CANCEL", new DummyListener());
            dialog.SetPositiveButton("OK", new OnOkClickListener(adUnitType, adRequest, listener));
            dialog.Show();
        }

    }

}