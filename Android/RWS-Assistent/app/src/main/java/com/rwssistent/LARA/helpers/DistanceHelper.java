package com.rwssistent.LARA.helpers;

import android.util.Log;

import com.rwssistent.LARA.exceptions.LaraException;
import com.rwssistent.LARA.model.Node;

import java.util.List;

/**
 * Created by Samme on 16-4-2015.
 */
public class DistanceHelper {

    public static Node getNearestNode(List<Node> nodes, double latitude, double longitude) {
        Node node = null;
        double result = 0;
        try {
            if(nodes == null) {
                throw new LaraException("Lijst met nodes is null.");
            }
            for (Node n : nodes) {
                double tempResult = distance(n.getLat(), n.getLon(), latitude, longitude);
                if (node == null || tempResult < result) {
                    node = n;
                    result = tempResult;
                }
            }
        } catch(LaraException le) {
            Log.e("DistanceHelper", "getNearestNode(): " + le.getMessage());
        }
        return node;
    }

    public static Node getNextNode(List<Node> nodes, Node currentNode, Node previousNode) {
        Long startTime = System.currentTimeMillis();
        Node node = null;
        double result = 0;
        for (Node n : nodes) {
            double tempResult = distance(n.getLat(), n.getLon(), currentNode.getLat(), currentNode.getLon());
            if (node == null || tempResult < result) {
                if (node.getId() != currentNode.getId() || node.getId() != previousNode.getId()) {
                    node = n;
                    result = tempResult;
                }
            }
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("DistanceHelper", "time taken (nextNode): " + timeTaken + "ms");
        return node;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist * 60 * 1.609344;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
