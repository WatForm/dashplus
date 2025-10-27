/*
 * For the util functions used many places
 */

package ca.uwaterloo.watform.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneralUtil {

    /* copied from https://stackoverflow.com/questions/7414667/identify-duplicates-in-a-list */
    public static <T> Set<T> findDuplicates(Collection<T> collection) {

        Set<T> duplicates = new LinkedHashSet<>();
        Set<T> uniques = new HashSet<>();

        for (T t : collection) if (!uniques.add(t)) duplicates.add(t);
        return duplicates;
    }

    public static <T> List<T> emptyList() {
        return new ArrayList<T>();
    }

    public static <T> String strCommaList(List<T> ll) {
        StringJoiner sj = new StringJoiner(", ");
        ll.forEach(n -> sj.add(n.toString()));
        return sj.toString();
    }

    public static <T> Set<T> listToSet(List<T> ll) {
        return ll.stream().collect(Collectors.toSet());
    }

    public static <T> List<T> setToList(Set<T> ll) {
        return new ArrayList<T>(ll);
    }

    public static void myprint(String s) {
        // debugging output
        System.out.println(s);
    }

    public static <T> List<T> newListWith(List<T> ll, T s) {
        List<T> x = new ArrayList<T>(ll);
        x.add(s);
        return x;
    }

    public static <T> String NoneStringIfNeeded(T x) {
        return ((x == null) ? "none" : x.toString());
    }

    public static void handleException(Exception e) {
        e.printStackTrace(System.err);
        System.exit(1);
    }

    public static <T> T lastElement(List<T> ll) {
        return ll.get(ll.size() - 1);
    }

    public static <T> List<T> allButLast(List<T> ll) {
        if (ll.isEmpty()) return ll;
        else return ll.subList(0, ll.size() - 1);
    }

    // java's Collection.reverse doesn't work sometimes
    public static <T> List<T> reverse(List<T> ll) {
        assert (!ll.isEmpty());
        List<T> x = new ArrayList<T>();
        for (int i = ll.size() - 1; i >= 0; i--) {
            x.add(ll.get(i));
        }
        assert (x.size() == ll.size());
        return x;
    }

    public static <T> List<T> tail(List<T> ll) {
        assert (ll.size() > 1);
        return ll.subList(1, ll.size());
    }

    public static List<Integer> listOfInt(int start, int stop) {
        assert (start <= stop);
        List<Integer> x = new ArrayList<Integer>();
        for (int i = start; i <= stop; i++) x.add(i);
        return x;
    }

    // from: https://stackoverflow.com/questions/53441973/mapping-over-a-list-in-java
    public static <T, S> List<S> mapBy(List<T> items, Function<T, S> mapFn) {
        return items.stream().map(mapFn).collect(Collectors.toList());
    }

    public static <T> List<T> filterBy(List<T> items, Predicate<T> filterFn) {
        return items.stream().filter(filterFn).collect(Collectors.toList());
    }

    public static <T> boolean containsMatch(List<T> items, Predicate<T> matchFn) {
        return items.stream().anyMatch(matchFn);
    }

    // this does not change the input list
    public static <T, S> List<T> extractItemsOfClass(List<S> items, Class<T> c) {
        return items.stream().filter(c::isInstance).map(c::cast).collect(Collectors.toList());
    }

    // like python range; inclusive at start and exclusive on stop
    public static List<Integer> range(int start, int stop) {
        return IntStream.range(start, stop)
                .boxed() // convert int → Integer
                .collect(Collectors.toList());
    }

    public static <T> ASTNode extractOneFromList(List<? extends ASTNode> xlist, String errorName) {
        if (xlist.size() == 0) return null;
        else if (xlist.size() > 1) {
            Error.tooMany(xlist.get(1).pos, errorName);
            return null;
        } else return xlist.get(0);
    }

    private class Error {
        public static void tooMany(Pos pos, String errorName) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, "Multiple " + errorName + "s");
        }
    }
}
