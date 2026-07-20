# README

* This package contains a *sequence* of classes that start with classes that are a **semantic** model of everything in the AlloyModel and its imports; and are followed by a **syntactic** model of elements within "this" namespace (top-level file).

* In Alloy, declarations are not required before use in Alloy, which creates two phases 1) init and 2) resolve.

* During *init* of a syntactic class by an AlloyFile, the semantic model is populated.  Field bounding expressions and signature facts are desugared and included in the list of facts of the semantic model.
* API calls in the syntactic model classes can also populate the semantic model.

* After the semantic model is fully populated, the user must call *resolve* to walk over everything in the semantic model to qualify all names, find typechecking and other errors.  
* Nothing is added to the semantic model during resolve.
* Resolve can be repeated and won't change anything already resolved.
* All translators (TLA+, Portus) should use **only** the semantic model.

* For sigs, adding a sig after resolve has to set children.
* In all other cases, a resolve *flag* is used to indicate whether it has been resolved already or not.


## AlloyModel Chain of Classes

* 'this' refers to top-level name space - first file reading

### Semantic Model (SM)

* contains info for ALL namespaces
* does **not** know about paras
* this list is in *order* so that resolves happen in correct order
* *init does little; resolve does a lot for these*
* after init/API calls, the semantic model contains everything it needs to resolve


* SMSigs 
    - all sigs of all namespaces plus parent/child/type
    - two sigs in different namespaces can have the same name
    - two sigs in the same namespace **cannot** have the same name
    - sigs are loaded into the sig table in their namespace -- there cannot be multiple entries for the same spot in the hashtable
    - (*init*) already empty; does nothing
    - (*addSig(name, namespaces, parent)*) 
    - (*lookup*) may return multiple namespace+name, arity 1
    - (*resolve*) sets children, look for circularity of parent/child
    - *addSig* after resolve also has to set children of newly added sigs

* SMFields 
    - fields are loaded into the sig table in their namespace (including the sig name) so there cannot be multiple entries for the same spot in the hashtable.
    - bounding expressions of fields of all namespaces
    - namespace includes sig, e.g., "this/A"
    - two fields in different sigs can have the same name
    - two fields in the same sig (and namespace) **cannot** have the same name
    - a field can only depend on previous fields in the same sig
    - (*init*) already empty; does nothing
    - (*addField(name, namespace(includes parentSig, boundingExpr)*) set unresolved flag
    - (*lookup for resolve*) 
        - a field can be looked up through just its name (not including the namespace), so there could be multiple sigs with the same name
        - can only return something if it is already resolved! (thus circularities are implicitly disallowed)
    - (*resolve1*) walk over and **replace** field bounding expressions to get mul set; set resolved flag 

* SMConstraints 
    - all facts of all namespaces (not asserts/cmds), resolved flag
    - have a namespace + kind (sigfact, bounding expression, fact) associated with each fact
    - (*init*) already empty; does nothing
    - (*addFact(name, namespace, expr)*)
    - (*resolve2*) walk over all (field decl facts, sig facts, all other facts)

* SMPredFuns 
    - all pred/fun of all namespaces, resolved flag
    - two different preds within the same namespace can have the same name 
    - two different funs within the same namespace can have the same name
    - a pred and a fun can have the same name
    - loaded with their namespace and body, but the hash table **must** accomodate multiple values for the same name/namespace
    - (*init*) contains built-ins (e.g., pred/totalOrder, fun/sum) with no body
    - (*addPred(name, namespace, args, body)*)
    - (*addFun(name, namespace, args, returnType, body)*)
    - (*lookup fo resolve*) may return multiple entries for name
    - (*resolve1*) on arg types, return types
    - (*resolve2*) must consider namespace when resolving body
    - TODO: check for circularities in arg types, return types, body
    - TODO: check for recursive calls

* SMCmds 
    - all cmds in list of *this namespace only* (for consistency, keep a namespace, resolved flag)
    - all asserts in namespace+name, resolved flag; no dups
    - scope info about ordered and exact prescribed by model (enum, ordering import)
    - (*init*) already empty; does nothing
    - (*addAssert(namespace, name, expr)*)
    - (*addCmdDecl(namespace, cmdDecl)*)
    - (*access*) facts of cmd, scope info of cmd
    - (*resolve2*) on assert and cmds bodies 


* SMResolve (no state)
    - either resolves any parts below it
    - or passes resolve1 and resolve2 for classes to resolve themselves

* all the above are initialized to empty and added to by init on AlloyFile or API

### Syntactic Model (AMThisX)

* The following classes store info about the current file *only*.  Nothing from Alloy parsing is changed here (not even default mul).
* Only used for 1) *init* of semantic model, 2) provide *API* to add syntactic things (which does init of semantic model), and 3) *string* of Alloy.
* *resolve* for these does nothing
* this list is in order, but the order does **not** matter

