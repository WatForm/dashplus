package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.filterBy;
import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import java.util.ArrayList;
import java.util.List;

import ca.uwaterloo.watform.tlaast.TlaAppl;
import ca.uwaterloo.watform.tlaast.TlaExp;
import ca.uwaterloo.watform.tlaast.TlaTypes;
import ca.uwaterloo.watform.tlaast.TlaVar;
import ca.uwaterloo.watform.tlamodel.TlaModel;

public class SignaturesA2T extends PredFunA2T {
	protected void addSigConstraints(TlaModel tlaModel) {

    tlaModel.addComment("signature constraints", verbose);

    List<TlaAppl> explicitConstraints = new ArrayList<>();

    for (var sig : alloyModel.allSigs()) {
      List<TlaExp> constraints = constraints(sig, alloyModel);
      if (constraints.size() != 0) {
        tlaModel.addDefn(TlaDefn(sigConstraint(sig), repeatedAnd(constraints)));
        explicitConstraints.add(TlaAppl(sigConstraint(sig)));
      }
    }

    tlaModel.addDefn(TlaDefn(ALL_SIG_CONSTRAINTS, repeatedAnd(explicitConstraints)));

    l.info(dump());
  }

  private List<TlaExp> constraints(String sig, AlloyModel alloyModel) {

    List<TlaExp> constraints = new ArrayList<>();

    if (alloyModel.isOneSig(sig)) {
      log("sig " + sig + " is a one sig");
      constraints.add(_ONE(TlaVar(sig)));
    }
    if (alloyModel.isLoneSig(sig)) {
      log("sig " + sig + " is a lone sig");
      constraints.add(_LONE(TlaVar(sig)));
    }
    if (alloyModel.isSomeSig(sig)) {
      log("sig " + sig + " is a some sig");
      constraints.add(_SOME(TlaVar(sig)));
    }

    List<String> extendsChildNames = alloyModel.extendsChildren(sig);
    int n = extendsChildNames.size();

    if (n != 0) {
      log(
          "sig "
              + sig
              + " has extends children "
              + extendsChildNames
              + (n > 1
                  ? ", translated to pairwise disjointedness constraints"
                  : ", no added constraints because only one child sig"));
    }

    // pairwise disjoint sets for sigs that extend the same sig

    for (int i = 0; i < n; i++)
      for (int j = i + 1; j < n; j++) {
        TlaVar si = TlaVar(extendsChildNames.get(i));
        TlaVar sj = TlaVar(extendsChildNames.get(j));
        // Si \intersect Sj = {}  (i < j)
        constraints.add(si.INTERSECTION(sj).EQUALS(TlaNullSet()));
      }

    // abstract sigs
    if (alloyModel.isAbstractSig(sig)) {
      log(
          "sig "
              + sig
              + " is an abstract sig, and is made up of only its extends children "
              + extendsChildNames);
      constraints.add(
          TlaVar(sig).EQUALS(repeatedUnion(mapBy(extendsChildNames, ecn -> TlaVar(ecn)))));
    }

    return constraints;
  }

  protected void addSigHierarchy(TlaModel tlaModel) {

    tlaModel.addComment("signature hierarchy", verbose);

    List<String> sortedSigs = alloyModel.topoSortedSigs();
    List<String> sortedNonTopLevelSigs = filterBy(sortedSigs, s -> !alloyModel.isTopLevelSig(s));

    log("toposorted non-top-level sigs: " + sortedNonTopLevelSigs);
    for (var s : sortedNonTopLevelSigs) {
      log("sig " + s + " has parents: " + alloyModel.allParents(s));
    }

    var sigSetClausesUnprimed =
        repeatedAnd(mapBy(sortedNonTopLevelSigs, sn -> sigSetClauseNonTopLevel(sn, false)));
    var sigSetClausesPrimed =
        repeatedAnd(mapBy(sortedNonTopLevelSigs, sn -> sigSetClauseNonTopLevel(sn, true)));

    tlaModel.addDefn(TlaDefn(SIG_SETS_UNPRIMED, sigSetClausesUnprimed));

    tlaModel.addDefn(TlaDefn(SIG_SETS_PRIMED, sigSetClausesPrimed));

    l.info(dump());
  }

  private TlaExp sigSetClauseNonTopLevel(String signame, boolean primed) {

    TlaExp v = primed ? TlaVar(signame).PRIME() : TlaVar(signame);
    List<TlaExp> parents =
        mapBy(alloyModel.allParents(signame), p -> primed ? TlaVar(p).PRIME() : TlaVar(p));
    return v.IN(TlaSubsetUnary(repeatedUnion(parents)));
  }

  protected void addSigVars(TlaModel tlaModel) {

    for (var sigName : alloyModel.allSigs()) {
      tlaModel.addVar(TlaVar(sigName), TlaTypes.Set(TlaTypes.Seq(TlaTypes.Str())));
      log("translated sig " + sigName + " into a VARIABLE");
    }

    l.info(dump());
  }
}
