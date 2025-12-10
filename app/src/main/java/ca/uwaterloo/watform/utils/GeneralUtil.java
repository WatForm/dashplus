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
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneralUtil {
    public static <T> T requireNonNull(T object, RuntimeException exception) {
        if (null == object) {
            throw exception;
        }
        return object;
    }

    public static String requireNonNull(String str, RuntimeException runtimeException) {
        if (null == str || str.isBlank()) {
            throw runtimeException;
        }
        return str;
    }

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

    public static <T extends ASTNode> void passIfNull(StringBuilder sb, int indent, T x) {
        if (x == null) {
        } else x.toString(sb, indent);
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

    // number of occurrences of c in s (no idiomatic way to write this in java)
    public static int occurrences(String s, String c) {
        return s.length() - s.replace(c, "").length();
    }

    // from: https://stackoverflow.com/questions/53441973/mapping-over-a-list-in-java
    public static <T, S> List<S> mapBy(List<T> items, Function<T, S> mapFn) {
        return items.stream().map(mapFn).collect(Collectors.toList());
    }

    // https://en.wikipedia.org/wiki/Fold_(higher-order_function)#Linear_vs._tree-like_folds
    // f: S -> T -> T
    public static <S, T> T foldRight(List<? extends S> items, BiFunction<S, T, T> f, T defaultVal) {
        if (items.size() == 0) return defaultVal;
        int n = items.size();
        T top = f.apply(items.get(n - 1), defaultVal);
        for (int i = n - 2; i >= 0; i--) top = f.apply(items.get(i), top);
        return top;
    }

    // f: T -> S -> T
    public static <S, T> T foldLeft(List<? extends S> items, BiFunction<T, S, T> f, T defaultVal) {
        if (items.size() == 0) return defaultVal;
        int n = items.size();
        T top = f.apply(defaultVal, items.get(0));
        for (int i = 1; i < n; i++) top = f.apply(top, items.get(i));
        return top;
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

    public static <T> boolean uniqueness(List<T> values, BiPredicate<T, T> equality) {
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.size(); j++) {
                if (i == j) continue;
                if (equality.test(values.get(i), values.get(j))) return false;
            }
        }
        return true;
    }

    private class Error {
        public static void tooMany(Pos pos, String errorName) throws Reporter.ErrorUser {
            throw new Reporter.ErrorUser(pos, "Multiple " + errorName + "s");
        }
    }
}
