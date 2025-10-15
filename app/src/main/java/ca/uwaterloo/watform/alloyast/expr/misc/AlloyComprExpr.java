package ca.uwaterloo.watform.alloyast.expr.misc;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.*;
import ca.uwaterloo.watform.alloyast.misc.*;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AlloyComprExpr extends AlloyExpr {
	public final List<AlloyDecl> decls;
	public final AlloyExpr body;

	public AlloyComprExpr(
			Pos pos, List<AlloyDecl> decls, AlloyExpr body) {
		super(pos);
		this.decls = Collections.unmodifiableList(decls);
		this.body = body;
	}

	@Override
	public void toString(StringBuilder sb, int indent) {
		sb.append(AlloyStrings.LBRACE);
		boolean first = true;
		for(AlloyDecl decl : this.decls) {
			if(! first) {
				sb.append(", ");
			}
			decl.toString(sb, indent);
			first = false;
		}
		sb.append(AlloyStrings.SPACE);
		sb.append(AlloyStrings.BAR);
		sb.append(AlloyStrings.SPACE);
		this.body.toString(sb, indent);
		sb.append(AlloyStrings.RBRACE);
	}
}
