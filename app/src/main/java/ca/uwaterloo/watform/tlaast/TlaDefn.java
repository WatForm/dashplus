package ca.uwaterloo.watform.tlaast;

import ca.uwaterloo.watform.tlaast.SnowCatTypes.SCType;
import java.util.*;

public class TlaDefn extends TlaExp {

  /*
  G(arg1,arg2...) == exp

  Here, G(arg1,arg2...) == exp is represented by this node
  GG(arg1,arg2...) is the decl
  exp is the body
  */

  public final TlaDecl decl;
  public final TlaExp body;

  public final Optional<SCType> type;

  public TlaDefn(TlaDecl decl, TlaExp body) {
    this.decl = decl;
    this.body = body;
    this.type = Optional.empty();
  }

  public TlaDefn(TlaDecl decl, TlaExp body, SCType type) {
    this.decl = decl;
    this.body = body;
    this.type = Optional.of(type);
  }

  @Override
  public void toString(StringBuilder sb, int ident) {
    sb.append(this.type.map(t -> t.annotation() + "\n").orElse(""));
    this.decl.toString(sb, ident);
    sb.append(TlaStrings.SPACE + TlaStrings.DEFINITION + TlaStrings.SPACE);
    this.body.toString(sb, ident);
    return;
  }

  public List<TlaExp> getChildren() {
    return Arrays.asList(this.decl, this.body);
  }

  @Override
  public String toTLAPlusSnippetCore() {

    // precedence and associativity is never a problem with definitions
    return this.type.map(t -> t.annotation() + "\n").orElse("testing 123")
        + this.decl.toTLAPlusSnippet(false)
        + TlaStrings.SPACE
        + TlaStrings.DEFINITION
        + TlaStrings.SPACE
        + this.body.toTLAPlusSnippet(false);
  }
}
