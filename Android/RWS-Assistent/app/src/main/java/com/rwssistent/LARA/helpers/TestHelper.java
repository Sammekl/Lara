package com.rwssistent.LARA.helpers;

import com.rwssistent.LARA.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samme on 17-5-2015.
 */
public class TestHelper {

    /**
     * Geef de dorpsweg in Maartensdijk terug. Deze heeft 3 zijstraten (Tuinlaan > Otto Doornenbalweg > Bantamlaan)
     * http://puu.sh/hQp8P.jpg
     *
     * @return De lijst met Nodes
     */
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

    /**
     * Soestdijkseweg in Bilthoven > Gezichtslaan > Gezichtslaan > Gezichtslaan > Hobbemalaan
     * http://puu.sh/hQqi3.png
     *
     * @return Een lijst met Nodes.
     */
    public static List<Node> getMultipleHighways() {

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
        nodes.add(new Node(52.14007,5.20856));
        nodes.add(new Node(52.1422699, 5.2073899));

        // Wissel naar hobbemalaan(rechts)
        nodes.add(new Node(52.14234, 5.20788));
        nodes.add(new Node(52.14241, 5.20833));

        return nodes;
    }

    /**
     * Geef een Node uit dat gat waar Martijn was, hier is een onbekende weg
     *
     * @return Een lijst met een enkele node
     */
    public static List<Node> getOneCoordinateHighway() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        nodes.add(new Node(52.14173, 5.09687));
        return nodes;
    }

    /**
     * Geef een lijst met Nodes van een weg wat langs een onbekende weg loopt http://puu.sh/hSQ9V.png
     *
     * @return Een lijst met een nodes
     */
    public static List<Node> getHighwaysWithUnknownHighway() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(52.13020, 5.19766));
        nodes.add(new Node(52.13032, 5.19764));
        nodes.add(new Node(52.13054, 5.19775));
        nodes.add(new Node(52.13069, 5.19784));
        nodes.add(new Node(52.13085, 5.19795));
        nodes.add(new Node(52.13099, 5.19806));
        nodes.add(new Node(52.13114, 5.19818));
        nodes.add(new Node(52.13128, 5.19830));
        nodes.add(new Node(52.13146, 5.19845));
        nodes.add(new Node(52.13158, 5.19854));
        nodes.add(new Node(52.13175, 5.19868));
        nodes.add(new Node(52.13190, 5.19881));
        nodes.add(new Node(52.13202, 5.19891));
        nodes.add(new Node(52.13214, 5.19901));
        nodes.add(new Node(52.13221, 5.19908));
        nodes.add(new Node(52.13230, 5.19916));
        nodes.add(new Node(52.13241, 5.19926));
        nodes.add(new Node(52.13251, 5.19935));
        nodes.add(new Node(52.13262, 5.19944));

        // Onbekende weg
        nodes.add(new Node(52.1308352, 5.1988611));
        nodes.add(new Node(52.1308352, 5.1988611));

        nodes.add(new Node(52.13263, 5.19964));
        nodes.add(new Node(52.13262, 5.19981));
        nodes.add(new Node(52.13257, 5.20006));
        nodes.add(new Node(52.13249, 5.20039));
        nodes.add(new Node(52.13242, 5.20066));
        nodes.add(new Node(52.13235, 5.20096));
        nodes.add(new Node(52.13217, 5.20161));


        return nodes;
    }

    public static List<Node> getHighwaysExit() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(52.07921, 4.99858));
        nodes.add(new Node(52.07943, 4.99736));
        nodes.add(new Node(52.07965, 4.99614));
        nodes.add(new Node(52.07943, 4.99736));
        nodes.add(new Node(52.07987, 4.99492));

        //afslag
        nodes.add(new Node(52.08012, 4.99371));
        nodes.add(new Node(52.08035, 4.99249));
        nodes.add(new Node(52.08054, 4.99135));
        nodes.add(new Node(52.08069, 4.99014));

        //splitsing
        nodes.add(new Node(52.08092, 4.98772));
        nodes.add(new Node(52.08115, 4.98660));
        nodes.add(new Node(52.08149, 4.98563));
        nodes.add(new Node(52.08178, 4.98444));
        nodes.add(new Node(52.08192, 4.98322));
        nodes.add(new Node(52.08200, 4.98201));
        nodes.add(new Node(52.08204, 4.98080));

        //bocht
        nodes.add(new Node(52.08215, 4.97958));
        nodes.add(new Node(52.08249, 4.97898));
        nodes.add(new Node(52.08286, 4.97876));
       // nodes.add(new Node(BLA));




        return nodes;
    }
}
