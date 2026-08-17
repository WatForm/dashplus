package ca.uwaterloo.watform.alloyinterface;

import ca.uwaterloo.watform.utils.*;

public class AlloyInterfaceError extends UserError {

  private AlloyInterfaceError(String msg) {
    super(msg);
  }

  public static AlloyInterfaceError tmpFileExists(String fileName) {
    return new AlloyInterfaceError("Tmp file already exists: " + fileName);
  }
}
