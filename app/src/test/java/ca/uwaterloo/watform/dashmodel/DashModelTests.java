/*
   check that the tests in the pass directory
   pass the parse and wff checks first
*/

package ca.uwaterloo.watform.dashmodel;

import static ca.uwaterloo.watform.parser.Parser.*;
import static org.junit.jupiter.api.Assertions.*;

import ca.uwaterloo.watform.utils.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

public class DashModelTests {

    private static String resourcePath = "src/test/resources/dashmodel";

    public static DashModel test(String fileName) {
        String absolutePath = new File(resourcePath).getAbsolutePath();
        return (DashModel) parseToModel(Paths.get(absolutePath + "/" + fileName + ".dsh"));
    }

    public List<String> ll(String[] k) {
        return Arrays.asList(k);
    }

    // helper functions ------------------

    public static String src(DashModel d, String s) {
        return d.fromR(s).toString();
    }

    public static String dest(DashModel d, String s) {
        return d.gotoR(s).toString();
    }

    public List<String> prefixDashRefs(DashModel d, String tfqn) {
        return d.prefixDashRefs(d.scope(tfqn)).stream()
                .map(i -> i.toString())
                .collect(Collectors.toList());
    }

    // src/dest ----------------
    @Test
    public void noDefaultNeededTest1() {
        DashModel d = test("noDefaultNeeded1");
        List<String> result = Arrays.asList(new String[] {"Root/S"});
        assertEquals(d.defaults("Root"), result);
    }

    @Test
    public void noDefaultNeededTest2() {
        DashModel d = test("noDefaultNeeded2");
        List<String> result = Arrays.asList(new String[] {"Root/C"});
        assertEquals(d.defaults("Root"), result);
    }

    @Test
    public void noSrcDest1() {
        DashModel d = test("noSrcDest1");
        assertEquals(src(d, "Root/t1"), "Root");
        assertEquals(dest(d, "Root/t1"), "Root");
    }

    @Test
    public void noSrc1() {
        DashModel d = test("noSrc1");
        assertEquals(src(d, "Root/S1/t1"), "Root/S1");
        assertEquals(dest(d, "Root/S1/t1"), "Root/S2");
    }

    @Test
    public void noSrc2() {
        DashModel d = test("noSrc2");
        assertEquals(src(d, "Root/S1/t1"), "Root/S1");
        assertEquals(dest(d, "Root/S1/t1"), "Root/S2");
    }

    @Test
    public void otherSrcDest1() {
        DashModel d = test("otherSrcDest1");
        assertEquals(src(d, "Root/S1/t1"), "Root/S1");
        assertEquals(dest(d, "Root/S1/t1"), "Root/S2/S3/S4");
        assertEquals(src(d, "Root/S2/t2"), "Root/S2/S3");
        assertEquals(dest(d, "Root/S2/t2"), "Root/S2");
    }

    @Test
    public void srcDestFQN1() {
        DashModel d = test("srcDestFQN1");
        assertEquals(src(d, "Root/S1/t1"), "Root/S1");
        assertEquals(dest(d, "Root/S1/t1"), "Root/S1/S7");
        assertEquals(src(d, "Root/S2/S3/S4/t2"), "Root/S2/S3/S4");
        assertEquals(dest(d, "Root/S2/S3/S4/t2"), "Root/S1/S7");
    }

    @Test
    public void paramSrcDest1() {
        DashModel d = test("paramSrcDest1");
        assertEquals(src(d, "Root/A/t1"), "Root/A[p_Root_A]");
        assertEquals(dest(d, "Root/A/t1"), "Root/A[p_Root_A]");
    }

    @Test
    public void paramSrcDest2() {
        DashModel d = test("paramSrcDest2");
        assertEquals(src(d, "Root/A/t1"), "Root/A[p_Root_A]");
        assertEquals(dest(d, "Root/A/t1"), "Root/B/S1[x]");
    }

    // allAnces --------------

    @Test
    public void allAnces1() {
        DashModel d = test("noSrc1");
        assertEquals(d.allAnces("Root/S1"), ll(new String[] {"Root", "Root/S1"}));
    }

    // closestParamAnces ----------------

