grammar Alloy;


@parser::members {
    boolean inImpliesRHS = false;
	// boolean isElse = false;
	// boolean isITE = false;
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
funDecl         : p='private'? 'fun' ( qname '.')?  name arguments? ':' multiplicity? value value;
assertDecl      : 'assert' name? block ;
macroDecl       : p='private'? 'let' name ( '[' names ']' )? '='? expr ;


block           : '{' formula* '}' ;

expr           : '{' expr '}'
                | value
                | formula
                ;

formula			: bindingQuantifier decl ( ',' decl )* ( block | ('|' formula) ) 									# bindingQuantifierFormula 
                | value ('[' value (',' value)* ']')?                                  								# boxFormula
				| value '.' value																					# joinFormula
                | value ('!' | 'not')? ('in' | '=' | '<' | '>' | '<=' | '=<' | '>=') value 							# comparisonFormula


				// Operations 
                | ('!' | 'not' | 'always' | 'eventually' | 'after' | 'before'| 'historically' | 'once' ) formula  	# unaryFormula
                | formula ('&&' | 'and') formula                                   									# andFormula
                | formula ( 'releases' | 'since' | 'triggered' ) formula            								# binaryFormula
				| <assoc=right> formula {inImpliesRHS}? 'else' {inImpliesRHS=false;} formula                                      {System.out.println("else");}                   # elseFormula
				| <assoc=right> formula ('implies' | '=>') {inImpliesRHS=true;} formula {inImpliesRHS=false;}  {System.out.println("implies");}                                                                # impliesFormula
                | formula ('<=>' | 'iff') formula                                   								# iffFormula
                | formula ('||'|'or') formula                                       		{System.out.println("or");}						# orFormula
				| 'let' name '=' value ( ',' name '=' value )* ( ('|' formula) | block )  							# letFormula
				| countingQuantifier value                    														# countingQuantifierFormula

                | formula ';' formula                                               								# sequenceFormula
                | '(' formula ')'                                                   								# parenthesisFormula
                | block                                                             								# blockFormula
				// | <assoc=right> formula ('implies' | '=>') formula ('else' formula)?					{System.out.println("else");}			# impliesFormula
				// | <assoc=right> formula {inImpliesRHS}? 'else' {inImpliesRHS=false;} formula {isElse=true;}                                     {System.out.println("else");}                   # elseFormula
				// | <assoc=right> formula {!isElse}? {isElse=false;} ('implies' | '=>') {inImpliesRHS=true;} formula {inImpliesRHS=false; isElse=false;}  {System.out.println("implies");}                                                                # impliesFormula
                ;
				
    
// Relational or Integer Expr

value           : ('~'|'^'|'*') value                                               # unaryOpValue
                | value '\''                                                        # primeValue
                | value '.' value                                                   # joinValue // definitly need this
                | value '[' value (',' value)* ']'                                  # boxJoinValue // keep
                | value ('<:'|':>') value                                           # restrictionValue
                | value multiplicity? '->' multiplicity? value                      # arrowValue
                | value '&' value                                                   # intersectionValue
                | value '++' value                                                  # relationOverrideValue
                | '#' value                                                         # cardinalityValue
                | value ('+'|'-') value                                             # unionDifferenceValue
                | '{' decl ( ',' decl )* ( block | ('|' formula) ) '}'              # comprehensionValue
                | 'sum' decl ( ',' decl )* '|' value                                # sumValue        
                | value qname value                                                 # primitiveValue
                | '(' value ')'                                                     # parenthesisValue                
                | '{' value '}'                                                     # parenthesisValue                
                | '@' name                                                          # atnameValue
                | qname '$'                                                         # metaValue 
                | number                                                            # numberValue
				| (qname | arithmatic)                                                             # qnameValue
				| 'let' name '=' value ( ',' name '=' value )* ( ('|' value) | ('{' value '}') )  #letValue
                ;

arithmatic 		:  ('plus' | 'minus' | 'mul' | 'div' | 'rem') ;	

conditional 	: <assoc=right> formula ('=>'|'implies') value 'else' value ;	
                  
let             : 'let' name '=' value ( ',' name '=' value )* ( block | ('|' formula) ) ;

// x: lone S in declarations
cardinality     : 'lone' | 'one' | 'some' | 'set' ; // LONEOF, ONEOF, SOMEOF, SETOF
decl            : disj? names ':' disj? cardinality? value  ;
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
