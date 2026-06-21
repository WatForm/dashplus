package ca.uwaterloo.watform.utils;

import ca.uwaterloo.watform.alloyinterface.Instance;
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

    public static Instance readInstance(String xmlPath) {
        try {
            return readInstanceUnchecked(xmlPath);
        } catch (IOException e) {
            throw UtilsUserError.fileNotFound(xmlPath, e.getMessage());
        } catch (ParserConfigurationException e) {
            throw UtilsImplError.xmlConfigError("reading " + xmlPath, e.getMessage());
        } catch (SAXException e) {
            throw UtilsError.malformedXml(xmlPath, e.getMessage());
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
    private static Instance readInstanceUnchecked(String xmlPath)
            throws ParserConfigurationException, SAXException, IOException {

        Document doc = DOC_FACTORY.newDocumentBuilder().parse(new File(xmlPath));
        doc.getDocumentElement().normalize();

        Map<String, Set<List<String>>> result = new HashMap<>();

        NodeList relations = doc.getElementsByTagName(XmlConstants.RELATION);
        for (int i = 0; i < relations.getLength(); i++) {
            Element rel = (Element) relations.item(i);
            String name = rel.getAttribute(XmlConstants.RELATION_NAME_ATTR);

            Set<List<String>> tuples = new HashSet<>();
            NodeList tupleNodes = rel.getElementsByTagName(XmlConstants.TUPLE);
            for (int j = 0; j < tupleNodes.getLength(); j++) {
                String raw = tupleNodes.item(j).getTextContent().trim();
                List<String> tuple = splitTrimmedNonEmpty(raw, ",");
                tuples.add(tuple);
            }

            result.put(name, tuples);
        }

        return new Instance(result);
    }

    // inverse of strCommaList-style joining: splits s on delim, trims each piece,
    // and drops any pieces that are empty after trimming
    private static List<String> splitTrimmedNonEmpty(String s, String delim) {
        List<String> pieces = Arrays.asList(s.split(delim));
        List<String> trimmed = GeneralUtil.mapBy(pieces, String::trim);
        return GeneralUtil.filterBy(trimmed, piece -> !piece.isEmpty());
    }
}
