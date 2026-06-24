/*
    Storage and special functionality for command paragraphs
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import java.util.*;

public class AMCmds extends AMAsserts {

    // cmdParas never have names
    protected List<AlloyCmdPara> cmds = emptyList();

    protected AMCmds(AMCmds other) {
        super(other);
        this.cmds = new ArrayList<AlloyCmdPara>(other.cmds);
    }

    protected AMCmds(AlloyFile alloyFile) {
        super(alloyFile);
        this.cmds = emptyList();
        extractItemsOfClass(alloyFile.paras, AlloyCmdPara.class).forEach(p -> this.addCmdPara(p));
    }

    protected void resolve() {
        super.resolve();
        List<AlloyCmdPara> newCmds = emptyList();
        for (AlloyCmdPara cmdPara : this.cmds) {
            AlloyCmdPara newCmdPara =
                    new AlloyCmdPara(
                            cmdPara.pos,
                            mapBy(
                                    cmdPara.cmdDecls,
                                    d ->
                                            (d.rebuild(
                                                    ((AlloyBlock)
                                                            d.constrBlock
                                                                    .map(x -> this.setMul(x))
                                                                    .orElse(null))))));
            newCmds.add(newCmdPara);
        }
        this.cmds = newCmds;
    }

    public void addCmdPara(AlloyCmdPara cmdPara) {
        this.cmds.add(cmdPara);
    }

    public Integer getNumCmds() {
        return this.cmds.size();
    }

    public List<AlloyCmdPara> allCmdParas() {
        // just to be safe, make a copy
        return new ArrayList<AlloyCmdPara>(this.cmds);
    }

    /** Retrieve the nth para in this table useful for cmds */
    public Optional<AlloyCmdPara> getCmdNum(int n) {
        if (n < 0 || n >= this.cmds.size()) return Optional.empty();
        else return Optional.of(this.cmds.get(n));
    }

    // accessors per CmdDecl

    public Boolean isRun(AlloyCmdPara.CommandDecl cmdDecl) {
        return cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN;
    }

    public Boolean isCheck(AlloyCmdPara.CommandDecl cmdDecl) {
        return cmdDecl.cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK;
    }

    // usually sigName is a sigRef, but for generality we can
    // leave it as a string
    // what if sigName does not get a scope?
    // do subsigs always get specific scopes?
    // should return a pair that is exact flag and value?
    public Integer getScope(AlloyCmdPara.CommandDecl cmdDecl, String sigName) {

        // TODO: should this be hard-coded here?
        int DEFAULT_SCOPE = 3;
        int defaultScope =
                cmdDecl.scope
                        .map(s -> s.num.map(n -> n.value).orElse(DEFAULT_SCOPE))
                        .orElse(DEFAULT_SCOPE);

        // check it is a sig in sigTable
        if (this.containsSig(sigName)) {
            // look for name in cmdDecl.scope
            // and return that value cmd.cmdScope.get(s).start.value
            // cmd.cmdScope.get(s).isExactly()
            // or if exists orElse default/non-exact

            // check out Mathew's summary (scopeComputer.md)
            // check out Elias' thesis
            // IntScope

            var typeScopes = cmdDecl.scope.map(s -> s.typescopes).orElse(new ArrayList<>());
            for (var typeScope : typeScopes) {

                String key = typeScope.scopableExpr.toString();
                // scopes.put(key, typeScope.start.value);
                // exact.put(key, typeScope.isExactly);
            }
        }
        return 1;
    }

    // could add other functions here to create a cmdPara that is added above
}
