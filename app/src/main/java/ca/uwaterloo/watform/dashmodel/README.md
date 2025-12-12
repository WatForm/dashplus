# DashModel

"DM" suffix means file is in DashModel inheritance
sequence.

Inheritance Sequence for decomposition of DM functionality:
* DashModel (to be used by anything outside this dir)
* ResolveDM (sets up tt, invs, inits after resolution)
* ResolverVisDM (a DM to avoid having to pass in all tables)
* InitializeDM (sets up st,vt,bt,pt)
(below all has public accessors to these parts)
* PredsDM
* EventsDM
* BuffersDM
* VarsDM 
* StatesDM
* TransDM
* InitsInvsDM inherits from AlloyModel

This are in order based on who needs previous functionality.

## Naming Conventions

* R is used for trans element suffixes to mean "resolved" (and b/c "goto" is a keyword)

## Initialize Phase

* walks over DashFile
- puts all state decls as an sfqn entry with parameters in StateTable, with appropriate defaults
- puts all event decls as an efqn entry with parameters in EventTable
- puts all var decls as a vfqn entry with parameters in VarTable
- puts all buffer decls as a bfqn entry with parameters in BufferTable
- puts all pred decls as pfqns in PredTable; no parameters for this one

## Resolve Phase
- walks over trans, invs and inits to resolve them and store them 

## Creating a DashModel from scratch (not from a DashFile)
* all entries must be resolved
* more to add

