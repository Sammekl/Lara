package com.rwssistent.LARA.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samme on 17-4-2015.
 */
public class HighwayHelper {

    private Highway previousHighway;
    private List<Highway> previousHighways = new ArrayList<>();

    public Highway getCurrentHighway(Context context, Node nearestNode, List<Highway> highways) {
        Highway highwayToDisplay = null;
        List<Highway> currentHighways = new ArrayList<>();
//
//        // ====================
//        // TEST
//        // ====================
        previousHighways.add(new Highway(1, 100, 130, "Emmalaan", null));
        previousHighways.add(new Highway(1, 50, 80, "Beatrixplatsoen", null));
        previousHighways.add(new Highway(1, 100, 130, "Julianalaan", null));
        previousHighways.add(new Highway(1, 100, 130, "Thuisweide", null));

        mainLoop:
        for (Highway highway : highways) {
            if (highway.getNodes().contains(nearestNode.getId())) {
                currentHighways.add(highway);
                for (Highway h : highways) {
                    if (previousHighway != null && h.getRoadName().equals(previousHighway.getRoadName())) {
                        highwayToDisplay = h;
//                      Toast.makeText(context, "U bevindt zich nog op dezelfde weg als hiervoor", Toast.LENGTH_SHORT).show();
                        Log.i(getClass().getSimpleName(), "Way found: " + highway.getRoadName() + " same as the previous way.");
                        break mainLoop;
                    }
                }
                // Controleer current & previous op zelfde wegen
                for (Highway pHighway : previousHighways) {
                    if (highway.getRoadName().equals(pHighway.getRoadName())) {
                        highwayToDisplay = highway;
                        Log.i(getClass().getSimpleName(), "Way found: " + highway.getRoadName() + " same as one of the previous ways.");
//                            Toast.makeText(context, "U bevindt zich op een weg die hiervoor al is gevonden", Toast.LENGTH_SHORT).show();
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

        // Set previous highway
        previousHighway = highwayToDisplay;
        return highwayToDisplay;
    }
}
