# Coding Standards

* use public attributes for any attributes that is get/set only (rather than get/set methods)
* all attributes of AST nodes should be final

## Abbreviations
* Expressions --> expr
* Type --> typ
* Definition --> defn
* Application --> appl
* Declaration --> decl
* Reference --> ref
* Variable --> var
* Fully Qualified Name --> FQN
* Temporal Logic of Actions (TLA+) --> Tla

## Naming
* Directory names: All lowercase and avoid underscores
* Class and .java file names: UpperCamelCase
* Method and variable names: lowerCamelCase 

## Importing
* Import utils files like so to avoid cluttering code:
    - `import static ca.uwaterloo.watform.utils.GeneralUtil.*;`
    - `extractOneFromList(...)` instead of `GeneralUtil.extractOneFromList(...)`

