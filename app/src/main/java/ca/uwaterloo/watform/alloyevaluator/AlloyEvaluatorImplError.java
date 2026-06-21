package ca.uwaterloo.watform.alloyevaluator;

import ca.uwaterloo.watform.utils.ImplementationError;
import ca.uwaterloo.watform.utils.Pos;

public class AlloyEvaluatorImplError extends ImplementationError {

    private AlloyEvaluatorImplError(String msg) {
        super(msg);
    }

    private AlloyEvaluatorImplError(Pos pos, String msg) {
        super(pos, msg);
    }

    public static AlloyEvaluatorImplError missingVisitCase(String exprDescription) {
        return new AlloyEvaluatorImplError(
                "FormulaEvaluator/SetEvaluator missing case for: " + exprDescription);
    }

    public static AlloyEvaluatorImplError notSupported(String msg) {
        return new AlloyEvaluatorImplError("Alloy evaluator case that is not supported: " + msg);
    }

    public static AlloyEvaluatorImplError relationNotInInstance(String relationName) {
        return new AlloyEvaluatorImplError(
                "Relation not found in XML instance (model/instance mismatch): " + relationName);
    }
}
