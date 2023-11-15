package fr.blazanome.routeplanner.test.tools;

import fr.blazanome.routeplanner.model.*;
import fr.blazanome.routeplanner.tools.XMLMapParser;
import fr.blazanome.routeplanner.tools.XMLSessionSerializer;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.contentOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLSessionSerializerTest {

    @Test
    public void testSave() throws ParserConfigurationException, IOException, SAXException {
        XMLMapParser mapParser = new XMLMapParser(new AdjacencyListMap.BuilderFactory());
        IMap map = mapParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/map/XMLParserTest.xml"));

        Courier courier = new Courier(1);
        for (Intersection intersection : map.getIntersections()) {
            courier.addDelivery(new DeliveryRequest(intersection, Timeframe.H8));
        }
        List<Courier> couriers = new ArrayList<>();
        couriers.add(courier);
        couriers.add(new Courier(2));

        XMLSessionSerializer xmlSessionSerializer = new XMLSessionSerializer();
        File savedFile = new File("src/test/resources/fr/blazanome/routeplanner/tools/session/SavedSession.xml");
        xmlSessionSerializer.serialize(couriers, savedFile);
        File referenceFile = new File("src/test/resources/fr/blazanome/routeplanner/tools/session/SavedSessionReference.xml");

        assertEquals(contentOf(savedFile), contentOf(referenceFile));
    }

    @Test
    void testLoad() throws ParserConfigurationException, IOException, SAXException {
        XMLMapParser mapParser = new XMLMapParser(new AdjacencyListMap.BuilderFactory());
        IMap map = mapParser.parse(new File("src/test/resources/fr/blazanome/routeplanner/tools/map/XMLParserTest.xml"));

        XMLSessionSerializer xmlSessionSerializer = new XMLSessionSerializer();
        File referenceFile = new File("src/test/resources/fr/blazanome/routeplanner/tools/session/SavedSessionReference.xml");
        List<Courier> couriers = xmlSessionSerializer.parse(referenceFile, map);
        Courier courier = couriers.get(0);
        assertEquals(courier.getId(), 1);

        int i = 0;
        for (Intersection intersection : map.getIntersections()) {
            assertEquals(intersection.getId(), courier.getRequests().get(i).getIntersection().getId());
            i++;
        }
    }

}
