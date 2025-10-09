# Gradle and Antlr
- The ANTLR plugin is applied in app/build.gradle
- The ANTLR plugin adds 3 tasks to the project, as shown below.
    1) generateGrammarSource 
        - Generates the source files for all production ANTLR grammars.
        - Default path: src/main/antlr 
        - compileJava's dependency

    2) generateTestGrammarSource
        - Generates the source files for all test ANTLR grammars.
        - Default path: src/test/antlr
        - compileTestJava's dependency
        - Note: see app/src/test/java/org/antlr for JUnit tests for testing ANTLR; not using generateTestGrammarSource

    3) generateSourceSetGrammarSource
        - Generates the source files for all ANTLR grammars for the given source set.
        - Default path: src/sourceSet/antlr
        - compileSourceSetJava's dependency
- So if you run ./gradlew build (or ./gradlew compileJava) or ./gradlew test (see app/build.gradlew), Gradle will first execute the generateGrammarSource task
- You can find ANTLR generated files in app/build/generated-src/antlr/main/
- This header in Alloy.g4 specifies the package where you can import antlr generated classes like AlloyLexer
    ```
    @header {
        package antlr.generated;
    }
    ```
- Then compileJava compiles those generated .java files together with src/main/java sources
- Don't put the antlr generated files in src/
    - https://stackoverflow.com/questions/36469546/what-is-minimal-sample-gradle-project-for-antlr4-with-antlr-plugin

- Reference: https://docs.gradle.org/current/userguide/antlr_plugin.html

# Changes made to Peter's grammar 
Started with: https://github.com/pkriens/org.alloytools.alloy/blob/pkriens/api/org.alloytools.alloy.parser/src/main/antlr/Alloy.g4

## Small changes
- optional ( qname '.' ) in 'funPara' and 'predPara'
- optional qnames inside [] in importPara
- optional params in box rule of 'expr'
- optional block/bar in comprehension

- accept '<=' for less or equal to; this is consistent with CUP
- accept leading, trailing, and sequence of commas in 'sigPara' 
- accept trailing comma in 'arguments'
- accept round brackets for macroPara
- accept more symbols for the PRIME token
- accept PRIVATE in more places
- accept one more rule (uses EQUAL) in varDecl

- added tokens for arithmatic operations, ExprConstant (Alloy AST), so they are not just 'qname'
    - added tokens for 'none', 'univ', 'iden', 'fun/min', 'fun/max', 'fun/next', STRING_LITERAL,
                    'int', 'Int', 'steps'
    - see builtinValue in 'expr'
    - grammar is updated to still accept builtins in places like 'sigRef'
- added tokens for 'pred/totalOrder' and 'disj'
- added the 'until' keyword to the temporal operators
- added bit shift operators
- added 'seq' expr as a rule (https://alloytools.org/quickguide/seq.html)

- removed the two 'boxValue' rules in 'value'

## Large changes
- separated the different uses of the 'multiplicity' keywords, so it's clear what they are being used for from context
- 'formula' concatenation in 'block': 
    - Before: optional AND operators, but this causes precedence issues with other binop rules
    - After: mandatory AND operators, but accept sequence of 'formula's in 'block'; this is consistent with CUP
- merged 'formula' and 'value', because 'ite' causes indirect left recursion between formula and value (starts with 'formula' in a 'value ite')
    - merged them to expr and ANTLR handles direct left recursion
    - need to check for usage of grammar rules (formula or value) in parser-visitors
    - most rules can be distinguished by the operator tokens alone, with exceptions like 'ite', 'let', 'join' and 'box', and 'parenthesis'
- break expr into three parts (expr1, implies, expr2)
    - because a 'ite's (ternary op) precedence is not handled correctly in the midst of other binop rules
    - other options tried:
        - 1) copying CUP; have a distinct expr type for every rule
            - relationExpr is right recursive and ANTLR is very inefficient when parsing relationExpr with other left recursive rules; frequent timeouts
        - 2) break ITE into 2 binop: implies and else. 
            - Use semantic predicate to reject some cases (using else outside of an ite)
            - parser-visitors need to check and restructure the ast, because some cases cannot be handled by semantic predicates alone
                such as (t => t else t => t), and (f=>t else f => f else t)
        - see previous attempts at 'expr' below
    - by breaking it into three parts 
        - we can always parse ITE correctly and quickly
        - maintain correct precedence
        - ANTLR handles direct left recursion quickly
        - we don't get indirect left recursion because rules with lower precedence (expr2) do not parse higher precedence rules (expr1 & implies) as subexpressions
