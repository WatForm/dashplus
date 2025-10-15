# Reorg for Dash+ Code
 
## Proposed New Code Organization

* cli
	- see org.alloytools.alloy.applications/.../watform/Dash4whole
	- we should start with this code to see how the pieces will fit together from the top level into the above code organization
	- from CLI
	- from GUI @Rocky
	- for @Aditya's work
	- for @Mathew's work
	- for incremental solving?

* util
	- Pos class (line number, column number)
	- other util functions

* alloy-ast
	- classes that match all parts of Alloy grammar 
	- all have toString methods
	- fields all public
	- top-level AlloyFile class that has filename, module name; its toString method should print out the input Alloy file (subject to spacing changes)
	- includes a class of AlloyExpr called DashRef
	- should we call this Alloy b/c it has a little more than Alloy in it (namely an expr extension for Dash)
	- these should be final

* dash-ast
	- classes that match all parts of Dash grammar (which includes Alloy stuff)
	- all have toString methods
	- fields all public
	- top-level DashFile has filename and module name; ; its toString method should print out the input Dash file (subject to spacing changes)
	- these should be final

* parser
	- visitor(s) to map dash/alloy grammar to AST parts
	- probably contains two top-level parsing functions:
		1) string -> AlloyFile
		2) stirng -> DashFile

* alloy-model
	- new AlloyModel(AlloyFile arg)
	- one class to contain everything about an Alloy model
	- contains name, lists of opens, sigs, facts, functions/predicates/commands
	- add/remove functions for these lists
	- how name unnamed things like facts?
	- toString method
	- execute cmd method
	- what to do with results -> do we want our own version of an A4Solution?

* dash-model
	- new DashModel(DashAst arg) (replacement for resolveDash)
		- creates the Inits, Invs, StateTable, TransTable, VarTable, EventTable, PredTable
		- when putting elements in this table it fully qualifies all "names" (state names, transition names, variable names, event names, predicate names) and adds all parameters (as DashParams) for these names (this process uses ResolveExpr)
	- one class to contain everything about a Dash model
	- hash tables for state/trans/events/vars/preds (these are preds within a state)
	- needs a toString method
	- extension of AlloyModel so that it also had lists of opens, sigs, facts, etc that come directly from Alloy parts of the Dash model
	- includes class DashParam (parameters for states/transitions)
	- includes a visitor for ResolveExpr: AlloyExpr -> AlloyExpr

* dash-to-alloy
	- translation from Dash to Alloy
	- new AlloyModel(DashModel arg)

* dash-to-tla
	- @Mathew
	- new TLAModel (DashModel arg)
	- TLAModel class has a toString method
	
* pred-abstraction
	- @Aditya
	- creates new Dash/AlloyModels frequently - would not require a copy of ast elements
	- but would require a copy/recreation of table elements
	- what are main interface points?

## Mapping from old Code

* mainfunctions/*
	- connections to CLI above?

* alloyasthelper/DeclExt.java
	- becomes a class for AlloyDecl in alloy-ast

* alloyasthelper/ExprHelper.java
	- moves into alloy-ast
	- becomes a bunch of classes for each different Alloy Expr type in the grammar
		- each class has a toString method that outputs it in Alloy string form
		- need to define equal methods?
		- need to access fields (make fields public)
	- all other classes are subclasses of AlloyExpr
		- line/pos fields in all (public)
	- create functions here will be replaced with "new X" functions in the rest of the code
	- test functions here (e.g. isExprJoin) will be replaced with isinstance(x, AlloyExprJoin) functions in the rest of the code

* alloyasthelper/ExprToString.java
	- contents put into toString methods of Alloy AST classes

* ast/*
	- moves into dash-ast
	- only changes will be references to Expr -> AlloyExpr 
	- can we clean up getX methods by just using public attributes?

* core/*
	- DashFQN.java should move to DashModel
	- DashErrors 
		- should be distributed
	- DashOptions.java moves to dash-to-alloy
	- DashParam.java should move to dash-model
	- DashRef.java
		- this is mostly a hidden data structure but it is used in Dash tables; could be moved to DashModel or DashAST?
	- DashSituation.java 
		- unsure where this goes
		- may no longer be needed 
	- DashStrings.java should move to dash-ast
	- DashUtilFcns.java moves to util

* dashtoalloy
	- belong in dash-to-alloy
	- mostly stays the same but with changes to accessor and creation of Alloy parts

* parser
	- CompModuleHelper.java
		- mostly becomes methods of AlloyModel
	- DashFilter.java will disappear
	- DashModule.java
		- becomes part of DashModel
	- DashTablesToDashAST.java
		- becomes part of toString method of DashModel
	- DashUtil.java will disappear
	- EventTable, PredTable, StateTable, TransTable, VarTable move to DashModel; probably add a separate BufferTable
	- install-alloy-files.sh disappears
	- ResolveExpr.java becomes a visitor over the alloy-ast and lives in DashModel

## General Questions

* options:
	- make this a part of the Alloy code base 
	- or make this a separate repo using Alloy as a library
	- depends on integration with Alloy Analyzer GUI (@Rocky)
* coding conventions to adopt?
	- make fields public
* what do we want to do with results returned from command execution?
* can @Mathew's translation depend only on the parsed (textual) Alloy or does it need more info from the current Alloy data structures? e.g., does it need to know SOME vs SOMEOF?
* best method for handling errors? (think of gui as well as CLI)
* should we change "dash" to "dashplus" above?
* during this process, we need to write the paper on Dash+ to give Mathew something to refer to for the semantics of Dash+ 

## Task List

* simple cli that takes an Alloy file; checks its extension
* get Peter Kriens grammar parsing Alloy files
* create alloy-ast classes with toString methods
* create visitors to turn elements of antlr grammar into alloy-ast classes that returns an Alloy-File at the top-level
* create a function that does alloyFile.toString() and sends the file to Alloy for execution; probably need to execute first command??


Peter Kriens grammar rules, .g4
Peter's post to discourse

unambiguous mapping of (none/lone, some & someof, etc) to

ExprToString (more) (->)
ExprModuleHelper (less) (->)
Expr (one to one mapping)


