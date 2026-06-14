package ca.uwaterloo.watform.utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class XmlReader {

    private static final DocumentBuilderFactory DOC_FACTORY = DocumentBuilderFactory.newInstance();

    private XmlReader() {}

    /**
     * Convenience wrapper that converts checked exceptions into a RuntimeException, matching the
     * style of XmlDumper.dumpInstance.
     */
    public static Map<String, Set<List<String>>> readInstance(String xmlPath) {
        try {
            return readInstanceUnchecked(xmlPath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(
                    "Failed to read instance XML from " + xmlPath + ": " + e.getMessage(), e);
        }
    }

    /**
     * Reads an instance XML file produced by XmlDumper.dumpInstance and returns a map from relation
     * name → set of tuples, where each tuple is an ordered list of atom name strings.
     *
     * <p>e.g. for: <relation name="this/Book.addr"> <tuple>Book$0, Name$0, Addr$0</tuple>
     * </relation>
     *
     * <p>returns: { "this/Book.addr" -> { ["Book$0", "Name$0", "Addr$0"] } }
     */
    private static Map<String, Set<List<String>>> readInstanceUnchecked(String xmlPath)
            throws ParserConfigurationException, SAXException, IOException {

        Document doc = DOC_FACTORY.newDocumentBuilder().parse(new File(xmlPath));
        doc.getDocumentElement().normalize();

        Map<String, Set<List<String>>> result = new HashMap<>();

        NodeList relations = doc.getElementsByTagName("relation");
        for (int i = 0; i < relations.getLength(); i++) {
            Element rel = (Element) relations.item(i);
            String name = rel.getAttribute("name");

            Set<List<String>> tuples = new HashSet<>();
            NodeList tupleNodes = rel.getElementsByTagName("tuple");
            for (int j = 0; j < tupleNodes.getLength(); j++) {
                String raw = tupleNodes.item(j).getTextContent().trim();
                List<String> tuple =
                        Arrays.stream(raw.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .toList();
                tuples.add(tuple);
            }

            result.put(name, tuples);
        }

        return result;
    }
}