- replaced ('expr' 'qname' 'expr') 
    - changed to (expr ('fun/mul' | 'fun/div' | 'fun/rem') expr) and (expr ('+' | '-' | 'fun/add' | 'fun/sub') expr)
    - placed them in the correct order of precedence
- refactored string literals in grammar rules to tokens; this improves reusability and makes ANTLR generated Context classes easier to work with
- reordered WS and COMMENT tokens
    - changed OPTION_COMMENT to properly handle 
        ```
            --
            sig S {}
        ```
- reject PLUS in front of NUMBER to be used as positive number
    - use semantic predicate to reduce local ambiguities for acceping MINUS before NUMBER (see bottom of CompModule in Alloy code)



## A small exception
Cup doesn't call nod() when parsing the Command rule, causing Command to accept names with the '$' symbol. 
So it accepts:
```
sig A  {} 
run $s { some A } for 1
```
Running it in the Alloy Analyzer throws error 
```
Executing "run $s for 1"

Fatal Error: the solver ran out of stack space!
Try simplifying your model or reducing the scope,
or increase stack under the Options menu.

An error has occurred!
```
This is probably a bug in CUP, and the Antlr Grammar will not accept this. 



## Previous attempts at Expr
```
expr 			: exprNoSeq 
				| exprNoSeq SEQUENCE_OP expr
				;

bind			: LET assignment ( COMMA assignment )* body 														# let
				| (ALL | NO | SOME | LONE | ONE | SUM) decl ( COMMA decl )* body 									# bindingQuantifierFormula
				;

exprNoSeq		: orExpr
				| bind
				;

orExpr 			: orExpr (OR_BAR | OR) orExpr
				| orExpr (OR_BAR | OR) bind
				| equivExpr
				; 

equivExpr 		: equivExpr (IFF_ARR | IFF) equivExpr
				| equivExpr (IFF_ARR | IFF) bind
				| impliesExpr
				;

impliesExpr 	: impliesExprClose
    			| impliesExprOpen
    			;

impliesExprClose 	: andExpr (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprClose  
					| andExpr (RFATARROW | IMPLIES) impliesExprClose ELSE bind             
					| andExpr
					;

impliesExprOpen 	: andExpr (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprOpen   
    				| andExpr (RFATARROW | IMPLIES) impliesExpr                            
    				| andExpr (RFATARROW | IMPLIES) bind                                  
    				;

andExpr 		: andExpr (AND_AMP | AND) tempBinary
				| andExpr (AND_AMP | AND) bind
				| tempBinary
				;

tempBinary		: tempBinary (UNTIL | SINCE | TRIGGERED | RELEASES) unaryExpr
				| tempBinary (UNTIL | SINCE | TRIGGERED | RELEASES) bind
				| unaryExpr
				;

unaryExpr		: (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | HISTORICALLY | ONCE | BEFORE) unaryExpr
				| (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | HISTORICALLY | ONCE | BEFORE) bind
				| compareExpr
				;

compareExpr		: compareExpr comparison shiftExpr
				| (ALL | NO | SOME | LONE | ONE | SET | SEQ ) shiftExpr
				| shiftExpr
				;

shiftExpr 		: shiftExpr (SHL | SHR | SHA) unionDiffExpr
				| shiftExpr (SHL | SHR | SHA) bind
				| unionDiffExpr
				;

unionDiffExpr	: unionDiffExpr (PLUS | MINUS | FUNADD | FUNSUB) mulExpr
				| unionDiffExpr (PLUS | MINUS | FUNADD | FUNSUB) bind
				| mulExpr
				;

mulExpr			: mulExpr (FUNMUL | FUNDIV | FUNREM) numUnopExpr
				| mulExpr (FUNMUL | FUNDIV | FUNREM) bind
				| numUnopExpr
				;

numUnopExpr		: (CARDINALITY | SUM | INT) numUnopExpr
				| (CARDINALITY | SUM | INT) bind
				| overrideExpr
				;

overrideExpr	: overrideExpr REL_OVERRIDE intersectExpr
				| overrideExpr REL_OVERRIDE bind
				| intersectExpr
				;

intersectExpr	: intersectExpr INTERSECTION relationExpr
				| intersectExpr INTERSECTION bind
				| relationExpr
				;

relationExpr 	: domainExpr arrow relationExpr
				| domainExpr arrow bind
				| domainExpr
				;

domainExpr		: domainExpr DOMRESTR rangeExpr 
				| domainExpr DOMRESTR bind 
				| rangeExpr
				;

rangeExpr 		: rangeExpr RNGRESTR joinExpr
				| rangeExpr RNGRESTR bind
				| joinExpr
				; 

// cannot be separate like in CUP, b/c indirect left recursion
joinExpr		: (DISJ | PRED_TOTALORDER | INT | SUM) LBRACK (expr (COMMA expr)*)? RBRACK
				| joinExpr LBRACK (expr (COMMA expr)*)? RBRACK 
		 		| joinExpr DOT unopExpr 
				| joinExpr DOT bind
				| joinExpr DOT (DISJ | PRED_TOTALORDER | INT | SUM) 
				| unopExpr
				;

unopExpr		: (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) unopExpr
				| (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) bind
				| unopExpr PRIME
				| bind PRIME
				| baseExpr
				;

baseExpr		: number
				| STRING_LITERAL
				| IDEN
				| THIS
				| FUNMIN
				| FUNMAX
				| FUNNEXT
				| LPAREN expr RPAREN
				| sigRef
				| AT name
				| block
				| LBRACE decl ( COMMA decl )* body? RBRACE              											
				;

// ____________________________________

@parser::members {
	// for handling ITE as two binop exprs
	private final java.util.Deque<Boolean> _inImpliesRHSStack = new java.util.ArrayDeque<>();
	{_inImpliesRHSStack.push(Boolean.FALSE);}
	private boolean inImpliesRHS() { return _inImpliesRHSStack.peek(); }
	private void pushImpliesRHS(boolean v) { _inImpliesRHSStack.push(v); }
	private void popImpliesRHS() { _inImpliesRHSStack.pop(); }
}
expr	        : (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) expr                                               		# unaryOpValue
				| expr PRIME                                                        								# primeValue // exprVar
				| expr DOT expr                                                   									# dotJoin 
                | expr LBRACK (expr (COMMA expr)*)? RBRACK                                  						# boxJoin
				| expr (DOMRESTR | RNGRESTR) expr                                           						# restrictionValue
				| expr arrow expr                      																# arrowValue
				| expr INTERSECTION expr                                                   							# intersectionValue
				| expr REL_OVERRIDE expr                                                  							# relationOverrideValue
				| expr (FUNMUL | FUNDIV | FUNREM) expr																# mulDivRemValue
                | CARDINALITY expr                                                         							# cardinalityValue
				| expr (PLUS | MINUS | FUNADD | FUNSUB) expr                                             			# unionDiffAddSubValue
				| expr (SHL | SHR | SHA) expr 																		# bitShiftValue
                | SUM decl ( COMMA decl )* body                                										# sumValue		// pg 289
				| LBRACE decl ( COMMA decl )* body? RBRACE              											# comprehensionValue

				| SEQ expr																							# seqValue
				| INT expr																							# castToSigIntValue

				| cardinalityConstraint expr                    													# cardinalityConstraintFormula
                | expr comparison expr 																				# comparisonFormula
                | (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | BEFORE | HISTORICALLY | ONCE) expr  				# unaryFormula
                | expr (UNTIL | RELEASES | SINCE | TRIGGERED) expr            										# binaryFormula
                | expr (AND_AMP | AND) expr                                   										# andFormula
				| <assoc=right> expr {inImpliesRHS()}? ELSE expr                                     				# elseFormula
				| <assoc=right> expr (RFATARROW | IMPLIES) {pushImpliesRHS(true);} expr {popImpliesRHS();}      	# impliesFormula
                | expr (IFF_ARR | IFF) expr                                   										# iffFormula
                | expr (OR_BAR | OR) expr                                       									# orFormula
				| LET assignment ( COMMA assignment )* body 														# let
				| bindingQuantifier decl ( COMMA decl )* body 														# bindingQuantifierFormula
                | expr SEQUENCE_OP expr                                               								# sequenceFormula

                | LPAREN expr RPAREN                                                     							# parenthesis                
                | LBRACE expr RBRACE                                                     							# parenthesis                
                | block                                                            									# exprBlock

                | AT name                                                         									# atnameValue
                | qname META                                                        								# metaValue 
				| qname                                                            									# qnameValue
				| (NONE | UNIV | IDEN | PRED_TOTALORDER | DISJ | SUM |
						THIS | INT | SIGINT | STEPS | SEQ_INT | STRING |												
						FUNMIN | FUNMAX | FUNNEXT | number | STRING_LITERAL) 										# builtinValue	 // exprConstant and exprVar
                ;

```

