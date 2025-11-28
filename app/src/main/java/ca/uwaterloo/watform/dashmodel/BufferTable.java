package ca.uwaterloo.watform.dashmodel;

// import java.util.Set;
import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.HashMap;
// import java.util.LinkedHashMap;
// import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BufferTable {

    // stores Buffer Decls in a HashMap based on the FQN
    private HashMap<String, BufferElement> bt;
    // private String tableName = "Buffer"; // TODO: this is not used anywhere else
    public int numBuffers = 0;

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
                String e) {
            assert (prms != null);
            this.kind = k;
            this.params = prms;
            // this.paramsIdx = prmsIdx;
            this.element = e;
            this.index = numBuffers;
            numBuffers++;
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

    public void add(Pos pos, String bfqn, IntEnvKind k, List<DashParam> prms, String el) {
        assert (prms != null);
        if (bt.containsKey(bfqn)) DashModelErrors.duplicateName(pos, "buffer", bfqn);
        else if (hasPrime(bfqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, bfqn);
        } else {
            bt.put(bfqn, new BufferElement(k, prms, el));
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

    public boolean contains(String bfqn) {
        return (bt.containsKey(bfqn));
    }

    // so we can treat this as a table
    // to the outside world
    public BufferElement get(String bfqn) {
        return bt.get(bfqn);
    }

    public List<String> keySet() {
        return setToList(bt.keySet());
    }

    public boolean isEmpty() {
        return bt.isEmpty();
    }

    public boolean isInternal(String bfqn) {
        return (bt.get(bfqn).kind == IntEnvKind.INT);
    }

    // group getters
    public List<String> getAllBufferNames() {
        return new ArrayList<String>(bt.keySet());
    }

    public List<Integer> getBufferIndices() {
        // 0 .. numBuffers-1
        return range(0, numBuffers);
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
