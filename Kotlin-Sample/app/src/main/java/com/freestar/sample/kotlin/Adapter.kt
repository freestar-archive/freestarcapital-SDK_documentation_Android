package com.freestar.sample.kotlin

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.freestar.sample.kotlin.Adapter.ItemWrapper
import com.freestar.android.ads.BannerAd
import com.freestar.sample.kotlin.Adapter.AdHolder
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.freestar.android.ads.NativeAd
import com.freestar.sample.kotlin.Adapter.ItemHolder
import com.freestar.sample.kotlin.databinding.AdItemBinding
import com.freestar.sample.kotlin.databinding.ItemBinding
import java.util.ArrayList

class Adapter
/**
 *
 * @param context
 * @param destroyRecycledAds - recyclerviews with infinite ads must pass true
 * recyclerviews with a fixed number of ads must pass false
 */(context: Context?, private val destroyRecycledAds: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemWrappers = ArrayList<ItemWrapper>()
    fun setItemWrappers(itemWrappers: ArrayList<ItemWrapper>) {
        this.itemWrappers = itemWrappers
        notifyItemRangeInserted(0, itemWrappers.size)
    }

    fun insertAd(position: Int, bannerAd: BannerAd?, nativeAd: NativeAd?) {
        Log.d(TAG, "insertAd")
        val itemWrapper: ItemWrapper
        itemWrapper = bannerAd?.let { ItemWrapper(it) } ?: ItemWrapper(nativeAd)
        itemWrappers.add(position, itemWrapper)
        notifyItemInserted(position)
    }

    fun cleanupAll() {
        for (wrapper in itemWrappers) {
            if (wrapper.type == TYPE_NATIVE_AD) {
                wrapper.nativeAd!!.destroyView()
            } else if (wrapper.type == TYPE_BANNER_AD) {
                wrapper.bannerAd!!.destroyView()
            }
        }
    }

    fun cleanupLastAd() {
        val adPos = adPosition
        if (adPos != -1 && destroyRecycledAds) {
            val removed = itemWrappers.removeAt(adPos)
            notifyItemRemoved(adPos)
            if (removed.type == TYPE_BANNER_AD) removed.bannerAd!!.destroyView() else if (removed.type == TYPE_NATIVE_AD) removed.nativeAd!!.destroyView()
        }
    }

    val adPosition: Int
        get() {
            for (i in itemWrappers.indices) {
                val itemWrapper = itemWrappers[i]
                if (itemWrapper.type == TYPE_BANNER_AD || itemWrapper.type == TYPE_NATIVE_AD) {
                    return i
                }
            }
            return -1
        }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is AdHolder) {
            //AdHolder adHolder = (AdHolder) holder;
            Log.d(TAG, "onViewRecycled holder: $holder")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_NORMAL_CONTENT) {
            val binding: ItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item,
                parent,
                false
            )
            ItemHolder(binding)
        } else {
            val binding: AdItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.ad_item,
                parent,
                false
            )
            AdHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemWrappers[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemWrapper = itemWrappers[position]
        if (itemWrapper.type == TYPE_NORMAL_CONTENT) {
            val itemHolder = holder as ItemHolder
            itemHolder.binding.item = itemWrapper.item
            //itemHolder.binding.image.getLayoutParams().height = (int) (SCREEN_HEIGHT * Config.CARD_HEIGHT_PERCENTAGE_OF_SCREEN);
        } else if (itemWrapper.type == TYPE_BANNER_AD) {
            val adHolder = holder as AdHolder
            adHolder.binding.adContainer.removeAllViews()
            //val adParent = itemWrapper.bannerAd!!.parent as ViewGroup
            //adParent?.removeAllViews()
            adHolder.binding.adContainer.addView(itemWrapper.bannerAd)
        } else if (itemWrapper.type == TYPE_NATIVE_AD) {
            val adHolder = holder as AdHolder
            adHolder.binding.adContainer.removeAllViews()
            //val adParent = itemWrapper.nativeAd!!.parent as ViewGroup
            //adParent?.removeAllViews()
            adHolder.binding.adContainer.addView(itemWrapper.nativeAd)
        }
    }

    override fun getItemCount(): Int {
        return itemWrappers.size
    }

    internal class ItemHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    internal class AdHolder(val binding: AdItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    class ItemWrapper {
        constructor(item: Item?) {
            type = TYPE_NORMAL_CONTENT
            this.item = item
        }

        constructor(bannerAd: BannerAd?) {
            type = TYPE_BANNER_AD
            this.bannerAd = bannerAd
        }

        constructor(nativeAd: NativeAd?) {
            type = TYPE_NATIVE_AD
            this.nativeAd = nativeAd
        }

        var type: Int
        var item: Item? = null
        var bannerAd: BannerAd? = null
        var nativeAd: NativeAd? = null
    }

    companion object {
        private const val TAG = "Adapter"
        private const val TYPE_NORMAL_CONTENT = 0
        private const val TYPE_BANNER_AD = 1
        private const val TYPE_NATIVE_AD = 2
    }
}