package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.dashast.DashStrings.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashFile;
import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.utils.Pos;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BuffersDM extends VarsDM {

    // stores Buffer Decls in a HashMap based on the FQN
    private HashMap<String, BufferEntry> bt = new HashMap<String, BufferEntry>();
    private Integer numBuffers = 0;

    public BuffersDM() {
        super();
    }

    public BuffersDM(DashFile d) {
        super(d);
    }

    // individual buffer non-complex getters/testers

    public boolean isIntBuffer(String bfqn) {
        return this.bt.get(bfqn).kind == IntEnvKind.INT;
    }

    public boolean isEnvBuffer(String bfqn) {
        return this.bt.get(bfqn).kind == IntEnvKind.ENV;
    }

    public List<DashParam> bufferParams(String bfqn) {
        return this.bt.get(bfqn).params;
    }

    public String bufferElement(String bfqn) {
        return this.bt.get(bfqn).element;
    }

    public Integer bufferIndex(String bfqn) {
        return this.bt.get(bfqn).index;
    }

    // group getters

    public List<String> allBufferNames() {
        return new ArrayList<String>(this.bt.keySet());
    }

    public List<String> intBufferNames() {
        return allBufferNames().stream().filter(i -> isIntBuffer(i)).collect(Collectors.toList());
    }

    public boolean hasBuffers() {
        return (!this.bt.isEmpty());
    }

    public boolean contains(String bfqn) {
        return (this.bt.containsKey(bfqn));
    }

    public List<Integer> bufferIndices() {
        // 0 .. numBuffers-1
        return range(0, this.bt.keySet().size() - 1);
    }

    public List<String> buffersOfState(String sfqn) {
        // return all buffers declared in this state
        // will have the sfqn as a prefix
        return this.bt.keySet().stream()
                // prefix of vfqn are state names
                .filter(i -> DashFQN.chopPrefixFromFQN(i).equals(sfqn))
                .collect(Collectors.toList());
    }

    public void addBuffer(Pos pos, String bfqn, IntEnvKind k, List<DashParam> prms, String el) {
        assert (prms != null);
        if (bt.containsKey(bfqn)) DashModelErrors.duplicateName(pos, "buffer", bfqn);
        else if (hasPrime(bfqn)) {
            DashModelErrors.nameShouldNotBePrimed(pos, bfqn);
        } else {
            this.bt.put(bfqn, new BufferEntry(pos, k, prms, el));
        }
    }

    public void addBuffer(String bfqn, IntEnvKind k, List<DashParam> prms, String el) {
        addBuffer(Pos.UNKNOWN, bfqn, k, prms, el);
    }

    public String btToString() {
        String s = new String("BUFFER TABLE\n");
        for (String k : this.bt.keySet()) {
            s += " ----- \n";
            s += k + "\n";
            s += this.bt.get(k).toString();
        }
        return s;
    }

    private class BufferEntry {
        public final Pos pos;
        public final IntEnvKind kind;
        public final List<DashParam> params;
        public final String element;
        public final Integer index;

        public BufferEntry(Pos p, IntEnvKind k, List<DashParam> prms, String e) {
            assert (prms != null);
            this.pos = p;
            this.kind = k;
            this.params = prms;
            this.element = e;
            this.index = numBuffers;
            numBuffers++;
        }

        public String toString() {
            String s = new String();
            s += "kind: " + kind + "\n";
            s += "params: " + NoneStringIfNeeded(params) + "\n";
            s += "element: " + element.toString() + "\n";
            s += "index:" + index;
            return s;
        }
    }
}
