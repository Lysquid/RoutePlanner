package fr.blazanome.routeplanner.tools;

import fr.blazanome.routeplanner.model.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Serialize and deserialize a session to an XML file, to save and restore the couriers
 */
public class XMLSessionSerializer {

    /**
     * @param couriers A list of couriers
     * @param file     The file to save the serialized couriers to
     */
    public void serialize(List<Courier> couriers, File file) {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element couriersList = doc.createElement("session");
            doc.appendChild(couriersList);

            for (Courier courier : couriers) {

                // courier element
                Element courierElem = doc.createElement("courier");
                couriersList.appendChild(courierElem);

                // courier id attribute
                Attr courierId = doc.createAttribute("id");
                courierId.setValue(String.valueOf(courier.getId()));
                courierElem.setAttributeNode(courierId);

                for (DeliveryRequest request : courier.getRequests()) {

                    // request element
                    Element requestElem = doc.createElement("request");
                    requestElem.appendChild(doc.createTextNode(String.valueOf(request.getIntersection().getId())));
                    courierElem.appendChild(requestElem);

                    // timeframe attribute
                    Attr timeframe = doc.createAttribute("timeframe");
                    timeframe.setValue(String.valueOf(request.getTimeframe()));
                    requestElem.setAttributeNode(timeframe);

                }

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Enable indentation
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find an intersection in a map given its id
     *
     * @param intersectionId The id of the intersection, found in an XML map
     * @return The intersection object
     */
    private static Intersection getIntersection(IMap map, long intersectionId) {
        for (Intersection intersection : map.getIntersections()) {
            if (intersection.getId() == intersectionId) {
                return intersection;
            }
        }
        return null;
    }

    /**
     * @param file The file containing the serialized session
     * @param map  The map corresponding to the session to be parsed
     * @return The deserialized list of couriers
     */
    public List<Courier> parse(File file, IMap map) throws IOException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList courierList = doc.getElementsByTagName("courier");
            ArrayList<Courier> couriers = new ArrayList<>();

            for (int i = 0; i < courierList.getLength(); i++) {
                Node courierNode = courierList.item(i);

                if (courierNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element courierElement = (Element) courierNode;
                    Courier courier = new Courier(Integer.parseInt(courierElement.getAttribute("id")));

                    NodeList requestList = courierElement.getElementsByTagName("request");

                    for (int j = 0; j < requestList.getLength(); j++) {
                        Node requestNode = requestList.item(j);

                        if (requestNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element requestElement = (Element) requestNode;
                            String timeframe = requestElement.getAttribute("timeframe");
                            long intersectionId = Long.parseLong(requestElement.getTextContent());
                            Intersection intersection = XMLSessionSerializer.getIntersection(map, intersectionId);
                            if (intersection != null) {
                                DeliveryRequest deliveryRequest = new DeliveryRequest(intersection, Timeframe.fromString(timeframe));
                                courier.addDelivery(deliveryRequest);
                            }
                        }
                    }

                    couriers.add(courier);
                }
            }

            return couriers;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