    @Test
    public void closestParamAnces1() {
        DashModel d = test("noSrc1");
        assertEquals(d.closestParamAnces("Root/S1"), "Root");
        assertEquals(d.closestParamAnces("Root/S2"), "Root");
    }

    @Test
    public void closestParamAnces2() {
        DashModel d = test("scopeParam2");
        assertEquals(d.closestParamAnces("Root/A/B/S1"), "Root/A/B");
        assertEquals(d.closestParamAnces("Root/A/B"), "Root/A/B");
    }

    // region ----------------------
    @Test
    public void region1() {
        DashModel d = test("region1");
        assertEquals(
                d.region("Root/S1/S2"),
                ll(
                        new String[] {
                            "Root",
                            "Root/S1",
                            "Root/S1/S2",
                            "Root/S3",
                            "Root/S3/S4",
                            "Root/S3/S4/S5",
                            "Root/S3/A",
                            "Root/S3/A/S7",
                            "Root/S3/A/S7/S8",
                            "Root/S3/B",
                            "Root/S3/B/S7"
                        }));
    }

    @Test
    public void region2() {
        DashModel d = test("allNonConcDesc1");
        assertEquals(
                d.region("Root/S1/S2"),
                ll(
                        new String[] {
                            "Root",
                            "Root/S1",
                            "Root/S1/S2",
                            "Root/S3",
                            "Root/S3/S4",
                            "Root/S3/S4/S5",
                            "Root/S3/A",
                            "Root/S3/A/S7",
                            "Root/S3/B",
                            "Root/S3/B/S7"
                        }));
    }

    // prefixDashRefs ----------------------
    // prefix dash refs of scope of transition
    // slashes in these b/c they have not been translated

    // "x" is a sig external to Dash so no FQN
    @Test
    public void prefixDashRefs1() {
        DashModel d = test("scopeParam1");
        assertEquals(
                d.scope("Root/A/S1/t1").toString(), "Root/A[p_Root_A = x => p_Root_A else APID]");
        assertEquals(
                prefixDashRefs(d, "Root/A/S1/t1"),
                ll(new String[] {"Root", "Root/A[p_Root_A = x => p_Root_A else APID]"}));
    }

    // "x" is a var decl within Root so it has an FQN
    @Test
    public void prefixDashRefs2() {
        DashModel d = test("scopeParam2");
        assertEquals(
                d.scope("Root/A/B/S1/t1").toString(),
                "Root/A/B[p_Root_A = Root/x => p_Root_A else APID, p_Root_A = Root/x and p_Root_A_B = Root/y => p_Root_A_B else BPID]");
        assertEquals(
                prefixDashRefs(d, "Root/A/B/S1/t1"),
                ll(
                        new String[] {
                            "Root",
                            "Root/A[p_Root_A = Root/x => p_Root_A else APID]",
                            "Root/A/B[p_Root_A = Root/x => p_Root_A else APID, p_Root_A = Root/x and p_Root_A_B = Root/y => p_Root_A_B else BPID]"
                        }));
    }

    // no parameter values in src or dest
    @Test
    public void prefixDashRefs3() {
        DashModel d = test("scopeParam3");
        assertEquals(d.scope("Root/A/B/S1/t1").toString(), "Root/A/B[p_Root_A, p_Root_A_B]");
        assertEquals(
                prefixDashRefs(d, "Root/A/B/S1/t1"),
                ll(new String[] {"Root", "Root/A[p_Root_A]", "Root/A/B[p_Root_A, p_Root_A_B]"}));
    }

    @Test
    public void prefixDashRefs4() {
        DashModel d = test("entered5");
        assertEquals(
                prefixDashRefs(d, "Root/S1/t1"),
                ll(
                        new String[] {
                            "Root",
                        }));
    }

    // leafStatesExited --------------------

    public List<String> exited(DashModel d, String tfqn) {
        return d.exited(tfqn).stream().map(i -> i.toString()).collect(Collectors.toList());
    }

    @Test
    public void leafStatesExited1() {
        DashModel d = test("noSrc1");
        assertEquals(exited(d, "Root/S1/t1"), ll(new String[] {"Root/S1", "Root/S2"}));
    }

