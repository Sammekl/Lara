package com.rwssistent.LARA.helpers;

import android.util.Log;

import com.rwssistent.LARA.exceptions.LaraException;
import com.rwssistent.LARA.model.LaraLocation;
import com.rwssistent.LARA.model.Node;
import com.rwssistent.LARA.model.NodeBearing;

import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Samme on 16-4-2015.
 */
public class DistanceHelper {

    /**
     * Haalt de nearestNode op van de huidige locatie
     *
     * @param nodes     alle nodes
     * @param latitude  de latitude van de locatie
     * @param longitude de longitude van de locatie
     * @return De nearest Node.
     */
    public static Node getNearestNode(List<Node> nodes, double latitude, double longitude) {
        Node node = null;
        double result = 0;
        try {
            if (nodes == null) {
                throw new LaraException("Lijst met nodes is null.");
            }
            for (Node n : nodes) {
                double tempResult = distance(n.getLat(), n.getLon(), latitude, longitude);
                if (node == null || tempResult < result) {
                    node = n;
                    result = tempResult;
                }
            }
        } catch (LaraException le) {
            Log.e("DistanceHelper", "getNearestNode(): " + le.getMessage());
        }
        return node;
    }

    /**
     * Haal de volgende node op vanaf de current en previous Node
     *
     * @param nodes        alle nodes
     * @param currentNode  de huidige node
     * @param previousNode de vorige node, als controle dat deze niet wordt teruggegeven
     * @return De volgende node
     */
    public static Node getNextNode(List<Node> nodes, Node currentNode, Node previousNode) {
        Node node = null;
        double result = 0;
        for (Node n : nodes) {
            double tempResult = distance(n.getLat(), n.getLon(), currentNode.getLat(), currentNode.getLon());
            if (node == null || tempResult < result) {
                if (n.getId() != currentNode.getId() || n.getId() != previousNode.getId()) {
                    node = n;
                    result = tempResult;
                }
            }
        }
        return node;
    }

    /**
     * Geeft het verschil tussen twee locaties in km
     *
     * @param lat1 de latitude van locatie 1
     * @param lon1 de longitude van locatie 1
     * @param lat2 de latitude van locatie 2
     * @param lon2 de latitude van locatie 2
     * @return De afstand in km tussen locatie1 en locatie2
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        return dist * 60 * 1.609344;
    }

    /**
     * Geeft de richting tussen twee locaties in graden
     *
     * @param lat1 de latitude van locatie 1
     * @param lon1 de longitude van locatie 1
     * @param lat2 de latitude van locatie 2
     * @param lon2 de latitude van locatie 2
     * @return De richting in graden tussen locatie1 en locatie2
     */
    public static double rumbLineBearing(double lat1, double lon1, double lat2, double lon2) {

        //difference in longitudinal coordinates
        double dLon = deg2rad(lon2) - deg2rad(lon1);

        //difference in the phi of latitudinal coordinates
        double dPhi = Math.log(Math.tan(deg2rad(lat2) / 2 + Math.PI / 4) / Math.tan(deg2rad(lat1) / 2 + Math.PI / 4));

        if (Math.abs(dLon) > Math.PI) {
            if (dLon > 0) {
                dLon = (2 * Math.PI - dLon) * -1;
            } else {
                dLon = 2 * Math.PI + dLon;
            }
        }

        //return the angle, normalized
        return (rad2deg(Math.atan2(dLon, dPhi)) + 360) % 360;
    }

    /**
     * Geef de node die het meest in de buurt is van de targetBearing
     * @param targetBearing de bearing waar de gebruiker op zit
     * @param nodeBearingList de lijst met mogelijke nodes + bearing
     * @return de node die het meest in de buurt komt van de huidige bearing.
     */
    public static Node giveNearestBearingNode(double targetBearing, List<NodeBearing> nodeBearingList) {
        Node returnNode = null;
        double min = Integer.MAX_VALUE;

        for (NodeBearing nodeBearing : nodeBearingList) {
            final double diff = Math.abs(nodeBearing.getBearing() - targetBearing);
            final double diffIn360 = Math.abs(nodeBearing.getBearing() - 360 - targetBearing);
            if(diff < min) {
                min = diff;
                returnNode = nodeBearing.getNode();
            }
            if (diffIn360 < min) {
                min = diffIn360;
                returnNode = nodeBearing.getNode();
            }
        }
        return returnNode;
    }
    /**
     * Geeft de snelheid tussen twee locaties in kmph
     *
     * @param lat1 de latitude van locatie 1
     * @param lon1 de longitude van locatie 1
     * @param lat2 de latitude van locatie 2
     * @param lon2 de latitude van locatie 2
     * @return De snelheid in kmph
     */
    public static double getSpeed(double lat1, double lon1, Date time1, double lat2, double lon2, Date time2) {
        double dist = distance(lat1, lon1, lat2, lon2);
        double seconds = (time2.getTime()-time1.getTime());
        double speed_kps = dist / seconds;
        double speed_kph = (speed_kps * 3.6);
        return speed_kph;
    }

    /**
     * Haalt de Node op die het meest in de goede richting ligt van de huidige locatie
     *
     * @param nodes            alle nodes
     * @param previousLocation de vorige locatie
     * @param currentLocation  de huidige locatie
     * @return De Node die het meest in de goede richting ligt.
     */
    public static Node getNodeInCourse(List<Node> nodes, LaraLocation previousLocation, LaraLocation currentLocation) {
        Node node = null;
        double result = 0;
        try {
            if (nodes == null) {
                throw new LaraException("Lijst met nodes is null.");
            }
            // Calculate bearing between previousLocation and currentLocation.
            double currentBearing = rumbLineBearing(previousLocation.getLat(), previousLocation.getLon(),
                    currentLocation.getLat(), currentLocation.getLon());

            for (Node n : nodes) {
                double tempResult = rumbLineBearing(currentLocation.getLat(), currentLocation.getLon(), n.getLat(), n.getLon());
                if (node == null
                        || (currentBearing - tempResult) < (currentBearing - result)
                        || (tempResult - currentBearing) > (result - currentBearing)) {
                    node = n;
                    result = tempResult;
                }
            }
        } catch (LaraException le) {
            Log.e("DistanceHelper", "getNodeInCourse(): " + le.getMessage());
        }
        return node;
    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
