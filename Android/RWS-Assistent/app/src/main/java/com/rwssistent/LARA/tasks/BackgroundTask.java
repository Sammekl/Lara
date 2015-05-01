package com.rwssistent.LARA.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.rwssistent.LARA.R;

/**
 * Created by Samme on 17-3-2015.
 */
public abstract class BackgroundTask extends AsyncTask<Void, Void, String> {

    ///////////////////////////////////
    // Properties
    ///////////////////////////////////

    protected Activity activity;
    protected ProgressDialog progressDialog;

    public BackgroundTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        showLoadingProgressDialog();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.doTask();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        doProcessResult(result);
        dismissProgressDialog();
    }
    ///////////////////////////////////
    // Public Methods
    ///////////////////////////////////

    public abstract void doProcessResult(String result);

    public abstract String doTask();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Private Methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void showLoadingProgressDialog() {
        this.showProgressDialog(activity.getString(R.string.dialog_get_highways));
    }

    private void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this.activity);
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
