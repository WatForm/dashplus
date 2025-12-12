package ca.uwaterloo.watform.dashtotla;

import static ca.uwaterloo.watform.utils.GeneralUtil.filterBy;

import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.List;

public class AuxDashAccessors {

    // this is a temporary class to use accessors pending a rewrite with proper adherence to access
    // specification standards. This is to be deleted later, and references to this be replaced

    public static String getRootStateName(DashModel dm) {
        return dm.rootName();
    }

    public static List<String> getAllStateNames(DashModel dm) {
        return dm.allStateNames();
    }

    public static List<String> getLeafStateNames(DashModel dm) {
        return filterBy(dm.allStateNames(), s -> dm.isLeaf(s));
    }

    public static List<String> getChildStateNames(String stateName, DashModel dm) {
        return dm.immChildren(stateName);
    }

    public static String getParentStateName(String stateName, DashModel dm) {
        return dm.parent(stateName);
    }

    public static List<String> getTransitionNames(DashModel dm) {
        return dm.allTransNames();
    }

    public static String getSourceOfTrans(String transFQN, DashModel dm) {
        return ""; // dm.tt.get(transFQN).fromP.toString();
    }

    public static List<String> initialEntered(DashModel dm) {
        return new ArrayList<>(); // mapBy(dashModel.st.getRootLeafStatesEntered(), x ->
        // x.toString());
    }

    public static boolean isLeaf(String stateFQN, DashModel dashModel) {
        return dashModel.isLeaf(stateFQN);
    }
}
