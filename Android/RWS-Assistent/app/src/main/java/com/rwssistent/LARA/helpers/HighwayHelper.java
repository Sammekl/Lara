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

    /**
     * Haalt de gehele Highway op van de huidige node.
     *
     * @param node        de node waarvan de weg opgehaald moet worden
     * @param allHighways alle wegen waaruit gekozen kan worden
     * @return Het highway object
     */
    public Highway getEntireHighway(Node node, List<Highway> allHighways) {
        Highway highway = null;
        for (Highway h : allHighways) {
            if (h.getNodes().contains(node.getId())) {
                highway = h;
            }
        }
        return highway;
    }

    /**
     * Zelfde als getEntireHighway() alleen haalt alle wegen op i.p.v. 1
     *
     * @param node        de node waarvan de wegen opgehaald moeten worden
     * @param allHighways alle wegen waaruit gekozen kan worden
     * @return Een list met Highways
     */
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

    /**
     * Haalt alle nodes die bij dezelfde weg(en) horen als de node die wordt meegegeven als parameter op.
     *
     * @param node        De node waarvan alle nodes van bijbehorende wegen opgehaald moeten worden
     * @param allHighways Alle wegen
     * @param allNodes    Alle nodes
     * @return Een lijst met nodes die bij dezelfde weg(en) horen als de node die wordt meegegeven als parameter
     */
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

    /**
     * Haalt alle nodes op van de Highway.
     *
     * @param currentHighway De highway waarvan de nodes opgehaald moeten worden
     * @param allNodes       Alle nodes waar uit gekozen kan worden
     * @return Een lijst met alle nodes die bij deze weg horen.
     */
    public List<Node> getAllNodesFromHighway(Highway currentHighway, List<Node> allNodes) {
        List<Node> nodesFromHighway = new ArrayList<>();
        try {
            if (currentHighway == null) {
                throw new LaraException("currentHighway is null");
            }
            if (allNodes == null) {
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

    /**
     * Haalt de vorige en volgende node op van de Highway.
     *
     * @param highway De highway waarvan de nodes opgehaald moeten worden
     * @param currentNode De node waarvan de volgende en vorige nodes opgehaald moeten worden
     * @param allNodes       Alle nodes waar uit gekozen kan worden
     * @return Een lijst met de vorige en volgende node van deze weg.
     */

    public List<Node> getPreviousNextNodeFromHighway(Highway highway, Node currentNode, List<Node> allNodes) {
        Long currentNodeId = currentNode.getId();
        List<Node> returnNodes = new ArrayList<>();
        List<Long> highwayNodes = highway.getNodes();

        int index = highwayNodes.indexOf(currentNodeId);
        int previousIndex = index - 1;
        int nextIndex = index + 1;

        Long previousNodeId = highwayNodes.get(previousIndex);
        Long nextNodeId = highwayNodes.get(nextIndex);

        for(Node n : allNodes) {
            if(previousNodeId != -1 && previousNodeId == n.getId()) {
                returnNodes.add(n);
            }
            if(nextNodeId != -1 && nextNodeId == n.getId()) {
                returnNodes.add(n);
            }
        }
        return returnNodes;
    }

    /**
     * Checkt of de conditionele snelheid geldig is
     *
     * @param highway de snelweg met een conditionele snelheid
     * @return of de conditionele snelheid geldig is
     */
    public boolean getConditionalValid(Highway highway) {
        Boolean result = false;
        try {
            if (highway == null) {
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
        } catch (LaraException le) {
            Log.e(getClass().getSimpleName(), "getConditionalValid(): " + le.getMessage());
        }
        return result;
    }
}
