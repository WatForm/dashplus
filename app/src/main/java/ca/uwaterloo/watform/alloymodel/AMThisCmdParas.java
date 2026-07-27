/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.alloymodel.SigScope.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.CmdType;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.Scope;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.Scope.Typescope;
import java.util.*;

public class AMThisCmdParas extends AMThisAssertParas {

    // cmdParas never have names
    protected List<AlloyCmdPara> cmds = emptyList();

    // completely internal
    private Integer cmdNum = 0;

    protected AMThisCmdParas() {}

    protected AMThisCmdParas(AMThisCmdParas other) {
        super(other);
        this.cmds = new ArrayList<AlloyCmdPara>(other.cmds);
    }

    protected void addSMPara(AlloyCmdPara cmdPara, String nameSpace) {
        AlloyCmdPara.CommandDecl cmdDecl = cmdPara.cmdDecls.get(0);
        Qname qname;
        if (cmdDecl.declQname.isPresent()) {
            qname = thisQname(cmdDecl.declQname.get().getName());
        } else {
            // have to make up a name, match AA naming conventions
            cmdNum += 1;
            qname =
                    thisQname(
                            cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK
                                    ? "check$" + Integer.toString(cmdNum)
                                    : "run$" + Integer.toString(cmdNum));
        }

        this.createCmd(qname, cmdDeclCmdData(cmdDecl));
    }

    /*
    protected void addPara(AlloyCmdPara cmdPara, String nameSpace) {
        addSMPara(cmdPara, nameSpace);
        this.cmds.add(cmdPara);
    }
    */

    public void addPara(AlloyCmdPara cmdPara) {
        addSMPara(cmdPara, THIS_NAMESPACE);
        this.cmds.add(cmdPara);
    }

    public CmdData cmdDeclCmdData(CommandDecl cmdDecl) {

        CmdData cd = new CmdData();
        cd.pos = cmdDecl.pos;
        cd.cmdType = cmdDecl.cmdType; // run or check

        // Optional.of(n.value) or Optional.empty()
        cd.defaultScope = cmdDecl.scope.flatMap(s -> s.num.map(n -> n.value));

        // cd.cmdScopes
        var typeScopes = cmdDecl.scope.map(s -> s.typescopes).orElse(new ArrayList<>());
        // put the scopes given in the cmd into a HashMap
        for (var typeScope : typeScopes) {
            String sigName = typeScope.scopableExpr.toString();
            /* AA accepts "int" as sig name for scopes */
            if (sigName.equals(AlloyStrings.INT)) sigName = AlloyStrings.SIGINT;

            // need to resolve these also!
            Qname sigQname = unknownQname(sigName);
            if (cd.cmdScopes.keySet().contains(sigQname)) {
                throw AlloyModelError.multipleScopeValuesForSameSig(cmdDecl.pos, sigName);
            }
            cd.cmdScopes.put(
                    sigQname,
                    typeScope.isExactly
                            ? ExactScope(typeScope.start.value)
                            : NonExactScope(typeScope.start.value));
        }
        // cd.expect
        cd.expect = cmdDecl.expect.map(e -> Integer.valueOf(e.value));

        if (cd.expect.isPresent())
            if (cd.expect.get() != 0 && cd.expect.get() != 1) {
                throw AlloyModelError.expectValueZeroOrOne(cmdDecl.pos);
            }
        // cd.expr
        if (cmdDecl.invoQname.isPresent()) {
            // an assert name
            cd.assertOrPredFunQname = Optional.of(unknownQname(cmdDecl.invoQname.get().getName()));
            cd.block = Optional.empty();
        } else {
            // constrBlock must be present
            cd.assertOrPredFunQname = Optional.empty();
            cd.block = Optional.of(cmdDecl.constrBlock.get());
        }
        cd.isResolved = false;
        return cd;
    }

    public void addCmdDecl(AlloyCmdPara.CommandDecl cmdDecl) {
        this.addPara(new AlloyCmdPara(List.of(cmdDecl)));
    }

    // TODO: return index of cmd added
    public void addCmd(
            CmdType cmdType,
            AlloyExpr expr,
            Integer defaultScope,
            HashMap<Qname, SigScope> scopes) {
        // it seems a bit wrong to turn this into a cmd decl only
        // to turn the CommandDecl back to CmdData
        // but we need it as an AlloyCmdPara to add to THIS_NAMESPACE
        List<Typescope> typeScopes = emptyList();
        for (Qname name : scopes.keySet()) {
            SigScope sc = scopes.get(name);
            typeScopes.add(
                    new Typescope(
                            sc.isExact(), // isExactly
                            sc.max.get(), // start
                            false, // hasDotDot
                            sc.max.get(), // end
                            0, // increment
                            ((AlloyScopableExpr) name.toAlloyExpr(Kind.SIG))));
        }
        Scope s = new Scope(new AlloyNumExpr(defaultScope), typeScopes);
        // no cmd name (null)
        // no invoQname (null)
        this.addCmdDecl(new CommandDecl(cmdType, null, null, new AlloyBlock(expr), s));
    }

    public List<AlloyCmdPara> allCmdParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyCmdPara>(this.cmds);
    }
}
