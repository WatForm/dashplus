package ca.uwaterloo.watform.dashmodel;

// import java.util.Set;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashParam;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.LinkedHashMap;
// import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BufferTable {

    // stores Buffer Decls in a HashMap based on the FQN
    private HashMap<String, BufferElement> bt;
    public String name = "Buffer";

    public BufferTable() {
        this.bt = new HashMap<String, BufferElement>();
    }

    public class BufferElement {
        private IntEnvKind kind;
        private List<DashParam> params;
        private String element;
        private Integer index;

        public BufferElement(
                IntEnvKind k,
                List<DashParam> prms,
                // List<Integer> prmsIdx,
                String e,
                Integer idx) {
            assert (prms != null);
            this.kind = k;
            this.params = prms;
            // this.paramsIdx = prmsIdx;
            this.element = e;
            this.index = idx;
        }

        public String toString() {
            String s = new String();
            s += "kind: " + kind + "\n";
            s += "params: " + NoneStringIfNeeded(params) + "\n";
            // s += "paramsIdx: "+ NoneStringIfNeeded(paramsIdx) +"\n";
            s += "element: " + element.toString() + "\n";
            s += "index:" + index;
            return s;
        }
    }

    public Boolean addBuffer(
            String bfqn, IntEnvKind k, List<DashParam> prms, String el, Integer idx) {
        assert (prms != null);
        if (bt.containsKey(bfqn)) return false;
        else if (hasPrime(bfqn)) {
            DashModelErrors.nameShouldNotBePrimed(bfqn);
            return false;
        } else {
            bt.put(bfqn, new BufferElement(k, prms, el, idx));
            return true;
        }
    }

    public String toString() {
        String s = new String("BUFFER TABLE\n");
        for (String k : bt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += bt.get(k).toString();
        }
        return s;
    }

    private boolean contains(String bfqn) {
        return (bt.containsKey(bfqn));
    }

    // individual getters
    public int getIndex(String bfqn) {
        return bt.get(bfqn).index;
    }

    public String getElement(String bfqn) {
        return bt.get(bfqn).element;
    }

    public List<DashParam> getParams(String bfqn) {
        return bt.get(bfqn).params;
    }

    public IntEnvKind getKind(String bfqn) {
        return (bt.get(bfqn).kind);
    }

    public boolean isInternal(String bfqn) {
        return (bt.get(bfqn).kind == IntEnvKind.INT);
    }

    // group getters
    public List<String> getAllBufferNames() {
        return new ArrayList<String>(bt.keySet());
    }

    public List<Integer> getBufferIndices() {
        List<Integer> k = new ArrayList();
        for (int i = 0; i < getAllBufferNames().size(); i++) k.add(i);
        return k;
    }

    public List<String> getBuffersOfState(String sfqn) {
        // return all buffers declared in this state
        // will have the sfqn as a prefix
        return bt.keySet().stream()
                // prefix of vfqn are state names
                .filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
                .collect(Collectors.toList());
    }
}
