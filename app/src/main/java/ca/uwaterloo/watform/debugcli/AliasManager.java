package ca.uwaterloo.watform.debugcli;

import java.util.LinkedHashMap;
import java.util.Map;

public class AliasManager {
    private final Map<String, String> aliases = new LinkedHashMap<>();

    public boolean addAlias(String alias, String formula) {
        aliases.put(alias, formula);
        return true;
    }

    public boolean removeAlias(String alias) {
        return aliases.remove(alias) != null;
    }

    public boolean isAlias(String alias) {
        return aliases.containsKey(alias);
    }

    public String getFormula(String alias) {
        return aliases.get(alias);
    }

    public String getFormattedAliases() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : aliases.entrySet()) {
            sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public void clearAliases() {
        aliases.clear();
    }
}