    @Test
    public void leafStatesExited2() {
        DashModel d = test("noSrc2");
        assertEquals(exited(d, "Root/S1/t1"), ll(new String[] {"Root/S1", "Root/S2/S3/S4"}));
    }

    @Test
    public void leafStatesExited3() {
        DashModel d = test("noDefaultNeeded2");
        assertEquals(exited(d, "Root/C/t1"), ll(new String[] {"Root/C"}));
    }

    @Test
    public void leafStatesExited4() {
        DashModel d = test("noSrcDest1");
        assertEquals(exited(d, "Root/t1"), ll(new String[] {"Root"}));
    }

    @Test
    public void leafStatesExited5() {
        DashModel d = test("otherSrcDest1");
        assertEquals(exited(d, "Root/S1/t1"), ll(new String[] {"Root/S1", "Root/S2/S3/S4"}));
        assertEquals(exited(d, "Root/S2/t2"), ll(new String[] {"Root/S2/S3/S4"}));
    }

    @Test
    public void leafStatesExited6() {
        DashModel d = test("paramSrcDest1");
        assertEquals(exited(d, "Root/A/t1"), ll(new String[] {"Root/A[p0_APID]"}));
    }

    @Test
    public void leafStatesExited7() {
        DashModel d = test("paramSrcDest3");
        assertEquals(exited(d, "Root/A/t1"), ll(new String[] {"Root/A[APID]", "Root/B/S1[BPID]"}));
    }

    // scope -------------------------------
    @Test
    public void scope1() {
        DashModel d = test("scopeParam1");
        assertEquals(
                d.scope("Root/A/S1/t1").toString(),
                // x could be something that has reference to
                // a dynamic variable in it
                "Root/A[p_Root_A = x => p_Root_A else APID]");
    }

    @Test
    public void scope2() {
        DashModel d = test("scopeParam2");
        assertEquals(
                d.scope("Root/A/B/S1/t1").toString(),
                "Root/A/B[p_Root_A = Root/x => p_Root_A else APID, p_Root_A = Root/x and p_Root_A_B = Root/y => p_Root_A_B else BPID]");
    }

    @Test
    public void scope3() {
        DashModel d = test("scopeParam3");
        assertEquals(d.scope("Root/A/B/S1/t1").toString(), "Root/A/B[p_Root_A, p_Root_A_B]");
    }

    // leafStatesEntered -----------------

    public List<String> entered(DashModel d, String tfqn) {
        // helper function to turn function result into a list
        // of strings for comparison
        return d.entered(tfqn).stream().map(i -> i.toString()).collect(Collectors.toList());
    }

    @Test
    public void leafStatesEntered1() {
        DashModel d = test("noSrc1");
        assertEquals(entered(d, "Root/S1/t1"), ll(new String[] {"Root/S2"}));
    }

    @Test
    public void leafStatesEntered2() {
        DashModel d = test("noSrc2");
        assertEquals(entered(d, "Root/S1/t1"), ll(new String[] {"Root/S2/S3/S4"}));
    }

    @Test
    public void leafStatesEntered3() {
        DashModel d = test("scopeParam1");
        assertEquals(entered(d, "Root/A/S1/t1"), ll(new String[] {"Root/A/S2[x]"}));
    }

    @Test
    public void leafStatesEntered4() {
        DashModel d = test("scopeParam2");
        assertEquals(entered(d, "Root/A/B/S1/t1"), ll(new String[] {"Root/A/B/S2[x, y]"}));
    }

    @Test
    public void leafStatesEntered5() {
        DashModel d = test("entered5");
        assertEquals(entered(d, "Root/S1/t1"), ll(new String[] {"Root/S2/A"}));
    }

    @Test
    public void leafStatesEntered6() {
        DashModel d = test("entered6");
        assertEquals(entered(d, "Root/S1/t1"), ll(new String[] {"Root/S2/A/S3"}));
    }

    @Test
    public void leafStatesEntered7() {
        DashModel d = test("entered7");
        assertEquals(entered(d, "Root/S1/t1"), ll(new String[] {"Root/S2/A/S3[AID]"}));
    }

    @Test
    public void leafStatesEntered8() {
        DashModel d = test("entered8");
        assertEquals(
                entered(d, "Root/S1/t1"),
                ll(new String[] {"Root/S2/A/B/S3[AID, BID]", "Root/S2/A/C[AID, CID]"}));
    }

