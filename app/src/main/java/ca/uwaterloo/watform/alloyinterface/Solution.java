package ca.uwaterloo.watform.alloyinterface;

import java.util.*;

public class Solution {
  // public Optional<CmdDecl> cmd;
  public final Optional<Instance> instance;
  public final Boolean isSat;

  private Solution(Instance instance, Boolean isSat) {
    assert ((instance == null && !isSat) || (instance != null && isSat));
    this.instance = (instance == null) ? Optional.empty() : Optional.of(instance);
    this.isSat = isSat;
  }

  public static Solution UnsatSolution() {
    return new Solution(null, false);
  }

  public static Solution SatSolution(Instance instance) {
    return new Solution(instance, true);
  }
}
