package ca.uwaterloo.watform.alloymodel;

import static ca.uwaterloo.watform.alloyast.AlloyStrings.*;
import static ca.uwaterloo.watform.alloyast.expr.AlloyExprFactory.*;
import static ca.uwaterloo.watform.alloymodel.AlloyModelError.*;
import static ca.uwaterloo.watform.alloymodel.Qname.*;
import static ca.uwaterloo.watform.alloymodel.SigScope.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;
import static ca.uwaterloo.watform.utils.Reporter.*;

import ca.uwaterloo.watform.alloyast.AlloyStrings;
import ca.uwaterloo.watform.alloyast.AssumptionError;
import ca.uwaterloo.watform.alloyast.expr.AlloyExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBlock;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyBracketExpr;
import ca.uwaterloo.watform.alloyast.expr.misc.AlloyDecl;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.utils.*;
import java.util.*;

public class SMCmds extends SMConstraints {

  // public API ------------------------

  public Integer getNumCmds() {
    return this.cmdTable.keySet().size();
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

  public AlloyExpr getCmdFormula(int cmdNum) {
    CmdData cd = getCmdNum(cmdNum);
    return getCmdFormula(cd);
  }

  public HashMap<Qname, SigScope> getGivenScopes(Integer cmdNum) {
    CmdData cmdData = getCmdNum(cmdNum);
    return cmdData.cmdScopes;
  }

  public Optional<Integer> getDefaultScope(Integer cmdNum) {
    CmdData cmdData = getCmdNum(cmdNum);
    return cmdData.defaultScope;
  }

  public Optional<Integer> getExpect(Integer cmdNum) {
    CmdData cmdData = getCmdNum(cmdNum);
    return cmdData.expect;
  }

  public CmdScopeProfile getCmdScopeProfile(Integer cmdNum) {
    CmdData cd = getCmdNum(cmdNum);
    return this.getCmdScopeProfile(cd);
  }

  // internal data --------------------

  private HashMap<Qname, AlloyExpr> assertTable = new HashMap<>();
  // Linked so they are in order
  private HashMap<Qname, CmdData> cmdTable = new LinkedHashMap<>();
  // scopes that are definitely set by the model,
  // and can override a cmd scope
  // e.g., one sigs, enum values and enum parent sig
  // these are always exact scopes
  // imports can force something to be exact even if we don't know the value
  // THESE ARE ALWAYS EXACT
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
    if (this.createSM) {
      if (this.assertTable.keySet().contains(qname)) {
        // this is not allowed in AA
        throw AlloyModelError.assertNameMustBeUnique(body.pos, qname.toString());
      } else {
        this.assertTable.put(qname, body);
      }
    }
  }

  protected Integer createCmd(Qname qname, CmdData cmdData) {
    if (this.createSM) {
      if (this.cmdTable.keySet().contains(qname)) {
        // this is allowed in AA
        throw AssumptionError.cmdNameMustBeUnique(cmdData.pos, qname.toString());
      } else {
        this.cmdTable.put(qname, cmdData);
      }
      // return the command number
      return this.cmdTable.keySet().size();
    } else {
      return 0;
    }
  }

  // import [exactly A] in the ordering module
  protected void createOrderedSigWithExactScope(Qname qname) {
    if (this.createSM) {
      // we don't know the value of the exact scope
      this.modelScopes.put(qname, ExactNoValue());
      this.orderedSigs.add(qname);
    }
  }

  // enum parent
  protected void createOrderedSigWithExactScopeValue(Qname qname, Integer value) {
    if (this.createSM) {
      // we know the value of the exact scope (enum)
      this.modelScopes.put(qname, ExactScope(value));
      this.orderedSigs.add(qname);
    }
  }

  // import [exactly A] NOT in the ordering module
  protected void createNonOrderedSigWithExactScope(Qname qname) {
    if (this.createSM) this.modelScopes.put(qname, ExactNoValue());
  }

  // one sig
  protected void createNonOrderedSigWithExactScopeValue(Qname qname, Integer value) {
    if (this.createSM) this.modelScopes.put(qname, ExactScope(value));
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
          if (sigQname.name.equals(AlloyStrings.SIGINT)) {
            newCmdScopes.put(sigQname, cmdData.cmdScopes.get(sigQname));
          } else {
            List<Qname> matches = this.sigQnameMatches(sigQname);
            if (matches.size() == 0) {
              throw AlloyModelError.unknownName(cmdData.pos, sigQname.name);
            } else if (matches.size() == 1) {
              // rebuild the map with the resolved names
              newCmdScopes.put(matches.get(0), cmdData.cmdScopes.get(sigQname));
            } else {
              throw AlloyModelError.nameCouldBeMultipleSigs(cmdData.pos, sigQname.name);
            }
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
                possibleMatches(new ArrayList<>(assertTable.keySet()), cmdData.assertQname.get());
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

  }

  // calculate formula of command

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
              predFunQname.toAlloyExpr(Kind.PREDFUN), mapBy(argDecls, d -> d.qnames.get(0)));

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

