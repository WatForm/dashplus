grammar Alloy;

@parser::members {
    boolean inImpliesRHS = false;
}

alloyFile
    : paragraph*
    ;

paragraph       : moduleDecl
                | importDecl
                | macroDecl
                | sigDecl
				| enumDecl
                | factDecl 
				| funDecl
                | predDecl 
                | assertDecl
                | labelCommandDecl
                ;

moduleDecl      : 'module' qname ( '[' moduleArg (',' moduleArg)* ']' )? ;
importDecl      : p='private'? 'open' qname ( '[' qnames ']' )? ( 'as' name )? ;
sigDecl         : qualifier* 'sig' names ('extends' extend=qname | 'in' qname ( '+' qname )*)? '{' (varDecl ( ',' varDecl )*)? ','? '}' block? ;
enumDecl        : p='private'? 'enum' name '{' names '}';
factDecl        : 'fact' name? block ;
predDecl        : p='private'? 'pred' ( qname '.')? name arguments? block ;
funDecl         : p='private'? 'fun' ( qname '.')?  name arguments? ':' multiplicity? expr expr;
assertDecl      : 'assert' name? block ;
macroDecl       : p='private'? 'let' name ( '[' names ']' )? '='? expr ;

block           : '{' expr* '}' ;

expr	        : ('~'|'^'|'*') expr                                               								# unaryOpValue
				| expr '\''                                                        								# primeValue
				| expr '.' expr                                                   								# join 
                | expr '[' expr (',' expr)* ']'                                  								# box
				| expr ('<:'|':>') expr                                           								# restrictionValue
				| expr multiplicity? '->' multiplicity? expr                      								# arrowValue
				| expr '&' expr                                                   								# intersectionValue
				| expr '++' expr                                                  								# relationOverrideValue
				| expr ('fun/mul' | 'fun/div' | 'fun/rem') expr													# mulDivRemValue
                | '#' expr                                                         								# cardinalityValue
				| expr ('+' | '-' | 'fun/add' | 'fun/sub') expr                                             	# unionDiffAddSubValue
                | 'sum' decl ( ',' decl )* '|' expr                                								# sumValue		// pg 289
				| '{' decl ( ',' decl )* ( block | ('|' expr) ) '}'              								# comprehensionValue

				| cardinalityConstraint expr                    												# cardinalityConstraintFormula
                | expr ('!' | 'not')? ('in' | '=' | '<' | '>' | '<=' | '=<' | '>=') expr 						# comparisonFormula
                | ('!' | 'not' | 'always' | 'eventually' | 'after' | 'before'| 'historically' | 'once' ) expr  	# unaryFormula
                | expr ( 'until' | 'releases' | 'since' | 'triggered' ) expr            						# binaryFormula
                | expr ('&&' | 'and') expr                                   									# andFormula
				| <assoc=right> expr {inImpliesRHS}? 'else' {inImpliesRHS=false;} expr                          # elseFormula
				| <assoc=right> expr ('implies' | '=>') {inImpliesRHS=true;} expr {inImpliesRHS=false;}         # impliesFormula
                | expr ('<=>' | 'iff') expr                                   									# iffFormula
                | expr ('||'|'or') expr                                       									# orFormula
				| 'let' name '=' expr ( ',' name '=' expr )* ( ('|' expr) | block )  							# letFormula
				| bindingQuantifier decl ( ',' decl )* ( block | ('|' expr) ) 									# bindingQuantifierFormula
                | expr ';' expr                                               									# sequenceFormula

                | '(' expr ')'                                                     	# parenthesisValue                
                | '{' expr '}'                                                     	# parenthesisValue                
                | block                                                            	# blockFormula

                | '@' name                                                         	# atnameValue
                | qname '$'                                                        	# metaValue 
                | number                                                           	# numberValue
				| qname                                                            	# qnameValue
				| 'this'															# thisValue
				| ('none' | 'univ' | 'iden' | 'fun/min' | 'fun/max' 
						| 'fun/next' | STRING_LITERAL)								# constValue	 // exprConstant
				| ('pred/totalOrder' | 'disj')										# listFormula      // exprList
                ;

// x: lone S in declarations
cardinality     : 'lone' | 'one' | 'some' | 'set' ; // LONEOF, ONEOF, SOMEOF, SETOF
decl            : disj? names ':' disj? cardinality? expr  ;
varDecl         : var? decl ;
arguments       : '(' ( decl ( ',' decl )* ','? )? ')'
                | '[' ( decl ( ',' decl )* ','? )? ']'
                ;

// no S means is the relation S empty
cardinalityConstraint		: 'lone' | 'one' | 'some' | 'no' ; 

// some x: e | F means is F true for some binding of the variable x
bindingQuantifier		: 'lone' | 'one' | 'some' | 'no' | 'all' ; 

multiplicity    : 'lone' | 'one' | 'some' |  'set' ;

qualifier       : 'var' | 'abstract' | 'private'| 'lone' | 'one' | 'some' ;

number          : ('-'|'+')? NUMBER ;

disj            : 'disj';
var             : 'var';
check           : 'check' ;
run             : 'run' ;
moduleArg       : ('exactly'? name ) ;

qname           : ID | QNAME;
qnames          : qname ( ',' qname )* ;
name            : ID;
names          	: name ( ',' name )* ;



// COMMANDS 
            
labelCommandDecl: label? commandDecl ;
commandDecl     : (check | run) name? ( qname | block ) scope? ('expect' number)? ( '=>' commandDecl )?;
label           : name ':' ;
scope           : 'for' number ( 'but' typescope ( ',' typescope )* )* 
                | 'for' typescope ( ',' typescope )*
                ;
typescope       : exactly? number ('..' (number (':' number)?)?)? qname ;
exactly         : 'exactly' ;

// LEXER 

ID              : [\p{L}\p{Lo}_%][\p{L}\p{Lo}_"0-9%]*;
PRIMITIVE       : ('fun'|'ord'|'seq') '/' ID ;
QNAME           : ID ( '/' ID )* ;
NUMBER          : [0-9]+ | '0x' [0-9A-Fa-f]+ | '0b' [10]+ ;
STRING_LITERAL  : '"' ( ~["\\] | '\\' . )* '"' ;
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
OPTION_COMMENT  : '--'~[0-9] ~[\r\n]* -> skip ; // 1--1 is not a comment
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;
