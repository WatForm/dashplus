package ca.uwaterloo.watform.alloymodel;

import java.util.*;
import java.util.function.Function;

public class DetectCycles {

    // throws exception if a cycle
    // o/w returns list in order of parent to child

    // largely written by ChatGPT
    public static List<Qname> topoOrderCycleDetector(
            List<Qname> startingSet, Function<Qname, List<Qname>> getChildren) {

        Set<Qname> visited = new HashSet<>();
        Set<Qname> inStack = new HashSet<>();
        List<Qname> result = new ArrayList<>();

        for (Qname node : startingSet) {
            if (visited.contains(node)) continue;

            Deque<Qname> stack = new ArrayDeque<>();
            Map<Qname, Iterator<Qname>> iters = new HashMap<>();

            stack.push(node);
            inStack.add(node);
            iters.put(node, getChildren.apply(node).iterator());

            while (!stack.isEmpty()) {
                Qname curr = stack.peek();
                Iterator<Qname> it = iters.get(curr);

                if (it.hasNext()) {
                    Qname child = it.next();

                    if (inStack.contains(child)) {
                        throw AlloyModelError.sigsInCycle(child.fullName());
                    }

                    if (!visited.contains(child)) {
                        stack.push(child);
                        inStack.add(child);
                        iters.put(child, getChildren.apply(child).iterator());
                    }
                } else {
                    // DONE with this node → postorder position
                    stack.pop();
                    inStack.remove(curr);
                    visited.add(curr);

                    result.add(curr); // children already added earlier
                }
            }
        }

        return result;
    }
}
