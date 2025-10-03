grammar Alloy;

@header {
	package antlr.generated;
}

@parser::members {
  private final java.util.Deque<Boolean> _inImpliesRHSStack = new java.util.ArrayDeque<>();
  {_inImpliesRHSStack.push(Boolean.FALSE);}
  private boolean inImpliesRHS() { return _inImpliesRHSStack.peek(); }
  private void pushImpliesRHS(boolean v) { _inImpliesRHSStack.push(v); }
  private void popImpliesRHS() { _inImpliesRHSStack.pop(); }
}



alloyFile
    : paragraph*
    ;




paragraph       : modulePara
                | importPara
                | macroPara
                | sigPara
				| enumPara
                | factPara 
				| funPara
                | predPara 
                | assertPara
                | labelCommandPara
                ;



modulePara      : 'module' qname ( '[' moduleArg (',' moduleArg)* ']' )? ;
importPara      : PRIVATE? 'open' qname ( '[' qnames? ']' )? ( 'as' name )? ;
sigPara         : qualifier* 'sig' names ('extends' extend=qname | 'in' qname ( PLUS qname )*)? '{' (varDecl ( ',' varDecl )*)? ','? '}' block? ;
enumPara        : PRIVATE? 'enum' name '{' names '}';
factPara        : 'fact' name? block ;
predPara        : PRIVATE? 'pred' ( qname '.')? name arguments? block ;
funPara         : PRIVATE? 'fun' ( qname '.')?  name arguments? ':' multiplicity? expr expr;
assertPara      : 'assert' name? block ;
macroPara       : PRIVATE? 'let' name ( '[' names ']' )? EQUAL? expr ;
labelCommandPara: (name ':')? commandDecl ;
commandDecl     : (CHECK | RUN) name? ( qname | block ) scope? ('expect' number)? ( RFATARROW commandDecl )?;
scope           : 'for' number ( 'but' typescope ( ',' typescope )* )* 
                | 'for' typescope ( ',' typescope )*
                ;
typescope       : EXACTLY? number ('..' (number (':' number)?)?)? qname ;


block           : '{' expr* '}' ;


expr	        : (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) expr                                               								# unaryOpValue
				| expr PRIME                                                        								# primeValue // exprVar
				| expr DOT expr                                                   								# dotJoin 
                | expr '[' (expr (',' expr)*)? ']'                                  							# boxJoin
				| expr (DOMRESTR | RNGRESTR) expr                                           								# restrictionValue
				| expr arrow expr                      															# arrowValue
				| expr INTERSECTION expr                                                   								# intersectionValue
				| expr REL_OVERRIDE expr                                                  								# relationOverrideValue
				| expr (FUNMUL | FUNDIV | FUNREM) expr													# mulDivRemValue
                | CARDINALITY expr                                                         								# cardinalityValue
				| expr (PLUS | MINUS | FUNADD | FUNSUB) expr                                             	# unionDiffAddSubValue
				| expr (SHL | SHR | SHA) expr 																# bitShiftValue
                | SUM decl ( ',' decl )* (block | ('|' expr))                                					# sumValue		// pg 289
				| '{' decl ( ',' decl )* ( block | ('|' expr) ) '}'              								# comprehensionValue

				| SEQ expr																					# seqValue
				| INT expr																					# castToSigIntValue

				| cardinalityConstraint expr                    												# cardinalityConstraintFormula
                | expr comparison expr 																			# comparisonFormula
                | (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | BEFORE | HISTORICALLY | ONCE) expr  	# unaryFormula
                | expr (UNTIL | RELEASES | SINCE | TRIGGERED) expr            						# binaryFormula
                | expr (AND_AMP | AND) expr                                   									# andFormula
				| <assoc=right> expr {inImpliesRHS()}? ELSE expr                                     			# elseFormula
				| <assoc=right> expr (RFATARROW | IMPLIES) {pushImpliesRHS(true);} expr {popImpliesRHS();}      	# impliesFormula
                | expr (IFF_ARR | IFF) expr                                   									# iffFormula
                | expr (OR_BAR | OR) expr                                       									# orFormula
				| 'let' name EQUAL expr ( ',' name EQUAL expr )* ( ('|' expr) | block )  							# let
				| bindingQuantifier decl ( ',' decl )* ( block | ('|' expr) ) 									# bindingQuantifierFormula
                | expr SEQUENCE_OP expr                                               									# sequenceFormula

                | '(' expr ')'                                                     	# parenthesis                
                | '{' expr '}'                                                     	# parenthesis                
                | block                                                            	# exprBlock

                | AT name                                                         	# atnameValue
                | qname META                                                        	# metaValue 
				| qname                                                            	# qnameValue
				| (NONE | UNIV | IDEN | PRED_TOTALORDER | DISJ
						THIS | INT | SIGINT | STEPS | SEQ_INT)					# varValue	 // exprVar
				| ( FUNMIN | FUNMAX | FUNNEXT | number | STRING_LITERAL )		# constValue	 // exprConstant
                ;

