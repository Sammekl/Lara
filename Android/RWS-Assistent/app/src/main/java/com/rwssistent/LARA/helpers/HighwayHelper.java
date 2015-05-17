package com.rwssistent.LARA.helpers;

import android.util.Log;

import com.rwssistent.LARA.exceptions.LaraException;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Samme on 17-4-2015.
 */
public class HighwayHelper {

    private Highway previousHighway;
    private List<Highway> previousHighways = new ArrayList<>();


    public Highway getEntireHighway(Node node, List<Highway> allHighways) {
        Highway highway = null;
        for (Highway h : allHighways) {
            if (h.getNodes().contains(node.getId())) {
                highway = h;
            }
        }
        return highway;
    }

    public List<Highway> getAllHighwaysFromNode(Node node, List<Highway> allHighways) {
        List<Highway> highways = new ArrayList<>();
        for (Highway h : allHighways) {
            if (h.getNodes().contains(node.getId())) {
                highways.add(h);
            }
        }
        return highways;
    }

    /**
     * Wordt niet gebruikt
     */
    public Node getNextNode(Highway currentHighway, Node currentNode, Node previousNode, List<Node> allNodes) {
        Node nextNode = null;
        List<Node> allNodesFromHighway = this.getAllNodesFromHighway(currentHighway, allNodes);
        if (allNodesFromHighway != null && allNodesFromHighway.size() > 0) {
            Node node = DistanceHelper.getNextNode(allNodesFromHighway, currentNode, previousNode);
            if (node != null) {
                nextNode = node;
            }
        }
        return nextNode;
    }

    public List<Node> getAllNodesFromAllHighwaysFromCurrentNode(Node node, List<Highway> allHighways, List<Node> allNodes) {
        List<Node> nodes = new ArrayList<>();
        try {
            if (allHighways == null) {
                throw new LaraException("allHighways is null.");
            }
            if (node == null) {
                throw new LaraException("Node is null.");
            }
            for (Highway h : allHighways) {
                if (h.getNodes() == null) {
                    throw new LaraException("Highway: " + h.getId() + " heeft geen nodes.");
                }
                if (h.getNodes().contains(node.getId())) {
                    nodes.addAll(getAllNodesFromHighway(h, allNodes));
                }
            }
        } catch (LaraException le) {
            Log.e(getClass().getSimpleName(), "getAllNodesFromAllHighwaysFromCurrentNode(): " + le.getMessage());
        }
        return nodes;
    }

    public List<Node> getAllNodesFromHighway(Highway currentHighway, List<Node> allNodes) {
        List<Node> nodesFromHighway = new ArrayList<>();
        try {
            if(currentHighway == null) {
                throw new LaraException("currentHighway is null");
            }
            if(allNodes == null) {
                throw new LaraException("allNodes is null");
            }
            List<Long> nodeIdsInHighway = currentHighway.getNodes();
            // Doorzoek alle nodes en voeg nodes die bij snelweg horen toe aan de lijst.
            for (Node n : allNodes) {
                if (nodeIdsInHighway.contains(n.getId())) {
                    nodesFromHighway.add(n);
                }
            }
        } catch (LaraException le) {
            Log.e(getClass().getSimpleName(), "getAllNodesFromHighway(): " + le.getMessage());
        }
        return nodesFromHighway;
    }
//    public Highway getCurrentHighway(Node nearestNode, List<Highway> highways) {
//        Highway highwayToDisplay = null;
//        List<Highway> currentHighways = new ArrayList<>();
//
//        // ====================
//        // TEST
//        // ====================
//        previousHighways.add(new Highway(1, 100, 130, "Oudenoord", null));
//        previousHighways.add(new Highway(1, 50, 80, "Beatrixplatsoen", null));
//        previousHighways.add(new Highway(1, 100, 130, "Julianalaan", null));
//        previousHighways.add(new Highway(1, 100, 130, "Thuisweide", null));
//
//        for (Highway highway : highways) {
//            if (highway.getNodes().contains(nearestNode.getId())) {
//                currentHighways.add(highway);
//                if (previousHighway != null && highway.getRoadName().equals(previousHighway.getRoadName())) {
//                    highwayToDisplay = highway;
////                      Toast.makeText(context, "U bevindt zich nog op dezelfde weg als hiervoor", Toast.LENGTH_SHORT).show();
//                    Log.i(getClass().getSimpleName(), "Way found: " + highway.getRoadName() + " same as the previous way.");
//                    return returnHighway(currentHighways, highwayToDisplay);
//                }
//            }
//        }
//        for (Highway highway : highways) {
//            if (highway.getNodes().contains(nearestNode.getId())) {
//                // Controleer current & previous op zelfde wegen
//                for (Highway pHighway : previousHighways) {
//                    if (highway.getRoadName().equals(pHighway.getRoadName())) {
//                        highwayToDisplay = highway;
//                        Log.i(getClass().getSimpleName(), "Way found: " + highway.getRoadName() + " same as one of the previous ways.");
////                            Toast.makeText(context, "U bevindt zich op een weg die hiervoor al is gevonden", Toast.LENGTH_SHORT).show();
//                        return returnHighway(currentHighways, highwayToDisplay);
//                    }
//                }
//            }
//
//        }
//        if(!currentHighways.isEmpty()) {
//            return returnHighway(currentHighways, currentHighways.get(0));
//        }
//        else return null;
//    }

    public boolean ConditionalValid(Highway highway) {
        Boolean result = false;
        try {
            if(highway == null) {
                throw new LaraException("Highway is null.");
            }
            if (highway.getMaxSpeedConditionalStart() != null && highway.getMaxSpeedConditionalEnd() != null) {
                Calendar currentCalendar = Calendar.getInstance(); //Create Calendar-Object
                currentCalendar.setTime(new Date());               //Set the Calendar to now
                int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
                int startHour = highway.getMaxSpeedConditionalStart().get(Calendar.HOUR_OF_DAY);
                int endHour = highway.getMaxSpeedConditionalEnd().get(Calendar.HOUR_OF_DAY);

                Log.d("JSONHelper", "dateCurrent: " + currentHour);
                Log.d("JSONHelper", "dateStart: " + startHour);
                Log.d("JSONHelper", "dateEnd: " + endHour);

                if (currentHour <= endHour && currentHour >= startHour) {
                    result = true;
                }
            }
        }catch (LaraException le) {
            Log.e(getClass().getSimpleName(), "ConditionalValid(): " + le.getMessage());
        }
        return result;
    }

    private Highway returnHighway(List<Highway> currentHighways, Highway highwayToDisplay) {

        previousHighways.clear();
        previousHighways = currentHighways;

        // Set previous highway
        previousHighway = highwayToDisplay;
        return highwayToDisplay;
    }
}
