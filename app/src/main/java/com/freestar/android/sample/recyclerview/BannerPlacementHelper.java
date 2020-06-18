package com.freestar.android.sample.recyclerview;

public class BannerPlacementHelper {

    private BannerPlacementHelper(){}

    private static String[] bannerPlacements = {"default"}; //do not remove 'default'
    private static int index;

    synchronized static String getNextPlacement() {
        if (index == bannerPlacements.length) {
            index = 0;
        }
        if (index == 0) {
            index++;
            return null;
        } else {
            return bannerPlacements[index++];
        }
    }

}
