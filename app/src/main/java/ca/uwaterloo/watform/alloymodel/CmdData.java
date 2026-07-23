package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara.CommandDecl.CmdType;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class CmdData {

    public Pos pos;
    public CmdType cmdType;
    public Optional<Integer> defaultScope;
    public HashMap<Qname, SigScope> cmdScopes = new HashMap<>();
    public Optional<Integer> expect; // 1 or 0

    // cannot have both of the following:
    // 1) run p { block } or check p {block} -- 'p is optional unused name of command
    public Optional<AlloyBlock> block;
    // 2) check assertName (no braces following)
    public Optional<Qname> assertOrPredFunQname;
    // which of these is determined at resolve time
    public Optional<Qname> predFunQname = Optional.empty();
    public Optional<Qname> assertQname = Optional.empty();

    public Boolean isResolved;

    public CmdData() {}

    @Override
    public String toString() {
        // moved to within SMCmds so could print out command nicely
        // after resolved
        throw ImplementationError.shouldNotReach();
    }
}
