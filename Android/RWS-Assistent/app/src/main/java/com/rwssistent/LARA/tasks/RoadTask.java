package com.rwssistent.LARA.tasks;

import android.app.Activity;

import com.rwssistent.LARA.model.ResponseDTO;

/**
 * Created by Samme on 17-3-2015.
 */
public class RoadTask extends BackgroundTask {

    private final String url;

    /**
     * @param activity The activity which invoked this task
     * @param url The url to call
     */
    public RoadTask(Activity activity, String url) {
        super(activity);
        this.url = url;
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

}
