/*
    open util/traces[Snapshot] as snapshot

    fact dsh_traces_fact {
      DshSnapshot/first.dsh_initial
      (some DshSnapshot/back) =>
          (all s: DshSnapshot | (s.DshSnapshot/next).(s.dsh_small_step))
      else
        (all s:DshSnapshot - last | (s.DshSnapshot/next).(s.dsh_small_step))
    }

    "back" is the DshSnapshot that is looped back to in the trace to
    make it infinite
    "next" is the "Next" relation in traces plus the loop.  If back is empty,
    loop is empty and next=Next.  If back is a DshSnapshot, loop exists and
    next=Next+loop
    To permit both infinite and finite traces we have to have
    the two cases above.
    If we only have the if-clause above, then the "last" Snapshot must have a next
    forcing all traces to be infinite.


    pred no_stutter {
        all s:DshSnapshot |
        s = first or NO_TRANS not in s.dsh_taken0
    }

*/

package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class TracesFactD2A extends SmallStepD2A {

    protected TracesFactD2A(DashModel dm, TranslateOutput opt) {
        super(dm, opt);
    }

    public void addTracesFact() {
        assert (this.isTraces);

        // open util/traces[Snapshot] as Snapshot
        this.am.addImport(
                List.of(AlloyStrings.utilName, D2AStrings.tracesName), D2AStrings.snapshotName);

        List<AlloyExpr> body = this.dsl.emptyExprList();

        AlloyExpr snapShotFirst =
                AlloyVar(D2AStrings.snapshotName + "/" + D2AStrings.tracesFirstName);
        AlloyExpr snapShotLast =
                AlloyVar(D2AStrings.snapshotName + "/" + D2AStrings.tracesLastName);
        AlloyExpr snapShotNext =
                AlloyVar(D2AStrings.snapshotName + "/" + D2AStrings.tracesNextName);
        AlloyExpr snapshotBack =
                AlloyVar(D2AStrings.snapshotName + "/" + D2AStrings.tracesBackName);

        List<AlloyExpr> args = this.dsl.emptyExprList();
        args.add(snapShotFirst);
        // __initial[__Snapshot/first]
        body.add(AlloyPredCall(D2AStrings.initFactName, args));

        args = this.dsl.emptyExprList();
        args.add(this.dsl.curVar());
        args.add(this.dsl.curJoinExpr(snapShotNext));

        List<AlloyDecl> decls1 = this.dsl.emptyDeclList();
        decls1.add(this.dsl.curDecl());

        List<AlloyDecl> decls2 = this.dsl.emptyDeclList();
        decls2.add(
                AlloyQuantVar(
                        D2AStrings.curName,
                        AlloyDiff(AlloyVar(D2AStrings.snapshotName), snapShotLast)));

        body.add(
                AlloyIte(
                        // some __Snapshot/back
                        AlloySome(snapshotBack),
                        // all s : __Snapshot | __small_step[s, s.__Snapshot/next]
                        AlloyAllVars(decls1, AlloyPredCall(D2AStrings.smallStepName, args)),
                        // all s : __Snapshot - __Snapshot/last | __small_step[s, s.__Snapshot/next]
                        AlloyAllVars(decls2, AlloyPredCall(D2AStrings.smallStepName, args))));

        this.am.addFact(D2AStrings.tracesFactName, body);
    }

    public void addStrongNoStutterPred() {

        assert (this.isTraces);
        AlloyExpr snapShotFirst =
                AlloyVar(D2AStrings.snapshotName + "/" + D2AStrings.tracesFirstName);
        List<AlloyExpr> body = this.dsl.emptyExprList();
        List<AlloyDecl> decls = this.dsl.emptyDeclList();

        List<AlloyExpr> bigOr = this.dsl.emptyExprList();
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            // don't need to make this stronger than an Or
            // b/c other parts of semantics will make sure only
            // one transTaken is true
            bigOr.add(AlloyNot(AlloyEqual(this.dsl.curTransTaken(i), this.dsl.noneArrow(i))));
        }
        AlloyExpr ex = AlloyOr(AlloyEqual(this.dsl.curVar(), snapShotFirst), AlloyOrList(bigOr));

        decls.add(AlloyQuantVar(D2AStrings.curName, AlloyVar(D2AStrings.snapshotName)));
        body.add(AlloyAllVars(decls, ex));

        List<AlloyDecl> emptyDeclList = this.dsl.emptyDeclList();
        this.am.addPred(D2AStrings.strongNoStutterName, emptyDeclList, body);
    }
}
