package ca.uwaterloo.watform.alloymodel;

import java.util.*;
import java.util.function.Function;

public class DetectCycles {

    // throws exception if a cycle
    // o/w returns list in order of parent to child

    // largely written by ChatGPT
    public static List<String> topoOrderCycleDetector(
            List<String> startingSet, Function<String, List<String>> getChildren) {

        Set<String> visited = new HashSet<>();
        Set<String> inStack = new HashSet<>();
        List<String> result = new ArrayList<>();

        for (String node : startingSet) {
            if (visited.contains(node)) continue;

            Deque<String> stack = new ArrayDeque<>();
            Map<String, Iterator<String>> iters = new HashMap<>();

            stack.push(node);
            inStack.add(node);
            iters.put(node, getChildren.apply(node).iterator());

            while (!stack.isEmpty()) {
                String curr = stack.peek();
                Iterator<String> it = iters.get(curr);

                if (it.hasNext()) {
                    String child = it.next();

                    if (inStack.contains(child)) {
                        throw AlloyModelError.sigsInCycle(child.toString());
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
