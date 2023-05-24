package com.freestar.sample.kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.freestar.android.ads.AdRequest
import com.freestar.android.ads.LVDOConstants.PARTNER
import java.util.Collections

/**
 * Only for testing purposes!
 */
object MediationPartners {
    const val ADTYPE_INTERSTITIAL = 0
    const val ADTYPE_REWARDED = 1
    const val ADTYPE_BANNER = 2
    const val ADTYPE_PREROLL = 3

    /**
     * INTERSTITIAL
     */
    private val sInterstitialPartners: MutableList<String> = ArrayList(30)
    private val sSelectedInterstitialPartners: BooleanArray

    init {
        sInterstitialPartners.add(PARTNER.ADCOLONY.name)
        sInterstitialPartners.add(PARTNER.APPLOVIN.name)
        sInterstitialPartners.add(PARTNER.APPLOVINMAX.name)
        sInterstitialPartners.add(PARTNER.CRITEO.name)
        sInterstitialPartners.add(PARTNER.GOOGLEADMOB.name)
        sInterstitialPartners.add(PARTNER.GOOGLE.name)
        sInterstitialPartners.add(PARTNER.HYPRMX.name)
        sInterstitialPartners.add(PARTNER.NIMBUS.name)
        sInterstitialPartners.add(PARTNER.PANGLE.name)
        sInterstitialPartners.add(PARTNER.TAM.name)
        sInterstitialPartners.add(PARTNER.TAPJOY.name)
        sInterstitialPartners.add(PARTNER.UNITY.name)
        sInterstitialPartners.add(PARTNER.VUNGLE.name)
        sInterstitialPartners.add(PARTNER.YAHOO.name)
        sInterstitialPartners.add(PARTNER.PREBID.name)
        sInterstitialPartners.add(PARTNER.OGURY.name)
        sInterstitialPartners.add(PARTNER.FYBER.name)
        sInterstitialPartners.add(PARTNER.SMAATO.name)
        Collections.sort(sInterstitialPartners)
        sSelectedInterstitialPartners = BooleanArray(sInterstitialPartners.size)
        for (i in sInterstitialPartners.indices) {
            sSelectedInterstitialPartners[i] = true
        }
    }

