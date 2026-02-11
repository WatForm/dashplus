package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.AlloyToTlaHelpers.repeatedAnd;
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

    // facts with string descriptions are part of the comments

    public static void translate(AlloyModel alloyModel, TlaModel tlaModel, boolean verbose) {
        List<String> factNames = new ArrayList<>();
        List<String> comments = new ArrayList<>();

        alloyModel
                .getParas(AlloyFactPara.class)
                .forEach(
                        fp -> {
                            System.out.println(fp.qname);
                            System.out.println(fp.strLit);
                            fp.block.exprs.forEach(exp -> ExprTranslate.translate(exp));
                        });

        tlaModel.addDefn(TlaDefn(ALL_FACTS, repeatedAnd(mapBy(factNames, fn -> TlaAppl(fn)))));
    }
}