* AMThisEnumPars init/API
    - adds to SMSigs
    - set ordering in SemCmds
    - no fields or facts allowed in enums

* AMThisSigPara init/API
    - adds to SMSigs with namespace and immediate parent
    - adds fields and bounding expr to SMFields (non-resolved flag)
    - desugaring field bounding expressions:
        `f: g -> A` in sig B with fields f and g 
        becomes all this:B this.f in this.g -> A
        (may not depend on anything but sigs and fields in this sig)
        added to SMConstraints with src
    - desugared sig facts
        `this in g` becomes all this:B | thus in this.g
        added to SMConstraints with src

* AMThisPredPara init/API
    - add to SMPredFuns

* AMThisFunPara init/API
    - add to SMPredFuns

* AMThisImportPara init/API
    - alloyFile of import is attached to importPara
    - do substitution of sig arguments throughout file
    - do 'init' of enums, sigs, pred, fun, fact, asserts in namespace of import
    - may add scope limits from [exactly elem] in module name in SemCmds
    - ignore cmds from imports
    - recursively does this for importParas
    - TODO: check for circularity in recursive import!
    - NOTE: none of these contents are added to "This" parts of AlloyModel

* AMThisMacroPara init/API
    - not supported; throw error

* AMThisFactPara init/API
    - adds to SMConstraints

* AMThisAssertPara init/API
    - adds to SMCmds in namespace

* AMThisCmdPara init/API
    - add cmdDelc to SMCmds in namespaces

* AMThisModulePara init/API
    - nothing to be done

* AlloyModel


## Resolving

* The purpose of resolving is to:
    - determine arities to set default mul in arrows and decls (if not already present)
    - figure out namespace+name for all AlloyQnameExprs
    - when resolved sigs become this/p or P/p
    - all uses of fields either **already** are this/A <: f or become like this during; field names aren't written as this/f ot this/A/f
    - typechecking errors
    - other errors
    
* For each expression **resolve** returns:
    - a new expr with mul set and namespaces added to names (no other changes)
    - removes overloading of pred/fun in **same** namespace by renaming them everywhere on first encounter
    - for bounding expr of fields, it must know other fields in sig to treat
    'f' as 'a.f' wrt to arity/typechecking
    - **resolve1** only depends on sigs and fields of this sig
    - **resolve2** additional looks up in pred/fun tables

* Lookup of anything can return multiple (namespace, name) elements
* field names and sig names can be the same
* a pred and a sig can have the same name -- distinguished by return type
* a pred and a field can have the same name -- distinguished by return type
* a fun and a sig **cannot** have the same name
* a field and a fun **cannot** have the same name - I'm a little iffy on this one; it seems disallowed syntactically in AA, but could make sense to me and be disambiguated by  


## Additional Classes

* CalcAritySetMulDefaultsExprVis is an instance of AlloyExprVis that walks over expressions and determines their arity and set mul defaults.  If choosing an arity is not possible, it throws an exception.

## Errors

Two kinds of errors are thrown in this package:

* AlloyModelError (all described in AlloyModelError.java)
These are errors in the model, which could be caused by something in the AlloyFile loaded or by a programmer using the API so these are caught in parseToModel, which records them in the Reporter as user errors.

* AlloyModelImplError (all in AlloyModelImplError.java) or its parent ImplementationError
These are programming errors that caught and output in Main.java .