    @Test
    public void leafStatesEntered9() {
        DashModel d = test("entered9");
        assertEquals(
                entered(d, "Root/S1/t1"),
                ll(
                        new String[] {
                            "Root/S2/A/B/S3[AID, BID]", "Root/S2/A/C/S5[AID, CID]",
                        }));
    }

    @Test
    public void leafStatesEntered10() {
        DashModel d = test("entered10");
        System.out.println("here27");
        System.out.println(entered(d, "Root/S1/t1"));
        assertEquals(
                entered(d, "Root/S1/t1"),
                ll(
                        new String[] {
                            "Root/S2/A/B/S3[AID, BID]",
                            "Root/S2/A/C/D/S5[AID, CID, DID]",
                            "Root/S2/A/C/E/S7[AID, CID, EID]"
                        }));
    }

    @Test
    public void leafStatesEntered11() {
        DashModel d = test("entered11");
        assertEquals(entered(d, "Root/A/S1/t1"), ll(new String[] {"Root/B/S2/C/S3[x, CID]"}));
    }

    // leafStatesEnteredInScope -----------------

    public List<String> enteredInScope(DashModel d, String tfqn) {
        return d.entered(tfqn).stream().map(i -> i.toString()).collect(Collectors.toList());
    }

    @Test
    public void leafStatesEnteredInScope1() {
        DashModel d = test("noSrc1");
        assertEquals(enteredInScope(d, "Root/S1/t1"), ll(new String[] {"Root/S2"}));
    }

    @Test
    public void leafStatesEnteredInScope2() {
        DashModel d = test("enteredInScope2");
        assertEquals(
                enteredInScope(d, "Root/S1/t1"),
                ll(
                        new String[] {
                            "Root/B/S3", "Root/A/S2",
                        }));
    }

    @Test
    public void leafStatesEnteredInScope3() {
        DashModel d = test("entered5");
        assertEquals(enteredInScope(d, "Root/S1/t1"), ll(new String[] {"Root/S2/A"}));
    }

    @Test
    public void leafStatesEnteredInScope4() {
        DashModel d = test("entered6");
        assertEquals(enteredInScope(d, "Root/S1/t1"), ll(new String[] {"Root/S2/A/S3"}));
    }

    @Test
    public void leafStatesEnteredInScope5() {
        DashModel d = test("entered7");
        assertEquals(enteredInScope(d, "Root/S1/t1"), ll(new String[] {"Root/S2/A/S3[AID]"}));
    }

    @Test
    public void leafStatesEnteredInScope6() {
        DashModel d = test("entered8");
        assertEquals(
                enteredInScope(d, "Root/S1/t1"),
                ll(new String[] {"Root/S2/A/B/S3[AID, BID]", "Root/S2/A/C[AID, CID]"}));
    }

    @Test
    public void leafStatesEnteredInScope7() {
        DashModel d = test("entered9");
        assertEquals(
                enteredInScope(d, "Root/S1/t1"),
                ll(
                        new String[] {
                            "Root/S2/A/B/S3[AID, BID]", "Root/S2/A/C/S5[AID, CID]",
                        }));
    }

