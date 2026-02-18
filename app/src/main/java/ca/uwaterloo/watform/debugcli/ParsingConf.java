package ca.uwaterloo.watform.debugcli;

import java.util.HashMap;
import java.util.Map;

public class ParsingConf {
    private String stateSigName = "State";
    private String initPredicateName = "init";
    private String transitionRelationName = "next";
    private Map<String, Integer> additionalSigScopes = new HashMap<>();

    public static ParsingConf initializeWithYaml(String file) {
        return new ParsingConf();
    }

    public void setStateSigName(String stateSigName) {
        this.stateSigName = stateSigName;
    }

    public String getStateSigName() {
        return stateSigName;
    }

    public void setInitPredicateName(String initPredicateName) {
        this.initPredicateName = initPredicateName;
    }

    public String getInitPredicateName() {
        return initPredicateName;
    }

    public void setTransitionRelationName(String transitionRelationName) {
        this.transitionRelationName = transitionRelationName;
    }

    public String getTransitionRelationName() {
        return transitionRelationName;
    }

    public void setAdditionalSigScopes(Map<String, Integer> additionalSigScopes) {
        this.additionalSigScopes = additionalSigScopes;
    }

    public Map<String, Integer> getAdditionalSigScopes() {
        return additionalSigScopes;
    }
}
