/*
   All the getters/setters to handle sigs.

   On initialization:
    - initializes sigTable
    - adds sig children
    - checks for cycles in sig (in and extends relations) and field declarations
    - calculates arity of fields

    Terminology
    sigName, fieldName = String that is name of sig or field
    sigPara, field = sigParagraph or AlloyDecl that is a field
    sigNames, fieldNames = string list of sig names or field names
    allSigs = string list of all sig names
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.exprvisitor.ReplaceExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class AMThisSigParas extends AMThisEnumParas {

    protected List<AlloySigPara> sigs = emptyList();

    // init ------------------

    protected AMThisSigParas() {}

    protected AMThisSigParas(AMThisSigParas other) {
        super(other);
        this.sigs = new ArrayList<AlloySigPara>(other.sigs);
    }

    /*
    private void addPara(AlloySigPara multiSigPara, String nameSpace) {
        addSMPara(multiSigPara, nameSpace);
        this.sigs.add(multiSigPara);
    }
    */

    public void addPara(AlloySigPara multiSigPara) {
        addSMPara(multiSigPara, THIS_NAMESPACE);
        this.sigs.add(multiSigPara);
    }

    protected void addSMPara(AlloySigPara multiSigPara, String nameSpace) {
        for (AlloySigPara sigPara : multiSigPara.expand()) {
            String sigName = sigPara.getName();
            Qname sigQname = nameSpaceQname(nameSpace, sigName);
            this.createSig(sigPara.pos, sigQname, sigParaSigData(sigPara, nameSpace));
            if (sigPara.quals.contains(AlloySigPara.Qual.ONE))
                this.createNonOrderedSigWithExactScopeValue(sigQname, 1);

            // get all the field names of this sig (needed for desugaring)
            List<String> allFieldNames = emptyList();
            for (AlloyDecl field : sigPara.fields) {
                for (AlloyQnameExpr qname : field.qnames) {
                    allFieldNames.add(qname.getName());
                }
            }

            for (AlloyDecl field : sigPara.fields) {
                AlloyExpr fieldExpr = field.expr;
                // may be multiple decls x,y: ... in a field
                for (AlloyQnameExpr qname : field.qnames) {
                    String fieldName = qname.getName();
                    // in resolve we ensure that this fieldExpr only contains fields from this sig
                    // TODO: should we allow the fieldExpr to contain the fieldName?
                    this.createField(field.pos, nameSpace, fieldName, sigName, fieldExpr);
                    // NAD: put bounding expression as a constraint
                    // this could be redundant -- not sure about this for every case
                    // desugar (f in fieldExpr)
                    // fields from other sigs cannot appear here in AA
                    // but they won't be removed here so they could be allowed past resolve
                    this.createConstraint(
                            nameSpace,
                            this.desugar(
                                    AlloyIn(new AlloyQnameExpr(fieldName), fieldExpr),
                                    sigQname,
                                    allFieldNames,
                                    nameSpace));
                }
            }
            if (sigPara.block.isPresent()) {
                for (AlloyExpr sigFact : sigPara.block.get().exprs) {
                    // can fields from other sigs appear in these?
                    this.createConstraint(
                            nameSpace, desugar(sigFact, sigQname, allFieldNames, nameSpace));
                }
            }
        }
    }

    private SigData sigParaSigData(AlloySigPara p, String nameSpace) {
        // this is data for a single sigPara
        SigData sd = new SigData();
        sd.pos = p.pos;
        if (p.rel.isPresent()) {
            if (p.rel.get() instanceof AlloySigPara.Extends e) {
                // this includes one sigs because they are extensions
                sd.extendsParent = Optional.of(nameSpaceQname(nameSpace, e.sigRef.getName()));
                sd.inParents = emptyList();
            } else if (p.rel.get() instanceof AlloySigPara.In e) {
                sd.extendsParent = Optional.empty();
                sd.inParents = mapBy(e.sigRefs, s -> nameSpaceQname(nameSpace, s.getName()));
                if (p.quals.contains(AlloySigPara.Qual.ABSTRACT)) {
                    throw AlloyModelError.subsetSigsCannotBeAbstrast(p.pos, p.toString());
                }
            } else if (p.rel.get() instanceof AlloySigPara.Equal e) {
                // sig A = B + C {}
                // means
                // sig A in B + C {}
                // fact { A = B + C }
                // it is not extends
                sd.extendsParent = Optional.empty();
                sd.inParents = mapBy(e.sigRefs, s -> nameSpaceQname(nameSpace, s.getName()));
            } else {
                sd.isTopLevelSig = true;
            }
        } else {
            sd.extendsParent = Optional.empty();
            sd.inParents = emptyList();
            sd.isTopLevelSig = true;
        }
        if (p.quals.contains(AlloySigPara.Qual.ABSTRACT)) sd.isAbstractSig = true;
        if (p.quals.contains(AlloySigPara.Qual.ONE)) sd.isOneSig = true;
        if (p.quals.contains(AlloySigPara.Qual.SOME)) sd.isSomeSig = true;
        if (p.quals.contains(AlloySigPara.Qual.LONE)) sd.isLoneSig = true;
        return sd;
    }

    private static Boolean desugarTest(AlloyExpr e, List<String> allFieldNames) {
        return (e instanceof AlloyThisExpr
                || e instanceof AlloyAtNameExpr
                || (e instanceof AlloyQnameExpr
                        && allFieldNames.contains(((AlloyQnameExpr) e).getName())));
    }

    private static String THIS_VAR = "this_var";

    private static AlloyExpr THIS_REPLACEMENT(Pos p) {
        return thisQname(THIS_VAR).toAlloyExpr(p, Kind.SIG);
    }

    private static AlloyExpr desugarReplace(
            AlloyExpr e, Qname sigParent, List<String> allFieldNames, String nameSpace) {
        if (e instanceof AlloyThisExpr) {
            // TODO: perhaps not a great choice?
            return THIS_REPLACEMENT(e.pos);
        } else if (e instanceof AlloyAtNameExpr) {
            String atName = ((AlloyAtNameExpr) e).getName();
            // @f becomes resolvedSigParent <: f
            if (!allFieldNames.contains(atName))
                throw AlloyModelError.cantAtNonFieldOfThisSig(atName);
            return fieldQname(sigParent.nameSpace, sigParent.name, atName)
                    .toAlloyExpr(e.pos, Kind.FIELD);
        } else if (e instanceof AlloyQnameExpr) {
            // we know it is a field from this sig b/c of test
            // replace any field (that is not with @ on the outside) with this.((resolvedSigParent)
            // <: f)
            Qname fieldQname =
                    fieldQname(nameSpace, sigParent.name, ((AlloyQnameExpr) e).getName());
            AlloyExpr sig = sigParent.toAlloyExpr(e.pos, Kind.SIG);
            AlloyExpr field = fieldQname.toAlloyExpr(e.pos, Kind.FIELD);
            return AlloyJoin(sig, field);
        } else {
            throw ImplementationError.shouldNotReach();
        }
    }

    static AlloyExpr desugar(
            AlloyExpr expr, Qname sigParent, List<String> allFieldNames, String nameSpace) {
        // note: leaves uses of "this" inside expression as they are
        AlloyExpr newExpr =
                new ReplaceExprVis(
                                e -> desugarTest(e, allFieldNames),
                                e -> desugarReplace(e, sigParent, allFieldNames, nameSpace))
                        .visit(expr);
        // put "all AlloyThisExpr:A |"" on outside
        return AlloyAllVars(
                AlloyDecl(THIS_VAR, AlloyQtEnum.ONE, sigParent.toAlloyExpr(expr.pos, Kind.SIG)),
                newExpr);
    }

    // API ---------------------

    // commonly used creation of sigs
    // two of all of these b/c common case is only working in THIS namespace

    // adding: sig "n" {}
    public void addSig(String sigName) {
        this.addPara(new AlloySigPara(sigName));
    }

    // adding: abstract sig "n" {}
    public void addAbstractSig(String sigName) {
        this.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(sigName)),
                        null,
                        Collections.emptyList(),
                        null));
    }

    // adding: abstract sig "child" extends "parent" {}
    public void addAbstractExtendsSig(String childName, String parentName) {
        this.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ABSTRACT),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parentName)),
                        Collections.emptyList(),
                        null));
    }

    // adding: one sig child extends parent {}
    public void addOneExtendsSig(String childName, String parentName) {
        this.addPara(
                new AlloySigPara(
                        List.of(AlloySigPara.Qual.ONE),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parentName)),
                        Collections.emptyList(),
                        null));
    }

    // adding: sig child extends parent {}
    public void addExtendsSig(String childName, String parentName) {
        this.addPara(
                new AlloySigPara(
                        Collections.emptyList(),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.Extends(new AlloyQnameExpr(parentName)),
                        Collections.emptyList(),
                        null));
    }

    // adding: sig child in parent {}
    public void addInSig(String childName, List<AlloySigRefExpr> parentExprs) {
        this.addPara(
                new AlloySigPara(
                        Collections.emptyList(),
                        List.of(new AlloyQnameExpr(childName)),
                        new AlloySigPara.In(parentExprs),
                        Collections.emptyList(),
                        null));
    }

    public List<AlloySigPara> allSigParas() {
        return new ArrayList<AlloySigPara>(this.sigs);
    }
}
