/*
    Apply test to each expression. If test is true, return result of "replace" function.
    Otherwise, spply visitors to subexpressions and return rebuild
	expressions.

	If nothing here is overwritten, it will return
	exactly the same expression.
*/

package ca.uwaterloo.watform.alloyexprvisitor;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.binary.*;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.unary.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
// import ca.uwaterloo.watform.dashast.dashref.DashRef;
import java.util.*;
import java.util.function.Function;

public class ReplaceExprVis implements AlloyExprVis<AlloyExpr> {

  protected Function<AlloyExpr, Boolean> test;
  protected Function<AlloyExpr, AlloyExpr> replace;
  private final boolean respectLocalBindings;
  private Set<String> boundNames = new HashSet<>();

  public ReplaceExprVis(Function<AlloyExpr, Boolean> test, Function<AlloyExpr, AlloyExpr> replace) {
    this(test, replace, false);
  }

  // Existing callers perform structural replacements. Enable this option to replace only free
  // names.
  public ReplaceExprVis(
      Function<AlloyExpr, Boolean> test,
      Function<AlloyExpr, AlloyExpr> replace,
      boolean respectLocalBindings) {
    this.test = test;
    this.replace = replace;
    this.respectLocalBindings = respectLocalBindings;
  }

  // all binary expressions
  @Override
  public AlloyExpr visit(AlloyBinaryExpr binExpr) {
    if (this.test.apply(binExpr)) return this.replace.apply(binExpr);
    else return binExpr.rebuild(this.visit(binExpr.left), this.visit(binExpr.right));
  }

  // all unary expressions
  @Override
  public AlloyExpr visit(AlloyUnaryExpr unaryExpr) {
    if (this.test.apply(unaryExpr)) return this.replace.apply(unaryExpr);
    else return unaryExpr.rebuild(this.visit(unaryExpr.sub));
  }

  @Override
  public AlloyExpr visit(AlloyVarExpr varExpr) {
    if (respectLocalBindings
        && varExpr instanceof AlloyQnameExpr name
        && boundNames.contains(name.getName())) return varExpr;
    if (this.test.apply(varExpr)) return this.replace.apply(varExpr);
    else return varExpr;
  }

  // all the misc expr -------------------

  @Override
  public AlloyExpr visit(AlloyBlock block) {
    if (this.test.apply(block)) return this.replace.apply(block);
    else return block.rebuild(mapBy(block.exprs, i -> this.visit(i)));
  }

  @Override
  public AlloyExpr visit(AlloyBracketExpr bracketExpr) {
    if (this.test.apply(bracketExpr)) return this.replace.apply(bracketExpr);
    else {
      return bracketExpr.rebuild(
          visit(bracketExpr.expr), mapBy(bracketExpr.exprs, i -> this.visit(i)));
    }
  }

  @Override
  public AlloyExpr visit(AlloyCphExpr comprehensionExpr) {
    if (this.test.apply(comprehensionExpr)) return this.replace.apply(comprehensionExpr);
    Set<String> outerNames = new HashSet<>(boundNames);
    try {
      List<AlloyDecl> declarations = emptyList();
      for (AlloyDecl declaration : comprehensionExpr.decls) {
        declarations.add((AlloyDecl) this.visit(declaration));
        bindNames(declaration);
      }
      return comprehensionExpr.rebuild(
          declarations, comprehensionExpr.body.map(this::visit).orElse(null));
    } finally {
      boundNames = outerNames;
    }
  }

  @Override
  public AlloyExpr visit(AlloyIteExpr iteExpr) {
    if (this.test.apply(iteExpr)) return this.replace.apply(iteExpr);
    else {
      return iteExpr.rebuild(
          this.visit(iteExpr.cond), this.visit(iteExpr.conseq), this.visit(iteExpr.alt));
    }
  }

  @Override
  public AlloyExpr visit(AlloyLetExpr letExpr) {
    if (this.test.apply(letExpr)) return this.replace.apply(letExpr);
    Set<String> outerNames = new HashSet<>(boundNames);
    try {
      List<AlloyLetExpr.AlloyLetAsn> assignments = emptyList();
      for (AlloyLetExpr.AlloyLetAsn assignment : letExpr.asns) {
        assignments.add(assignment.rebuild(this.visit(assignment.expr)));
        boundNames.add(assignment.qname.getName());
      }
      return letExpr.rebuild(assignments, this.visit(letExpr.body));
    } finally {
      boundNames = outerNames;
    }
  }

  @Override
  public AlloyExpr visit(AlloyDecl decl) {
    if (this.test.apply(decl)) return this.replace.apply(decl);
    return decl.rebuild(this.visit(decl.expr));
  }

  @Override
  public AlloyExpr visit(AlloyQuantificationExpr quantificationExpr) {
    if (this.test.apply(quantificationExpr)) return this.replace.apply(quantificationExpr);
    Set<String> outerNames = new HashSet<>(boundNames);
    try {
      List<AlloyDecl> declarations = emptyList();
      for (AlloyDecl declaration : quantificationExpr.decls) {
        declarations.add(declaration.rebuild(this.visit(declaration.expr)));
        bindNames(declaration);
      }
      return quantificationExpr.rebuild(declarations, this.visit(quantificationExpr.body));
    } finally {
      boundNames = outerNames;
    }
  }

  private void bindNames(AlloyDecl declaration) {
    // A name is bound only after visiting its domain; later domains can use earlier bindings.
    for (AlloyQnameExpr name : declaration.qnames) boundNames.add(name.getName());
  }

  @Override
  public AlloyExpr visit(AlloyParenExpr parenExpr) {
    if (this.test.apply(parenExpr)) return this.replace.apply(parenExpr);
    else {
      return parenExpr.rebuild(this.visit(parenExpr.sub));
    }
  }
}
