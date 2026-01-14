package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A data structure to hold Alloy paras, indexed by a string name. This table maps a name (e.g.,
 * "sig", "fact", or a specific signature name) to a list of para AST nodes.
 *
 * @param <T> The specific type of AlloyPara being stored, e.g., AlloySigPara, AlloyFactPara.
 */
public final class AlloyModelTable<T extends AlloyPara> {
    private final Map<String, T> mp;
    private final List<T> li; // list for holding paras with no name

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

    private AlloyModelTable(AlloyModelTable<T> other) {
        this.mp = new HashMap<String, T>(other.mp);
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
        if (para instanceof AlloySigPara) {
            List<AlloySigPara> expandedSigs = ((AlloySigPara) para).expand();
            if (expandedSigs.size() > 1) {
                @SuppressWarnings("unchecked")
                List<T> castedExpandedSigs = (List<T>) expandedSigs;
                this.addParas(castedExpandedSigs, additionalParas);
                return;
            }
            @SuppressWarnings("unchecked")
            T castedExpandedSig = (T) expandedSigs.get(0);
            para = castedExpandedSig;
        }

        Optional<String> name = para.getName();
        if (name == null || name.isEmpty() || name.get().isBlank()) {
            // relying on short-circuiting to not throw NoSuchElementException
            this.li.add(para);
        } else {
            // AlloyFactPara names are allowed to overlap
            if (!(para instanceof AlloyFactPara) && this.mp.containsKey(name.get())) {
                throw AlloyModelError.duplicateName(this.mp.get(name.get()).pos, para.pos);
            }
            this.mp.put(name.get(), para);
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
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw AlloyModelImplError.lookUpWithNoName();
        }
        if (!this.mp.containsKey(name)) {
            throw AlloyModelError.paraDNE(name);
        }
        return this.mp.get(name);
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
    public Set<String> getNames() {
        // Return an unmodifiable view of the key set
        return Collections.unmodifiableSet(this.mp.keySet());
    }

    /**
     * Checks if the table contains a para for a given name.
     *
     * @param name The name to check.
     * @return true if the name is a key in the map, false otherwise.
     */
    public boolean containsName(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw AlloyModelImplError.lookUpWithNoName();
        }
        return this.mp.containsKey(name);
    }
}
