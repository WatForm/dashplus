package ca.uwaterloo.watform.alloyevaluator;

import static ca.uwaterloo.watform.utils.CommonStrings.*;

import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.exprvisitor.AlloyExprVis;
import ca.uwaterloo.watform.utils.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetEvaluator implements AlloyExprVis<Set<List<String>>> {
    private final Map<String, Set<List<String>>> map;

    public SetEvaluator(String xmlFileName) {
        map = XmlReader.readInstance(xmlFileName);
    }

    public Set<List<String>> visit(DashRef dashRef) {
        dashOutput("Hit generic DashRef visit " + dashRef);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyBinaryExpr binExpr) {
        dashOutput("Hit generic AlloyBinaryExpr visit " + binExpr);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyUnaryExpr unaryExpr) {
        dashOutput("Hit generic AlloyUnaryExpr visit " + unaryExpr);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyVarExpr varExpr) {
        dashOutput(
                "Hit generic AlloyVarExpr visit " + varExpr + " " + varExpr.getClass().getName());
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyBlock block) {
        dashOutput("Hit generic AlloyBlock visit " + block);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyBracketExpr bracketExpr) {
        dashOutput("Hit generic AlloyBracketExpr visit " + bracketExpr);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyCphExpr comprehensionExpr) {
        dashOutput("Hit generic AlloyCphExpr visit " + comprehensionExpr);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyIteExpr iteExpr) {
        dashOutput("Hit generic AlloyIteExpr visit " + iteExpr);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyLetExpr letExpr) {
        dashOutput("Hit generic AlloyLetExpr visit " + letExpr);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyQuantificationExpr quantificationExpr) {
        dashOutput("Hit generic AlloyQuantificationExpr visit " + quantificationExpr);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyDecl decl) {
        dashOutput("Hit generic AlloyDecl visit " + decl);
        return new HashSet<List<String>>();
    }

    public Set<List<String>> visit(AlloyQnameExpr qName) {
        dashOutput("Evaluating qname expr: " + qName + " " + qName.vars);
        if (qName.vars.isEmpty()) {
            dashOutput("AlloyQnameExpr evaluated to: []. The vars are empty");
            return new HashSet<List<String>>();
        } else {
            var temp = map.get("this/" + qName.vars.getLast().label);
            dashOutput("AlloyQnameExpr evaluated to: " + temp);
            return map.get("this/" + qName.vars.getLast().label);
        }
    }
}
