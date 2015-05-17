package com.rwssistent.LARA.helpers;

import com.rwssistent.LARA.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samme on 17-5-2015.
 */
public class TestHelper {

    public static List<Node> getSingleHighway() {
        // Geef de dorpsweg in Maartensdijk terug. Deze heeft 3 zijstraten (Tuinlaan > Otto Doornenbalweg > Bantamlaan) http://puu.sh/hQp8P.jpg
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(52.15813, 5.184));
        nodes.add(new Node(52.1582422, 5.185626));
        nodes.add(new Node(52.1583165, 5.1861905));
        nodes.add(new Node(52.1583165, 5.1861905));
        nodes.add(new Node(52.15861, 5.18812));
        nodes.add(new Node(52.1587758, 5.1888625));
        nodes.add(new Node(52.1589451, 5.189715));
        nodes.add(new Node(52.1589451, 5.189715));
        nodes.add(new Node(52.1593001, 5.1923875));
        nodes.add(new Node(52.1593001, 5.1923875));
        return nodes;
    }

    public static List<Node> getMultipleHighways() {
        // Soestdijkseweg in Bilthoven > Gezichtslaan > Gezichtslaan > Gezichtslaan > Hobbemalaan
        // http://puu.sh/hQqi3.png
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(52.14203, 5.2149));
        nodes.add(new Node(52.14159, 5.2145));
        nodes.add(new Node(52.14033, 5.21347));
        nodes.add(new Node(52.13961, 5.21289));
        nodes.add(new Node(52.13961, 5.21289));
        nodes.add(new Node(52.13869, 5.21215));
        nodes.add(new Node(52.1378117, 5.2114405));
        nodes.add(new Node(52.1376577, 5.2113183));
        nodes.add(new Node(52.1376577, 5.2113183));

        // Wissel naar gezichtslaan
        nodes.add(new Node(52.1367219, 5.2103356));
        nodes.add(new Node(52.14001, 5.20859));

        // Wissel naar volgende gezichtslaan
        nodes.add(new Node(52.14001, 5.20859));
        nodes.add(new Node(52.1422699, 5.2073899));

        // Wissel naar hobbemalaan(rechts)
        nodes.add(new Node(52.14234, 5.20788));
        nodes.add(new Node(52.14241, 5.20833));

        return nodes;
    }

    public static List<Node> getOneCoordinateHighway() {
        List<Node> toReturn = new ArrayList<>();
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        toReturn.add(new Node(52.14173, 5.09687));
        return toReturn;
    }
}
