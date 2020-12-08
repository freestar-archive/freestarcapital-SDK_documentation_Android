package com.freestar.android.sample.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.freestar.android.ads.BannerAd;
import com.freestar.android.ads.NativeAd;
import com.freestar.android.sample.R;
import com.freestar.android.sample.databinding.AdItemBinding;
import com.freestar.android.sample.databinding.ItemBinding;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "Adapter";
    private static final int TYPE_NORMAL_CONTENT = 0;
    private static final int TYPE_BANNER_AD = 1;
    private static final int TYPE_NATIVE_AD = 2;
    private boolean destroyRecycledAds;

    /**
     *
     * @param context
     * @param destroyRecycledAds - recyclerviews with infinite ads must pass true
     *                           recyclerviews with a fixed number of ads must pass false
     *
     */
    public Adapter(Context context, boolean destroyRecycledAds) {
        this.destroyRecycledAds = destroyRecycledAds;
    }

    private ArrayList<ItemWrapper> itemWrappers = new ArrayList<>();

    public void setItemWrappers(ArrayList<ItemWrapper> itemWrappers) {
        this.itemWrappers = itemWrappers;
        notifyItemRangeInserted(0, itemWrappers.size());
    }

    public void insertAd(int position, BannerAd bannerAd, NativeAd nativeAd) {
        Log.d(TAG, "insertAd");
        ItemWrapper itemWrapper;
        if (bannerAd != null) {
            itemWrapper = new ItemWrapper(bannerAd);
        } else {
            itemWrapper = new ItemWrapper(nativeAd);
        }
        itemWrappers.add(position, itemWrapper);
        notifyItemInserted(position);
    }

    public void cleanupAll() {
        for (ItemWrapper wrapper : itemWrappers) {
            if (wrapper.type == TYPE_NATIVE_AD) {
                wrapper.nativeAd.destroyView();
            } else if (wrapper.type == TYPE_BANNER_AD) {
                wrapper.bannerAd.destroyView();
            }
        }
    }

    public void cleanupLastAd() {
        int adPos = getAdPosition();
        if (adPos != -1 && destroyRecycledAds) {
            ItemWrapper removed = itemWrappers.remove(adPos);
            notifyItemRemoved(adPos);
            if (removed.type == TYPE_BANNER_AD)
                removed.bannerAd.destroyView();
            else if (removed.type == TYPE_NATIVE_AD)
                removed.nativeAd.destroyView();
        }
    }

    public int getAdPosition() {
        for (int i = 0; i < itemWrappers.size(); i++) {
            ItemWrapper itemWrapper = itemWrappers.get(i);
            if (itemWrapper.type == TYPE_BANNER_AD || itemWrapper.type == TYPE_NATIVE_AD) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof AdHolder) {
            //AdHolder adHolder = (AdHolder) holder;
            Log.d(TAG, "onViewRecycled holder: " + holder);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL_CONTENT) {
            ItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item, parent, false);
            return new ItemHolder(binding);
        } else {
            AdItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ad_item, parent, false);
            return new AdHolder(binding);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return itemWrappers.get(position).type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemWrapper itemWrapper = itemWrappers.get(position);
        if (itemWrapper.type == TYPE_NORMAL_CONTENT) {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.binding.setItem(itemWrapper.item);
            //itemHolder.binding.image.getLayoutParams().height = (int) (SCREEN_HEIGHT * Config.CARD_HEIGHT_PERCENTAGE_OF_SCREEN);
        } else if (itemWrapper.type == TYPE_BANNER_AD) {

            AdHolder adHolder = (AdHolder) holder;
            adHolder.binding.adContainer.removeAllViews();

            ViewGroup adParent = (ViewGroup)itemWrapper.bannerAd.getParent();
            if (adParent != null) {
                adParent.removeAllViews();
            }
            adHolder.binding.adContainer.addView(itemWrapper.bannerAd);

        } else if (itemWrapper.type == TYPE_NATIVE_AD) {

            AdHolder adHolder = (AdHolder) holder;
            adHolder.binding.adContainer.removeAllViews();

            ViewGroup adParent = (ViewGroup)itemWrapper.nativeAd.getParent();
            if (adParent != null) {
                adParent.removeAllViews();
            }

            adHolder.binding.adContainer.addView(itemWrapper.nativeAd);
        }
    }

    @Override
    public int getItemCount() {
        return itemWrappers.size();
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        private ItemBinding binding;

        public ItemHolder(ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class AdHolder extends RecyclerView.ViewHolder {
        private AdItemBinding binding;

        public AdHolder(AdItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static class ItemWrapper {
        ItemWrapper(Item item) {
            this.type = TYPE_NORMAL_CONTENT;
            this.item = item;
        }

        ItemWrapper(BannerAd bannerAd) {
            type = TYPE_BANNER_AD;
            this.bannerAd = bannerAd;
        }

        ItemWrapper(NativeAd nativeAd) {
            type = TYPE_NATIVE_AD;
            this.nativeAd = nativeAd;
        }

        int type;
        Item item;
        BannerAd bannerAd;
        NativeAd nativeAd;
    }

}
