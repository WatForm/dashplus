/*
    The purpose of this class is only to calculate information about the
    correctness of scopes in a command
    and
    build a ScopeProfile for a cmd to tell someone
    what are scopes that need to be enforced for this command.

    Only local state here is for local computations and it is private
    and not useful to anyone outside of AlloyModel
*/

package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AlloyFile;
import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import java.util.*;

public class AMScopes extends AMAsserts {

    int DEFAULT_SCOPE = 3;
    int INT_DEFAULT_SCOPE = 4;

    // to avoid passing it to all arguments
    private HashMap<String, SigScope> givenScopes;
    // recursively calculated
    private CmdScopeProfile scopeProfile;
    private Integer default_scope;

    protected AMScopes(AMScopes other) {
        super(other);
    }

    protected AMScopes(AlloyFile alloyFile) {
        super(alloyFile);
    }

    protected void resolve() {
        super.resolve();
    }

    protected void checkForErrorsInScopes(AlloyCmdPara.CommandDecl cmdDecl) {
        /* errors to check for
        1) every sig given an explicit scope size is a sigName in the model
        2) every one sig has size 1 (if one sig is given a default scope != 1, error; but it does not have to be an `exactly` 1)
        3) "in" children can't have an explicit scope (error)
        4) if an "extends" (including a one sig) child is given an explicit scope, its top-level sig must be given an explicit scope in the command (even if this is by designating a default scope for everything else) (otherwise error)
            sig A {}
            sig A1 extends A {}

            run {} for 3 but exactly 2 A1
          is okay, but
            sig A {}
            sig A1 extends A {}

            run {} for 2 A1
          is not allowed
        */

        // is there a default including explicitly in the cmd?
        Optional<Integer> givenDefault;
        if (cmdDecl.scope.isPresent() && cmdDecl.scope.get().num.isPresent())
            givenDefault = Optional.of(cmdDecl.scope.get().num.get().value);
        else givenDefault = Optional.empty();

        // these are the sigs given explicit scopes in the cmd
        List<AlloyCmdPara.CommandDecl.Scope.Typescope> typeScopes =
                cmdDecl.scope.map(s -> s.typescopes).orElse(new ArrayList<>());
        HashMap<String, SigScope> givenScopes = new HashMap<String, SigScope>();
        // put the scopes given in the cmd into a HashMap
        for (AlloyCmdPara.CommandDecl.Scope.Typescope typeScope : typeScopes) {
            // Int will not be declared and in set of all sigs
            String sigName = typeScope.scopableExpr.toString();
            /* AA accepts "int" as sig name for scopes */
            if (sigName.equals(AlloyStrings.INT)) sigName = AlloyStrings.SIGINT;
            if (!sigName.equals(AlloyStrings.SIGINT))
                givenScopes.put(
                        sigName,
                        typeScope.isExactly
                                ? ExactScope(typeScope.start.value)
                                : NonExactScope(typeScope.start.value));
        }
        for (String sigName : givenScopes.keySet()) {
            SigScope scope = givenScopes.get(sigName);
            // 1)
            if (this.containsSig(sigName)) {
                // 2)
                if (this.isOneSig(sigName) && (scope.max != 1)) {
                    throw AlloyModelError.nonOneScopeForOneSig(cmdDecl.pos, cmdDecl.toString());
                }
                // 3)
                if (this.isInChild(sigName)) {
                    throw AlloyModelError.cantSetScopeOfInChild(cmdDecl.pos, cmdDecl.toString());
                }
                // 4)
                if (this.isExtendsChild(sigName) && !givenDefault.isPresent()) {
                    // we have to look in rest of givenScopes to see if top-level
                    // parent of sigName has an explicit scope given in the command
                    if (!givenScopes.keySet().contains(this.topLevelExtendsAncestor(sigName))) {
                        throw AlloyModelError.scopeOfTopLevelSigMustBeGiven(
                                cmdDecl.pos, cmdDecl.toString());
                    }
                }
            } else {
                throw AlloyModelImplError.noScopeForNonSig(sigName);
            }
        }
    }

    /*
        Here we assume no errors in the scopes given in the cmd

        Rules about scopes
        - every one sig has size 1
        - "extends" children (even abstract ones) can have an explicit scope
        - scope of parents (even if prescribed in the cmd) are overridden to accommodate exact scopes for children
        - default scope for top-level sigs not forced to have explicit scopes is 3
        - default scope for Int is always 4 even if a different scope is chosen as a default for everything else

       Notes (constraints not enforced by sigs)
       - sig A in B means A subseteq B
       - sig A1, A2 extends B means A1 subseteq B, A2 subseteq B, A1 inter A2 = empty
       - abstract sig A means A = all its extends children
       - abstract can be used without any children
       - every top-level sig must have a scope
    */

