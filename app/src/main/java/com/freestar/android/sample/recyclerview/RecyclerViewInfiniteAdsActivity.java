package com.freestar.android.sample.recyclerview;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.freestar.android.ads.AdRequest;
import com.freestar.android.ads.AdSize;
import com.freestar.android.ads.BannerAd;
import com.freestar.android.ads.BannerAdListener;
import com.freestar.android.ads.NativeAd;
import com.freestar.android.ads.NativeAdListener;
import com.freestar.android.sample.MediationPartners;
import com.freestar.android.sample.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewInfiniteAdsActivity extends AppCompatActivity implements BannerAdListener,
        NativeAdListener {

    private static final String TAG = "RecyclerView";

    private Adapter adapter;
    private LinearLayoutManager lm;
    private BannerAd bannerAd;
    private NativeAd nativeAd;
    private boolean isAdRequestInProgress;
    private int scrollDy;
    private AdRequest adRequest;
    private boolean flip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adRequest = new AdRequest(this);

        adapter = new Adapter(this, true);
        DataBindingUtil.setContentView(this, R.layout.activity_recyclerview);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager((lm = new LinearLayoutManager(this)));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Log.d(TAG, "SCROLL_STATE_IDLE");
                    int adPos = adapter.getAdPosition();
                    int vis = scrollDy >= 0 ? lm.findLastVisibleItemPosition() : lm.findFirstVisibleItemPosition();
                    if (Math.abs(vis - adPos) >= Config.AD_DISTANCE) {
                        requestAd();
                        Log.d(TAG, "REQUEST NEW AD");
                    } else {
                        Log.d(TAG, "SCROLL_STATE_IDLE BUT DON'T REQUEST NEW AD");
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "scrolled dy: " + dy);
                /**
                 * A negative dy value means the user is scrolling in the upwards direction
                 * and a positive dy value means the user is scrolling downwards.
                 */
                scrollDy = dy;
            }
        });
        loadData();
        setTitle("RecyclerView Infinite Ads");
    }

    private void loadData() {
        new AsyncTask<Void, Void, List<Item>>() {
            @Override
            protected List<Item> doInBackground(Void... voids) {
                try {
                    return Arrays.asList(new Gson().fromJson(new InputStreamReader(RecyclerViewInfiniteAdsActivity.this.getAssets().open("media_list.json")), Item[].class));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            protected void onPostExecute(List<Item> items) {
                ArrayList<Adapter.ItemWrapper> list = new ArrayList<>(items.size());
                for (Item item : items) {
                    list.add(new Adapter.ItemWrapper(item));
                }
                adapter.setItemWrappers(list);
                requestAd();
            }
        }.execute();
    }

    private void requestAd() {
        if (isAdRequestInProgress) {
            Log.d(TAG, "requestAd (don't do; already in progress)");
            return;
        } else {
            Log.d(TAG, "requestAd");
            isAdRequestInProgress = true;
        }
        if (!flip) {
            flip = true;
            cleanupBannerAd();
            bannerAd = new BannerAd(this);
            bannerAd.setBannerAdListener(this);
            bannerAd.setAdSize(AdSize.MEDIUM_RECTANGLE_300_250);
            bannerAd.loadAd(adRequest);
        } else {
            flip = false;
            cleanupNativeAd();
            nativeAd = new NativeAd(this);
            nativeAd.setTemplate(NativeAd.TEMPLATE_MEDIUM);
            nativeAd.setNativeAdListener(this);
            nativeAd.loadAd(adRequest);
        }
    }

    private void cleanupBannerAd() {
        if (bannerAd != null) {
            bannerAd.destroyView();
            bannerAd = null;
        }
    }
    private void cleanupNativeAd() {
        if (nativeAd != null) {
            nativeAd.destroyView();
            nativeAd = null;
        }
    }

    @Override
    public void onBannerAdLoaded(View banner, String placement) {
        Log.d(TAG, "onBannerAdLoaded");
        isAdRequestInProgress = false;
        if (banner != null) {
            int vis = scrollDy >= 0 ? lm.findLastVisibleItemPosition() : lm.findFirstVisibleItemPosition();

            int adPos = adapter.getAdPosition();
            if (adPos != -1) {
                if (Math.abs(vis - adPos) < Config.AD_DISTANCE) {
                    Log.d(TAG, "onBannerAdLoaded but there's already an ad nearby.  dont ad to list");
                    return;//there's already an ad nearby in the list
                }
            }
            vis = vis < Config.AD_DISTANCE ? Config.AD_DISTANCE : vis;
            Log.d(TAG, "onBannerAdLoaded and inserted");
            adapter.cleanupLastAd();
            adapter.insertAd(vis, (BannerAd)banner, null);
        }
    }

    @Override
    public void onBannerAdFailed(View view, String placement, int errorCode) {
        Log.d(TAG, "onBannerAdFailed: " + errorCode);
        isAdRequestInProgress = false;
    }

    @Override
    public void onBannerAdClicked(View view, String placement) {
        Log.d(TAG, "onBannerAdClicked");
    }

    @Override
    public void onBannerAdClosed(View view, String placement) {
        Log.d(TAG, "onBannerAdClosed");
        isAdRequestInProgress = false;
    }

    @Override
    public void onNativeAdLoaded(View nativeAdView, String placement) {
        Log.d(TAG, "onNativeAdLoaded");
        isAdRequestInProgress = false;
        if (nativeAdView != null) {
            int vis = scrollDy >= 0 ? lm.findLastVisibleItemPosition() : lm.findFirstVisibleItemPosition();

            int adPos = adapter.getAdPosition();
            if (adPos != -1) {
                if (Math.abs(vis - adPos) < Config.AD_DISTANCE) {
                    Log.d(TAG, "onNativeAdLoaded but there's already an ad nearby.  dont ad to list");
                    return;//there's already an ad nearby in the list
                }
            }
            vis = vis < Config.AD_DISTANCE ? Config.AD_DISTANCE : vis;
            Log.d(TAG, "onNativeAdLoaded and inserted");
            adapter.cleanupLastAd();
            adapter.insertAd(vis, null, (NativeAd)nativeAdView);
        }
    }

    @Override
    public void onNativeAdFailed(String placement, int errorCode) {
        Log.d(TAG, "onNativeAdFailed: " + errorCode);
        isAdRequestInProgress = false;
    }

    @Override
    public void onNativeAdClicked(String placement) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanupAll();
        cleanupNativeAd();
        cleanupBannerAd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_partners, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_choose_partners:
                choosePartners();
                return true;
            case R.id.menu_show_current_winner:
                if (bannerAd != null) {
                    new AlertDialog.Builder(this).setMessage("Last Winning Partner: " + bannerAd.getWinningPartnerName()).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void choosePartners() {
        MediationPartners.choosePartners(this, adRequest, MediationPartners.ADTYPE_BANNER, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //not implemented
            }
        });
    }
}
