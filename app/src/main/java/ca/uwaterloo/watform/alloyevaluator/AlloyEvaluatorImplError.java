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

    public static AlloyEvaluatorImplError cardinalityError(String description) {
        return new AlloyEvaluatorImplError("Cardinality error: " + description);
    }

    public static AlloyEvaluatorImplError arityError(String description) {
        return new AlloyEvaluatorImplError("Arity error: " + description);
    }

    public static AlloyEvaluatorImplError typeError(String description) {
        return new AlloyEvaluatorImplError(
                "The type of the set evaluated is inconsistent: " + description);
    }

    public static AlloyEvaluatorImplError atomConstructionError(String description) {
        return new AlloyEvaluatorImplError("The atom was incorrectly constructed: " + description);
    }

    public static AlloyEvaluatorImplError comparisonError(String description) {
        return new AlloyEvaluatorImplError("Atom comparison error: " + description);
    }

    public static AlloyEvaluatorImplError setError(String description) {
        return new AlloyEvaluatorImplError("Set error: " + description);
    }
}