  // scope computations for a command -------------------

  private int DEFAULT_SCOPE = 3;
  private int INT_DEFAULT_SCOPE = 4;

  // recursively calculated
  private CmdScopeProfile scopeProfile;
  private Integer default_scope;
  private HashMap<Qname, SigScope> givenScopes;
  private CmdData cd;
  private Pos pos;

  /*

      E = exact; NE = non-exact

      Inputs:
      - modelScopes (one sigs (E1), enums (Em), forced exactly (E?) from import
      - givenScopes (Eg or NEG first check is for errors in these)
      - recursive calculation from extends children of min scope (c)
      - closest parent given scope (p) (which might be overridden), but givenScope of parent is used if child is forced to be something by E?)

      Rules about scopes
      - default scope for Int is always E4 even if a different scope is chosen as a default for everything else
      - default scope for top-level sigs not forced to have explicit scopes is 3
      - if child is E? and there is no given scope or calculated scope from children to limit it, it takes the closest ancestor's prescribed scope
      - scope of parents (even if prescribed in the cmd) are overridden to accommodate exact scopes for children

      - some calculations in AA are ignored, e.g.,
        - if A is abstract, unscoped and all children are scopes, A's scope is sum of children; for us this is only true if A is top-level; if A is not top-level, nothing about A is included in the CmdScopeProfile.
        - if A is abstract, given a scope and all but one child is scopes then A's child scope is the difference - for us, we don't include A's child in the CmdScopeProfile

     Notes (constraints assumed by CmdScopeProfile that are not given explicit scopes)
     - sig A in B means A subseteq B
     - sig A1, A2 extends B means A1 subseteq B, A2 subseteq B, A1 inter A2 = empty
     - abstract sig A means A = all its extends children
     - abstract can be used without any children

  */

  private CmdScopeProfile getCmdScopeProfile(CmdData cd) {

    // should we force orderSigs to be top-level??
    // for (Qname s : this.orderedSigs) {
    //  if (!this.topLevelSigs().contains(s)) {
    //    throw AlloyModelError.orderedOnlyOnTopLevelSigs(s.fullName());
    //  }
    // }

    this.cd = cd;
    this.default_scope = cd.defaultScope.map(n -> n).orElse(DEFAULT_SCOPE);
    this.givenScopes = cd.cmdScopes;

    this.scopeProfile = new CmdScopeProfile();
    this.pos = this.cd.pos;

    checkForErrorsInGivenScopes();

    Integer parentSigScope;
    // modifies the scopeProfile
    // Int should not be in top level sigs
    for (Qname s : this.topLevelSigs()) {
      if (givenScope(s).equals(NoScope()))
        // used for children of s
        parentSigScope = recurseExtendsProfile(s, this.default_scope);
      else parentSigScope = recurseExtendsProfile(s, this.default_scope);
    }

    // set Int's scope
    if (givenScopes.keySet().contains(unknownQname(AlloyStrings.SIGINT)))
      // givenScopes might not say INT is exactly, but it is always exactly in Alloy
      this.scopeProfile.setIntScope(givenScopes.get(unknownQname(AlloyStrings.SIGINT)).getValue());
    else this.scopeProfile.setIntScope(this.INT_DEFAULT_SCOPE);

    // Alloy instances also always have:
    // seq/Int={0, 1, 2, 3}
    // String={}
    // none={}
    // TODO: may be something to fix here

    // check ordered sigs have scope greater than zero
    for (Qname s : this.orderedSigs) {
      if (this.scopeProfile.getValue(s) <= 0)
        throw AlloyModelError.exactScopeForOrderedMustBeGreaterThanZero(s.fullName());
    }

    return this.scopeProfile;
  }

  private Integer recurseExtendsProfile(Qname sigName, Integer closestParentScope) {

    // recurse from a top-level sig down to leaves and back up

    // look at what scopes from children say
    // sum their exact scopes (might be none)

    // c = calculated scope
    // 0 if no min limits
    Integer c = 0;
    Integer d;
    for (Qname child : this.extendsChildren(sigName)) {
      if (!givenScope(sigName).equals(NoScope()))
        d = recurseExtendsProfile(child, givenScope(sigName).getValue());
      else d = recurseExtendsProfile(child, closestParentScope);
      c = c + d;
    }

    SigScope g = givenScope(sigName);
    SigScope m = modelScope(sigName);
    // top level decision (g has Scope or Not, m has scope or Not)
    if (g.equals(NoScope()) && m.equals(NoScope())) {
      return this.NoGivenScopeNoModelScope(sigName, g, m, c, closestParentScope);
    } else if (g.equals(NoScope()) && !m.equals(NoScope())) {
      return this.NoGivenScopeModelScope(sigName, g, m, c, closestParentScope);
    } else if (!g.equals(NoScope()) && m.equals(NoScope())) {
      return this.GivenScopeNoModelScope(sigName, g, m, c, closestParentScope);
    } else {
      return this.GivenScopeModelScope(sigName, g, m, c, closestParentScope);
    }
  }

