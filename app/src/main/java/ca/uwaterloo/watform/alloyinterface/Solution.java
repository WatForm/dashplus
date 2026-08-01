package ca.uwaterloo.watform.alloyinterface;

import java.util.*;

public class Solution {
  // public Optional<CmdDecl> cmd;
  public Optional<Instance> instance;
  public Boolean isSat;

  private Solution(Instance instance, Boolean isSat) {
    assert ((instance == null && !isSat) || (instance != null && isSat));
    this.instance = (instance == null) ? Optional.empty() : Optional.of(instance);
  }

  public static Solution UnsatSolution() {
    return new Solution(null, false);
  }

  public static Solution SatSolution(Instance instance) {
    return new Solution(instance, true);
  }
}
