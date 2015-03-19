package com.rwssistent.LARA.tasks;

import android.app.Activity;

import com.rwssistent.LARA.model.ResponseDTO;

/**
 * Created by Samme on 17-3-2015.
 */
public class RoadTask extends BackgroundTask {

    private String url;
    private double longitude;
    private double latitude;

    /**
     * @param activity The activity which invoked this task
     */
    public RoadTask(Activity activity) {
        super(activity);
    }

    /**
     * The task to execute
     */
    @Override
    public ResponseDTO doTask() {

        return null;
    }

    /**
     * @param result The result from doTask()
     */
    @Override
    public void doProcessResult(ResponseDTO result) {

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
