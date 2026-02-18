package ca.uwaterloo.watform.visualization;

import ca.uwaterloo.watform.dashast.DashParam;
import ca.uwaterloo.watform.dashast.DashStrings;
import ca.uwaterloo.watform.dashast.dashref.DashRef;
import ca.uwaterloo.watform.dashmodel.DashModel;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ControlStateHierarchyVisualizer {

    public static final String DEFAULT_PREFIX = "control_states";

    public void visualize(DashModel dm, Path outputDir) {
        visualize(dm, outputDir, DEFAULT_PREFIX, null);
    }

    public void visualize(DashModel dm, Path outputDir, String prefix) {
        visualize(dm, outputDir, prefix, null);
    }

    public void visualize(
            DashModel dm, Path outputDir, String prefix, List<String> highlightedStates) {
        GraphPrinter gp = new GraphPrinter(outputDir);
        Set<String> highlighted = normalizeHighlights(highlightedStates);
        buildHierarchy(dm, dm.rootName(), gp, highlighted);
        addTransitions(dm, gp);
        gp.print(prefix);
        gp.generateJson(prefix);
    }

    private void buildHierarchy(
            DashModel dm, String nodeName, GraphPrinter gp, Set<String> highlightedStates) {
        boolean atMaxDepth = findNodeDepth(nodeName, dm) == maxDepth(dm, dm.rootName());
        if (!dm.isLeaf(nodeName) || !atMaxDepth) {
            if (dm.isRoot(nodeName)) {
                gp.addln("subgraph cluster_" + formatString(nodeName) + " {");
                gp.addln("label=" + labelForNode(dm, nodeName));
                if (dm.isAnd(nodeName)) {
                    gp.addln("style=dashed");
                }
            } else {
                gp.addln("subgraph cluster_" + formatString(nodeName) + " {");
                if (highlightedStates.contains(formatString(nodeName))) {
                    gp.addln("style=filled");
                    gp.addln("fillcolor=yellow");
                }
                gp.addln("label=" + labelForNode(dm, nodeName));
                gp.addln(formatString(nodeName) + " [style=invis,shape=point,  penwidth=0]");
                if (dm.isAnd(nodeName)) {
                    gp.addln("style=dashed");
                }
            }
        } else {
            if (highlightedStates.contains(formatString(nodeName))) {
                gp.addln(
                        formatString(nodeName)
                                + " [label="
                                + labelForNode(dm, nodeName)
                                + ", style=filled, fillcolor=yellow]");
            } else {
                gp.addln(formatString(nodeName) + " [label=" + labelForNode(dm, nodeName) + "]");
            }
        }

        for (String child : dm.immChildren(nodeName)) {
            buildHierarchy(dm, child, gp, highlightedStates);
        }

        if (!dm.isLeaf(nodeName) || !atMaxDepth) {
            if (!dm.isRoot(nodeName)) {
                gp.addln(
                        formatString(nodeName)
                                + "_other_side [style=invis,shape=point,penwidth=0]");
            }
            gp.addln("}");
        }
    }

    private void addTransitions(DashModel dm, GraphPrinter gp) {
        for (String transition : dm.allTransNames()) {
            DashRef sourceRef = dm.fromR(transition);
            DashRef destRef = dm.gotoR(transition);
            if (sourceRef == null || destRef == null) {
                continue;
            }
            String source = sourceRef.name;
            String destination = destRef.name;
            if (findNodeDepth(source, dm) == maxDepth(dm, dm.rootName())) {
                gp.addln(
                        formatString(source)
                                + "->"
                                + formatString(destination)
                                + " [label="
                                + formatString(transition)
                                + "]");
            } else {
                gp.addln(
                        formatString(source)
                                + "->"
                                + formatString(destination)
                                + "_other_side [label="
                                + formatString(transition)
                                + ",ltail=cluster_"
                                + formatString(source)
                                + ",lhead=cluster_"
                                + formatString(destination)
                                + ",]");
            }
        }
    }

    private static int maxDepth(DashModel dm, String root) {
        if (root == null) {
            return 0;
        }
        if (dm.immChildren(root).isEmpty()) {
            return 1;
        }
        int maxDepth = 0;
        for (String child : dm.immChildren(root)) {
            maxDepth = Math.max(maxDepth, maxDepth(dm, child));
        }
        return maxDepth + 1;
    }

    private static int findNodeDepth(String targetNode, DashModel dm) {
        return findNodeDepthHelper(dm.rootName(), targetNode, 1, dm);
    }

    private static int findNodeDepthHelper(
            String root, String targetNode, int depth, DashModel dm) {
        if (root == null) {
            return -1;
        }
        if (root.equals(targetNode)) {
            return depth;
        }
        for (String child : dm.immChildren(root)) {
            int childDepth = findNodeDepthHelper(child, targetNode, depth + 1, dm);
            if (childDepth != -1) {
                return childDepth;
            }
        }
        return -1;
    }

    private static String formatString(String name) {
        return name.replace(DashStrings.internalQualChar, DashStrings.outputQualChar);
    }

    private static String labelFromFormatted(String formattedName) {
        int lastIndex = formattedName.lastIndexOf('_');
        return lastIndex >= 0 ? formattedName.substring(lastIndex + 1) : formattedName;
    }

    private static String labelForNode(DashModel dm, String nodeName) {
        String formattedName = formatString(nodeName);
        String baseLabel = labelFromFormatted(formattedName);
        List<DashParam> params = dm.stateParams(nodeName);
        if (params.isEmpty()) {
            return quoteLabel(baseLabel);
        }
        String parent = dm.parent(nodeName);
        if (parent != null && dm.stateHasParams(parent)) {
            return quoteLabel(baseLabel);
        }
        String paramText =
                params.stream().map(param -> param.paramSig).collect(Collectors.joining(", "));
        return quoteLabel(baseLabel + " [" + paramText + "]");
    }

    private static String quoteLabel(String label) {
        return "\"" + label.replace("\"", "\\\"") + "\"";
    }

    private static Set<String> normalizeHighlights(List<String> highlightedStates) {
        if (highlightedStates == null || highlightedStates.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> highlights = new HashSet<>();
        for (String state : highlightedStates) {
            highlights.add(formatString(state));
        }
        return highlights;
    }
}
