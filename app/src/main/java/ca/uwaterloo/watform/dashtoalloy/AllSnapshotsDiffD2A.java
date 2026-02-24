/*
    fact/pred allSnapshotsDifferent {
        all s:DshSnapshot, sn:DshSnapshot |
            s.dsh_confi = sn.dsh_confi, etc forall i, and
            s.dsh_sc_usedi = sn.dsh_sc_usedi, etc forall i, and
            s.dsh_takeni = sn.dsh_takeni, etc forall i, and
            s.dsh_eventsi = sn.dsh_eventsi, etc forall i, and
            s.dsh_stable = sn.dsh_stable =>
        s = sn
    }
*/
package ca.uwaterloo.watform.dashtoalloy;

import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.dashast.DashFQN.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.ImplementationError.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.dashast.DashFQN;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.List;

public class AllSnapshotsDiffD2A extends CompleteBigStepsD2A {

    protected AllSnapshotsDiffD2A(DashModel dm, Options opt) {
        super(dm, opt);
    }

    private List<AlloyExpr> addAllSnapshotsDiffBody() {
        List<AlloyExpr> body = this.dsl.emptyExprList();
        AlloyExpr e;
        for (int i = 0; i <= this.dm.maxDepthParams(); i++) {
            if (!this.dm.hasOnlyOneState())
                // s.confi = sn.confi
                body.add(AlloyEqual(this.dsl.curConf(i), this.dsl.nextConf(i)));
            // s.scopesUsedi = sn.scopesUsedi
            if (this.dm.hasConcurrency())
                body.add(AlloyEqual(this.dsl.curScopesUsed(i), this.dsl.nextScopesUsed(i)));
            body.add(AlloyEqual(this.dsl.curTransTaken(i), this.dsl.nextTransTaken(i)));
            if (this.dm.hasIntEventsAti(i))
                body.add(AlloyEqual(this.dsl.curEvents(i), this.dsl.nextEvents(i)));
        }
        if (this.dm.hasConcurrency())
            body.add(AlloyEqual(this.dsl.curStable(), this.dsl.nextStable()));

        // variables and buffers
        List<String> allVarsAndBuffers = this.dm.allVarNames();
        allVarsAndBuffers.addAll(this.dm.allBufferNames());
        for (String v : allVarsAndBuffers) {
            body.add(
                    AlloyEqual(
                            this.dsl.curJoinExpr(AlloyVar(DashFQN.translateFQN(v))),
                            this.dsl.nextJoinExpr(AlloyVar(DashFQN.translateFQN(v)))));
        }

        e =
                AlloyAllVars(
                        this.dsl.curNextDecls(),
                        AlloyImplies(
                                AlloyAndList(body),
                                AlloyEqual(this.dsl.curVar(), this.dsl.nextVar())));

        body = this.dsl.emptyExprList();
        body.add(e);
        return body;
    }

    public void addAllSnapshotsDiffPred() {
        List<AlloyDecl> nodecls = this.dsl.emptyDeclList();
        this.am.addPred(D2AStrings.allSnapshotsDiffName, nodecls, addAllSnapshotsDiffBody());
    }

    public void addAllSnapshotsDiffFact() {
        List<AlloyDecl> nodecls = this.dsl.emptyDeclList();
        this.am.addFact(D2AStrings.allSnapshotsDiffName, addAllSnapshotsDiffBody());
    }
}
