package com.freestar.android.sample.recyclerview;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewFixedAdsActivity extends AppCompatActivity implements BannerAdListener,
        NativeAdListener {

    public static final String TAG = "FixedAds";
    public static final int MAX_ADS = 8;
    public static final int NUM_CARDS_IN_BETWEEN = 4;

    private Adapter adapter;
    private int adCount;
    private AdRequest adRequest;
    private RecyclerView recyclerView;
    private boolean flip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adRequest = new AdRequest(this);

        adapter = new Adapter(this, false);
        DataBindingUtil.setContentView(this, R.layout.activity_recyclerview);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadData();

        setTitle("RecyclerView " + MAX_ADS + " Fixed Ads");
    }

    private void loadData() {
        new AsyncTask<Void, Void, List<Item>>() {

            @Override
            protected List<Item> doInBackground(Void... voids) {
                try {
                    return Arrays.asList(new Gson().fromJson(new InputStreamReader(RecyclerViewFixedAdsActivity.this.getAssets().open("media_list.json")), Item[].class));
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

        if (adCount >= MAX_ADS) {
            return;
        }
        if (!flip) {
            flip = true;
            BannerAd bannerAd = new BannerAd(this);
            bannerAd.setBannerAdListener(this);
            bannerAd.setAdSize(AdSize.MEDIUM_RECTANGLE_300_250);
            bannerAd.loadAd(adRequest);
        } else {
            flip = false;
            NativeAd nativeAd = new NativeAd(this);
            nativeAd.setTemplate(NativeAd.TEMPLATE_MEDIUM);
            nativeAd.setNativeAdListener(this);
            nativeAd.loadAd(adRequest);
        }
    }

    @Override
    public void onNativeAdLoaded(View nativeAdView, String placement) {
        Log.d(TAG, "onNativeAdLoaded");
        if (nativeAdView != null) {
            adapter.insertAd((adCount) * NUM_CARDS_IN_BETWEEN,
                    null, (NativeAd) nativeAdView);
            if (adCount == 0) {
                recyclerView.scrollToPosition(0);
            }
            adCount++;
        }
        requestAd();
    }

    @Override
    public void onNativeAdFailed(String placement, int errorCode) {
        requestAd();
    }

    @Override
    public void onNativeAdClicked(String placement) {

    }

    @Override
    public void onBannerAdLoaded(View banner, String placement) {
        Log.d(TAG, "onBannerAdLoaded");
        if (banner != null) {
            adapter.insertAd((adCount) * NUM_CARDS_IN_BETWEEN, (BannerAd)banner, null);
            if (adCount == 0) {
                recyclerView.scrollToPosition(0);
            }
            adCount++;
        }
        requestAd();
    }

    @Override
    public void onBannerAdFailed(View view, String placement, int errorCode) {
        Log.d(TAG, "onBannerAdFailed: " + errorCode);
        requestAd();
    }

    @Override
    public void onBannerAdClicked(View view, String placement) {
        Log.d(TAG, "onBannerAdClicked");
    }

    @Override
    public void onBannerAdClosed(View view, String placement) {
        Log.d(TAG, "onBannerAdClosed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanupAll();
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
                Toast.makeText(this, "not applicable in this test case", Toast.LENGTH_SHORT).show();
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