    private fun setInterstitialPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(sInterstitialPartners.size)
        for (i in sInterstitialPartners.indices) {
            if (sSelectedInterstitialPartners[i]) {
                list.add(PARTNER.valueOf(sInterstitialPartners[i]))
            }
        }
        adRequest.partnerNames = list
        return list
    }

    /**
     * REWARDED
     */
    private val sRewardedPartners: MutableList<String> = ArrayList(30)
    private val sSelectedRewardedPartners: BooleanArray

    init {
        sRewardedPartners.add(PARTNER.ADCOLONY.name)
        sRewardedPartners.add(PARTNER.APPLOVIN.name)
        sRewardedPartners.add(PARTNER.APPLOVINMAX.name)
        sRewardedPartners.add(PARTNER.CRITEO.name)
        sRewardedPartners.add(PARTNER.GOOGLEADMOB.name)
        sRewardedPartners.add(PARTNER.GOOGLE.name)
        sRewardedPartners.add(PARTNER.NIMBUS.name)
        sRewardedPartners.add(PARTNER.TAPJOY.name)
        sRewardedPartners.add(PARTNER.UNITY.name)
        sRewardedPartners.add(PARTNER.VUNGLE.name)
        sRewardedPartners.add(PARTNER.PANGLE.name)
        sRewardedPartners.add(PARTNER.HYPRMX.name)
        sRewardedPartners.add(PARTNER.PREBID.name)
        sRewardedPartners.add(PARTNER.OGURY.name)
        sRewardedPartners.add(PARTNER.FYBER.name)
        sRewardedPartners.add(PARTNER.SMAATO.name)
        Collections.sort(sRewardedPartners)
        sSelectedRewardedPartners = BooleanArray(sRewardedPartners.size)
        for (i in sRewardedPartners.indices) {
            sSelectedRewardedPartners[i] = true
        }
    }

    private fun setRewardedPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(sRewardedPartners.size)
        for (i in sRewardedPartners.indices) {
            if (sSelectedRewardedPartners[i]) {
                list.add(PARTNER.valueOf(sRewardedPartners[i]))
            }
        }
        adRequest.partnerNames = list
        return list
    }

    /**
     * NATIVE INVIEW
     */
    private val sBannerPartners: MutableList<String> = ArrayList(30)
    private val sSelectedBannerPartners: BooleanArray

    init {
        sBannerPartners.add(PARTNER.TAM.name)
        sBannerPartners.add(PARTNER.ADCOLONY.name)
        sBannerPartners.add(PARTNER.APPLOVIN.name)
        sBannerPartners.add(PARTNER.APPLOVINMAX.name)
        sBannerPartners.add(PARTNER.CRITEO.name)
        sBannerPartners.add(PARTNER.GOOGLEADMOB.name)
        sBannerPartners.add(PARTNER.GOOGLE.name)
        sBannerPartners.add(PARTNER.NIMBUS.name)
        sBannerPartners.add(PARTNER.UNITY.name)
        sBannerPartners.add(PARTNER.PANGLE.name)
        sBannerPartners.add(PARTNER.VUNGLE.name)
        sBannerPartners.add(PARTNER.HYPRMX.name)
        sBannerPartners.add(PARTNER.YAHOO.name)
        sBannerPartners.add(PARTNER.PREBID.name)
        sBannerPartners.add(PARTNER.OGURY.name)
        sBannerPartners.add(PARTNER.FYBER.name)
        sBannerPartners.add(PARTNER.SMAATO.name)
        Collections.sort(sBannerPartners)
        sSelectedBannerPartners = BooleanArray(sBannerPartners.size)
        for (i in sBannerPartners.indices) {
            sSelectedBannerPartners[i] = true
        }
    }

    private fun setInviewPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(sBannerPartners.size)
        for (i in sBannerPartners.indices) {
            if (sSelectedBannerPartners[i]) {
                list.add(PARTNER.valueOf(sBannerPartners[i]))
            }
        }
        adRequest.partnerNames = list
        return list
    }

    /**
     * PRE-ROLL
     */
    private val sPrerollPartners: MutableList<String> = ArrayList(30)
    private val sSelectedPrerollPartners: BooleanArray

    init {
        sPrerollPartners.add(PARTNER.GOOGLE.name)
        sPrerollPartners.add(PARTNER.NIMBUS.name)
        Collections.sort(sPrerollPartners)
        sSelectedPrerollPartners = BooleanArray(sPrerollPartners.size)
        for (i in sPrerollPartners.indices) {
            sSelectedPrerollPartners[i] = true
        }
    }

    private fun setPrerollPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(sPrerollPartners.size)
        for (i in sPrerollPartners.indices) {
            if (sSelectedPrerollPartners[i]) {
                list.add(PARTNER.valueOf(sPrerollPartners[i]))
            }
        }
        adRequest.partnerNames = list
        return list
    }

    /**
     * @param adUnitType
     * @param context
     * @param listener
     */
    fun choosePartners(
        context: Context?,
        adRequest: AdRequest,
        adUnitType: Int,
        listener: DialogInterface.OnClickListener
    ) {
        val partners: Array<String?>
        val selected: BooleanArray
        val title: String
        if (adUnitType == ADTYPE_INTERSTITIAL) {
            //partners = arrayOfNulls(sInterstitialPartners.size)
            //sInterstitialPartners.toArray<String>(partners)
            partners = sInterstitialPartners.toTypedArray()
            selected = sSelectedInterstitialPartners
            title = "Interstitial"
        } else if (adUnitType == ADTYPE_REWARDED) {
            partners = sRewardedPartners.toTypedArray()
            selected = sSelectedRewardedPartners
            title = "Rewarded"
        } else if (adUnitType == ADTYPE_BANNER) {
            partners = sBannerPartners.toTypedArray()
            selected = sSelectedBannerPartners
            title = "Display"
        } else {
            partners = sPrerollPartners.toTypedArray()
            selected = sSelectedPrerollPartners
            title = "Pre-Roll"
        }
        //w/out the listener, even though it does nothing, things don't work so well
        //so we need to pass a listener
        AlertDialog.Builder(context)
            .setMultiChoiceItems(partners, selected) { dialog, which, isChecked -> }
            .setPositiveButton("OK") { dialog, which ->
                when (adUnitType) {
                    ADTYPE_INTERSTITIAL -> setInterstitialPartners(adRequest)
                    ADTYPE_BANNER -> setInviewPartners(adRequest)
                    ADTYPE_PREROLL -> setPrerollPartners(adRequest)
                    ADTYPE_REWARDED -> setRewardedPartners(adRequest)
                    else -> {}
                }
                listener.onClick(dialog, which)
            }
            .setNegativeButton("CANCEL", dummyOnClick)
            .setTitle(title)
            .show()
    }

    private val dummyOnClick = DialogInterface.OnClickListener { dialog, which -> }

    /**
     * @param adUnitType 0:interstitial, 1:rewarded, 2:inview, 3:preroll
     */
    fun getChosenPartners(adUnitType: Int): Array<String?> {
        val partners: Array<String?>
        val selected: BooleanArray
        val chosen: Array<String?>
        val total: Int
        if (adUnitType == ADTYPE_INTERSTITIAL) {
            partners = sInterstitialPartners.toTypedArray()
            selected = sSelectedInterstitialPartners
            total = sInterstitialPartners.size
            chosen = arrayOfNulls(sInterstitialPartners.size)
        } else if (adUnitType == ADTYPE_REWARDED) {
            partners = sRewardedPartners.toTypedArray()
            selected = sSelectedRewardedPartners
            total = sRewardedPartners.size
            chosen = arrayOfNulls(sRewardedPartners.size)
        } else if (adUnitType == ADTYPE_BANNER) {
            partners = sBannerPartners.toTypedArray()
            selected = sSelectedBannerPartners
            total = sBannerPartners.size
            chosen = arrayOfNulls(sBannerPartners.size)
        } else {
            partners = sPrerollPartners.toTypedArray()
            selected = sSelectedPrerollPartners
            total = sPrerollPartners.size
            chosen = arrayOfNulls(sPrerollPartners.size)
        }
        var j = 0
        for (i in 0 until total) {
            if (selected[i]) {
                chosen[j++] = partners[i]
            }
        }
        return chosen
    }
}