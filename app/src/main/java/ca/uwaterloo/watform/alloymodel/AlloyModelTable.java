package ca.uwaterloo.watform.alloymodel;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.paragraph.*;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.utils.GeneralUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A data structure to hold Alloy paragraphs, indexed by a string name. This table maps a name
 * (e.g., "sig", "fact", or a specific signature name) to a list of paragraph AST nodes.
 *
 * @param <T> The specific type of AlloyParagraph being stored, e.g., AlloySigPara, AlloyFactPara.
 */
public final class AlloyModelTable<T extends AlloyParagraph> {
    private final Map<String, T> mp;
    private final List<T> li; // list for holding paragraphs with no name
    private final Set<T> instanceTracker; // avoid duplicated instance

    // final here means the reference cannot change
    // but the data structure is mutable

    /**
     * @param AlloyFile
     * @param typeToken(problem with Java type erasure; cannot use the generic T)
     */
    public AlloyModelTable(AlloyFile alloyFile, Class<T> typeToken) {
        this.mp = new HashMap<>();
        this.li = new ArrayList<>();
        this.instanceTracker = Collections.newSetFromMap(new IdentityHashMap<>());
        List<T> TList = new ArrayList<>();
        if (AlloySigPara.class == typeToken) {
            // sig is special, b/c we need to expand it
            for (AlloySigPara sig :
                    GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, AlloySigPara.class)) {
                @SuppressWarnings("unchecked")
                List<T> expandedSigs = (List<T>) sig.expand();
                TList.addAll(expandedSigs);
            }
        } else {
            TList = GeneralUtil.extractItemsOfClass(alloyFile.paragraphs, typeToken);
        }
        this.addParagraphs(TList, new ArrayList<>());
    }

    /**
     * @param paragraph
     * @param additionalParas: need to keep a list of paragraphs added to AlloyModel, so they can be
     *     printed after AlloyFile in AlloyModel
     */
    public void addParagraph(T paragraph, List<AlloyParagraph> additionalParas) {
        checkDuplicates(paragraph);
        Optional<String> name = paragraph.getName();
        if (name == null || name.isEmpty() || name.get().isBlank()) {
            // relying on short-circuiting to not throw NoSuchElementException
            this.li.add(paragraph);
        } else {
            if (this.mp.containsKey(name.get())) {
                throw AlloyModelError.duplicateName(this.mp.get(name.get()).pos, paragraph.pos);
            }
            this.mp.put(name.get(), paragraph);
        }
        additionalParas.add(paragraph);
    }

    /**
     * @param paragraphs
     * @param additionalParas: need to keep a list of paragraphs added to AlloyModel, so they can be
     *     printed after AlloyFile in AlloyModel
     */
    public void addParagraphs(List<T> paragraphs, List<AlloyParagraph> additionalParas) {
        paragraphs.forEach(p -> this.addParagraph(p, additionalParas));
    }

    /**
     * Retrieves the single paragraph associated with a given name.
     *
     * @param name The name to look up.
     * @return An Optional containing the paragraph if found, or Optional.empty() if not.
     */
    public T getParagraph(String name) {
        if (name == null || name.isEmpty() || name.isBlank()) {
            throw AlloyModelImplError.lookUpWithNoName();
        }
        if (!this.mp.containsKey(name)) {
            throw AlloyModelError.paragraphDNE(name);
        }
        return this.mp.get(name);
    }

    /**
     * Retrieves an **immutable** list of all paragraphs that were added *without* a name.
     *
     * @return An immutable List of unnamed paragraphs.
     */
    public List<T> getUnnamedParagraphs() {
        return Collections.unmodifiableList(this.li);
    }

    /**
     * Retrieves a single, **immutable** list containing *all* paragraphs, both named (from the map)
     * and unnamed (from the list).
     *
     * @return An immutable List of all paragraphs stored in this table.
     */
    public List<T> getAllParagraphs() {
        return Stream.concat(mp.values().stream(), li.stream())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Gets an **immutable set** of all the names (keys) for named paragraphs.
     *
     * @return An immutable Set of all string keys from the map.
     */
    public Set<String> getNames() {
        // Return an unmodifiable view of the key set
        return Collections.unmodifiableSet(this.mp.keySet());
    }

    /**
     * Checks if the table contains a paragraph for a given name.
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

    private void checkDuplicates(T paragraph) {
        if (!this.instanceTracker.add(paragraph)) {
            throw AlloyModelImplError.duplicateInstance(paragraph.pos);
        }
    }
}
