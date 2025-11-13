package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.expr.var.AlloySigRefExpr;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloymodel.alloytype.AlloyTyp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// The ctor sigs arg already doesn't contain duplicates
// 1) var sigs look at parent: var sig's parent is not static
// 2) type sig look at parent: doens't extend from subset sig
// 3) check acyclic dependency by populating this.ancestors
// 		- this table eliminates redundant recursion
// 		- useful for step 4
// 4) every sig's ancestors: no field name duplication
// 		- sig name duplication already check in AlloyModel.AlloyModelTable<AlloySigPara> ctor
// 		- not concerned about type yet, just name duplication
// 5) populate this.symbolTable
// 		1) look up branch, stop at first type sig or top-level sig
// 		- this is useful in AlloyModel and step below
// 6) check subset sig with overlapping types don't have same fields:
//      1) hashmap <string field, set<AlloyRelTy>(the type of sigs in which the field occur)>
//      2) iterate thru sig map, add to hashmap and check for overlap
//          1) newType = get type of sig (from this.symbolTable)
//          2) compare against the set already in the hashmap
//          	- newType cannot be in the set
//          	- newType is not the ancestor of anything in the set
//          	- anything in the set is not ancestor of newType
//          	- add to set
public final class AlloyModelSigVerifier {
    private final AlloyModelTable<AlloySigPara> sigs;
    // same as AlloyModel.sigs
    private final Map<AlloySigPara, Set<AlloySigPara>> ancestors;
    // if Set<AlloySigPara> is empty, this is a top-level sig
    private final Map<String, AlloyTyp> symbolTable;

    public AlloyModelSigVerifier(AlloyModelTable<AlloySigPara> sigs) {
        this.sigs = sigs;
        this.ancestors = new HashMap<>();
        this.symbolTable = new HashMap<>();

        // step 1
        this.verifyVar();
    }

    private List<AlloySigPara> getParents(AlloySigPara sig) {
        if (sig.isTopLevel()) {
            return Collections.emptyList();
        } else if (sig.isSubsig()) {
            AlloySigRefExpr sigRefExpr = ((AlloySigPara.Extends) sig.rel.get()).sigRef;
        }
        return Collections.emptyList();
    }

    private void verifyVar() {
        for (AlloySigPara sig : this.sigs.getAllParagraphs()) {
            if (!sig.isVar()) {
                continue;
            }
        }
    }
}
