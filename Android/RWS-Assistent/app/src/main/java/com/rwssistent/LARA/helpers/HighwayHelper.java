package com.rwssistent.LARA.helpers;

import android.util.Log;

import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samme on 17-4-2015.
 */
public class HighwayHelper {

    private List<Highway> previousHighways = new ArrayList<>();
    ;
    private List<Highway> currentHighways;

    public Highway getCurrentHighway(Node nearestNode, List<Highway> highways) {
        Highway highwayToDisplay = null;
        currentHighways = new ArrayList<>();
//
//        // ====================
//        // TEST
//        // ====================
//        previousHighways.add(new Highway(1, 100, 130, "Dorpsstraat", null));
//        previousHighways.add(new Highway(1, 50, 80, "Emmalaan", null));
//        previousHighways.add(new Highway(1, 100, 130, "Molenweg", null));
//        previousHighways.add(new Highway(1, 100, 130, "Martijn(ga)weg", null));

        mainLoop:
        for (Highway highway : highways) {
            if (highway.getNodes().contains(nearestNode.getId())) {
                currentHighways.add(highway);
                // Controleer current & previous op zelfde wegen
                for (Highway previousHighway : previousHighways) {
                    // TODO Previous way, niet alleen previouswayS. Hierdoor worden geen zijstraten meer opgenomen
                    if (highway.getRoadName().equals(previousHighway.getRoadName())) {
                        highwayToDisplay = highway;
                        Log.i(getClass().getSimpleName(), "Way found: " + highway.getRoadName() + " same as previous way.");
                        break mainLoop;
                    }
                }
                if (highwayToDisplay == null) {
                    highwayToDisplay = highway;
                }
            }
        }
        previousHighways.clear();
        previousHighways = currentHighways;
        return highwayToDisplay;
    }
}
