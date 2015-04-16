package com.rwssistent.LARA.tasks;

import android.app.Activity;
import android.util.Log;


import com.rwssistent.LARA.activities.MainActivity;
import com.rwssistent.LARA.helpers.DistanceHelper;
import com.rwssistent.LARA.helpers.JSONHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Samme on 17-3-2015.
 */
public class RoadTask extends BackgroundTask {

    private String url;
    private double longitude;
    private double latitude;
    private MainActivity mainActivity;
    private List<Highway> previousHighways = new ArrayList<>();
    ;
    private List<Highway> currentHighways;

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
        Node nearestNode = DistanceHelper.getNearestNode(nodes, longitude, latitude);
        List<Highway> highways = JSONHelper.getHighwaysFromResult(result);

        // Leeg current Highways
        currentHighways = new ArrayList<>();
        Highway highwayToDisplay = null;

        previousHighways.add(new Highway(1, 100, 130, "Dorpsstraat", null));
        previousHighways.add(new Highway(1, 50, 80, "Emmalaan", null));
        previousHighways.add(new Highway(1, 100, 130, "Molenweg", null));
        previousHighways.add(new Highway(1, 100, 130, "Martijn(ga)weg", null));

        Long startTime = System.currentTimeMillis();
        for (Highway highway : highways) {
            if (highway.getNodes().contains(nearestNode.getId())) {
                currentHighways.add(highway);
                // Controleer current & previous op zelfde wegen
                for (Highway previousHighway : previousHighways) {
                    if (highway.getRoadName().equals(previousHighway.getRoadName())) {
                        highwayToDisplay = highway;
                        Log.i(getClass().getSimpleName(), "Way found: " + highway.getRoadName() + " same as previous way.");
                        break;
                    }
                }
                if (highwayToDisplay == null) {
                    highwayToDisplay = highway;
                }
            }
        }
        mainActivity.displayValues(highwayToDisplay);
        previousHighways = currentHighways;

        Long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("previousWaysDing", "time taken: " + timeTaken);
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

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
