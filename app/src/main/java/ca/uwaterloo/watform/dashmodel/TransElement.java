package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.dashast.*;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import java.util.List;

public class TransElement {
    public List<DashParam> params; // empty if no params // only DashParam's
    // public List<Integer> paramsIdx;

    // from parsing
    public List<DashFrom> fromList;
    public List<DashOn> onList;
    public List<DashWhen> whenList;
    public List<DashGoto> gotoList;
    public List<DashSend> sendList;
    public List<DashDo> doList;

    // calculated when resolved
    public DashRef src = null;
    public DashRef dest = null;
    public AlloyExpr when = null; // expr
    public DashRef on = null; // event
    public AlloyExpr act = null;
    public DashRef send = null; // event

    public TransElement(
            List<DashParam> prms,
            // List<Integer> prmsIdx,
            List<DashFrom> fl,
            List<DashOn> ol,
            List<DashWhen> wl,
            List<DashGoto> gl,
            List<DashSend> sl,
            List<DashDo> dl) {
        this.params = prms;
        // this.paramsIdx = prmsIdx;
        this.fromList = fl;
        this.onList = ol;
        this.whenList = wl;
        this.gotoList = gl;
        this.sendList = sl;
        this.doList = dl;
    }

    public String toString() {
        String s = new String();
        s += "params: " + NoneStringIfNeeded(params) + "\n";
        // s += "paramsIdx: " + NoneStringIfNeeded(paramsIdx) +"\n";
        s += "src: " + NoneStringIfNeeded(src) + "\n";
        s += "dest: " + NoneStringIfNeeded(dest) + "\n";
        s += "on: " + NoneStringIfNeeded(on) + "\n";
        s += "send: " + NoneStringIfNeeded(send) + "\n";
        s += "when: " + NoneStringIfNeeded(when) + "\n";
        s += "do: " + NoneStringIfNeeded(act) + "\n";
        // add more
        return s;
    }
}
