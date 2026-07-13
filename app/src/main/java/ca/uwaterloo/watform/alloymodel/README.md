# README

## AlloyModel Chain of Classes

* The following store info about the model and its imports.
For resolving these, fieldArityAndSetMul are used and info about
arities is updated during resolve.
AMFields
AMSigs
AMPredFuns
AMFacts
AMScopes - contains a little state for exactly scopes loaded by imports; only local state
AMArity - when the arity checking functions are set up; the collection
          of tables below this are used in arity checking for each other

* The following store info about the current file and use only setMul because resolving them can't change 
the multiplicites of any symbols
AMThisEnums - adds to SigTable
AMThisSigs - adds to SigTable, FieldTable
AMThisPreds - add to PredFunTable
AMThisFuns - add to PredFunTable

AMThisImports - adds to SigTable, FieldTable, PredFunTable, Facts
AMThisMacros
AMThisFacts
AMThisAsserts
AMThisCmds
AMThisModules
AlloyModel

* All AMxxx(notTable) are initialized with an empty list so that a model can be created completely through the API.

### Initalization

* When creating via an AlloyFile, copy of another AlloyModel, or through the API, all tables, and lists are initialized, but default muls and arities are not calculated.  

* Imported files are included in the table above during initialization but are.

Any exact scopes set via imports are recorded in AMScopes.

### Resolving

In the use of an Alloy Model, it must be resolved before it is final.
- Done in CLI when parse Alloy File.
- Done in Dash2Alloy at very end.
- Should be done in predAbs or DashToTla?

* The purpose of resolving is to:
    - set default mul in arrows and decls (determined by arity during typecheckingsl)
    - figure out namespace+name for all AlloyQnameExprs
    - typechecking errors
    - other errors

* Overloading:
    - AMSigs
        - two sigs in different namespaces can have the same name
        - two sigs in the same namespace **cannot** have the same name
        - (*init*) sigs are loaded into the sig table in their namespace -- there cannot be multiple entries for the same spot in the hashtable
        - (*lookup*) a sig can be looked up through just its name (not including the namespace), so there could be multiple sigs with the same name
        - (*resolve*) nothing to do
    - AMFields
        - two fields in different sigs can have the same name
        - two fields in the same sig (and namespace) **cannot** have the same name
        - a field and a sig can have the same name
        - (*init*) fields are loaded into the sig table in their namespace (including the sig name) so there cannot be multiple entries for the same spot in the hashtable.
        - (*lookup*) a field can be looked up through just its name (not including the namespace), so there could be multiple sigs with the same name
        - (*resolve*) walks over field bounding expressions; desugaring
            - what order to desugar and resolve 
    - AMPredFuns
        - two different preds within the same namespace can have the same name 
        - two different funs within the same namespace can have the same name
        - a pred and a fun can have the same name
        - (*init*) loaded with their namespace and body, but the hash table **must** accomodate multiple values for the same name/namespace
        - (*resolve*) must consider namespace when resolving body; when resolved becomes this/p or P/p
    - AMFacts
        - (*init*) have a namespace associated with each fact
        - (*resolve*) must consider namespace in resolve
    - *Typechecking* (uses *lookup*s above):
        - Result
            - all sig names become "namespace/sigName"
            - all uses of fields either **already** are this/A <: f or become like this during; field names aren't written as this/f ot this/A/f
            - all typechecking passes
            - all multiplicities in arrows and decls must be set to defaults if not already present
        - Notes:
            - sig and field names can be the same
            - a pred and a sig can have the same name -- distinguished by return type
            - a pred and a field can have the same name -- distinguished by return type
            - a fun and a sig **cannot** have the same name
            - a field and a fun **cannot** have the same name - I'm a little iffy on this one; it seems disallowed syntactically in AA, but could make sense to me and be disambiguated by  


### Creating an AlloyModel through API calls

Any API additions of paragraphs are NOT checked for arity and default multiplicities.  The "resolve" phase of an AlloyModel is a separate check that the user can call.  This is because declarations are not required before use in Alloy.


## Additional Classes

* CalcAritySetMulDefaultsExprVis is an instance of AlloyExprVis that walks over expressions and determines their arity and set mul defaults.  If choosing an arity is not possible, it throws an exception.

## Errors

Two kinds of errors are thrown in this package:

* AlloyModelError (all described in AlloyModelError.java)
These are errors in the model, which could be caused by something in the AlloyFile loaded or by a programmer using the API so these are caught in parseToModel, which records them in the Reporter as user errors.

* AlloyModelImplError (all in AlloyModelImplError.java) or its parent ImplementationError
These are programming errors that caught and output in Main.java .