package com.rwssistent.LARA.utils;

import android.app.Activity;
import android.content.Context;

import com.rwssistent.LARA.helpers.DistanceHelper;
import com.rwssistent.LARA.helpers.HighwayHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;
import com.rwssistent.LARA.tasks.RoadTask;

import java.util.List;

/**
 * Created by Samme on 17-3-2015.
 */
public class LaraService {

    private HighwayHelper highwayHelper = new HighwayHelper();

    public void getHighwayData(final Activity activity, double latitude, double longitude) {
        final RoadTask roadTask = new RoadTask(activity);
        roadTask.setLatitude(latitude);
        roadTask.setLongitude(longitude);
        String url = String.format(Constants.PREF_API_URL, latitude, longitude);
        roadTask.setUrl(url);
        roadTask.execute();
    }

    public Node pollNearestNode(List<Node> nodes, double lat, double lon) {
        return DistanceHelper.getNearestNode(nodes, lat, lon);
    }

    public double distanceFromPollLocation(double lat1, double lon1, double lat2, double lon2) {
        return DistanceHelper.distance(lat1, lon1, lat2, lon2);
    }

    public Highway getEntireHighway(Node node, List<Highway> allHighways) {
        return highwayHelper.getEntireHighway(node, allHighways);
    }

    public List<Highway> getAllHighwaysFromNode(Node node, List<Highway> allHighways) {
        return highwayHelper.getAllHighwaysFromNode(node, allHighways);
    }

    public List<Node> getAllNodesFromAllHighwaysFromCurrentNode(Node node, List<Highway> allHighways, List<Node> allNodes) {
        return highwayHelper.getAllNodesFromAllHighwaysFromCurrentNode(node, allHighways, allNodes);
    }

    public List<Node> getAllNodesFromHighway(Highway currentHighway, List<Node> allNodes) {
        return highwayHelper.getAllNodesFromHighway(currentHighway, allNodes);
    }

    public Node getNextNode(Highway currentHighway, Node currentNode, Node previousNode, List<Node> allNodes) {
        return highwayHelper.getNextNode(currentHighway, currentNode, previousNode, allNodes);
    }

}
