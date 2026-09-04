package ca.uwaterloo.watform.alloytotla;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.tlaast.TlaDefn;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaStdLibs;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class CommandsA2T extends BoilerplateA2T {
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

    for (var s : alloyModel.topLevelSigs()) {

      var scope = scopeLimits.getTopLevelScope(s);
      int n = scope.map(sc -> sc.max()).orElse(DEFAULT_SCOPE);
      boolean exact = scope.map(sc -> sc.isExact()).orElse(false);

      if (exact) clauses.add(TlaVar(s).EQUALS(sigAtoms(s, 0, n - 1)));
      else {
        List<TlaExp> subClauses = new ArrayList<>();
        for (int i = 0; i < n; i++) subClauses.add(TlaVar(s).EQUALS(sigAtoms(s, 0, i)));
        clauses.add(repeatedOr(subClauses));
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
    */

    boolean isRun = cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN;

    // var expect = cmdDecl.expect.map(e -> e.value).orElse(0);
    // boolean is1 = expect == 1;

    // if (!is1 && isRun || is1 && !isRun) block = TlaNot(block);

    // TODO invokeQname, which is an alternative for the block

    TlaExp block = cmdDecl.constrBlock.map(b -> (translateSnippet(b))).orElse(TlaTrue());

    block = block.AND(augmentedTrue());
    if (isRun) block = TlaNot(block);

    return TlaDefn(COMMAND, block);
  }
}
