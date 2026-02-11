package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedAnd;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.unnamedFact;
import static ca.uwaterloo.watform.alloytotla.AlloyToTlaStrings.ALL_FACTS;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaAppl;
import static ca.uwaterloo.watform.tlaast.CreateHelper.TlaDefn;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.alloyast.paragraph.AlloyFactPara;
import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.*;

public class Facts {

    static int count = 0; // used to number un-named facts

    public static String generateFactName() {
        count += 1;
        return unnamedFact(count);
    }

    // facts with string descriptions are part of the comments

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose) {

        List<String> factNames = new ArrayList<>();
        List<String> comments = new ArrayList<>();
        List<AlloyFactPara> factParas = alloyModel.getParas(AlloyFactPara.class);

        factParas.forEach(
                fp -> {
                    String factName = generateFactName();
                    factNames.add(factName);
                    fp.qname.ifPresent(n -> comments.add(factName + " -> " + n));
                    fp.strLit.ifPresent(str -> comments.add(factName + " -> " + str));

                    tlaModel.addDefn(
                            TlaDefn(
                                    factName,
                                    repeatedAnd(
                                            mapBy(
                                                    fp.block.exprs,
                                                    exp -> ExprTranslate.translate(exp)))));
                });

        tlaModel.addDefn(TlaDefn(ALL_FACTS, repeatedAnd(mapBy(factNames, fn -> TlaAppl(fn)))));

        comments.forEach(c -> tlaModel.addComment(c, verbose));
    }
}