    public CmdScopeProfile getScopeLimits(AlloyCmdPara.CommandDecl cmdDecl) {

        this.default_scope =
                cmdDecl.scope
                        .map(s -> s.num.map(n -> n.value).orElse(DEFAULT_SCOPE))
                        .orElse(DEFAULT_SCOPE);

        var typeScopes = cmdDecl.scope.map(s -> s.typescopes).orElse(new ArrayList<>());
        this.givenScopes = new HashMap<String, SigScope>();

        // put the scopes given in the cmd into a HashMap
        for (var typeScope : typeScopes) {
            String sigName = typeScope.scopableExpr.toString();
            /* AA accepts "int" as sig name for scopes */
            if (sigName.equals(AlloyStrings.INT)) sigName = AlloyStrings.SIGINT;
            givenScopes.put(
                    sigName,
                    typeScope.isExactly
                            ? ExactScope(typeScope.start.value)
                            : NonExactScope(typeScope.start.value));
        }
        this.scopeProfile = new CmdScopeProfile();
        // modifies the scopeProfile
        // Int might or might not be in the topLevelSigs
        for (String s : this.topLevelSigs()) {
            Optional<Integer> sigBound = recurseExtendsProfile(s, true);
        }
        // make sure Int has a value
        if (this.givenScopes.keySet().contains(AlloyStrings.SIGINT))
            // givenScopes might not say INT is exactly, but it is always exactly in Alloy
            this.scopeProfile.addTopLevel(
                    AlloyStrings.SIGINT,
                    ExactScope(this.givenScopes.get(AlloyStrings.SIGINT).max()));
        else this.scopeProfile.addTopLevel(AlloyStrings.SIGINT, ExactScope(this.INT_DEFAULT_SCOPE));

        // Alloy instances also always have:
        // seq/Int={0, 1, 2, 3}
        // String={}
        // none={}
        // TODO: may be something to fix here

        return this.scopeProfile;
    }

    private Optional<Integer> recurseExtendsProfile(String sigName, Boolean isTopLevel) {

        // recurse from a top-level sig down to leaves and back up

        // System.out.println("calc: " + sigName);
        Optional<Integer> exactBoundOpt = Optional.empty();

        if (this.isOneSig(sigName)) {
            exactBoundOpt = Optional.of(1);
            // has no children
        } else {
            for (String child : this.extendsChildren(sigName)) {
                Optional<Integer> exactBoundChildOpt = recurseExtendsProfile(child, false);

                if (exactBoundOpt.isPresent() && exactBoundChildOpt.isPresent()) {
                    exactBoundOpt = Optional.of(exactBoundOpt.get() + exactBoundChildOpt.get());
                } else if (!exactBoundOpt.isPresent() && exactBoundChildOpt.isPresent()) {
                    exactBoundOpt = exactBoundChildOpt;
                } // else no change to exactBoundOpt (might have value or not)
            }
        }
        // exactBoundOpt could be nothing, exact or non-exact, 0 or not (exact 0 is possible)

        // System.out.println(exactBoundOpt);
        if (!this.givenScopes.keySet().contains(sigName)
                && !isTopLevel
                && !this.isOneSig(sigName)) {
            // no prescribed scope and not one sig and not top level
            // so just pass the bound up
            return exactBoundOpt; // might be empty or might be exact scope
        }

        SigScope givenScope;
        if (!this.givenScopes.keySet().contains(sigName) && isTopLevel) {
            // top-level so must be given a scope
            // chosen from exactBoundOpt and this.defaultScope
            givenScope = NonExactScope(this.default_scope);
        } else if (!this.givenScopes.keySet().contains(sigName) && this.isOneSig(sigName)) {
            givenScope = ExactScope(1);
        } else {
            // given a scope; might or might not be top-level
            givenScope = this.givenScopes.get(sigName);
        }
        // System.out.println(givenScope);
        if (!exactBoundOpt.isPresent()) {
            // givenScope could be exact or not
            if (isTopLevel) this.scopeProfile.addTopLevel(sigName, givenScope);
            else this.scopeProfile.addExplicitExtends(sigName, givenScope);
            if (givenScope.isExact()) {
                return Optional.of(givenScope.max());
            } else {
                return Optional.empty();
            }
        } else {
            // we have both a givenScope and an exactScope from children
            if (exactBoundOpt.get() >= givenScope.max()) {
                // does not matter if givenScope is exact or not
                // sum of children  is higher and takes precedence
                if (isTopLevel)
                    this.scopeProfile.addTopLevel(sigName, ExactScope(exactBoundOpt.get()));
                else this.scopeProfile.addExplicitExtends(sigName, ExactScope(exactBoundOpt.get()));
                return exactBoundOpt;
            } else {
                // we have an exact bound from children that
                // is less than givenScope
                // givenScope could be exact or not-exact
                if (isTopLevel) this.scopeProfile.addTopLevel(sigName, givenScope);
                else this.scopeProfile.addExplicitExtends(sigName, givenScope);
                // lower bound is exact scope from either child or givenScope
                if (givenScope.isExact()) {
                    return Optional.of(givenScope.max());
                } else {
                    return Optional.of(exactBoundOpt.get());
                }
            }
        }
    }

