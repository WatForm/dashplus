package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.alloymodel.SigScope.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.alloyast.AssumptionError;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMCmds extends SMConstraints {

    // first two are added by paragraphs
    private HashMap<Qname, AlloyExpr> assertTable = new HashMap<>();
    // Linked so they are in order
    private HashMap<Qname, CmdData> cmdTable = new LinkedHashMap<>();

    // rest of these elements are added by other paras
    // scopes that are definitely set by the model,
    // and can override a cmd scope
    // e.g., one sigs, enum values and enum parent sig
    // these are always exact scopes
    // imports can force something to be exact even if we don't know the value
    private HashMap<Qname, SigScope> modelScopes = new HashMap<>();
    // because of symmetry breaking, these are special to give errors
    private List<Qname> orderedSigs = emptyList();

    // init ----------------------

    protected SMCmds() {}

    protected SMCmds(SMCmds other) {
        super(other);
        this.assertTable = new HashMap<>(other.assertTable);
        this.cmdTable = new LinkedHashMap<>(other.cmdTable);
        this.modelScopes = new HashMap<>(other.modelScopes);
        this.orderedSigs = new ArrayList<>(other.orderedSigs);
    }

    protected void createAssert(Qname qname, AlloyExpr body) {
        if (this.assertTable.keySet().contains(qname)) {
            // this is not allowed in AA
            throw AlloyModelError.assertNameMustBeUnique(body.pos, qname.toString());
        } else {
            this.assertTable.put(qname, body);
        }
    }

    protected void createCmd(Qname qname, CmdData cmdData) {
        if (this.cmdTable.keySet().contains(qname)) {
            // this is allowed in AA
            throw AssumptionError.cmdNameMustBeUnique(cmdData.pos, qname.toString());
        } else {
            this.cmdTable.put(qname, cmdData);
        }
    }

    // import [exactly A] in the ordering module
    public void createOrderedSigWithExactScope(Qname qname) {
        // we don't know the value of the exact scope
        this.modelScopes.put(qname, ExactNoValue());
        this.orderedSigs.add(qname);
    }

    // enum parent
    public void createOrderedSigWithExactScopeValue(Qname qname, Integer value) {
        // we know the value of the exact scope (enum)
        this.modelScopes.put(qname, ExactScope(value));
        this.orderedSigs.add(qname);
    }

    // import [exactly A] NOT in the ordering module
    public void createNonOrderedSigWithExactScope(Qname qname) {
        this.modelScopes.put(qname, ExactNoValue());
    }

    // one sig
    public void createNonOrderedSigWithExactScopeValue(Qname qname, Integer value) {
        this.modelScopes.put(qname, ExactScope(value));
    }

    // resolve ------------------------

    protected void resolveSMCmds(
            TriFunction<AlloyExpr, String, List<AlloyDecl>, ResolveInfo> resolve2) {
        // resolve the asserts
        AlloyExpr expr;
        ResolveInfo r;
        for (Qname qname : this.assertTable.keySet()) {
            expr = this.assertTable.get(qname);
            r = resolve2.apply(expr, qname.nameSpace, emptyList());
            if (r.arity.isPresent()) this.assertTable.put(qname, r.exp);
            else {
                throw AlloyModelError.unknownArity(expr.pos, expr.toString());
            }
        }

        for (Qname qname : this.cmdTable.keySet()) {
            // changes info for each cmd
            CmdData cmdData = this.cmdTable.get(qname);
            if (!cmdData.isResolved) {

                // resolve sig names in scopes
                HashMap<Qname, SigScope> newCmdScopes = new HashMap();
                for (Qname sigQname : cmdData.cmdScopes.keySet()) {
                    List<Qname> matches = this.sigQnameMatches(sigQname);
                    if (matches.size() == 1) {
                        // rebuild the map with the resolved names
                        newCmdScopes.put(matches.get(0), cmdData.cmdScopes.get(sigQname));
                    } else {
                        throw AlloyModelError.nameCouldBeMultipleSigs(cmdData.pos, sigQname.name);
                    }
                }
                cmdData.cmdScopes = newCmdScopes;

                // resolve either 1) assert or pred name or 2) expression in block
                if (cmdData.assertOrPredFunQname.isPresent()) {
                    if (cmdData.cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN) {
                        // must be a pred or run
                        // either matches exactly (which would mean only one match)
                        // or could match on multiple of UNKNOWN_NAMESPACE
                        Qname predFunQname = cmdData.assertOrPredFunQname.get();
                        List<Qname> possibleMatches = predFunQnameMatches(predFunQname);
                        if (possibleMatches.size() == 1) {
                            cmdData.predFunQname = Optional.of(possibleMatches.get(0));
                        } else {
                            throw AlloyModelError.cannotResolvePredFunName(
                                    cmdData.pos, cmdData.assertOrPredFunQname.toString());
                        }
                    } else {
                        // its a check -- name can only be an assert
                        // either matches exactly (which would mean only one match)
                        // or could match on multiple of UNKNOWN_NAMESPACE
                        List<Qname> possibleMatches =
                                possibleMatches(
                                        new ArrayList<>(assertTable.keySet()),
                                        cmdData.assertQname.get());
                        if (possibleMatches.size() == 1) {
                            cmdData.assertQname = Optional.of(possibleMatches.get(0));
                        } else {
                            throw AlloyModelError.cannotResolveAssertName(
                                    cmdData.pos, cmdData.assertOrPredFunQname.toString());
                        }
                    }
                } else {
                    expr = cmdData.block.get();
                    r = resolve2.apply(expr, qname.nameSpace, emptyList());
                    if (r.arity.isPresent()) cmdData.block = Optional.of(((AlloyBlock) r.exp));
                    else throw AlloyModelError.unknownArity(expr.pos, expr.toString());
                }
                cmdData.isResolved = true;
            }
        }

        // TODO need to resolve the names in orderedSigs, etc.
        // Need to resolve scope names in commands (that will check that they all exist)
    }

    // API ------------------------

    public Integer getNumCmds() {
        return this.cmdTable.keySet().size();
    }

    private void existsCmd(int n) {
        // only used by impl
        assert (n < 0 || n >= this.cmdTable.keySet().size());
        /*
        if (n < 0 || n >= this.cmdDataList.size())
            throw AlloyModelImplError.noCmdAtThatPosition(Integer.toString(n));
        */
    }

    // indexed from 0
    private CmdData getCmdNum(int n) {
        existsCmd(n);
        return new ArrayList<>(this.cmdTable.values()).get(n);
    }

    // accessed by cmd number
    public Boolean isRunCmd(int n) {
        existsCmd(n);
        return getCmdNum(n).cmdType == AlloyCmdPara.CommandDecl.CmdType.RUN;
    }

    public Boolean isCheckCmd(int n) {
        existsCmd(n);
        return getCmdNum(n).cmdType == AlloyCmdPara.CommandDecl.CmdType.CHECK;
    }

    public AlloyExpr getCmdFormula(int n) {
        CmdData cmdData = getCmdNum(n);
        return getCmdFormula(cmdData);
    }

    private AlloyExpr getCmdFormula(CmdData cmdData) {

        // may return an empty block e.g., run {}
        assert (cmdData.isResolved);
        if (cmdData.assertQname.isPresent()) {
            // must be a check
            return this.assertTable.get(cmdData.assertQname.get());
        } else if (cmdData.predFunQname.isPresent()) {
            Qname predFunQname = cmdData.predFunQname.get();
            List<AlloyDecl> argDecls = this.predFunArgDecls(predFunQname);
            // p[a,b] or f[a,b]
            AlloyExpr predFunCall =
                    new AlloyBracketExpr(
                            predFunQname.toAlloyExpr(Kind.PREDFUN),
                            mapBy(argDecls, d -> d.qnames.get(0)));

            // must be a run
            if (this.isPred(cmdData.predFunQname.get())) {
                // run p, where p is a predicate that takes arg a: A becomes:
                // run { some a:A | p[a] }
                if (argDecls.isEmpty()) return predFunCall;
                else return AlloySomeVars(argDecls, predFunCall);
            } else {
                // run f,  where f is a function that takes arg a: A becomes:
                // run { some a:A | some f[a] }
                if (argDecls.isEmpty()) return AlloySome(predFunCall);
                else return AlloySomeVars(argDecls, AlloySome(predFunCall));
            }
        } else {
            // can be a check or run
            return cmdData.block.get();
        }
    }

    /*
    public List<AlloyExpr> getCmdConstraints(int n) {
        List<AlloyExpr> constraints = this.allConstraints();
        if (this.isRunCmd(n)) {
            constraints.add(this.cmdDataList.get(n).expr);
        } else {
            constraints.add(AlloyNot(this.cmdDataList.get(n).expr));
        }
        return constraints;
    }
    */

    // scope computations for a command -------------------

    private int DEFAULT_SCOPE = 3;
    private int INT_DEFAULT_SCOPE = 4;

    // recursively calculated
    private CmdScopeProfile scopeProfile;
    private Integer default_scope;

    /*
        Here we assume no errors in the scopes given in the cmd

        Rules about scopes
        - every one sig has size 1 (info already in modelScopes)
        - "extends" children (even abstract ones) can have an explicit scope
        - scope of parents (even if prescribed in the cmd) are overridden to accommodate exact scopes for children
        - default scope for top-level sigs not forced to have explicit scopes is 3
        - default scope for Int is always 4 even if a different scope is chosen as a default for everything else
        - a sig may have been set to an exact scope from an import paragraph

       Notes (constraints not enforced by sigs)
       - sig A in B means A subseteq B
       - sig A1, A2 extends B means A1 subseteq B, A2 subseteq B, A1 inter A2 = empty
       - abstract sig A means A = all its extends children
       - abstract can be used without any children
       - every top-level sig must have a scope
    */

    /*
    public CmdScopeProfile getCmdScopeLimits(Integer cmdNum) {

        this.checkForErrorsInScopes(cmdNum);
        // general error
        for (Qname s : this.orderedSigs) {
            if (!this.topLevelSigs().contains(s)) {
                throw AlloyModelError.orderedOnlyOnTopLevelSigs(s.fullName());
            }
        }

        CmdData cd = getCmdNum(cmdNum);
        this.default_scope = cd.defaultScope.map(n -> n).orElse(DEFAULT_SCOPE);
        this.scopeProfile = new CmdScopeProfile();

        // modifies the scopeProfile
        // Int might or might not be in the topLevelSigs
        for (Qname s : this.topLevelSigs()) {
            Optional<Integer> sigBound = recurseExtendsProfile(s, true);
        }
        // make sure Int has a value
        if (cd.cmdScopes.keySet().contains(AlloyStrings.SIGINT))
            // givenScopes might not say INT is exactly, but it is always exactly in Alloy
            this.scopeProfile.addTopLevel(
                    AlloyStrings.SIGINT, ExactScope(cd.cmdScopes.get(AlloyStrings.SIGINT).max()));
        else this.scopeProfile.addTopLevel(AlloyStrings.SIGINT, ExactScope(this.INT_DEFAULT_SCOPE));

        // Alloy instances also always have:
        // seq/Int={0, 1, 2, 3}
        // String={}
        // none={}
        // TODO: may be something to fix here

        return this.scopeProfile;
    }

    private Integer recurseExtendsProfile(Qname sigName, Boolean isTopLevel) {

        // recurse from a top-level sig down to leaves and back up

        // look at what scopes from children say
        // sum their exact scopes (might be none)
        Integer minFromChildren = 0;
        for (String child : this.extendsChildren(sigName)) {
            minFromChildren = minFromChildren + recurseExtendsProfile(child, false);
        }

        // we SET a scope in the scope profile (rather than just returning a value) if
        // 1) it is top-level
        // or
        // 2) it is in model scopes (exactly with or without value)
        if (!cd.cmdScopes.keySet().contains(sigName)
                && !this.modelScopes.keySet().contains(sigName)
                && !isTopLevel) {
            // no prescribed scope and not modelScope sig and not top level
            // so just pass the exact bound up
            // but if an import forced it to be exact add that
            if (this.sigsWithExactScope().contains(sigName)) {
                this.scopeProfile.addTopLevel(sigName, ExactScope(exactBoundOpt.get()));
            }
            return exactBoundOpt; // might be empty or might be exact scope
        }

        SigScope givenScope;
        Boolean overrideFlag = false;
        if (!cd.cmdScopes.keySet().contains(sigName) && isTopLevel && !this.isOneSig(sigName)) {
            // top-level so must be given a scope
            // chosen from exactBoundOpt and this.defaultScope
            if (this.sigsWithExactScope().contains(sigName)) {
                givenScope = ExactScope(this.default_scope);
            } else {
                givenScope = NonExactScope(this.default_scope);
            }
        } else if (!cd.cmdScopes.keySet().contains(sigName) && this.isOneSig(sigName)) {
            // it is an error earlier if 'one sig's are given a scope other than 1
            givenScope = ExactScope(1);
        } else {
            // given a scope; might or might not be top-level
            // only time when override of scope in cmd could be true
            overrideFlag = true;
            // already been set to exact from an import above
            givenScope = cd.cmdScopes.get(sigName);
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
                if (overrideFlag) System.out.println("Overriding cmd scope for " + sigName);
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
    */

    protected void checkForErrorsInScopes(Integer cmdNum) {
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
        /*
        CmdData cd = getCmdNum(cmdNum);

        // is there a default included explicitly in the cmd?
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
            // AA accepts "int" as sig name for scopes *
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
            */
    }

    public void debugSMCmds() {
        StringBuilder sb = new StringBuilder("SMCmds:\n");

        sb.append("  assertTable:\n");
        assertTable.forEach(
                (k, v) -> sb.append("    ").append(k).append(" -> ").append(v).append('\n'));

        sb.append("  cmdTable:\n");
        for (var entry : cmdTable.entrySet()) {
            sb.append("    ").append(entry.getKey()).append(" -> ");
            CmdData cd = entry.getValue();

            if (cd.assertQname.isPresent()) {
                // resolved
                sb.append(
                        String.format(
                                "%s %s (scopes=%s%s, resolved=%s%s)",
                                cd.cmdType,
                                "assert=" + cd.assertQname.get(),
                                cd.cmdScopes,
                                cd.defaultScope.map(s -> ", def=" + s).orElse(""),
                                cd.isResolved,
                                cd.expect.map(e -> ", expect=" + e).orElse("")));
            } else if (cd.predFunQname.isPresent()) {
                // resolved
                sb.append(
                        String.format(
                                "%s %s (scopes=%s%s, resolved=%s%s)",
                                cd.cmdType,
                                "formula=" + this.getCmdFormula(cd).toString(),
                                cd.cmdScopes,
                                cd.defaultScope.map(s -> ", def=" + s).orElse(""),
                                cd.isResolved,
                                cd.expect.map(e -> ", expect=" + e).orElse("")));
            } else {
                // either not resolved
                // or a block to begin with
                sb.append(
                        String.format(
                                "%s\n %s \n   (scopes=%s%s, resolved=%s%s)",
                                cd.cmdType,
                                "contents="
                                        + (cd.block.isPresent()
                                                ? cd.block.get().toString()
                                                : cd.assertOrPredFunQname.get()),
                                cd.cmdScopes,
                                cd.defaultScope.map(s -> ", def=" + s).orElse(""),
                                cd.isResolved,
                                cd.expect.map(e -> ", expect=" + e).orElse("")));
            }
            sb.append('\n');
        }
        sb.append("  modelScopes:\n");
        modelScopes.forEach(
                (k, v) -> sb.append("    ").append(k).append(" -> ").append(v).append('\n'));

        sb.append("  orderedSigs:\n");
        orderedSigs.forEach(sig -> sb.append("    ").append(sig).append('\n'));

        System.out.println(sb.toString() + "\n");
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

        public Optional<SigScope> getExplicitExtendsScope(String sigName) {
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
