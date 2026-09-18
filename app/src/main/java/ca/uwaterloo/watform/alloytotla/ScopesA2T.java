package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;

import ca.uwaterloo.watform.alloymodel.AlloyModel;
import ca.uwaterloo.watform.alloymodel.Qname;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import java.util.ArrayList;
import java.util.List;

public class ScopesA2T extends PredFunA2T {

  public ScopesA2T(
      AlloyModel alloyModel, Optimization optimization, boolean verbose, boolean debug) {
    super(alloyModel, optimization, verbose, debug);
  }

  public TlaExp placeholderScope(TlaModel tlaModel) {

    List<TlaExp> clauses = new ArrayList<>();

    for (var s : alloyModel.allSigQnames()) clauses.add(TlaVar(tlaQname(s)).EQUALS(TlaNullSet()));
    for (var f : alloyModel.allFieldQnames()) clauses.add(TlaVar(tlaQname(f)).EQUALS(TlaNullSet()));

    return repeatedAnd(clauses);
  }

  public void addScopes(TlaModel tlaModel, int cmdNum) {
    var cmdScopeProfile = alloyModel.getCmdScopeProfile(cmdNum);

    List<TlaExp> clauses = new ArrayList<>();
    for (Qname topSig : cmdScopeProfile.getTopLevelSigs()) {
      var scopeEntry = cmdScopeProfile.getTopLevelScope(topSig);
      if (scopeEntry.isExact()) {
        clauses.add(
            TlaVar(tlaQname(topSig))
                .EQUALS(sigAtoms(topSig.fullName(), 0, scopeEntry.getValue() - 1)));
      } else {
        if (optimization.nonExactSymmetry()) {
          // TODO change this
          clauses.add(
              TlaVar(tlaQname(topSig))
                  .IN(TlaSubsetUnary(sigAtoms(topSig.fullName(), 0, scopeEntry.getValue() - 1))));
        } else {
          clauses.add(
              TlaVar(tlaQname(topSig))
                  .IN(TlaSubsetUnary(sigAtoms(topSig.fullName(), 0, scopeEntry.getValue() - 1))));
        }
      }
    }
    tlaModel.addDefn(TlaDefn(SCOPE, repeatedAnd(clauses)));
  }
}
