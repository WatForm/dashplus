package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.AlloyPara.AlloyId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A data structure to hold Alloy paras, indexed by a string name. This table maps a name (e.g.,
 * "sig", "fact", or a specific signature name) to a list of para AST nodes.
 *
 * @param <T> The specific type of AlloyPara being stored, e.g., AlloySigPara, AlloyFactPara.
 */
public class AlloyModelTable<T extends AlloyPara> {
    protected final Map<AlloyId, T> mp;
    protected final List<T> li; // list for holding paras with no name

    // final here means the reference cannot change
    // but the data structure is mutable

    /**
     * @param AlloyFile
     * @param typeToken(problem with Java type erasure; cannot use the generic T)
     */
    public AlloyModelTable(AlloyFile alloyFile, Class<T> typeToken) {
        this.mp = new HashMap<>();
        this.li = new ArrayList<>();
        if (null == alloyFile) return;
        this.addParas(extractItemsOfClass(alloyFile.paras, typeToken), new ArrayList<>());
    }

    protected AlloyModelTable(AlloyModelTable<T> other) {
        this.mp = new HashMap<AlloyId, T>(other.mp);
        this.li = new ArrayList<>(other.li);
    }

    public AlloyModelTable<T> copy() {
        return new AlloyModelTable<>(this);
    }

    /**
     * @param para
     * @param additionalParas: need to keep a list of paras added to AlloyModel, so they can be
     *     printed after AlloyFile in AlloyModel
     */
    public void addPara(T para, List<AlloyPara> additionalParas) {
        AlloyId alloyId = para.getId();
        if (alloyId.name == null || alloyId.name.isBlank()) {
            this.li.add(para);
        } else {
            // AlloyFactPara names are allowed to overlap
            if (!(para instanceof AlloyFactPara) && this.mp.containsKey(alloyId)) {
                throw AlloyModelError.duplicateName(this.mp.get(alloyId).pos, para.pos);
            }
            this.mp.put(alloyId, para);
        }

        additionalParas.add(para);
    }

    /**
     * @param paras
     * @param additionalParas: need to keep a list of paras added to AlloyModel, so they can be
     *     printed after AlloyFile in AlloyModel
     */
    public void addParas(List<T> paras, List<AlloyPara> additionalParas) {
        paras.forEach(p -> this.addPara(p, additionalParas));
    }

    /**
     * Retrieves the single para associated with a given name.
     *
     * @param name The name to look up.
     * @return An Optional containing the para if found, or Optional.empty() if not.
     */
    public T getPara(String name) {
        return getPara(new AlloyId(name));
    }

    public T getPara(AlloyId alloyId) {
        if (alloyId == null || alloyId.name == null || alloyId.name.isBlank()) {
            throw AlloyModelImplError.lookUpWithNoName();
        }
        if (!this.mp.containsKey(alloyId)) {
            throw AlloyModelError.paraDNE(alloyId.name);
        }
        return this.mp.get(alloyId);
    }

    /**
     * Retrieves an **immutable** list of all paras that were added *without* a name.
     *
     * @return An immutable List of unnamed paras.
     */
    public List<T> getUnnamedParas() {
        return Collections.unmodifiableList(this.li);
    }

    /**
     * Retrieves a single, **immutable** list containing *all* paras, both named (from the map) and
     * unnamed (from the list).
     *
     * @return An immutable List of all paras stored in this table.
     */
    public List<T> getAllParas() {
        return Stream.concat(mp.values().stream(), li.stream())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Gets an **immutable set** of all the names (keys) for named paras.
     *
     * @return An immutable Set of all string keys from the map.
     */
    public Set<AlloyId> getIds() {
        // Return an unmodifiable view of the key set
        return Collections.unmodifiableSet(this.mp.keySet());
    }

    /**
     * Checks if the table contains a para for a given name.
     *
     * @param name The name to check.
     * @return true if the name is a key in the map, false otherwise.
     */
    public boolean contains(String name) {
        return contains(new AlloyId(name));
    }

    public boolean contains(AlloyId alloyId) {
        if (alloyId == null || alloyId.name == null || alloyId.name.isBlank()) {
            throw AlloyModelImplError.lookUpWithNoName();
        }
        return this.mp.containsKey(alloyId);
    }
}
