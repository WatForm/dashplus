package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMImports extends SMSigs {

    public HashMap<String, List<ImportData>> importTable = new LinkedHashMap<>();

    protected SMImports() {}

    protected SMImports(SMImports other) {
        super(other);
        this.importTable = new HashMap<>(other.importTable);
    }

    protected void createImport(
            Pos pos, String nameSpace, String importedModule, List<Qname> sigParamValues) {
        this.importTable
                .computeIfAbsent(nameSpace, k -> new ArrayList())
                .add(new ImportData(pos, importedModule, sigParamValues));
    }

    protected void resolveSMImports() {
        // check values substituted on imports are okay
        // because o/w error will show up a weird place
        List<Qname> resolvedSigParamValues;
        for (String ns : importTable.keySet()) {
            for (ImportData id : this.importTable.get(ns)) {
                resolvedSigParamValues = emptyList();
                for (Qname sigName : id.sigParamValues) {
                    List<Qname> possibleMatches = this.sigQnameMatches(sigName);
                    if (possibleMatches.size() != 1) {
                        throw AlloyModelError.ambiguousSigRef(id.pos, sigName.toString());
                    } else {
                        // put resolved sig name in list
                        resolvedSigParamValues.add(possibleMatches.get(0));
                    }
                }
                id.sigParamValues = resolvedSigParamValues;
            }
        }
    }

    public void debugSMImports() {
        // written by ChatGPT
        StringBuilder sb = new StringBuilder("SMImports:\n");

        for (Map.Entry<String, List<ImportData>> entry : this.importTable.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" -> [");
            sb.append(String.join(", ", mapBy(entry.getValue(), i -> i.toString())));
            sb.append("]");
            sb.append("\n");
        }

        System.out.println(sb.toString() + "\n");
    }

    // Mathew - what accessor functions should be here?

}
