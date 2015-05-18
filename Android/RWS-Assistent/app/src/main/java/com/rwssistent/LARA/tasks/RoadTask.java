package com.rwssistent.LARA.tasks;

import android.app.Activity;
import android.util.Log;

import com.rwssistent.LARA.activities.MainActivity;
import com.rwssistent.LARA.helpers.JSONHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samme on 17-3-2015.
 */
public class RoadTask extends BackgroundTask {

    private String url;
    private MainActivity mainActivity;

    /**
     * @param activity The activity which invoked this task
     */
    public RoadTask(Activity activity) {
        super(activity);
        this.mainActivity = (MainActivity) activity;
    }

    /**
     * The task to execute
     */
    @Override
    public String doTask() {
        Long startTime = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
            }
        } catch (Exception e) {
            // TODO UnknownHostException aka geen internet afhandelen!
            Log.e(getClass().getSimpleName(), "Error in request");
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("RoadTask.doTask", "time taken: " + timeTaken);
        return stringBuilder.toString();
    }

    /**
     * @param result The result from doTask()
     */
    @Override
    public void doProcessResult(String result) {
        List<Node> nodes = JSONHelper.getNodesFromResult(result);
        if (nodes != null && nodes.size() > 0) {
            mainActivity.setAllNodes(nodes);
        }
        List<Highway> highways = JSONHelper.getHighwaysFromResult(result);
        if (highways != null && highways.size() > 0) {
            mainActivity.setAllHighways(highways);
        }
    }
    public void setUrl(String url) {
        this.url = url;
    }

}
