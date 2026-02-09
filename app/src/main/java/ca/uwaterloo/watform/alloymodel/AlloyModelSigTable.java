package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.paragraph.AlloyPara.*;
import static ca.uwaterloo.watform.parser.Parser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyQnameExpr;
import ca.uwaterloo.watform.alloyast.expr.var.AlloyVarExpr;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.utils.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AlloyModelSigTable extends AlloyModelTable<AlloySigPara> {
    private final Map<String, AlloyDecl> fields;

    public AlloyModelSigTable(AlloyFile alloyFile) {
        fields = new HashMap<>();
        // This syntax (not putting super(...) at first line) is allowed in Java 25.
        // This is needed here because super ctor calls addPara, which
        // requires this.fields to be initialized
        super(alloyFile, AlloySigPara.class);
        if (null == alloyFile) return;
        getAllParas().forEach(this::addFields);
    }

    private void addFields(AlloySigPara sig) {
        for (AlloyDecl decl : sig.fields) {
            for (AlloyQnameExpr qname : decl.qnames) {
                for (AlloyVarExpr var : qname.vars) {
                    fields.put(var.label, decl);
                }
            }
        }
    }

    private AlloyModelSigTable(AlloyModelSigTable other) {
        super(other);
        this.fields = new HashMap<>(other.fields);
    }

    @Override
    public AlloyModelSigTable copy() {
        return new AlloyModelSigTable(this);
    }

    @Override
    public void addPara(AlloySigPara sig) {
        List<AlloySigPara> expandedSigs = sig.expand();
        if (expandedSigs.size() > 1) {
            this.addParas(expandedSigs);
            return;
        }
        sig = expandedSigs.getFirst();

        // fields cannot overlap with existing sig names
        for (AlloyDecl decl : sig.fields) {
            for (AlloyQnameExpr qname : decl.qnames) {
                for (AlloyVarExpr var : qname.vars) {
                    if (contains(var.toString())) {
                        throw AlloyModelError.duplicateName(
                                this.mp.get(new AlloyId(var.toString())).pos, var.pos);
                    }
                }
            }
        }

        // sig cannot overlap with existing fields
        if (fields.containsKey(sig.getId().name)) {
            throw AlloyModelError.duplicateName(sig.pos, fields.get(sig.getId().name).pos);
        }

        super.addPara(sig);

        // add these fields
        addFields(sig);
    }

    public boolean containsField(String name) {
        return fields.containsKey(name);
    }
}
