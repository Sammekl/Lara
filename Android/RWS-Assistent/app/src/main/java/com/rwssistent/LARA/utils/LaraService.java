package com.rwssistent.LARA.utils;

import android.app.Activity;

import com.rwssistent.LARA.tasks.RoadTask;

/**
 * Created by Samme on 17-3-2015.
 */
public class LaraService {

    public static void getRoadData(final Activity activity, double longitude, double latitude) {
        final RoadTask roadTask = new RoadTask(activity);
        roadTask.setLongitude(longitude);
        roadTask.setLatitude(latitude);
        String url = String.format(Constants.PREF_API_URL, longitude, latitude);
        roadTask.setUrl(url);
        roadTask.execute();
    }
}
