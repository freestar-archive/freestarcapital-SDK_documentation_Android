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
import com.freestar.android.ads.FreeStarAds;
import com.freestar.android.sample.MainActivity;
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

public class RecyclerViewActivity extends AppCompatActivity implements BannerAdListener {

    public static final String TAG = "RecyclerViewActivity";

    private Adapter adapter;
    private LinearLayoutManager lm;
    private BannerAd bannerAd;
    private boolean isAdRequestInProgress;
    private int scrollDy;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adRequest = new AdRequest(this);
        adRequest.addCustomTargeting("my custom target", "value");
        adapter = new Adapter(this);
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
    }

    private void loadData() {
        new AsyncTask<Void, Void, List<Item>>() {
            @Override
            protected void onPostExecute(List<Item> items) {
                ArrayList<Adapter.ItemWrapper> list = new ArrayList<>(items.size());
                for (Item item : items) {
                    list.add(new Adapter.ItemWrapper(item));
                }
                adapter.setItemWrappers(list);
                requestAd();
            }

            @Override
            protected List<Item> doInBackground(Void... voids) {
                try {
                    return Arrays.asList(new Gson().fromJson(new InputStreamReader(RecyclerViewActivity.this.getAssets().open("media_list.json")), Item[].class));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
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
        bannerAd = new BannerAd(this);
        bannerAd.setBannerAdListener(this);
        bannerAd.setAdSize(AdSize.MEDIUM_RECTANGLE_300_250);
        String placement = BannerPlacementHelper.getNextPlacement();
        if (placement != null) {
            bannerAd.loadAd(adRequest, placement);
        } else {
            bannerAd.loadAd(adRequest);
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
            adapter.insertAd(vis, banner, bannerAd);
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
        adapter.cleanUp();
        isAdRequestInProgress = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanUp();
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
                    new AlertDialog.Builder(this).setMessage("Current Winning Partner: " + bannerAd.getWinningPartnerName()).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void choosePartners() {
        MediationPartners.choosePartners( this, adRequest, MediationPartners.ADTYPE_BANNER, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
