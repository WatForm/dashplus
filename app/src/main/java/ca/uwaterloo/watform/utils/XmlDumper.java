package ca.uwaterloo.watform.utils;

import static ca.uwaterloo.watform.utils.CommonStrings.*;

import ca.uwaterloo.watform.alloyinterface.Solution;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class XmlDumper {

    // Safe to reuse — stateless after creation, thread-safe
    private static final DocumentBuilderFactory DOC_FACTORY = DocumentBuilderFactory.newInstance();
    private static final TransformerFactory TF_FACTORY = TransformerFactory.newInstance();

    // Prevent instantiation
    private XmlDumper() {}

    public static void dumpInstance(Solution soln, String outputPath, String context) {
        try {
            dumpInstanceUnchecked(soln, outputPath);
        } catch (ParserConfigurationException e) {
            dashOutput(
                    "Warning: XML configuration error while dumping "
                            + context
                            + ": "
                            + e.getMessage());
        } catch (TransformerException e) {
            dashOutput("Warning: failed to write XML for " + context + ": " + e.getMessage());
        } catch (ImplementationError e) {
            dashOutput(
                    "Warning: XML dump failed, the solution is unsatisfiable for "
                            + context
                            + ": "
                            + e.getMessage());
        }
    }

    public static void dumpModel(AlloyModel am, String outputPath) {
        try {
            dumpModelUnchecked(am, outputPath);
        } catch (ParserConfigurationException e) {
            dashOutput("Warning: XML configuration error while dumping model: " + e.getMessage());
        } catch (TransformerException e) {
            dashOutput("Warning: failed to write XML for model: " + e.getMessage());
        }
    }

    private static void dumpInstanceUnchecked(Solution soln, String outputPath)
            throws ParserConfigurationException, TransformerException, ImplementationError {
        if (!soln.isSat()) throw ImplementationError.shouldNotReach();
        // Recreated per call — not thread-safe
        Document doc = DOC_FACTORY.newDocumentBuilder().newDocument();
        Transformer tf = TF_FACTORY.newTransformer();

        Element root = doc.createElement("instance");
        doc.appendChild(root);

        for (String key : soln.getSolnMapKeys()) {
            Element rel = doc.createElement("relation");
            rel.setAttribute("name", key);
            for (List<String> tuple : soln.get(key)) {
                Element t = doc.createElement("tuple");
                t.setTextContent(String.join(", ", tuple));
                rel.appendChild(t);
            }
            root.appendChild(rel);
        }

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs(); // creates all missing parent dirs
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(doc), new StreamResult(new File(outputPath)));
    }

    private static void dumpModelUnchecked(AlloyModel model, String outputPath)
            throws ParserConfigurationException, TransformerException {
        Document doc = DOC_FACTORY.newDocumentBuilder().newDocument();
        Transformer tf = TF_FACTORY.newTransformer();

        Element root = doc.createElement("model");
        doc.appendChild(root);

        Element textNode = doc.createElement("modelText");
        textNode.setTextContent(model.toString());
        root.appendChild(textNode);

        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs(); // creates all missing parent dirs
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.transform(new DOMSource(doc), new StreamResult(new File(outputPath)));
    }
}
