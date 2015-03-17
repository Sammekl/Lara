package com.rwssistent.LARA.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.rwssistent.LARA.model.ResponseDTO;

/**
 * Created by Samme on 17-3-2015.
 */
public abstract class BackgroundTask extends AsyncTask<Void, Void, ResponseDTO> {

    ///////////////////////////////////
    // Properties
    ///////////////////////////////////

    protected Activity activity;
    protected ProgressDialog dialog;

    public BackgroundTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ResponseDTO doInBackground(Void... params) {
        return this.doTask();
    }

    ///////////////////////////////////
    // Public Methods
    ///////////////////////////////////

    public abstract void doProcessResult(ResponseDTO result);

    public abstract ResponseDTO doTask();
}
