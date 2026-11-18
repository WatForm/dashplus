/*
	all facts of an Alloy model (including ones from sigs and imports)
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.PredFunData.*;
// import static ca.uwaterloo.watform.parser.AlloyParser.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyQtEnum;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyexprvisitor.ReplaceExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMConstraints extends SMPredFuns {

  // ignore the name of the fact
  // we just care about the namespace
  public record Constraint(String nameSpace, AlloyExpr expr, Boolean resolved) {
    @Override
    public String toString() {
      return String.format("[%s] %s (resolved=%s)", nameSpace, expr, resolved);
    }
  }

  private record SignatureConstraint(Qname sig, AlloyExpr expr) {}

  private List<SignatureConstraint> pendingSignatureConstraints = new ArrayList<>();

  List<Constraint> constraints = emptyList();

  protected SMConstraints() {}

  protected SMConstraints(SMConstraints other) {
    super(other);
    this.constraints = new ArrayList<Constraint>(other.constraints);
    this.pendingSignatureConstraints = new ArrayList<>(other.pendingSignatureConstraints);
  }

  protected void resolveSMConstraints(
      TriFunction<AlloyExpr, String, List<AlloyDecl>, ResolveInfo> resolve2) {
    // Parent links and fields have been resolved before this stage.
    for (SignatureConstraint constraint : pendingSignatureConstraints) {
      createConstraint(
          constraint.sig.nameSpace,
          desugar(constraint.expr, constraint.sig, inheritedFields(constraint.sig)));
    }
    pendingSignatureConstraints.clear();
    List<Constraint> newConstraints = emptyList();
    for (Constraint c : this.constraints) {
      if (!c.resolved) {
        ResolveInfo r = resolve2.apply(c.expr, c.nameSpace, emptyList());
        if (r.arity.isPresent()) {
          newConstraints.add(new Constraint(c.nameSpace, r.exp, true));
        } else {
          throw AlloyModelError.unknownArity(c.expr.pos, c.toString());
        }
      } else newConstraints.add(c);
    }
    this.constraints = newConstraints;
  }

  protected void deferSignatureConstraint(Qname sig, AlloyExpr expr) {
    if (this.createSM) pendingSignatureConstraints.add(new SignatureConstraint(sig, expr));
  }

  private static Boolean desugarTest(AlloyExpr e, Map<String, Qname> visibleFields) {
    return (e instanceof AlloyThisExpr
        || e instanceof AlloyAtNameExpr
        || (e instanceof AlloyQnameExpr
            && visibleFields.containsKey(((AlloyQnameExpr) e).getName())));
  }

  private static String THIS_VAR = "this_var";

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
    AlloyExpr newExpr =
        new ReplaceExprVis(
                e -> desugarTest(e, visibleFields), e -> desugarReplace(e, visibleFields), true)
            .visit(expr);
    // put "all AlloyThisExpr:A |"" on outside
    AlloyExpr e =
        AlloyAllVars(
            AlloyDecl(THIS_VAR, AlloyQtEnum.ONE, sigParent.toAlloyExpr(expr.pos, Kind.SIG)),
            newExpr);
    // System.out.println(e);
    return e;
  }

  public void createConstraint(String nameSpace, AlloyExpr expr) {
    if (this.createSM) this.constraints.add(new Constraint(nameSpace, expr, false));
  }

  public List<AlloyExpr> allConstraints() {
    return mapBy(this.constraints, r -> r.expr);
  }

  public void debugSMConstraints() {
    StringBuilder sb = new StringBuilder("SMConstraints:\n");

    constraints.forEach(c -> sb.append("    ").append(c.toString()).append('\n'));

    System.out.println(sb.toString() + "\n");
  }
}
