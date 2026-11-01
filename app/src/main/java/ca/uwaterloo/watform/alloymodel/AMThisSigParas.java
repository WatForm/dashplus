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
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyexprvisitor.ReplaceExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class AMThisSigParas extends AMThisEnumParas {

  protected List<AlloySigPara> sigs = emptyList();

  private record SignatureConstraint(Qname sig, AlloyExpr expr) {}

  private List<SignatureConstraint> pendingSignatureConstraints = new ArrayList<>();

  // init ------------------

  protected AMThisSigParas() {}

  protected AMThisSigParas(AMThisSigParas other) {
    super(other);
    this.sigs = new ArrayList<AlloySigPara>(other.sigs);
    this.pendingSignatureConstraints = new ArrayList<>(other.pendingSignatureConstraints);
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
      // System.out.println("creating sig: " + sigQname.toString());
      this.createSig(sigPara.pos, sigQname, sigParaSigData(sigPara, nameSpace));
      if (sigPara.quals.contains(AlloySigPara.Qual.ONE))
        this.createNonOrderedSigWithExactScopeValue(sigQname, 1);

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
          deferSignatureConstraint(sigQname, AlloyIn(new AlloyQnameExpr(fieldName), fieldExpr));
        }
      }
      if (sigPara.block.isPresent()) {
        for (AlloyExpr sigFact : sigPara.block.get().exprs) {
          // can fields from other sigs appear in these?
          deferSignatureConstraint(sigQname, sigFact);
        }
      }
    }
  }

  private void deferSignatureConstraint(Qname sig, AlloyExpr expr) {
    if (this.createSM) pendingSignatureConstraints.add(new SignatureConstraint(sig, expr));
  }

  @Override
  protected void resolveSMConstraints(
      TriFunction<AlloyExpr, String, List<AlloyDecl>, ResolveInfo> resolve2) {
    // All signatures, fields and parent links are available now, regardless of declaration order.
    for (SignatureConstraint constraint : pendingSignatureConstraints) {
      createConstraint(
          constraint.sig.nameSpace,
          desugar(constraint.expr, constraint.sig, inheritedFields(constraint.sig)));
    }
    pendingSignatureConstraints.clear();
    super.resolveSMConstraints(resolve2);
  }

  private Map<String, Qname> inheritedFields(Qname sigQname) {
    Set<Qname> ancestors = new LinkedHashSet<>();
    Deque<Qname> remaining = new ArrayDeque<>();
    remaining.add(sigQname);

    while (!remaining.isEmpty()) {
      Qname current = remaining.removeFirst();
      if (!ancestors.add(current)) continue;

      remaining.addAll(this.allParents(current));
    }

    Map<String, Qname> fields = new LinkedHashMap<>();
    for (Qname field : this.allFieldQnames()) {
      boolean inherited =
          ancestors.stream()
              .anyMatch(
                  ancestor ->
                      ancestor.nameSpace.equals(field.nameSpace)
                          && ancestor.name.equals(field.sigParent));
      if (inherited) fields.put(field.name, field);
    }
    return fields;
  }

  private SigData sigParaSigData(AlloySigPara p, String nameSpace) {
    // this is data for a single sigPara
    SigData sd = new SigData();
    sd.pos = p.pos;
    if (p.rel.isPresent()) {
      if (p.rel.get() instanceof AlloySigPara.Extends e) {
        // this includes one sigs because they are extensions
        sd.extendsParent = Optional.of(unknownQname(e.sigRef.getName()));
        sd.inParents = emptyList();
      } else if (p.rel.get() instanceof AlloySigPara.In e) {
        sd.extendsParent = Optional.empty();
        sd.inParents = mapBy(e.sigRefs, s -> unknownQname(s.getName()));
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
        sd.inParents = mapBy(e.sigRefs, s -> unknownQname(s.getName()));
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

  private static Boolean desugarTest(AlloyExpr e, Map<String, Qname> visibleFields) {
    return (e instanceof AlloyThisExpr
        || e instanceof AlloyAtNameExpr
        || (e instanceof AlloyQnameExpr
            && visibleFields.containsKey(((AlloyQnameExpr) e).getName())));
  }

  private static String THIS_VAR = "this_var";

  private static AlloyExpr THIS_REPLACEMENT(Pos p) {
    return thisQname(THIS_VAR).toAlloyExpr(p, Kind.SIG);
  }

  private static AlloyExpr desugarReplace(AlloyExpr e, Map<String, Qname> visibleFields) {
    if (e instanceof AlloyThisExpr) {
      // TODO: perhaps not a great choice?
      return AlloyVar(THIS_VAR);
    } else if (e instanceof AlloyAtNameExpr) {
      String atName = ((AlloyAtNameExpr) e).getName();
      // @f becomes resolvedSigParent <: f if it is a field of this sig
      if (!visibleFields.containsKey(atName)) {
        // this is tricky because @ is allowed on non-field of this sig names
        // could be a field of some other sig or some parent sig
        // lets try just removing the at since this formula
        // will be put in a regular constraint not in a sig fact anymore
        return ((AlloyAtNameExpr) e).name; // this is a AlloyQnameExpr
        // throw AlloyModelError.cantAtNonFieldOfThisSig(e.pos, atName);
      } else {
        return visibleFields.get(atName).toAlloyExpr(e.pos, Kind.FIELD);
      }
    } else if (e instanceof AlloyQnameExpr) {
      // we know it is a field from this sig b/c of test
      // replace any field (that is not with @ on the outside) with
      // this_var.((resolvedSigParent) <: f)
      Qname fieldQname = visibleFields.get(((AlloyQnameExpr) e).getName());
      // (resolvedSigParent) <: f
      AlloyExpr field = fieldQname.toAlloyExpr(e.pos, Kind.FIELD);
      return AlloyJoin(AlloyVar(THIS_VAR), field);
    } else {
      throw ImplementationError.shouldNotReach();
    }
  }

  static AlloyExpr desugar(AlloyExpr expr, Qname sigParent, Map<String, Qname> visibleFields) {
    AlloyExpr newExpr = new SignatureFactDesugarVis(visibleFields).visit(expr);
    // put "all AlloyThisExpr:A |"" on outside
    AlloyExpr e =
        AlloyAllVars(
            AlloyDecl(THIS_VAR, AlloyQtEnum.ONE, sigParent.toAlloyExpr(expr.pos, Kind.SIG)),
            newExpr);
    // System.out.println(e);
    return e;
  }

  private static final class SignatureFactDesugarVis extends ReplaceExprVis {
    private Set<String> boundNames = new HashSet<>();

    SignatureFactDesugarVis(Map<String, Qname> visibleFields) {
      super(e -> false, e -> desugarReplace(e, visibleFields));
      this.test =
          e ->
              desugarTest(e, visibleFields)
                  && !(e instanceof AlloyQnameExpr name && boundNames.contains(name.getName()));
    }

    private List<AlloyDecl> visitDeclarations(List<AlloyDecl> declarations) {
      List<AlloyDecl> result = new ArrayList<>();
      for (AlloyDecl declaration : declarations) {
        // A binding is visible in subsequent domains and the body, but not its own domain.
        result.add(declaration.rebuild(visit(declaration.expr)));
        for (AlloyQnameExpr name : declaration.qnames) boundNames.add(name.getName());
      }
      return result;
    }

    @Override
    public AlloyExpr visit(AlloyQuantificationExpr expr) {
      Set<String> outerNames = new HashSet<>(boundNames);
      try {
        List<AlloyDecl> declarations = visitDeclarations(expr.decls);
        return expr.rebuild(declarations, visit(expr.body));
      } finally {
        boundNames = outerNames;
      }
    }

    @Override
    public AlloyExpr visit(AlloyCphExpr expr) {
      Set<String> outerNames = new HashSet<>(boundNames);
      try {
        List<AlloyDecl> declarations = visitDeclarations(expr.decls);
        return expr.rebuild(declarations, expr.body.map(this::visit).orElse(null));
      } finally {
        boundNames = outerNames;
      }
    }

    @Override
    public AlloyExpr visit(AlloyLetExpr expr) {
      Set<String> outerNames = new HashSet<>(boundNames);
      try {
        List<AlloyLetExpr.AlloyLetAsn> assignments = new ArrayList<>();
        for (AlloyLetExpr.AlloyLetAsn assignment : expr.asns) {
          assignments.add(assignment.rebuild(visit(assignment.expr)));
          boundNames.add(assignment.qname.getName());
        }
        return expr.rebuild(assignments, visit(expr.body));
      } finally {
        boundNames = outerNames;
      }
    }
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