arrow			: multiplicity? RARROW multiplicity? ;
comparison 		: (NOT_EXCL | NOT)? (IN | EQUAL | LT | GT | LE | EL | GE) ;	

// x: lone S in declarations
cardinality     : LONE | ONE | SOME | SET ; // LONEOF, ONEOF, SOMEOF, SETOF
decl            : DISJ? names ':' DISJ? cardinality? expr  ;
varDecl         : VAR? decl ;
arguments       : '(' ( decl ( ',' decl )* ','? )? ')'
                | '[' ( decl ( ',' decl )* ','? )? ']'
                ;

// no S means is the relation S empty
cardinalityConstraint		: LONE |  ONE | SOME | NO ; 

// some x: e | F means is F true for some binding of the variable x
bindingQuantifier		: LONE | ONE | SOME | NO | ALL ; 

multiplicity    : LONE | ONE | SOME |  SET ;

qualifier       : VAR | ABSTRACT | PRIVATE | LONE | ONE | SOME ;

number          : (MINUS | PLUS)? NUMBER ;

moduleArg       : (EXACTLY? name ) ;

qname           : ID | QNAME;
qnames          : qname ( ',' qname )* ;
name            : ID;
names          	: name ( ',' name )* ;







// LEXER 
CHECK           : 'check' ;
RUN             : 'run' ;
EXACTLY 		: 'exactly' ;


VAR : 'var' ;
ABSTRACT : 'abstract';
PRIVATE : 'private';




DOMRESTR : '<:'  ;
RNGRESTR : ':>'  ;

TRANS : '~'  ;
TRANS_CLOS : '^' ;
REFL_TRANS_CLOS : '*' ;

PRIME : '\'' ;

DOT : '.' ;

INTERSECTION : '&' ; 

REL_OVERRIDE : '++' ;

FUNMUL : 'fun/mul' ;
FUNDIV : 'fun/div' ;
FUNREM : 'fun/rem' ;

CARDINALITY : '#' ;

PLUS : '+' ; // used as union for expr and positive for number
MINUS  : '-' ; // used as diff for expr and negative for number
FUNADD : 'fun/add' ;
FUNSUB : 'fun/sub' ; 

SHL : '<<' ;
SHR : '>>>' ; 
SHA : '>>' ;

SUM : 'sum' ; 

SEQ : 'seq' ;

INT : 'int' ; 

IN      : 'in' ;
EQUAL      : '=' ;
LT      : '<' ;
GT      : '>' ;
LE      : '<=' ;
EL      : '=<';
GE      : '>=' ;

LONE : 'lone' ; 
ONE : 'one' ; 
SOME : 'some' ; 
SET : 'set' ; 
NO : 'no' ;
ALL : 'all';


NOT_EXCL : '!' ; 
NOT : 'not' ;
ALWAYS : 'always' ;
EVENTUALLY : 'eventually' ;
AFTER : 'after' ;
BEFORE : 'before' ;
HISTORICALLY : 'historically' ;
ONCE : 'once' ;

UNTIL : 'until' ; 
RELEASES : 'releases' ;
SINCE : 'since' ; 
TRIGGERED : 'triggered' ;

AND_AMP : '&&' ;
AND : 'and' ;

ELSE : 'else' ;

RFATARROW : '=>' ; // used as implies or ITE or in commandDecl
IMPLIES : 'implies';

IFF_ARR : '<=>' ;
IFF : 'iff' ;

OR_BAR : '||' ;
OR : 'or' ;

SEQUENCE_OP : ';' ;

AT : '@' ;
META : '$' ;

NONE       : 'none' ;
UNIV       : 'univ' ;
IDEN       : 'iden' ;
PRED_TOTALORDER : 'pred/totalOrder' ;
DISJ       : 'disj' ;
THIS       : 'this' ;
SIGINT    : 'Int' ;
STEPS      : 'steps' ;
SEQ_INT    : 'seq/Int' ;

FUNMIN : 'fun/min' ;
FUNMAX : 'fun/max' ;
FUNNEXT : 'fun/next' ;

RARROW : '->' ; 



ID              : [\p{L}\p{Lo}_%][\p{L}\p{Lo}_"0-9%]*;
// PRIMITIVE       : ('fun'|'ord'|'seq') '/' ID ;
QNAME           : ID ( '/' ID )* ;
NUMBER          : [0-9]+ | '0x' [0-9A-Fa-f]+ | '0b' [10]+ ;
STRING_LITERAL  : '"' ( ~["\\] | '\\' . )* '"' ;
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
OPTION_COMMENT  : '--'~[0-9] ~[\r\n]* -> skip ; // 1--1 is not a comment
BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;

