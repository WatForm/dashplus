package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.*;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class FactsA2T extends CommandA2T {

    public FactsA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
        super(alloyModel, verbose, debug);
    }

    private int count = 0; // used to number un-named facts

    public String generateFactName() {
        count += 1;
        return unnamedFact(count);
    }

    protected void addFacts(TlaModel tlaModel) {

        tlaModel.addComment("facts", verbose);

        List<String> factNames = new ArrayList<>();
        List<String> comments = new ArrayList<>();
        List<AlloyFactPara> factParas = alloyModel.allFactParas();

        for (var fp : factParas) {
            String factName = generateFactName();
            factNames.add(factName);
            fp.qname.ifPresent(n -> comments.add(factName + " -> " + n));
            fp.strLit.ifPresent(str -> comments.add(factName + " -> " + str));

            tlaModel.addDefn(TlaDefn(factName, translateSnippet(fp.block)));
        }

        tlaModel.addDefn(TlaDefn(ALL_FACTS, repeatedAnd(mapBy(factNames, fn -> TlaAppl(fn)))));

        comments.forEach(c -> tlaModel.addComment(c, verbose));
    }
}