    public static class SigScope {
        Integer max;
        boolean isExact;

        private SigScope(int max, boolean isExact) {
            this.max = max;
            this.isExact = isExact;
        }

        public boolean isExact() {
            return this.isExact;
        }

        public Integer max() {
            return this.max;
        }

        @Override
        public String toString() {
            return (isExact ? "exact " : "") + max;
        }

        @Override
        public boolean equals(Object obj) {
            // written by ChatGPT
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SigScope)) {
                return false;
            }

            SigScope other = (SigScope) obj;
            return isExact == other.isExact && Objects.equals(max, other.max);
        }

        @Override
        public int hashCode() {
            // written by ChatGPT
            return Objects.hash(max, isExact);
        }
    }

    public static SigScope ExactScope(int max) {
        return new SigScope(max, true);
    }

    public static SigScope NonExactScope(int max) {
        return new SigScope(max, false);
    }

    public static class CmdScopeProfile {
        // this object contains everything a translator
        // needs to know about limitations of scopes of a cmd
        // the process above should result in a scope for every top Level Sig
        // ending up in the ScopeProfile
        // plus any required limitations on sigs lower in the sig hierarchy

        // usually sigName is a sigRef, but for generality we can
        // leave it as a string

        // for now only separately topLevel ones from others
        // one sigs could be a separate category, but they could be
        // a topLevelSig or an explicitExtends
        // so until we have a use case for handling one sigs separately
        // here, leave it as is

        private final HashMap<String, SigScope> topLevel;
        private final HashMap<String, SigScope> explicitExtends;

        public CmdScopeProfile() {
            this.topLevel = new HashMap<>();
            this.explicitExtends = new HashMap<>();
        }

        public Optional<SigScope> getTopLevelScope(String sigName) {
            return Optional.ofNullable(this.topLevel.get(sigName));
        }

        public Optional<SigScope> getexplicitExtendsScope(String sigName) {
            return Optional.ofNullable(this.explicitExtends.get(sigName));
        }

        public void addTopLevel(String sigName, SigScope ss) {
            this.topLevel.put(sigName, ss);
        }

        public void addExplicitExtends(String sigName, SigScope ss) {
            this.explicitExtends.put(sigName, ss);
        }

        // written by chatgpt
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("CmdScopeProfile {\n");

            sb.append("  topLevel:\n");
            if (topLevel.isEmpty()) {
                sb.append("    <none>\n");
            } else {
                for (Map.Entry<String, SigScope> e : topLevel.entrySet()) {
                    sb.append("    ")
                            .append(e.getKey())
                            .append(" -> ")
                            .append(e.getValue())
                            .append('\n');
                }
            }

            sb.append("  explicitExtends:\n");
            if (explicitExtends.isEmpty()) {
                sb.append("    <none>\n");
            } else {
                for (Map.Entry<String, SigScope> e : explicitExtends.entrySet()) {
                    sb.append("    ")
                            .append(e.getKey())
                            .append(" -> ")
                            .append(e.getValue())
                            .append('\n');
                }
            }

            sb.append("}");

            return sb.toString();
        }

        @Override
        public boolean equals(Object obj) {
            // written by ChatGPT
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CmdScopeProfile)) {
                return false;
            }

            CmdScopeProfile other = (CmdScopeProfile) obj;
            return Objects.equals(topLevel, other.topLevel)
                    && Objects.equals(explicitExtends, other.explicitExtends);
        }

        @Override
        public int hashCode() {
            // written by ChatGPT
            return Objects.hash(topLevel, explicitExtends);
        }
    }
}
