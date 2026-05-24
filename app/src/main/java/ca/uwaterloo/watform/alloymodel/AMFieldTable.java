/*
    A data structure to hold the relations (fields) of an Alloy Model.
    It does nothing on initialization, because it is populated by the Sig
    initialization.
    Enforces that all field names in the AlloyModel are distinct.
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.paragraph.AlloyPara.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.exprvisitor.TestAndCollectVarsExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;
import java.util.function.BiFunction;

public class AMFieldTable {

    // there are no AlloyParagrapsh that directly match this table
    // its data comes from within sigParas
    // final put can still change inside of it
    private Map<String, FieldData> fieldTable = new LinkedHashMap<String, FieldData>();

    protected AMFieldTable(AlloyFile alloyFile) {
        // no super because this is a root class
        // rest of initialization from alloyFile is done in AMSigs
    }

    protected AMFieldTable(AMFieldTable other) {
        // we assume this has all arities and default mul set
        this.fieldTable = new LinkedHashMap<>(other.fieldTable);
    }

    public List<String> allFields() {
        return setToList(this.fieldTable.keySet());
    }

    // might be allFields (on initialization) or some fields from sigs added (on addSig)
    // sets arities in fieldTable and default mul in field Types
    protected void resolve(
            BiFunction<AlloyExpr, String, CalcAritySetMulDefaultsExprVis.Result>
                    fieldArityAndSetMul) {

        // set up for getChildren function
        TestAndCollectVarsExprVis collect =
                new TestAndCollectVarsExprVis(
                        // test if AlloyQnameVar's name is a fieldName
                        n -> this.allFields().contains(n));

        // throws error if cycles
        // otherwise returns topoOrder
        List<String> topoOrderFields =
                DetectCycles.topoOrderCycleDetector(
                        this.allFields(),
                        // getChildren: any field names used in fieldType
                        f -> setToList(collect.visit(this.fieldTable.get(f).fieldExpr())));
        // no need to check what was visited (return value of detectCycles)
        // afterwards b/c started from
        // all fields unlike in detecting cycles in sigs where start
        // only from top-level sigs

        for (String fieldName : topoOrderFields) {
            AlloyExpr fieldExpr = this.fieldTable.get(fieldName).fieldExpr();
            // throws an error if it can't calculate it and set defaults
            // System.out.println(fieldName);
            // System.out.println(fieldExpr);
            // this is a field expression so we need the Boolean arg to arityAndSetMul to be True
            CalcAritySetMulDefaultsExprVis.Result result =
                    fieldArityAndSetMul.apply(fieldExpr, this.fieldParent(fieldName));

            // fields have +1 in arity than returned here b/c of sig
            int fieldArity = result.arity.map(b -> b + 1).orElse(null);
            // System.out.println(fieldArity);
            this.fieldTable.get(fieldName).setFieldArity(Optional.of(fieldArity));
            this.fieldTable.get(fieldName).setFieldExpr(result.exp);
        }
    }

    // no one outside AlloyModel should add a field directly
    // one can only be added via a sigPara
    // allSigs is needed to check for duplicates
    // duplicates are checked in order of addition of sig/fields, another sig/fields, etc
    // so allSigs is for all sigs seen so far
    // don't just pass the AlloyDecl field, because it may contain multiple fields
    protected void addToFieldTable(List<AlloyDecl> fields, String sigParent) {
        for (AlloyDecl field : fields) {
            // may be multiple field decls x,y: ...
            for (AlloyQnameExpr qname : field.qnames) {
                for (AlloyVarExpr var : qname.vars) {
                    String fieldName = var.getName();
                    AlloyExpr fieldExpr = field.expr;
                    if (this.allFields().contains(fieldName)) {
                        throw AlloyModelError.sigNameIsFieldName(field.pos, field.toString());
                    } else {
                        this.entry(
                                field.pos,
                                fieldName,
                                new FieldData(fieldName, fieldExpr, sigParent));
                    }
                }
            }
        }
    }

    private void entry(Pos p, String fieldName, FieldData fd) {
        if (this.allFields().contains(fieldName))
            throw AlloyModelError.duplicateFieldName(p, fieldName);
        else this.fieldTable.put(fieldName, fd);
    }

    // individual getters

    protected AlloyExpr fieldExpr(String name) {
        // TODO: check it exists
        return this.fieldTable.get(name).fieldExpr();
    }

    protected Optional<Integer> fieldArity(String name) {
        // TODO: check it exists
        return this.fieldTable.get(name).fieldArity();
    }

    protected String fieldParent(String name) {
        // TODO: check it exists
        return this.fieldTable.get(name).fieldParent();
    }

    // checks

}
