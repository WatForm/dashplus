package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class CommandsA2T extends BoilerplateA2T {

  public CommandsA2T(
      AlloyModel alloyModel, Optimization optimization, boolean verbose, boolean debug) {
    super(alloyModel, optimization, verbose, debug);
  }

  public void addCommand(TlaModel tlaModel, int cmdNum) {

    var cmdExpr = alloyModel.getCmdFormula(cmdNum);
    boolean isRun = alloyModel.isRunCmd(cmdNum);
    var cmdScopeProfile = alloyModel.getCmdScopeProfile(cmdNum);

    log("command number -> " + cmdNum);
    log("isRun: " + isRun);
    log("scopeProfile: " + cmdScopeProfile.toString());
    log("cmd expr -> " + cmdExpr.toString());
    l.info(dump());

    List<TlaExp> clauses = new ArrayList<>();

    clauses.add(translateSnippet(cmdExpr));

    for (Qname sig : cmdScopeProfile.getExplicitExtendsSigs()) {
      int n = cmdScopeProfile.getTopLevelScope(sig).getValue();
      TlaExp right = TlaStdLibs.Cardinality(TlaVar(tlaQname(sig)));
      if (cmdScopeProfile.getTopLevelScope(sig).isExact())
        clauses.add(right.EQUALS(TlaIntLiteral(n)));
      else clauses.add(TlaLesserEq(right, TlaIntLiteral(n)));
    }

    // tlaModel.addComment("command: " + cmdDecl.toString(), verbose);
    // tlaModel.addDefn(cmdConstraints(tlaModel, cmdDecl));
    // tlaModel.addDefn(scopeConstraints(tlaModel, cmdDecl));
    // tlaModel.addInvariant(TlaAppl(COMMAND));
  }

  /*

  public void addCommand(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

    tlaModel.addComment("command: " + cmdDecl.toString(), verbose);
    tlaModel.addDefn(cmdConstraints(tlaModel, cmdDecl));
    tlaModel.addDefn(scopeConstraints(tlaModel, cmdDecl));
    tlaModel.addInvariant(TlaAppl(COMMAND));
  }



  public TlaDefn scopeConstraints(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {
    List<TlaExp> clauses = new ArrayList<>();
    var scopeLimits = alloyModel.getScopeLimits(cmdDecl);

    l.info("computed scopes:" + alloyModel.getScopeLimits(cmdDecl).toString());

    l.info("inner commandDecl:" + cmdDecl.toString());

    for (var s : alloyModel.topLevelSigs()) {

      var scope = scopeLimits.getTopLevelScope(s);
      l.info("scope for sig: "+s+" is:"+scope.toString());
      int n = scope.map(sc -> sc.max()).orElse(DEFAULT_SCOPE);
      boolean exact = scope.map(sc -> sc.isExact()).orElse(false);
      l.info(exact ? "scope is exact" : "scpoe is not exact");
      l.info("number in scope data structure is:"+n);

      if (exact) clauses.add(TlaVar(s).EQUALS(sigAtoms(s, 0, n - 1)));
      else {
        l.info("choosing path where scope is inexact");
        List<TlaExp> subClauses = new ArrayList<>();
        StringBuilder innerClauses = new StringBuilder();
        for (int i = 0; i < n; i++)
        {
          subClauses.add(TlaVar(s).EQUALS(sigAtoms(s, 0, i)));
          innerClauses.append(subClauses.getLast().toString());
        }
        clauses.add(repeatedOr(subClauses));
        l.info("constructed sub-clauses: ");
        l.info(subClauses.toString());
      }
    }

    for (var s : alloyModel.allSigs()) {
      scopeLimits
          .getExplicitExtendsScope(s)
          .ifPresent(
              sc -> {
                int n = sc.max();
                TlaExp cardinality = TlaStdLibs.Cardinality(TlaVar(s));
                if (sc.isExact()) clauses.add(cardinality.EQUALS(TlaIntLiteral(n)));
                else clauses.add(TlaLesserEq(cardinality, TlaIntLiteral(n)));
              });
    }

    return TlaDefn(SCOPE, repeatedAnd(clauses));
  }

  public TlaDefn cmdConstraints(TlaModel tlaModel, AlloyCmdPara.CommandDecl cmdDecl) {

    /*
    [run/check] {block} for [num but] ([exactly] num sig)* [expect 1/0]

    parity is defined as: is_run XOR is_1

    if there is no block, it defaults to {true}


    commandname == (~)(block) /\
    commandname_scope == [scope_constraints]

    exact -> A = {"a0"..."an"}
    not exact -> A = {"a0"..."an"} \/ ...


    boolean isRun = cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN;

    var expect = cmdDecl.expect.map(e -> e.value).orElse(0);

    l.info(block.toString());

    boolean is1 = expect == 1;

    // todo this needs to be rewritten
    boolean finalFlag = !is1 && isRun || is1 && !isRun;
    l.info("final flag to determine negation: "+finalFlag);



    // TODO invokeQname, which is an alternative for the block

    TlaExp block = cmdDecl.constrBlock.map(b -> (translateSnippet(b))).orElse(TlaTrue());

    if (!is1 && isRun || is1 && !isRun) block = TlaNot(block);
    l.info("augmented block: "+block.toString());

    block = block.AND(augmentedTrue());
    if (isRun) block = TlaNot(block);

    var answerFinal = TlaDefn(COMMAND, block);
    l.info("final answer: "+answerFinal.toString());

    return answerFinal;
  }

  */
}
