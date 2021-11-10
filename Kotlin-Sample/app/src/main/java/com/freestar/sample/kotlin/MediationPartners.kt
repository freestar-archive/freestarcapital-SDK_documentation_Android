package com.freestar.sample.kotlin

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.freestar.android.ads.AdRequest
import com.freestar.android.ads.LVDOConstants.PARTNER
import java.util.*

/**
 * Only for testing purposes!
 */
object MediationPartners {
    private const val numInterstitial = 14
    private const val numRewarded = 13
    private const val numInview = 13
    private const val numPreroll = 2
    const val ADTYPE_INTERSTITIAL = 0
    const val ADTYPE_REWARDED = 1
    const val ADTYPE_BANNER = 2
    const val ADTYPE_PREROLL = 3

    /**
     * INTERSTITIAL
     */
    private val interstitial_partners = arrayOfNulls<String>(numInterstitial)
    private val interstitial_parters_selected = BooleanArray(numInterstitial)
    private fun setInterstitialPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(numInterstitial)
        for (i in 0 until numInterstitial) {
            if (interstitial_parters_selected[i]) {
                list.add(PARTNER.valueOf(interstitial_partners[i]!!))
            }
        }
        adRequest.partnerNames = list
        return list
    }

    /**
     * REWARDED
     */
    private val rewarded_partners = arrayOfNulls<String>(numRewarded)
    private val rewarded_parters_selected = BooleanArray(numRewarded)
    private fun setRewardedPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(numRewarded)
        for (i in 0 until numRewarded) {
            if (rewarded_parters_selected[i]) {
                list.add(PARTNER.valueOf(rewarded_partners[i]!!))
            }
        }
        adRequest.partnerNames = list
        return list
    }

    /**
     * NATIVE INVIEW
     */
    private val inview_partners = arrayOfNulls<String>(numInview)
    private val inview_parters_selected = BooleanArray(numInview)
    private fun setInviewPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(numInview)
        for (i in 0 until numInview) {
            if (inview_parters_selected[i]) {
                list.add(PARTNER.valueOf(inview_partners[i]!!))
            }
        }
        adRequest.partnerNames = list
        return list
    }

    /**
     * PRE-ROLL
     */
    private val preroll_partners = arrayOfNulls<String>(numPreroll)
    private val preroll_parters_selected = BooleanArray(numPreroll)
    private fun setPrerollPartners(adRequest: AdRequest): List<PARTNER> {
        val list: MutableList<PARTNER> = ArrayList(numPreroll)
        for (i in 0 until numPreroll) {
            if (preroll_parters_selected[i]) {
                list.add(PARTNER.valueOf(preroll_partners[i]!!))
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
    @JvmStatic
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
            partners = interstitial_partners
            selected = interstitial_parters_selected
            title = "Interstitial"
        } else if (adUnitType == ADTYPE_REWARDED) {
            partners = rewarded_partners
            selected = rewarded_parters_selected
            title = "Rewarded"
        } else if (adUnitType == ADTYPE_BANNER) {
            partners = inview_partners
            selected = inview_parters_selected
            title = "Display"
        } else {
            partners = preroll_partners
            selected = preroll_parters_selected
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
                    else -> {
                    }
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
            partners = interstitial_partners
            selected = interstitial_parters_selected
            total = numInterstitial
            chosen = arrayOfNulls(numInterstitial)
        } else if (adUnitType == ADTYPE_REWARDED) {
            partners = rewarded_partners
            selected = rewarded_parters_selected
            total = numRewarded
            chosen = arrayOfNulls(numRewarded)
        } else if (adUnitType == ADTYPE_BANNER) {
            partners = inview_partners
            selected = inview_parters_selected
            total = numInview
            chosen = arrayOfNulls(numInview)
        } else {
            partners = preroll_partners
            selected = preroll_parters_selected
            total = numPreroll
            chosen = arrayOfNulls(numPreroll)
        }
        var j = 0
        for (i in 0 until total) {
            if (selected[i]) {
                chosen[j++] = partners[i]
            }
        }
        return chosen
    }

    init {
        interstitial_partners[0] = PARTNER.TAM.name
        interstitial_partners[1] = PARTNER.ADCOLONY.name
        interstitial_partners[2] = PARTNER.APPLOVIN.name
        interstitial_partners[3] = PARTNER.CRITEO.name
        interstitial_partners[4] = PARTNER.FACEBOOK.name
        interstitial_partners[5] = PARTNER.GOOGLEADMOB.name
        interstitial_partners[6] = PARTNER.GOOGLE.name
        interstitial_partners[7] = PARTNER.MOPUB.name
        interstitial_partners[8] = PARTNER.NIMBUS.name
        interstitial_partners[9] = PARTNER.TAPJOY.name
        interstitial_partners[10] = PARTNER.UNITY.name
        interstitial_partners[11] = PARTNER.VUNGLE.name
        interstitial_partners[12] = PARTNER.PANGLE.name
        interstitial_partners[13] = PARTNER.HYPRMX.name
    }

    init {
        for (i in 0 until numInterstitial) {
            interstitial_parters_selected[i] = true
        }
    }

    init {
        rewarded_partners[0] = PARTNER.ADCOLONY.name
        rewarded_partners[1] = PARTNER.APPLOVIN.name
        rewarded_partners[2] = PARTNER.CRITEO.name
        rewarded_partners[3] = PARTNER.FACEBOOK.name
        rewarded_partners[4] = PARTNER.GOOGLEADMOB.name
        rewarded_partners[5] = PARTNER.GOOGLE.name
        rewarded_partners[6] = PARTNER.MOPUB.name
        rewarded_partners[7] = PARTNER.NIMBUS.name
        rewarded_partners[8] = PARTNER.TAPJOY.name
        rewarded_partners[9] = PARTNER.UNITY.name
        rewarded_partners[10] = PARTNER.VUNGLE.name
        rewarded_partners[11] = PARTNER.PANGLE.name
        rewarded_partners[12] = PARTNER.HYPRMX.name
    }

    init {
        for (i in 0 until numRewarded) {
            rewarded_parters_selected[i] = true
        }
    }

    init {
        inview_partners[0] = PARTNER.TAM.name
        inview_partners[1] = PARTNER.ADCOLONY.name
        inview_partners[2] = PARTNER.APPLOVIN.name
        inview_partners[3] = PARTNER.CRITEO.name
        inview_partners[4] = PARTNER.FACEBOOK.name
        inview_partners[5] = PARTNER.GOOGLEADMOB.name
        inview_partners[6] = PARTNER.GOOGLE.name
        inview_partners[7] = PARTNER.MOPUB.name
        inview_partners[8] = PARTNER.NIMBUS.name
        inview_partners[9] = PARTNER.UNITY.name
        inview_partners[10] = PARTNER.PANGLE.name
        inview_partners[11] = PARTNER.VUNGLE.name
        inview_partners[12] = PARTNER.HYPRMX.name
    }

    init {
        for (i in 0 until numInview) {
            inview_parters_selected[i] = true
        }
    }

    init {
        preroll_partners[0] = PARTNER.GOOGLE.name
        preroll_partners[1] = PARTNER.NIMBUS.name
    }

    init {
        for (i in 0 until numPreroll) {
            preroll_parters_selected[i] = true
        }
    }
}