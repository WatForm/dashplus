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

    // could add other functions here to create a cmdPara that is added above
}
