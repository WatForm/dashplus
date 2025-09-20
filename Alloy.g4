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
                | factDecl 
				| funDecl
                | predDecl 
                | assertDecl
                | labelCommandDecl
                ;

moduleDecl      : 'module' qname ( '[' moduleArg (',' moduleArg)* ']' )? ;
importDecl      : p='private'? 'open' qname ( '[' qnames ']' )? ( 'as' name )? ;
sigDecl         : qualifier* 'sig' names ('extends' extend=qname | 'in' qname ( '+' qname )*)? '{' (varDecl ( ',' varDecl )*)? '}' block? ;
enumDecl        : p='private'? 'enum' name '{' names '}';
factDecl        : 'fact' name? block ;
predDecl        : p='private'? 'pred' ( qname '.')? name arguments? block ;
funDecl         : p='private'? 'fun' ( qname '.')?  name arguments? ':' multiplicity? expr expr;
assertDecl      : 'assert' name? block ;
macroDecl       : p='private'? 'let' name ( '[' names ']' )? '='? expr ;

block           : '{' expr* '}' ;

expr			: expr '\''                                                        # primeValue
	          	| ('~'|'^'|'*') expr                                               # unaryOpValue
                | expr '.' expr                                                   # join 
                | expr '[' expr (',' expr)* ']'                                  # box
                | expr ('<:'|':>') expr                                           # restrictionValue
                | expr multiplicity? '->' multiplicity? expr                      # arrowValue
                | expr '&' expr                                                   # intersectionValue
                | expr '++' expr                                                  # relationOverrideValue
                | '#' expr                                                         # cardinalityValue
                | expr ('+'|'-') expr                                             # unionDifferenceValue

                | '{' decl ( ',' decl )* ( block | ('|' expr) ) '}'              # comprehensionValue
                | 'sum' decl ( ',' decl )* '|' expr                                # sumValue        // pg 289
                | expr qname expr                                                 # primitiveValue
                | '(' expr ')'                                                     # parenthesisValue                
                | '{' expr '}'                                                     # parenthesisValue                

                | '@' name                                                          # atnameValue
                | qname '$'                                                         # metaValue 
                | number                                                            # numberValue
				| qname                                                             # qnameValue
				| ('plus' | 'minus' | 'mul' | 'div' | 'rem')						# arithmaticOpValue
				| 'this'															# thisValue
				| ('none' | 'univ' | 'iden')										# constValue

				| countingQuantifier expr                    														# countingQuantifierFormula
                | expr ('!' | 'not')? ('in' | '=' | '<' | '>' | '<=' | '=<' | '>=') expr 							# comparisonFormula

                | ('!' | 'not' | 'always' | 'eventually' | 'after' | 'before'| 'historically' | 'once' ) expr  	# unaryFormula
                | expr ('&&' | 'and') expr                                   									# andFormula
                | expr ( 'releases' | 'since' | 'triggered' ) expr            								# binaryFormula
				| <assoc=right> expr {inImpliesRHS}? 'else' {inImpliesRHS=false;} expr                                      {System.out.println("else");}                   # elseFormula
				| <assoc=right> expr ('implies' | '=>') {inImpliesRHS=true;} expr {inImpliesRHS=false;}  {System.out.println("implies");}                                                                # impliesFormula
                | expr ('<=>' | 'iff') expr                                   								# iffFormula
                | expr ('||'|'or') expr                                       		{System.out.println("or");}						# orFormula
				| 'let' name '=' expr ( ',' name '=' expr )* ( ('|' expr) | block )  							# letFormula
				| bindingQuantifier decl ( ',' decl )* ( block | ('|' expr) ) 									# bindingQuantifierFormula

                | expr ';' expr                                               								# sequenceFormula
                | '(' expr ')'                                                   								# parenthesisFormula
                | block                                                             								# blockFormula

				// | <assoc=right> formula ('implies' | '=>') formula ('else' formula)?					{System.out.println("else");}			# impliesFormula
				// | <assoc=right> formula {inImpliesRHS}? 'else' {inImpliesRHS=false;} formula {isElse=true;}                                     {System.out.println("else");}                   # elseFormula
				// | <assoc=right> formula {!isElse}? {isElse=false;} ('implies' | '=>') {inImpliesRHS=true;} formula {inImpliesRHS=false; isElse=false;}  {System.out.println("implies");}                                                                # impliesFormula
                ;

// x: lone S in declarations
cardinality     : 'lone' | 'one' | 'some' | 'set' ; // LONEOF, ONEOF, SOMEOF, SETOF
decl            : disj? names ':' disj? cardinality? expr  ;
varDecl         : var? decl ;
arguments       : '(' ( decl ( ',' decl )* )? ')'
                | '[' ( decl ( ',' decl )* )? ']'
                ;

// no S means is the relation S empty
countingQuantifier		: 'lone' | 'one' | 'some' | 'no' ; 

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
names          : name ( ',' name )* ;



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
