using Android.Content;
using Com.Freestar.Android.Ads;

namespace fs_sample
{
    public class OnOkClickListener : Java.Lang.Object, IDialogInterfaceOnClickListener
    {

        private int adType;
        private AdRequest adRequest;
        private IDialogInterfaceOnClickListener listener;

        public OnOkClickListener(int adType, AdRequest adRequest, IDialogInterfaceOnClickListener listener)
        {
            this.adType = adType;
            this.adRequest = adRequest;
            this.listener = listener;
        }

        public void OnClick(IDialogInterface dialog, int which)
        {
            switch (adType)
            {
                case MainActivity.ADTYPE_REWARDED:
                    MediationPartners.setRewardedPartners(adRequest);
                    break;
                case MainActivity.ADTYPE_INTERSTITIAL:
                    MediationPartners.setInterstitialPartners(adRequest);
                    break;
                case MainActivity.ADTYPE_BANNER:
                case MainActivity.ADTYPE_MREC_BANNER:
                    MediationPartners.setBannerPartners(adRequest);
                    break;
                default:
                    break;
                    
            }
            listener.OnClick(dialog, adType);
        }

    }
}