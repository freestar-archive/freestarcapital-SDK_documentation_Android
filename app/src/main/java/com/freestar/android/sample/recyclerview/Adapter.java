package com.freestar.android.sample.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freestar.android.ads.BannerAd;
import com.freestar.android.sample.R;
import com.freestar.android.sample.databinding.AdItemBinding;
import com.freestar.android.sample.databinding.ItemBinding;

import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "KevinAdapter";
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_AD = 1;

    public Adapter(Context context) {
    }

    private ArrayList<ItemWrapper> itemWrappers = new ArrayList<>();

    public void setItemWrappers(ArrayList<ItemWrapper> itemWrappers) {
        this.itemWrappers = itemWrappers;
        notifyItemRangeInserted(0, itemWrappers.size());
    }

    public void insertAd(int position, View adView, BannerAd bannerAd) {
        Log.d(TAG, "insertAd");
        cleanUp();
        ItemWrapper itemWrapper = new ItemWrapper(adView, bannerAd);
        itemWrappers.add(position, itemWrapper);
        notifyItemInserted(position);
    }

    public void cleanUp() {
        int adPos = getAdPosition();
        if (adPos != -1) {
            ItemWrapper removed = itemWrappers.remove(adPos);
            notifyItemRemoved(adPos);
            removed.bannerAd.destroyView();
        }
    }

    public int getAdPosition() {
        for (int i = 0; i < itemWrappers.size(); i++) {
            ItemWrapper itemWrapper = itemWrappers.get(i);
            if (itemWrapper.type == TYPE_AD) {
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
        if (viewType == TYPE_NORMAL) {
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
        if (itemWrapper.type == TYPE_NORMAL) {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.binding.setItem(itemWrapper.item);
            //itemHolder.binding.image.getLayoutParams().height = (int) (SCREEN_HEIGHT * Config.CARD_HEIGHT_PERCENTAGE_OF_SCREEN);

        } else {
            AdHolder adHolder = (AdHolder) holder;
            adHolder.binding.adContainer.removeAllViews();
            ViewGroup parent = (ViewGroup) itemWrapper.adView.getParent();
            if (parent != null) {
                parent.removeView(itemWrapper.adView);
            }
            adHolder.binding.adContainer.addView(itemWrapper.adView);
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
            this.type = TYPE_NORMAL;
            this.item = item;
        }

        ItemWrapper(View adView, BannerAd bannerAd) {
            type = TYPE_AD;
            this.adView = adView;
            this.bannerAd = bannerAd;
        }

        int type;
        Item item;
        View adView;
        BannerAd bannerAd;
    }
}
