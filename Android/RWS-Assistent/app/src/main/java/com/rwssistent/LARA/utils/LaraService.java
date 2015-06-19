package com.rwssistent.LARA.utils;

import android.app.Activity;

import com.rwssistent.LARA.helpers.DistanceHelper;
import com.rwssistent.LARA.helpers.HighwayHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.LaraLocation;
import com.rwssistent.LARA.model.Node;
import com.rwssistent.LARA.model.NodeBearing;
import com.rwssistent.LARA.tasks.RoadTask;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by Samme on 17-3-2015.
 */
public class LaraService {

    private HighwayHelper highwayHelper = new HighwayHelper();

    /**
     * Haal de Highways en nodes op uit de overpass API
     *
     * @param activity  de activity die deze methode invoked
     * @param latitude  de latitude van de locatie
     * @param longitude de longitude van de locatie
     */
    public void getHighwayData(final Activity activity, double latitude, double longitude) {
        final RoadTask roadTask = new RoadTask(activity);
        String url = String.format(Constants.PREF_API_URL, latitude, longitude);
        roadTask.setUrl(url);
        roadTask.execute();
    }

    /**
     * Haalt de nearestNode op van de huidige locatie
     *
     * @param nodes alle nodes
     * @param lat   de latitude van de locatie
     * @param lon   de longitude van de locatie
     * @return De nearest Node.
     */
    public Node pollNearestNode(List<Node> nodes, double lat, double lon) {
        return DistanceHelper.getNearestNode(nodes, lat, lon);
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
    public double distanceBetweenLocations(double lat1, double lon1, double lat2, double lon2) {
        return DistanceHelper.distance(lat1, lon1, lat2, lon2);
    }

    /**
     * Haalt de gehele Highway op van de huidige node.
     *
     * @param node        de node waarvan de weg opgehaald moet worden
     * @param allHighways alle wegen waaruit gekozen kan worden
     * @return Het highway object
     */
    public Highway getEntireHighway(Node node, List<Highway> allHighways) {
        return highwayHelper.getEntireHighway(node, allHighways);
    }

    /**
     * Zelfde als getEntireHighway() alleen haalt alle wegen op i.p.v. 1
     *
     * @param node        de node waarvan de wegen opgehaald moeten worden
     * @param allHighways alle wegen waaruit gekozen kan worden
     * @return Een list met Highways
     */
    public List<Highway> getAllHighwaysFromNode(Node node, List<Highway> allHighways) {
        return highwayHelper.getAllHighwaysFromNode(node, allHighways);
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
        return highwayHelper.getAllNodesFromAllHighwaysFromCurrentNode(node, allHighways, allNodes);
    }

    public List<Node> getAllNextNodesFromCurrentNode(Node currentNode, List<Highway> allHighways, List<Node> allNodes) {
        return highwayHelper.getAllNextNodesFromCurrentNode(currentNode, allHighways, allNodes);
    }

    /**
     * Haalt alle nodes op van de Highway.
     *
     * @param currentHighway De highway waarvan de nodes opgehaald moeten worden
     * @param allNodes       Alle nodes waar uit gekozen kan worden
     * @return Een lijst met alle nodes die bij deze weg horen.
     */
    public List<Node> getAllNodesFromHighway(Highway currentHighway, List<Node> allNodes) {
        return highwayHelper.getAllNodesFromHighway(currentHighway, allNodes);
    }

    /**
     * Checkt of de conditionele snelheid geldig is
     *
     * @param highway de snelweg met een conditionele snelheid
     * @return of de conditionele snelheid geldig is
     */
    public boolean getConditionalValid(Highway highway) {
        return highwayHelper.getConditionalValid(highway);
    }

    /**
     * @param nodes
     * @param previousLocation
     * @param currentLocation
     * @return
     */
    public Node getNodeInCourse(List<Node> nodes, LaraLocation previousLocation, LaraLocation currentLocation) {
        return DistanceHelper.getNodeInCourse(nodes, previousLocation, currentLocation);
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
    public double rumbLineBearing(double lat1, double lon1, double lat2, double lon2) {
        return DistanceHelper.rumbLineBearing(lat1, lon1, lat2, lon2);
    }

    /**
     * Geef de node die het meest in de buurt is van de targetBearing
     * @param targetBearing de bearing waar de gebruiker op zit
     * @param nodeBearingList de lijst met mogelijke nodes + bearing
     * @return de node die het meest in de buurt komt van de huidige bearing.
     */
    public Node giveNearestBearingNode(double targetBearing, List<NodeBearing> nodeBearingList) {
        return DistanceHelper.giveNearestBearingNode(targetBearing, nodeBearingList);
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
    public double getSpeed(double lat1, double lon1, Date time1, double lat2, double lon2, Date time2) {
        return DistanceHelper.getSpeed(lat1, lon1, time1, lat2, lon2, time2);
    }
}
