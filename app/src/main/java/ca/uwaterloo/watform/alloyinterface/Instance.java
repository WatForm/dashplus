/*
	TODO Does not yet handle seq/Int, maxseq,
	Ignore the following: mintrace, maxtrace, tracelength, backloop
*/

package ca.uwaterloo.watform.alloyinterface;

import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.utils.ImplementationError;
import java.io.StringReader;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Instance {
  private Map<Qname, SigValue> sigs = new HashMap<>();
  private Map<Qname, FieldValue> fields = new HashMap<>();
  private Integer maxInt;
  private Integer minInt;

  private static String alloyName(String name) {
    if (name.contains("$")) {
      return name.replace("$", "ʃ");
    } else {
      return name;
    }
  }

  private static Set<String> getAtoms(Element sig) {
    NodeList atoms = sig.getElementsByTagName("atom");
    Set<String> atomSet = new HashSet<String>();
    for (int j = 0; j < atoms.getLength(); j++) {
      Element atom = (Element) atoms.item(j);
      String atomLabel = atom.getAttribute("label");
      atomSet.add(alloyName(atomLabel));
    }
    return atomSet;
  }

  private Qname getQnameOf(Integer id) {
    for (Qname qname : this.sigs.keySet()) {
      if (this.sigs.get(qname).id == id) return qname;
    }
    throw ImplementationError.shouldNotReach();
  }

  public Instance(String xml) {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(xml)));

      NodeList instance = doc.getElementsByTagName("instance");
      // expect only one instance
      Integer bitwidth = Integer.parseInt(((Element) instance.item(0)).getAttribute("bitwidth"));
      this.minInt = -(1 << (bitwidth - 1)); // 2^(bitWidth-1)
      this.maxInt = (1 << (bitwidth - 1)) - 1; // 2^(bitWidth-1) - 1
      NodeList sigs = doc.getElementsByTagName("sig");
      Qname qname;
      for (int i = 0; i < sigs.getLength(); i++) {
        Element sig = (Element) sigs.item(i);
        String label = sig.getAttribute("label");
        if (label.equals("Int") || (label.equals("univ"))) {
          // TODO: not sure how to handle this
          // possibly just ignore?  what about univ?
        } else {
          Integer id = Integer.parseInt(sig.getAttribute("ID"));
          Integer parentId;
          if (sig.getAttribute("parentID") != "") {
            parentId = Integer.parseInt(sig.getAttribute("parentID"));
          } else {
            parentId = null;
          }
          if (label.equals("String") || label.equals("univ")) {
            // does not have a Qname
            qname = thisQname(label);
          } else {
            // a/b becomes Qname(a, b)
            int lastSlash = label.lastIndexOf('/');
            String a = label.substring(0, lastSlash).trim();
            String b = label.substring(lastSlash + 1).trim();
            qname = nameSpaceQname(a, b);
          }
          this.sigs.put(qname, new SigValue(getAtoms(sig), id, parentId));
        }
      }
      // set extendsChildren attribute
      for (Qname q : this.sigs.keySet()) {
        if (this.sigs.get(q).parentId != null) {
          this.sigs.get(getQnameOf(this.sigs.get(q).parentId)).addExtendsChild(q);
        }
      }

      // fields
      NodeList fields = doc.getElementsByTagName("field");
      for (int i = 0; i < fields.getLength(); i++) {
        Element field = (Element) fields.item(i);
        String label = field.getAttribute("label");
        // sometimes a field appears like this in the XML (if it is overloaded?)
        if (label.contains(AlloyStrings.DOMRESTR)) {
          // a/b <: f becomes Qname(a, b, f)
          int subtype = label.indexOf(AlloyStrings.DOMRESTR);

          String left = label.substring(0, subtype).trim();
          String f = label.substring(subtype + 2).trim();
          int lastSlash = left.lastIndexOf('/');
          String a = left.substring(0, lastSlash).trim();
          String b = left.substring(lastSlash + 1).trim();
          qname = fieldQname(a, b, f);
        } else {
          // sometimes we have to use its parent id to get the qname
          Integer parentId = Integer.parseInt(field.getAttribute("parentID"));
          qname = null;
          for (Qname q : this.sigs.keySet()) {
            if (parentId.equals(this.sigs.get(q).id)) {
              qname = q;
            }
          }
          assert (qname != null);
          String qnameFullName = qname.fullName();
          int lastSlash = qnameFullName.lastIndexOf('/');
          String a = qnameFullName.substring(0, lastSlash).trim();
          String b = qnameFullName.substring(lastSlash + 1).trim();
          qname = fieldQname(a, b, label);
        }
        // NodeList typesList = field.getElementsByTagName("types");
        // String parentId = field.getAttribute("parentID");
        // xmlFieldNames.add(alloyName(label)); //+" of " + idToSigInfo.get(parentId).label);
        NodeList xmlTuples = field.getElementsByTagName("tuple");
        Element xmlTuple;
        Set<List<String>> tupleSet = new HashSet<List<String>>();

        for (int k = 0; k < xmlTuples.getLength(); k++) {
          List<String> tuple = new ArrayList<>();
          xmlTuple = (Element) xmlTuples.item(k);
          NodeList atoms = xmlTuple.getElementsByTagName("atom");
          for (int j = 0; j < atoms.getLength(); j++) {
            Element atom = (Element) atoms.item(j);
            String atomLabel = atom.getAttribute("label");
            tuple.add(alloyName(atomLabel));
          }
          tupleSet.add(tuple);
        }
        this.fields.put(qname, new FieldValue(tupleSet));
      }

      this.debugInstance();
      instanceChecks();
    } catch (Exception e) {
      // sends it up to main
      throw new RuntimeException(e);
    }
  }

  protected void setMaxInt(Integer maxInt) {
    this.maxInt = maxInt;
  }

  protected void setMinInt(Integer minInt) {
    this.minInt = minInt;
  }

  public Set<List<String>> getAllValues(Qname qname) {
    // qname could be a sig or a field
    // if a sig, collect
    if (qname.isFieldQname() && this.fields.keySet().contains(qname)) {
      return this.fields.get(qname).values();
    } else if (this.sigs.keySet().contains(qname)) {
      Set<List<String>> ret = this.fields.get(qname).values();
      // recursion ends when no children
      for (Qname k : this.sigs.get(qname).extendsChildren()) {
        ret.addAll(getAllValues(k));
      }
      return ret;
    } else {
      // can't just return emptySet() b/c a found qname could be empty
      throw ImplementationError.shouldNotReach();
    }
  }

  protected void instanceChecks() {
    // all tuples in set of values of a qname must be the same length
    // elements in tuples must match types
    // minInt must be smaller than maxInt
  }

  public record SigValue(
      Set<String> values, Integer id, Integer parentId, List<Qname> extendsChildren) {
    public SigValue(Set<String> values, Integer id, Integer parentId) {
      this(values, id, parentId, emptyList());
    }

    public void addExtendsChild(Qname qname) {
      this.extendsChildren.add(qname);
    }
    // parentId might be null if subset sig
  }

  // we might add types later?
  public record FieldValue(Set<List<String>> values) {}

  public Set<Qname> allSigNames() {
    return this.sigs.keySet();
  }

  public Set<Qname> allFieldNames() {
    return this.fields.keySet();
  }

  public Integer minInt() {
    return this.minInt;
  }

  public Integer maxInt() {
    return this.maxInt;
  }

  public void debugInstance() {
    System.out.println("Instance:");
    System.out.println("Min int " + this.minInt);
    System.out.println("Max int " + this.maxInt);
    System.out.println("Sigs");
    for (Qname qname : this.sigs.keySet()) {
      System.out.println(qname + " -> " + this.sigs.get(qname));
    }
    System.out.println("Fields");
    for (Qname qname : this.fields.keySet()) {
      System.out.println(qname + " -> " + this.fields.get(qname));
    }
  }
}
