/*
    This centralizes knowledge about walking over paragraphs
    for different purposes
*/

package ca.uwaterloo.watform.paravisitor;

import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.*;
import ca.uwaterloo.watform.dashast.DashState;

public interface AlloyParaVis<T> {
    public default T visit(AlloyPara para) {
        return para.accept(this);
    }

    T visit(AlloySigPara sigPara);

    T visit(AlloyAssertPara assertPara);

    T visit(AlloyEnumPara enumPara);

    T visit(AlloyFactPara factPara);

    T visit(AlloyFunPara funPara);

    T visit(AlloyImportPara importPara);

    T visit(AlloyMacroPara macroPara);

    T visit(AlloyPredPara predPara);

    T visit(AlloyCmdPara cmdPara);

    T visit(AlloyModulePara modPara);

    T visit(DashState dashPara);
}
