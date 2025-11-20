package ca.uwaterloo.watform.dashtotlaplus;

import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDeclaration;
import ca.uwaterloo.watform.tlaplusast.TLAPlusFormulaDefinition;
import ca.uwaterloo.watform.tlaplusast.tlaplusbinaryoperators.TLAPlusEquals;
import ca.uwaterloo.watform.tlaplusmodel.TLAPlusModule;

public class Events {
    public static void translateEvents(Temp t, TLAPlusModule tm) {

        tm.addVariable(Util.getEvents());

        tm.addFormulaDefinition(
                new TLAPlusFormulaDefinition(
                        new TLAPlusFormulaDeclaration(Util.SET_EVENTS),
                        new TLAPlusEquals(Util.getEvents(), Util.getNullSet())));
    }
}