  private SigScope modelScope(Qname sigName) {
    if (this.modelScopes.keySet().contains(sigName)) {
      return this.modelScopes.get(sigName);
    } else return NoScope();
  }

  private SigScope givenScope(Qname sigName) {
    if (this.givenScopes.keySet().contains(sigName)) {
      return this.givenScopes.get(sigName);
    } else return NoScope();
  }

  // separate into these four cases to make it easier to understand

  private Integer NoGivenScopeNoModelScope(
      Qname sigName, SigScope g, SigScope m, Integer c, Integer closestParentScope) {
    if (this.isTopLevelSig(sigName)) {
      if (this.isAbstractSig(sigName) && c != 0) {
        // no g, no m, top-level, abstract, c != 0
        addToCmdProfile(sigName, ExactScope(c));
        return 0; // does not matter
      } else {
        // no g, no m, top-level, c == 0 || !abstract
        addToCmdProfile(sigName, NonExactScope(Math.max(c, this.default_scope)));
        return 0; // does not matter
      }
    } else {
      // no g, no m, !top-level, c=?, abstract?
      return c; // abstract contraints take care of it
    }
  }

  private Integer GivenScopeNoModelScope(
      Qname sigName, SigScope g, SigScope m, Integer c, Integer closestParentScope) {
    // g is Ev or NEv, no m
    if (g.getValue() < c) {
      cmdScopeWarningExactFromBelowTakesPrecedence(this.pos, sigName, ExactScope(c), g.getValue());
      if (isTopLevelSig(sigName)) {
        // g is Ev or NEv, no m, v<c, top-level, abstract?
        addToCmdProfile(sigName, ExactScope(c));
        return c; // does not matter
      } else {
        // g is Ev or NEv, no m, v<c, !top-level, abstract?
        return c;
      }
    } else {
      // g is Ev or NEv, no m, v>=c, top-level?, abstract?
      if (this.isAbstractSig(sigName)) {
        if (c != 0 && g.getValue() != c) {
          cmdScopeWarningAbstractTakesPrecedence(this.pos, sigName, ExactScope(c), g.getValue());
        }
        // g is Ev or NEv, no m, v>=c, top-level?, abstract
        addToCmdProfile(sigName, ExactScope(c));
        return c;
      } else {
        // g is Ev or NEv, no m, v>=c, top-level?
        addToCmdProfile(sigName, g);
        return g.isExact() ? g.getValue() : c;
      }
    }
  }

  private Integer NoGivenScopeModelScope(
      Qname sigName, SigScope g, SigScope m, Integer c, Integer closestParentScope) {
    if (m.hasValue()) {
      if (c != 0 && c != m.getValue()) {
        // no g, m is Evalue, c>m
        throw ImplementationError.shouldNotReach();
      } else {
        // no g, m is Evalue, c=m, !abstract || !top-level
        addToCmdProfile(sigName, ExactScope(m.getValue()));
        return m.getValue();
      }
    } else {
      // no g, m is E?, abstract?, top-level?
      // only place p is used
      addToCmdProfile(sigName, ExactScope(closestParentScope));
      return closestParentScope;
    }
  }

  private Integer GivenScopeModelScope(
      Qname sigName, SigScope g, SigScope m, Integer c, Integer closestParentScope) {
    // g is Eg or NEg, m is E? or Em, c>0 or c==0
    if (m.hasValue() && c != 0 && c != m.getValue()) {
      throw ImplementationError.shouldNotReach();
    }
    // g is Ev1 or NEv1, m is E? or Ec, c>0 or c==0
    if (!m.hasValue()) {
      if (g.isExact()) {
        if (c > g.getValue()) {
          // g is Eg, m is E? , c>g, toplevel?, abstract?
          cmdScopeWarningExactFromBelowTakesPrecedence(this.pos, sigName, g, c);
          addToCmdProfile(sigName, ExactScope(c));
          return c;
        } else {
          // g is Eg, m is E? , c<=g, toplevel?, abstract?
          addToCmdProfile(sigName, g);
          return g.getValue();
        }
      } else {
        // g is NEg, m is E?, toplevel?, abstract?
        if (c != 0 && c != g.getValue() && this.isAbstractSig(sigName)) {
          cmdScopeWarningExactFromBelowTakesPrecedence(this.pos, sigName, g, c);
          addToCmdProfile(sigName, ExactScope(c));
          return c;
        } else {
          cmdScopeWarningExactFromBelowTakesPrecedence(this.pos, sigName, g, c);
          addToCmdProfile(sigName, ExactScope(g.getValue()));
          return g.getValue();
        }
      }
    } else {
      // g is Eg or NEg, m is Ec, toplevel?, abstract?
      if (g.getValue() != m.getValue()) {
        // m.getValue() == c
        cmdScopeWarningExactFromBelowTakesPrecedence(this.pos, sigName, g, m.getValue());
      }
      addToCmdProfile(sigName, m);
      return m.getValue();
    }
  }

