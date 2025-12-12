package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.mapBy;

import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.List;

public class AuxDashAccessors {

    // this is a temporary class to use accessors pending a rewrite with proper adherence to access
    // specification standards. This is to be deleted later, and references to this be replaced

    public static String getRootStateName(DashModel dm) {
        return dm.getRootName();
    }

    public static List<String> getAllStateNames(DashModel dm) {
        return dm.st.getAllNames();
    }

    public static List<String> getLeafStateNames(DashModel dm) {
        return GeneralUtil.filterBy(getAllStateNames(dm), x -> dm.st.isLeaf(x));
    }

    public static List<String> getChildStateNames(String stateName, DashModel dm) {
        return dm.st.get(stateName).immChildren;
    }

    public static String getParentStateName(String stateName, DashModel dm) {
        return dm.st.get(stateName).parent;
    }

    public static List<String> getDescendantStateNames(String stateName, DashModel dm) {
        return GeneralUtil.filterBy(dm.st.getAllNames(), name -> name.startsWith(stateName));
    }

    public static List<String> getAncestorStateNames(String stateName, DashModel dm) {
        return dm.st.getAllAnces(stateName);
    }

    public static List<String> getTransitionNames(DashModel dm) {
        return dm.tt.keySet();
    }

    public static String getSourceOfTrans(String transFQN, DashModel dm) {
        return dm.tt.get(transFQN).fromP.toString();
    }

    public static List<String> initialEntered(DashModel dashModel) {
        return mapBy(dashModel.st.getRootLeafStatesEntered(), x -> x.toString());
    }
}
