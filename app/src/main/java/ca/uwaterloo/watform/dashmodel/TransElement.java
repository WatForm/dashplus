package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashNamedExpr.*;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import java.util.List;

public class TransElement {
    public List<DashParam> params; // empty if no params // only DashParam's
    // public List<Integer> paramsIdx;

    // after initialization (from parsing)
    public DashFrom fromP;
    public DashGoto gotoP;
    public DashOn onP;
    public DashSend sendP;
    public DashWhen whenP;
    public DashDo doP;

    // after resolved
    // some of these will be or contain DashRefs
    public DashRef fromR = null;
    public DashRef gotoR = null;
    public DashRef onR = null; // event
    public DashRef sendR = null; // event
    public AlloyExpr whenR = null; // expr
    public AlloyExpr doR = null;

    public TransElement(
            List<DashParam> prms,
            // List<Integer> prmsIdx,
            DashFrom f,
            DashOn o,
            DashWhen w,
            DashGoto g,
            DashSend s,
            DashDo d) {
        this.params = prms;
        // this.paramsIdx = prmsIdx;
        this.fromP = f;
        this.onP = o;
        this.whenP = w;
        this.gotoP = g;
        this.sendP = s;
        this.doP = d;
    }

    public String toString() {
        String s = new String();
        s += "params: " + NoneStringIfNeeded(params) + "\n";
        // s += "paramsIdx: " + NoneStringIfNeeded(paramsIdx) +"\n";
        s += "from: " + NoneStringIfNeeded(fromP) + "\n";
        s += "goto: " + NoneStringIfNeeded(gotoP) + "\n";
        s += "on: " + NoneStringIfNeeded(onP) + "\n";
        s += "send: " + NoneStringIfNeeded(sendP) + "\n";
        s += "when: " + NoneStringIfNeeded(whenP) + "\n";
        s += "do: " + NoneStringIfNeeded(doP) + "\n";
        // add more
        return s;
    }
}
