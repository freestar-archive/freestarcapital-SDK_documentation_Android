package com.freestar.sample.kotlin

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freestar.android.ads.*
import com.freestar.sample.kotlin.Adapter.ItemWrapper
import com.freestar.sample.kotlin.MediationPartners.choosePartners
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class RecyclerViewInfiniteAdsActivity : AppCompatActivity(), BannerAdListener, NativeAdListener {
    private var adapter: Adapter? = null
    private var lm: LinearLayoutManager? = null
    private var bannerAd: BannerAd? = null
    private var nativeAd: NativeAd? = null
    private var isAdRequestInProgress = false
    private var scrollDy = 0
    private var adRequest: AdRequest? = null
    private var flip = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adRequest = AdRequest(this)
        adapter = Adapter(this, true)
        DataBindingUtil.setContentView<ViewDataBinding>(this, R.layout.activity_recyclerview)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this).also { lm = it }
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Log.d(TAG, "SCROLL_STATE_IDLE")
                    val adPos = adapter!!.adPosition
                    val vis =
                        if (scrollDy >= 0) lm!!.findLastVisibleItemPosition() else lm!!.findFirstVisibleItemPosition()
                    if (Math.abs(vis - adPos) >= Config.AD_DISTANCE) {
                        requestAd()
                        Log.d(TAG, "REQUEST NEW AD")
                    } else {
                        Log.d(TAG, "SCROLL_STATE_IDLE BUT DON'T REQUEST NEW AD")
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.d(TAG, "scrolled dy: $dy")
                /**
                 * A negative dy value means the user is scrolling in the upwards direction
                 * and a positive dy value means the user is scrolling downwards.
                 */
                scrollDy = dy
            }
        })
        loadData()
        title = "RecyclerView Infinite Ads"
    }

    private fun loadData() {
        object : AsyncTask<Void?, Void?, List<Item>?>() {
            override fun doInBackground(vararg p0: Void?): List<Item>? {
                return try {
                    Arrays.asList(
                        *Gson().fromJson(
                            InputStreamReader(
                                this@RecyclerViewInfiniteAdsActivity.assets.open(
                                    "media_list.json"
                                )
                            ), Array<Item>::class.java
                        )
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }

            override fun onPostExecute(items: List<Item>?) {
                val list = ArrayList<ItemWrapper>(
                    items!!.size
                )
                for (item in items) {
                    list.add(ItemWrapper(item))
                }
                adapter!!.setItemWrappers(list)
                requestAd()
            }

        }.execute()
    }

    private fun requestAd() {
        isAdRequestInProgress = if (isAdRequestInProgress) {
            Log.d(
                TAG,
                "requestAd (don't do; already in progress)"
            )
            return
        } else {
            Log.d(TAG, "requestAd")
            true
        }
        if (!flip) {
            flip = true
            bannerAd = BannerAd(this)
            bannerAd!!.setBannerAdListener(this)
            bannerAd!!.adSize = AdSize.MEDIUM_RECTANGLE_300_250
            bannerAd!!.loadAd(adRequest)
        } else {
            flip = false
            nativeAd = NativeAd(this)
            nativeAd!!.template = NativeAd.TEMPLATE_MEDIUM
            nativeAd!!.setNativeAdListener(this)
            nativeAd!!.loadAd(adRequest)
        }
    }

    override fun onBannerAdLoaded(banner: View, placement: String?) {
        Log.d(TAG, "onBannerAdLoaded")
        isAdRequestInProgress = false
        if (banner != null) {
            var vis =
                if (scrollDy >= 0) lm!!.findLastVisibleItemPosition() else lm!!.findFirstVisibleItemPosition()
            val adPos = adapter!!.adPosition
            if (adPos != -1) {
                if (Math.abs(vis - adPos) < Config.AD_DISTANCE) {
                    Log.d(
                        TAG,
                        "onBannerAdLoaded but there's already an ad nearby.  dont ad to list"
                    )
                    return  //there's already an ad nearby in the list
                }
            }
            vis = if (vis < Config.AD_DISTANCE) Config.AD_DISTANCE else vis
            Log.d(TAG, "onBannerAdLoaded and inserted")
            adapter!!.cleanupLastAd()
            adapter!!.insertAd(vis, banner as BannerAd, null)
        }
    }

    override fun onBannerAdFailed(view: View, placement: String?, errorCode: Int) {
        Log.d(TAG, "onBannerAdFailed: $errorCode")
        isAdRequestInProgress = false
    }

    override fun onBannerAdClicked(view: View, placement: String?) {
        Log.d(TAG, "onBannerAdClicked")
    }

    override fun onBannerAdClosed(view: View, placement: String?) {
        Log.d(TAG, "onBannerAdClosed")
        isAdRequestInProgress = false
    }

    override fun onNativeAdLoaded(nativeAdView: View, placement: String?) {
        Log.d(TAG, "onNativeAdLoaded")
        isAdRequestInProgress = false
        if (nativeAdView != null) {
            var vis =
                if (scrollDy >= 0) lm!!.findLastVisibleItemPosition() else lm!!.findFirstVisibleItemPosition()
            val adPos = adapter!!.adPosition
            if (adPos != -1) {
                if (Math.abs(vis - adPos) < Config.AD_DISTANCE) {
                    Log.d(
                        TAG,
                        "onNativeAdLoaded but there's already an ad nearby.  dont ad to list"
                    )
                    return  //there's already an ad nearby in the list
                }
            }
            vis = if (vis < Config.AD_DISTANCE) Config.AD_DISTANCE else vis
            Log.d(TAG, "onNativeAdLoaded and inserted")
            adapter!!.cleanupLastAd()
            adapter!!.insertAd(vis, null, nativeAdView as NativeAd)
        }
    }

    override fun onNativeAdFailed(placement: String?, errorCode: Int) {
        Log.d(TAG, "onNativeAdFailed: $errorCode")
        isAdRequestInProgress = false
    }

    override fun onNativeAdClicked(placement: String?) {}
    override fun onDestroy() {
        super.onDestroy()
        adapter!!.cleanupAll()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.choose_partners, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_choose_partners -> {
                choosePartners()
                true
            }
            R.id.menu_show_current_winner -> {
                if (bannerAd != null) {
                    AlertDialog.Builder(this)
                        .setMessage("Last Winning Partner: " + bannerAd!!.winningPartnerName).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun choosePartners() {
        choosePartners(this, adRequest!!, MediationPartners.ADTYPE_BANNER) { dialog, which ->
            //not implemented
        }
    }

    companion object {
        private const val TAG = "RecyclerView"
    }
}