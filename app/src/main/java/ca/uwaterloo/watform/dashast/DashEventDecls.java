package ca.uwaterloo.watform.dashast;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.dashast.DashStrings.*;

import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public final class DashEventDecls extends ASTNode implements DashStateItem {
    public final List<String> names;
    public final DashStrings.IntEnvKind kind;

    public DashEventDecls(Pos pos, List<String> n, DashStrings.IntEnvKind kind) {
        super(pos);
        assert (n != null);
        this.names = Collections.unmodifiableList(n);
        this.kind = kind;
    }

    @Override
    public void pp(PrintContext pCtx) {
        if (kind == IntEnvKind.ENV) {
            pCtx.append(envName + SPACE);
        }
        pCtx.append(eventName + SPACE);
        pCtx.appendList(names, COMMA);
        pCtx.append(SPACE);
        pCtx.append(LBRACE + RBRACE);
    }

    public List<String> getNames() {
        return names;
    }

    public DashStrings.IntEnvKind getKind() {
        return kind;
    }
}
