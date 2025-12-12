# Coding Standards

* use public attributes for any attributes that is get/set only (rather than get/set methods)
* all attributes of AST nodes should be final

## Abbreviations
* Expression --> expr/exp
* Type --> typ
* Definition --> defn
* Application --> appl
* Declaration --> decl
* Parameter --> param/prm
* Reference --> ref
* Variable --> var
* Visitor --> vis 
* Fully Qualified Name --> FQN
* Temporal Logic of Actions (TLA+) --> Tla
* Ancestor --> ances
* Assignment --> asn
* Iterator? --> iter
* Concurrent --> conc
* Operand --> op


Use the abbreviated form everywhere in the code, to keep names short and readable. If a new abbreviation is added, update this list.

## Naming
* Directory names: All lowercase and avoid underscores
* Class and .java file names: UpperCamelCase
* Method and variable names: lowerCamelCase 

## Importing
* Import utils files like so to avoid cluttering code:
    - `import static ca.uwaterloo.watform.utils.GeneralUtil.*;`
    - `extractOneFromList(...)` instead of `GeneralUtil.extractOneFromList(...)`

