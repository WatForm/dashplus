package ca.uwaterloo.watform.alloymodel.alloytype;

import ca.uwaterloo.watform.utils.*;
import java.util.List;

public final class AlloyTypeError extends UserOrImplError {

  private AlloyTypeError(String msg) {
    super(msg);
  }

  private AlloyTypeError(Pos pos, String msg) {
    super(pos, msg);
  }

  private AlloyTypeError(List<Pos> posList, String msg) {
    super(posList, msg);
  }

  // errors

  public static AlloyTypeError operationOnEmptyType(String operation) {
    return new AlloyTypeError("Cannot call this operation on the empty type: " + operation);
  }
}

//   public static AlloyModelError unknownArity(Pos p, String s) {
//     return new AlloyModelError(p, "Unknown arity: " + s);
//   }

//   public static AlloyModelError fieldBoundingExprCanContainOnlySigsAndCurrentFieldNames(
//       Pos pos, String expr) {
//     return new AlloyModelError(
//         pos, "Field bounding expression can contain only sigs and fields of this sig: " + expr);
//   }

//   public static AlloyModelError arityMismatchReturnType(
//       Pos pos, String name, Integer returnTypeArity, Integer bodyArity) {
//     return new AlloyModelError(
//         pos,
//         "Arity of return type of "
//             + name
//             + " ("
//             + Integer.toString(returnTypeArity)
//             + ") does not match arity of body ("
//             + Integer.toString(bodyArity)
//             + ")");
//   }
