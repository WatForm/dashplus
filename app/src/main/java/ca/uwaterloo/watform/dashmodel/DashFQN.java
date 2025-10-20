package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.dashast.DashStrings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class DashFQN {

    // to create Alloy output
    public static String translateFQN(String n) {
        return n.replace(DashStrings.internalQualChar, DashStrings.outputQualChar);
    }

    // testing inputs from parsing
    public static Boolean isFQN(String n) {
        return n.contains(DashStrings.inputQualChar);
    }

    // creating FQNs from inputs --------------------------

    public static String fqn(String s1, String s2) {
        String q = new String(s1);
        q += DashStrings.internalQualChar;
        q += s2;
        return q;
    }

    // not really needed unless we change it so that
    // inputQualChar != internal QualChar
    public static String fqn(String n) {
        return n.replace(DashStrings.inputQualChar, DashStrings.internalQualChar);
    }

    public static String fqn(List<String> pth) {
        if (pth.isEmpty()) return null; // for root
        StringJoiner sj = new StringJoiner(DashStrings.internalQualChar);
        pth.forEach(n -> sj.add(fqn(n)));
        return sj.toString();
    }

    public static String fqn(List<String> pth, String name) {
        if (isFQN(name)) return fqn(name);
        else {
            StringJoiner sj = new StringJoiner(DashStrings.internalQualChar);
            pth.forEach(n -> sj.add(fqn(n)));
            sj.add(fqn(name));
            return sj.toString();
        }
    }

    public static String fqn(List<String> pth, String parent, String child) {
        if (isFQN(child))
            // return child.replace(inputQualChar,outputQualChar);
            return fqn(child);
        else {
            StringJoiner sj = new StringJoiner(DashStrings.internalQualChar);
            pth.forEach(n -> sj.add(fqn(n)));
            sj.add(fqn(parent));
            sj.add(fqn(child));
            return sj.toString();
        }
    }

    // operations on FQNs
    public static List<String> splitFQN(String fqn) {
        return Arrays.asList(fqn.split(DashStrings.internalQualChar));
    }

    // A/B + B/C => A/B/C
    // no longer used probably
    public static String mergeFQN(String fqn1, String fqn2) {
        List<String> fqn1parts = splitFQN(fqn1);
        List<String> fqn2parts = splitFQN(fqn2);
        int fqn1i = 0;
        int fqn2i = 0;
        List<String> outparts = new ArrayList<String>();
        while (fqn1i < fqn1parts.size() && !(fqn1parts.get(fqn1i).equals(fqn2parts.get(fqn2i)))) {
            outparts.add(fqn1parts.get(fqn1i));
            fqn1i++;
        }
        if (fqn1i == fqn1parts.size()) return "";
        while (fqn1i < fqn1parts.size()
                && fqn2i < fqn2parts.size()
                && fqn1parts.get(fqn1i).equals(fqn2parts.get(fqn2i))) {
            outparts.add(fqn1parts.get(fqn1i));
            fqn1i++;
            fqn2i++;
        }
        if (fqn1i == fqn1parts.size() && fqn2i == fqn2parts.size()) return fqn(outparts);
        if (fqn1i == fqn1parts.size() && fqn2i < fqn2parts.size()) {
            while (fqn2i < fqn2parts.size()) {
                outparts.add(fqn2parts.get(fqn2i));
                fqn2i++;
            }
            return fqn(outparts);
        } else return "";
    }

    public static int commonPrefixLength(String s1, String s2) {
        List<String> parts1 = splitFQN(s1);
        List<String> parts2 = splitFQN(s2);
        int i = 0;
        while (i < parts1.size() && i < parts2.size() && parts1.get(i).equals(parts2.get(i))) i++;
        return i;
    }

    public static String chopNameFromFQN(String fqn) {
        // this is from an output FQN
        return lastElement(splitFQN(fqn));
    }

    // useful for getting parent state of a trans/event decl, etc.
    public static String chopPrefixFromFQN(String fqn) {
        List<String> s = splitFQN(fqn);
        if (s.size() < 2) {
            DashModelErrors.chopPrefixFromFQNwithNoPrefix(fqn);
            return null;
        } else return fqn(allButLast(splitFQN(fqn)));
    }

    // can't just take longest prefix because states may have similar names
    // such as Bit1 and Bit2
    public static String longestCommonFQN(String a, String b) {
        List<String> aSplit = splitFQN(a);
        List<String> bSplit = splitFQN(b);
        String result = new String();
        int minLength = Math.min(aSplit.size(), bSplit.size());
        int i = 0;
        while (i < minLength && aSplit.get(i).equals(bSplit.get(i))) i++;
        return fqn(aSplit.subList(0, i));
    }

    // include this fqn
    public static List<String> allPrefixes(String fqn) {
        List<String> prefixes = new ArrayList<String>();
        List<String> splitfqn = splitFQN(fqn);
        for (int i = 0; i < splitfqn.size(); i++) {
            StringJoiner sj = new StringJoiner(DashStrings.internalQualChar);
            for (int j = 0; j <= i; j++) {
                sj.add(splitfqn.get(j));
            }
            prefixes.add(sj.toString());
        }
        return prefixes;
    }

    /*
    	suffix("A/B/C/x", "C/x") is true
    	suffix("A/B/C/x", "x") is true
    	suffix("x","x") is true
    	suffix("A/B/xyz", "yz") is false

    	can't just end with the suffix b/c that might not be a complete name!
    	Chairs can match xChairs and yChairs but those are actually different identifiers!
    */
    public static boolean suffix(String fqn, String suffix) {
        // System.out.println(a);
        // System.out.println(b);
        if (fqn.endsWith(suffix)) {
            int x = fqn.lastIndexOf(suffix);
            // System.out.println(x);
            if (x != -1) {
                if (x == 0) return true;
                // System.out.println(a.charAt(x-1));
                if (fqn.charAt(x - 1) == DashStrings.internalQualChar.charAt(0)) return true;
            }
        }
        return false;
    }

    /* ances: A/B/C dest: A/B/C/D/E returns A/B/C/D */
    public static String getChildOfContextAncesOfDest(String ances, String dest) {
        if (dest.equals(ances)) return dest;
        else if (dest.startsWith(ances))
            // dest must be longer than ances
            return fqn(splitFQN(dest).subList(0, splitFQN(ances).size() + 1));
        else {
            DashModelErrors.ancesNotPrefix(ances, dest);
            return null;
        }
    }

    /*
    used for determining if a "name" (var/event/etc) was declared in a state
    (at this level)
    where the fqn of the state is the "prefix" arg and
    the fqn of the "name" is the "fqn" arg

    isLongestPrefix("A/B/C", "A/B/C/x") is true
    isLongestPrefix("A/B/C", "A/B/C/D/x") is false
    isLongestPrefix("A/B/C","A/B/D/x") is false
     */
    public static boolean isLongestPrefix(String prefix, String fqn) {
        return prefix.equals(chopPrefixFromFQN(fqn));
    }

    /*
    used for determining if a "name" (var/event/etc) was declared WITHIN a state
    (could be declared in a substate)
    where the sfqn of the state is the "prefix" arg and
    the fqn of the "name" is the "fqn" arg

    prefix("A/B/C", "A/B/C/x") is true
    prefix("A/B/C", "A/B/C/D/x") is true
    prefix("x", "x") is true
    prefix("A/B/C","A/B/D/x") is false
    prefix("x", "y") is false
    prefix("x", "xi") is false

     */
    public static boolean prefix(String sfqn, String fqn) {
        if (fqn.startsWith(sfqn)) {
            int x = sfqn.length() - 1;
            if (x == 0) return true;
            // A/B/C/ev
            // A/B
            if (fqn.charAt(x) + 1 == DashStrings.internalQualChar.charAt(0)) return true;
        }
        return false;
    }
}