  private void addToCmdProfile(Qname sigName, SigScope scope) {
    // q.println(sigName + " assigned scope: " + scope.toString());
    // assert (false);
    if (isTopLevelSig(sigName)) {
      this.scopeProfile.addTopLevel(sigName, scope);
    } else {
      this.scopeProfile.addExplicitExtends(sigName, scope);
    }
  }

  private void checkForErrorsInGivenScopes() {

    // trying to match what AA considers errors

    for (Qname sigName : givenScopes.keySet()) {
      SigScope scope = givenScopes.get(sigName);

      // every sig given an explicit scope size is a sigName in the model

      if (this.isSig(sigName)) {

        if (!this.isExtendsChild(sigName) && !this.isTopLevelSig(sigName)) {
          // "in" or "equals" sigs cannot be given scopes in the command
          throw AlloyModelError.cantSetScopeOfInEqualsChild(this.pos, sigName.toString());

        } else if (this.isEnumSig(sigName)) {
          // no enum sig can have a givenScope
          throw AlloyModelError.cantSetScopeOfEnum(this.pos, sigName.toString());

        } else if (this.isLoneSig(sigName) && !scope.equals(NonExactScope(1))) {
          // lone sig must be NE<=1
          throw AlloyModelError.loneSigMustBeNonExactOne(this.pos, sigName.toString());

        } else if (this.isSomeSig(sigName)) {
          if (scope.getValue() <= 0) {
            // every some sig has NE>=1
            throw AlloyModelError.someSigMustBeOneAndUp(this.pos, sigName.toString());
          }

        } else if (this.isExtendsChild(sigName) && !cd.defaultScope.isPresent()) {
          /*
          if an "extends" (including a one sig) child is given an explicit scope, its top-level sig must be given an explicit scope in the command (even if this is by designating a default scope for everything else) (otherwise error)
              sig A {}
              sig A1 extends A {}

              run {} for 3 but exactly 2 A1
            is okay, but
              sig A {}
              sig A1 extends A {}

              run {} for 2 A1
            is not allowed
          */
          // we have to look in rest of givenScopes to see if top-level
          // parent of sigName has an explicit scope given in the command
          if (!givenScopes.keySet().contains(this.topLevelExtendsAncestor(sigName))) {
            throw AlloyModelError.scopeOfTopLevelSigMustBeGiven(this.pos, sigName.toString());
          }
        }
      } else if (sigName.name.equals(AlloyStrings.NONE)) {
        throw AlloyModelError.noScopeForNone(this.pos);
      } else if (!sigName.name.equals(AlloyStrings.SIGINT)) {
        // TODO: other builtins to add here
        throw AlloyModelError.noScopeForNonSig(sigName.toString());
      }
    }
  }

  // private  -------------------

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

  // debugging -----------------

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
                "%s %s  (scopes=%s%s, resolved=%s%s)",
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
      // sb.append("\n" + getCmdScopeProfile(cd));
      sb.append('\n');
    }
    sb.append("  modelScopes:\n");
    modelScopes.forEach(
        (k, v) -> sb.append("    ").append(k).append(" -> ").append(v).append('\n'));

    sb.append("  orderedSigs:\n");
    orderedSigs.forEach(sig -> sb.append("    ").append(sig).append('\n'));

    System.out.println(sb.toString() + "\n");
  }

  private void cmdScopeWarningAbstractTakesPrecedence(
      Pos pos, Qname sigName, SigScope scope, Integer v) {
    Reporter.INSTANCE.addWarning(
        new WarningUser(
            pos,
            "abstract takes precedence to make scope of "
                + sigName.toString()
                + " be "
                + scope.toString()
                + " rather than scope given in command of: "
                + v.toString()));
  }

  private void cmdScopeWarningExactFromBelowTakesPrecedence(
      Pos pos, Qname sigName, SigScope scope, Integer v) {
    Reporter.INSTANCE.addWarning(
        new WarningUser(
            pos,
            "exact scope from children takes precedence to make scope of "
                + sigName.toString()
                + " be "
                + scope.toString()
                + " rather than scope given in command of: "
                + v.toString()));
  }
}