    // connected tests on one model
    @Test
    public void overall1() {
        DashModel d = test("overall1");
        String tfqn = "Root/t1";
        assertEquals(src(d, tfqn), "Root/A/B/S1[a1, b1]");
        assertEquals(dest(d, tfqn), "Root/C/S2[c1]");
        assertEquals(d.scope(tfqn).toString(), "Root");

        assertEquals(exited(d, tfqn), ll(new String[] {"Root/A/B/S1[AID, BID]", "Root/C/S2[CID]"}));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(new String[] {"Root/C/S2[CID - c1]", "Root/A/B/S1[AID, BID]", "Root/C/S2[c1]"}));
        assertEquals(d.transParams(tfqn), Arrays.asList());
    }

    @Test
    public void overall2() {
        DashModel d = test("overall2");
        String tfqn = "Root/A/B/S1/t1";
        assertEquals(src(d, tfqn), "Root/A/B/S1[p0_AID, p1_BID]");
        assertEquals(dest(d, tfqn), "Root/A/S2[a2]");
        assertEquals(d.scope(tfqn).toString(), "Root/A[(p0_AID = a2 => p0_AID else AID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(p0_AID = a2 => p0_AID else AID), BID]",
                            "Root/A/S2[(p0_AID = a2 => p0_AID else AID)]"
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/S2[(p0_AID = a2 => p0_AID else AID) - a2]", "Root/A/S2[a2]",
                        }));
        assertEquals(d.transParams(tfqn), Arrays.asList(0, 1));
    }

    @Test
    public void overall3() {
        DashModel d = test("overall3");
        String tfqn = "Root/A/S2/t1";
        assertEquals(src(d, tfqn), "Root/A/S2[p0_AID]");
        assertEquals(dest(d, tfqn), "Root/A/B/S1[a1, b1]");
        assertEquals(d.scope(tfqn).toString(), "Root/A[(p0_AID = a1 => p0_AID else AID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(p0_AID = a1 => p0_AID else AID), BID]",
                            "Root/A/S2[(p0_AID = a1 => p0_AID else AID)]"
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/S2[(p0_AID = a1 => p0_AID else AID) - a1]",
                            "Root/A/B/S1[a1, BID - b1]",
                            "Root/A/B/S1[a1, b1]"
                        }));

        tfqn = "Root/A/B/S1/t2";
        assertEquals(src(d, tfqn), "Root/A/B/S1[p0_AID, p1_BID]");
        assertEquals(dest(d, tfqn), "Root/A/B/S1[p0_AID, p1_BID]");
        assertEquals(d.scope(tfqn).toString(), "Root/A/B/S1[p0_AID, p1_BID]");
        assertEquals(exited(d, tfqn), ll(new String[] {"Root/A/B/S1[p0_AID, p1_BID]"}));
        assertEquals(enteredInScope(d, tfqn), ll(new String[] {"Root/A/B/S1[p0_AID, p1_BID]"}));

        tfqn = "Root/A/B/S1/t3";
        assertEquals(src(d, tfqn), "Root/A/B/S1[p0_AID, b1]");
        assertEquals(dest(d, tfqn), "Root/A/B/S1[p0_AID, b2]");
        assertEquals(d.scope(tfqn).toString(), "Root/A/B/S1[p0_AID, (b1 = b2 => b1 else BID)]");
        assertEquals(
                exited(d, tfqn),
                ll(new String[] {"Root/A/B/S1[p0_AID, (b1 = b2 => b1 else BID)]"}));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[p0_AID, (b1 = b2 => b1 else BID) - b2]",
                            "Root/A/B/S1[p0_AID, b2]"
                        }));
    }

    @Test
    public void overall4() {
        DashModel d = test("overall4");
        String tfqn = "Root/A/B/S1/t1";
        assertEquals(src(d, tfqn), "Root/A/B/S1[a1, b1]");
        assertEquals(dest(d, tfqn), "Root/A/S2[a2]");
        assertEquals(d.scope(tfqn).toString(), "Root/A[(a1 = a2 => a1 else AID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a1 = a2 => a1 else AID), BID]",
                            "Root/A/S2[(a1 = a2 => a1 else AID)]"
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a1 = a2 => a1 else AID) - a2, BID]", "Root/A/S2[a2]"
                        }));
    }

    @Test
    public void overall5() {
        DashModel d = test("overall5");
        String tfqn = "Root/A/B/S1/t1";
        assertEquals(src(d, tfqn), "Root/A/B/S1[a1, b1]");
        assertEquals(dest(d, tfqn), "Root/A[a2]");
        assertEquals(d.scope(tfqn).toString(), "Root/A[(a1 = a2 => a1 else AID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a1 = a2 => a1 else AID), BID]",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a1 = a2 => a1 else AID) - a2, BID]",
                            "Root/A/B/S1[a2, BID]",
                        }));

        tfqn = "Root/t2";
        assertEquals(src(d, tfqn), "Root/A/B/S1[a3, b3]");
        assertEquals(dest(d, tfqn), "Root/A/B/S1[a4, b4]");
        assertEquals(
                d.scope(tfqn).toString(),
                "Root/A/B/S1[(a3 = a4 => a3 else AID), (AND[a3 = a4, b3 = b4] => b3 else BID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a3 = a4 => a3 else AID), (AND[a3 = a4, b3 = b4] => b3 else BID)]",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a3 = a4 => a3 else AID) - a4, BID]",
                            "Root/A/B/S1[a4, (AND[a3 = a4, b3 = b4] => b3 else BID) - b4]",
                            "Root/A/B/S1[a4, b4]"
                        }));

        tfqn = "Root/t3";
        assertEquals(src(d, tfqn), "Root/A/B/S1[a5, b5]");
        assertEquals(dest(d, tfqn), "Root/A/B[a6, b6]");
        assertEquals(
                d.scope(tfqn).toString(),
                "Root/A/B[(a5 = a6 => a5 else AID), (AND[a5 = a6, b5 = b6] => b5 else BID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a5 = a6 => a5 else AID), (AND[a5 = a6, b5 = b6] => b5 else BID)]",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/B/S1[(a5 = a6 => a5 else AID) - a6, BID]",
                            "Root/A/B/S1[a6, (AND[a5 = a6, b5 = b6] => b5 else BID) - b6]",
                            "Root/A/B/S1[a6, b6]"
                        }));
    }

    @Test
    public void overall6() {
        DashModel d = test("overall6");
        String tfqn = "Root/B/S1/t1";
        assertEquals(src(d, tfqn), "Root/B/S1[p0_BID]");
        assertEquals(dest(d, tfqn), "Root/B[p0_BID]");
        assertEquals(d.scope(tfqn).toString(), "Root/B[p0_BID]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/B/S1[p0_BID]",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/B/S1[p0_BID]",
                        }));

        tfqn = "Root/B/S1/t2";
        assertEquals(src(d, tfqn), "Root/B/S1[p0_BID]");
        assertEquals(dest(d, tfqn), "Root/B[b1]");
        assertEquals(d.scope(tfqn).toString(), "Root/B[(p0_BID = b1 => p0_BID else BID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/B/S1[(p0_BID = b1 => p0_BID else BID)]",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/B/S1[(p0_BID = b1 => p0_BID else BID) - b1]", "Root/B/S1[b1]"
                        }));

        tfqn = "Root/B/S1/t3";
        assertEquals(src(d, tfqn), "Root/B/S1[p0_BID]");
        assertEquals(dest(d, tfqn), "Root/C");
        assertEquals(d.scope(tfqn).toString(), "Root");
        assertEquals(exited(d, tfqn), ll(new String[] {"Root/C", "Root/B/S1[BID]"}));
        assertEquals(enteredInScope(d, tfqn), ll(new String[] {"Root/C"}));

        assertEquals(d.transParams(tfqn), Arrays.asList(0));
    }

    @Test
    public void overall7() {
        DashModel d = test("overall7");
        String tfqn = "Root/S1/t1";
        assertEquals(src(d, tfqn), "Root/S1");
        assertEquals(dest(d, tfqn), "Root/S1");
        assertEquals(d.scope(tfqn).toString(), "Root/S1");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/S1",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/S1",
                        }));

        tfqn = "Root/S1/t2";
        assertEquals(src(d, tfqn), "Root/S1");
        assertEquals(dest(d, tfqn), "Root/B");
        assertEquals(d.scope(tfqn).toString(), "Root");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A", "Root/B", "Root/C[CID]", "Root/S1",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A", "Root/C[CID]", "Root/B",
                        }));

        tfqn = "Root/S1/t3";
        assertEquals(src(d, tfqn), "Root/S1");
        assertEquals(dest(d, tfqn), "Root/C[c1]");
        assertEquals(d.scope(tfqn).toString(), "Root");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A", "Root/B", "Root/C[CID]", "Root/S1",
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(new String[] {"Root/C[CID - c1]", "Root/A", "Root/B", "Root/C[c1]"}));
    }

    @Test
    public void overall8() {
        DashModel d = test("overall8");
        String tfqn = "Root/t1";
        assertEquals(src(d, tfqn), "Root/S3");
        assertEquals(dest(d, tfqn), "Root/B/E/S2[e1]");
        assertEquals(d.scope(tfqn).toString(), "Root");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/S1[AID]",
                            "Root/B/C",
                            "Root/B/D",
                            "Root/B/E/S2[EID]",
                            "Root/B/E/S4[EID]",
                            "Root/S3"
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/A/S1[AID]",
                            "Root/B/E/S4[EID - e1]",
                            "Root/B/C",
                            "Root/B/D",
                            "Root/B/E/S2[e1]"
                        }));
    }

    @Test
    public void overall9() {
        DashModel d = test("overall9");
        String tfqn = "Root/t1";
        assertEquals(src(d, tfqn), "Root/B/E/F[e1, f1]");
        assertEquals(dest(d, tfqn), "Root/B/E/G[e2]");
        assertEquals(d.scope(tfqn).toString(), "Root/B/E[(e1 = e2 => e1 else EID)]");
        assertEquals(
                exited(d, tfqn),
                ll(
                        new String[] {
                            "Root/B/E/S2[(e1 = e2 => e1 else EID)]",
                            "Root/B/E/S4[(e1 = e2 => e1 else EID)]",
                            "Root/B/E/F[(e1 = e2 => e1 else EID), FID]",
                            "Root/B/E/G[(e1 = e2 => e1 else EID)]"
                        }));
        assertEquals(
                enteredInScope(d, tfqn),
                ll(
                        new String[] {
                            "Root/B/E/S4[(e1 = e2 => e1 else EID) - e2]",
                            "Root/B/E/F[e2, FID]",
                            "Root/B/E/G[e2]"
                        }));
    }

    @Test
    public void overall10() {
        DashModel d = test("overall10");
        String tfqn = "Root/S1/t1";
        assertEquals(src(d, tfqn), "Root/S1");
        assertEquals(dest(d, tfqn), "Root/S2");
    }

    @Test
    public void overall11() {
        DashModel d = test("overall11");
        String tfqn = "Root/S1/t1";
        assertEquals(src(d, tfqn), "Root/S1");
        assertEquals(dest(d, tfqn), "Root/S2");
    }

    @Test
    public void overall12() {
        DashModel d = test("overall12");
        String tfqn = "Root/S1/t1";
        assertEquals(src(d, tfqn), "Root/S1");
        assertEquals(dest(d, tfqn), "Root/S1/S7");
    }

    @Test
    public void overall13() {
        DashModel d = test("overall13");
        String tfqn = "Root/A/B/t1";
        assertEquals(d.transParams(tfqn), Arrays.asList(0, 1));
        tfqn = "Root/A/t2";
        assertEquals(d.transParams(tfqn), Arrays.asList(0));
        tfqn = "Root/C/t3";
        assertEquals(d.transParams(tfqn), Arrays.asList(2));
        tfqn = "Root/C/D/t4";
        assertEquals(d.transParams(tfqn), Arrays.asList(2, 3));
        assertEquals(
                d.allParams(),
                Arrays.asList("Root_A_AID", "Root_B_BID", "Root_C_BID", "Root_D_AID"));
    }

    // priority
    @Test
    public void pri1() {
        DashModel d = test("pri1");
        String tfqn = "Root/t1";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {}));
    }

    @Test
    public void pri2() {
        DashModel d = test("pri2");
        String tfqn = "Root/t1";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {}));
        tfqn = "Root/A/t2";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {"Root/t1"}));
    }

    @Test
    public void pri3() {
        DashModel d = test("pri3");
        String tfqn = "Root/t1";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {}));
        tfqn = "Root/t2";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {}));
        tfqn = "Root/t3";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {"Root/t1", "Root/t2"}));
    }

    @Test
    public void pri4() {
        DashModel d = test("pri4");
        String tfqn = "Root/t1";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {}));
        tfqn = "Root/t2";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {"Root/t1"}));
    }

    @Test
    public void pri5() {
        DashModel d = test("pri5");
        String tfqn = "Root/t1";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {}));
        tfqn = "Root/t2";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {"Root/t1"}));
    }

    @Test
    public void pri6() {
        DashModel d = test("pri6");
        String tfqn = "Root/A/B/t1";

        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {"Root/A/t2", "Root/t3"}));

        tfqn = "Root/A/t2";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {"Root/t3"}));

        tfqn = "Root/t3";
        assertEquals(d.higherPriTrans(tfqn), ll(new String[] {}));
    }
}
