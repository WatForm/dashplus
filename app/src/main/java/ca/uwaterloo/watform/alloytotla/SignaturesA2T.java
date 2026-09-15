package ca.uwaterloo.watform.alloytotla;

import static ca.uwaterloo.watform.alloytotla.A2THelpers.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.*;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.SIG_SETS_PRIMED;
import static ca.uwaterloo.watform.alloytotla.A2TStrings.SIG_SETS_UNPRIMED;
import static ca.uwaterloo.watform.tlaast.CreateHelper.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloymodel.*;
import ca.uwaterloo.watform.tlaast.*;
import ca.uwaterloo.watform.tlamodel.*;
import java.util.*;

public class SignaturesA2T extends PredFunA2T {

  public SignaturesA2T(AlloyModel alloyModel, boolean verbose, boolean debug) {
    super(alloyModel, verbose, debug);
  }

  protected void addSigVars(TlaModel tlaModel) {
    for (Qname sig : alloyModel.allSigQnames()) {
      String s = tlaQname(sig);
      tlaModel.addVar(TlaVar(s), relationType());
      log("translated sig " + sig.fullName() + " into a VARIABLE " + s);
    }
    l.info(dump());
  }

  private TlaExp sigSetClauseNonTopLevelUnprimed(Qname sig) {

    TlaExp v = TlaVar(tlaQname(sig));
    List<TlaExp> parents = mapBy(alloyModel.allParents(sig), p -> TlaVar(tlaQname(p)));
    return v.IN(TlaSubsetUnary(repeatedUnion(parents)));
  }

  private TlaExp sigSetClauseNonTopLevelPrimed(Qname sig) {

    TlaExp v = TlaVar(tlaQname(sig)).PRIME();
    List<TlaExp> parents = mapBy(alloyModel.allParents(sig), p -> TlaVar(tlaQname(p)).PRIME());
    return v.IN(TlaSubsetUnary(repeatedUnion(parents)));
  }

  protected void addSigHierarchy(TlaModel tlaModel) {
    tlaModel.addComment("signature hierarchy", verbose);

    List<Qname> sortedSigs = alloyModel.topoSortedSigs();
    List<Qname> sortedNonTopLevelSigs = filterBy(sortedSigs, s -> !alloyModel.isTopLevelSig(s));

    log("toposorted non-top-level sigs: " + sortedNonTopLevelSigs);
    for (var s : sortedNonTopLevelSigs) {
      log("sig " + s.fullName() + " has parents: " + alloyModel.allParents(s));
    }

    var sigSetClausesUnprimed =
        repeatedAnd(mapBy(sortedNonTopLevelSigs, sn -> sigSetClauseNonTopLevelUnprimed(sn)));
    var sigSetClausesPrimed =
        repeatedAnd(mapBy(sortedNonTopLevelSigs, sn -> sigSetClauseNonTopLevelPrimed(sn)));

    tlaModel.addDefn(TlaDefn(SIG_SETS_UNPRIMED, sigSetClausesUnprimed));
    tlaModel.addDefn(TlaDefn(SIG_SETS_PRIMED, sigSetClausesPrimed));

    l.info(dump());
  }

  protected void addSigConstraints(TlaModel tlaModel) {

    tlaModel.addComment("signature constraints", verbose);

    List<TlaAppl> explicitConstraints = new ArrayList<>();

    for (var sig : alloyModel.allSigQnames()) {
      List<TlaExp> constraints = constraints(sig, alloyModel);
      if (constraints.size() != 0) {
        tlaModel.addDefn(TlaDefn(sigConstraint(sig), repeatedAnd(constraints)));
        explicitConstraints.add(TlaAppl(sigConstraint(sig)));
      }
    }

    tlaModel.addDefn(TlaDefn(ALL_SIG_CONSTRAINTS, repeatedAnd(explicitConstraints)));

    l.info(dump());
  }

  private List<TlaExp> constraints(Qname sig, AlloyModel alloyModel) {

    List<TlaExp> constraints = new ArrayList<>();

    if (alloyModel.isOneSig(sig)) {
      log("sig " + sig.fullName() + " is a one sig");
      constraints.add(_ONE(TlaVar(sig.name)));
    }
    if (alloyModel.isLoneSig(sig)) {
      log("sig " + sig.fullName() + " is a lone sig");
      constraints.add(_LONE(TlaVar(sig.name)));
    }
    if (alloyModel.isSomeSig(sig)) {
      log("sig " + sig.fullName() + " is a some sig");
      constraints.add(_SOME(TlaVar(sig.name)));
    }

    List<Qname> extendsChildNames = alloyModel.extendsChildren(sig);
    int n = extendsChildNames.size();

    if (n != 0) {
      log(
          "sig "
              + sig.fullName()
              + " has extends children "
              + extendsChildNames
              + (n > 1
                  ? ", translated to pairwise disjointedness constraints"
                  : ", no added constraints because only one child sig"));
    }

    // pairwise disjoint sets for sigs that extend the same sig

    for (int i = 0; i < n; i++)
      for (int j = i + 1; j < n; j++) {
        TlaVar si = TlaVar(tlaQname(extendsChildNames.get(i)));
        TlaVar sj = TlaVar(tlaQname(extendsChildNames.get(j)));
        // Si \intersect Sj = {}  (i < j)
        constraints.add(si.INTERSECTION(sj).EQUALS(TlaNullSet()));
      }

    // abstract sigs
    if (alloyModel.isAbstractSig(sig)) {
      log(
          "sig "
              + sig.fullName()
              + " is an abstract sig, and is made up of only its extends children "
              + mapBy(extendsChildNames, e -> e.fullName()));
      var abstractSig = TlaVar(tlaQname(sig));
      var children = mapBy(extendsChildNames, ecn -> TlaVar(tlaQname(ecn)));
      constraints.add(abstractSig.EQUALS(repeatedUnion(children)));
    }

    return constraints;
  }
}
