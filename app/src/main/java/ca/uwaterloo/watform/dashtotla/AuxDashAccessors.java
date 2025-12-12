package ca.uwaterloo.watform.dashtotla;

import ca.uwaterloo.watform.dashmodel.DashModel;
import java.util.ArrayList;
import java.util.List;

public class AuxDashAccessors {

    // this is a temporary class to use accessors pending a rewrite with proper adherence to access
    // specification standards. This is to be deleted later, and references to this be replaced

    public static String getRootStateName(DashModel dm) {
        return "dm.getRootName();";
    }

    public static List<String> getAllStateNames(DashModel dm) {
        return new ArrayList<>(); // dm.st.getAllNames();
    }

    public static List<String> getLeafStateNames(DashModel dm) {
        return new ArrayList<>(); // GeneralUtil.filterBy(getAllStateNames(dm), x ->
        // dm.st.isLeaf(x));
    }

    public static List<String> getChildStateNames(String stateName, DashModel dm) {
        return new ArrayList<>(); // dm.st.get(stateName).immChildren;
    }

    public static String getParentStateName(String stateName, DashModel dm) {
        return ""; // dm.st.get(stateName).parent;
    }

    public static List<String> getDescendantStateNames(String stateName, DashModel dm) {
        return new ArrayList<>(); // GeneralUtil.filterBy(dm.st.getAllNames(), name ->
        // name.startsWith(stateName));
    }

    public static List<String> getAncestorStateNames(String stateName, DashModel dm) {
        return new ArrayList<>(); // dm.st.getAllAnces(stateName);
    }

    public static List<String> getTransitionNames(DashModel dm) {
        return new ArrayList<>(); // dm.tt.keySet();
    }

    public static String getSourceOfTrans(String transFQN, DashModel dm) {
        return ""; // dm.tt.get(transFQN).fromP.toString();
    }

    public static List<String> initialEntered(DashModel dashModel) {
        return new ArrayList<>(); // mapBy(dashModel.st.getRootLeafStatesEntered(), x ->
        // x.toString());
    }
}
