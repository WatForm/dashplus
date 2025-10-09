grammar Alloy;

@header {
	package antlr.generated;
}

@parser::members {
	// for handling ITE as two binop exprs
	// private final java.util.Deque<Boolean> _inImpliesRHSStack = new java.util.ArrayDeque<>();
	// {_inImpliesRHSStack.push(Boolean.FALSE);}
	// private boolean inImpliesRHS() { return _inImpliesRHSStack.peek(); }
	// private void pushImpliesRHS(boolean v) { _inImpliesRHSStack.push(v); }
	// private void popImpliesRHS() { _inImpliesRHSStack.pop(); }


	// for accepting MINUS before NUMBER
	boolean prevTokenIsAllowed() {
		// see bottom of CompFilter
		int[] blacklistTokens = {
			RPAREN, RBRACK, RBRACE, DISJ, PRED_TOTALORDER,
			INT, SUM, ID, NUMBER, STRING, IDEN, THIS,
			FUNMIN, FUNMAX, FUNNEXT, UNIV, SIGINT, NONE
		};
		Token prev = _input.LT(-1);
		int prevType = prev.getType();
		return java.util.Arrays.stream(blacklistTokens).anyMatch(t -> t != prevType);
	}
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



modulePara      : MODULE qname ( LBRACK moduleArg (COMMA moduleArg)* RBRACK )? ;
moduleArg       : (EXACTLY? name ) ;

importPara      : PRIVATE? OPEN qname ( LBRACK sigRefs? RBRACK )? ( AS name )? ;

sigPara         : sigQualifier* SIG names sigIn? LBRACE COMMA? (varDecl ( COMMA* varDecl )*)? COMMA? RBRACE block? ;
varDecl         : VAR? PRIVATE? decl 
				| PRIVATE? DISJ? names EQUAL DISJ? expr1
				;
sigQualifier    : VAR | ABSTRACT | PRIVATE | LONE | ONE | SOME ;
sigIn			: EXTENDS sigRef 					# extendSigIn
				| IN sigRef (PLUS sigRef)* 			# inSigIn
				| EQUAL sigRef (PLUS sigRef)* 		# equalSigIn
				;
sigRef			: (qname | UNIV | STRING | STEPS | SIGINT | SEQ_INT | NONE) ;
sigRefs			: sigRef (COMMA sigRef)* ;

enumPara        : PRIVATE? ENUM name LBRACE names RBRACE;

factPara        : FACT (name | STRING_LITERAL)? block ;

predPara        : PRIVATE? PRED ( sigRef DOT)? name arguments? block ;

funPara         : PRIVATE? FUN ( sigRef DOT)?  name arguments? COLON multiplicity? expr1 expr1;
arguments       : LPAREN ( decl ( COMMA decl )* COMMA? )? RPAREN
                | LBRACK ( decl ( COMMA decl )* COMMA? )? RBRACK
                ;

assertPara      : ASSERT (name | STRING_LITERAL)? block ;

macroPara       : PRIVATE? LET name ( LBRACK names? RBRACK )? (block | (EQUAL expr1))
				| PRIVATE? LET name ( LPAREN names? RPAREN )? (block | (EQUAL expr1))
				;

labelCommandPara: (name COLON)? commandDecl ;
commandDecl     : (CHECK | RUN) name? ( qname | block ) scope? (EXPECT number)? ( RFATARROW commandDecl )?;
scope           : FOR number ( BUT typescope ( COMMA typescope )* )* 
                | FOR typescope ( COMMA typescope )*
                ;
typescope       : EXACTLY? number (DOT DOT (number (COLON number)?)?)? 
					(qname | SIGINT | INT | SEQ | STRING | STEPS | NONE) ;

// ____________________________________

bind			: LET assignment ( COMMA assignment )* body 														# let
				| (ALL | NO | SOME | LONE | ONE | SUM) decl ( COMMA decl )* body 									# bindingQuantifierExpr
				;

expr1			: bind																				# bindExpr
				| expr1 (IFF_ARR | IFF)  expr1														# iffExpr
				| expr1 (IFF_ARR | IFF)  bind														# iffBindExpr
				| expr1 (OR_BAR | OR) expr1															# orExpr
				| expr1 (OR_BAR | OR) bind															# orBindExpr
				| expr1 SEQUENCE_OP expr1															# stateSeqExpr
				| expr1 SEQUENCE_OP bind															# stateSeqBindExpr
				| impliesExpr																		# impExprOpenOrClose
				;


impliesExpr 	: impliesExprClose																	# impExprCloseFromImplies
    			| impliesExprOpen																	# impExprOpenFromImplies
    			;

impliesExprClose 	: expr2 (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprClose  			# iterCloseExpr
					| expr2 (RFATARROW | IMPLIES) impliesExprClose ELSE bind             			# iteBindCloseExpr
					| expr2																			# expr2FromImpClose
					;

impliesExprOpen 	: expr2 (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprOpen   			# iteOpenExpr
    				| expr2 (RFATARROW | IMPLIES) impliesExpr                            			# impExpr
    				| expr2 (RFATARROW | IMPLIES) bind                                  			# impBindExpr
    				;

expr2			: expr2 PRIME																				# primeExpr
				| (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) expr2 												# transExpr
				| (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) bind												# transBind
				| expr2 DOT (DISJ | PRED_TOTALORDER | INT | SUM) 											# dotBuiltinExpr
		 		| expr2 DOT expr2																			# dotExpr 
		 		| expr2 DOT bind																			# dotBindExpr
				| expr2 LBRACK (expr1 (COMMA expr1)*)? RBRACK 												# bracketExpr
				| (DISJ | PRED_TOTALORDER | INT | SUM) LBRACK (expr1 (COMMA expr1)*)? RBRACK				# bracketBuiltinExpr
				| expr2 RNGRESTR expr2																		# rangExpr
				| expr2 RNGRESTR bind																		# rangBindExpr
				| expr2 DOMRESTR expr2																		# domExpr
				| expr2 DOMRESTR bind																		# domBindExpr
				| expr2 arrow expr2																			# arrowExpr
				| expr2 arrow bind																			# arrowBindExpr
				| expr2 INTERSECTION expr2 																	# intersectExpr
				| expr2 INTERSECTION bind																	# intersectBindExpr
				| expr2 REL_OVERRIDE expr2																	# relationOverrideExpr
				| expr2 REL_OVERRIDE bind																	# relationOverrideBindExpr
				| (CARDINALITY | SUM | INT) expr2 															# numericExpr
				| (CARDINALITY | SUM | INT) bind															# numericBindExpr
				| expr2 (FUNMUL | FUNDIV | FUNREM) expr2													# mulDivRemExpr
				| expr2 (FUNMUL | FUNDIV | FUNREM) bind														# mulDivRemBindExpr
				| expr2 (PLUS | MINUS | FUNADD | FUNSUB) expr2												# plusMinusExpr	
				| expr2 (PLUS | MINUS | FUNADD | FUNSUB) bind												# plusMinusBindExpr
				| expr2 (SHL | SHR | SHA) expr2																# shiftExpr
				| expr2 (SHL | SHR | SHA) bind																# shiftBindExpr
				| (ALL | NO | SOME | LONE | ONE | SET | SEQ ) expr2											# quantifiedExpr
				| expr2 comparison  expr2																	# compExpr
				| (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | HISTORICALLY | ONCE | BEFORE) expr2		# unTempExpr
				| (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | HISTORICALLY | ONCE | BEFORE) bind		# unTempBindExpr
				| expr2 (UNTIL | SINCE | TRIGGERED | RELEASES)  expr2										# binTempExpr
				| expr2 (UNTIL | SINCE | TRIGGERED | RELEASES)  bind										# binTempBindExpr
				| expr2 (AND_AMP | AND)  expr2																# andExpr
				| expr2 (AND_AMP | AND)  bind																# andBindExpr

				| number																					# numberExpr
				| STRING_LITERAL																			# strLiteralExpr
				| IDEN																						# idenExpr
				| THIS																						# thisExpr
				| FUNMIN																					# funMinExpr
				| FUNMAX																					# funMaxExpr
				| FUNNEXT																					# funNextExpr
				| LPAREN expr1 RPAREN																		# parenExpr
				| sigRef																					# sigRefExpr
				| AT name																					# atNameExpr
				| block																						# blockExpr
				| LBRACE decl ( COMMA decl )* body? RBRACE              									# comprehensionExpr
				;

// ____________________________________


block           : LBRACE expr1* RBRACE ;
decl            : DISJ? names COLON DISJ? cardinality? expr1 ;
names          	: name ( COMMA name )* ;

number          : ({prevTokenIsAllowed()}? MINUS)? NUMBER ;
qname           : name 					# simpleQname
				| (ID | THIS) SLASH ID  # qualifiedQname
				;
name            : ID;




assignment		: name EQUAL expr1 ;
body			: block  		# blockBody
				| BAR expr1 # barBody
				;

arrow			: multiplicity? RARROW multiplicity? ;
comparison 		: (NOT_EXCL | NOT)? (IN | EQUAL | LT | GT | LE | EL | GE) ;	

// x: lone S in declarations
cardinality     : LONE | ONE | SOME | SET ; // LONEOF, ONEOF, SOMEOF, SETOF

// no S means is the relation S empty
cardinalityConstraint		: LONE |  ONE | SOME | NO | SET ; 

// some x: e | F means is F true for some binding of the variable x
bindingQuantifier		: LONE | ONE | SOME | NO | ALL ; 

multiplicity    : LONE | ONE | SOME |  SET ;







// LEXER 
MODULE : 'module' ;
OPEN : 'open' ;
AS : 'as' ;
SIG : 'sig' ;
EXTENDS : 'extends' ; 
ENUM : 'enum' ; 
FACT : 'fact' ;
PRED : 'pred' ;
FUN  : 'fun' ;
COLON : ':' ;
CHECK : 'check' ;
RUN   : 'run' ;
EXACTLY : 'exactly' ;
VAR : 'var' ;
ABSTRACT : 'abstract';
PRIVATE : 'private';
ASSERT : 'assert' ;
LET : 'let' ;
FOR : 'for' ;
BUT : 'but' ;
EXPECT : 'expect' ;
BAR: '|' ;
SLASH : '/';


COMMA : ',' ;

LPAREN : '(' ;
RPAREN : ')' ;

LBRACK : '[' ;
RBRACK : ']' ;

LBRACE : '{' ;
RBRACE : '}' ;



DOMRESTR : '<:'  ;
RNGRESTR : ':>'  ;

TRANS : '~'  ;
TRANS_CLOS : '^' ;
REFL_TRANS_CLOS : '*' ;

PRIME : '\'' | '‘' | '’';


DOT : '.' ;

INTERSECTION : '&' ; 

REL_OVERRIDE : '++' ;

FUNMUL : 'fun/mul' ;
FUNDIV : 'fun/div' ;
FUNREM : 'fun/rem' ;

CARDINALITY : '#' ;

PLUS : '+' ; // used as union for expr or PLUS in sigIN
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
STRING		: 'String' ;

FUNMIN : 'fun/min' ;
FUNMAX : 'fun/max' ;
FUNNEXT : 'fun/next' ;

RARROW : '->' ; 

LINE_COMMENT    : '//' ~[\r\n]* -> skip ;
OPTION_COMMENT 	: '--'
				  ( ~[0-9\r\n] ~[\r\n]*           // case: -- followed by text
				  | [\r\n]                         // case: -- alone followed by newline
				  | EOF                             // case: -- at end of file
				  )
				  -> skip
				;

BLOCK_COMMENT   : '/*' .*? '*/' -> skip ;
WS              : [ \t\r\n]+ -> skip ;


ID              : [\p{L}\p{Lo}_%][\p{L}\p{Lo}_"0-9%]*;
NUMBER          : [0-9]+ | '0x' [0-9A-Fa-f]+ | '0b' [10]+ ;
STRING_LITERAL  : '"' ( ~["\\] | '\\' . )* '"' ;

// Previous attemps at Expr
// expr 			: exprNoSeq 
// 				| exprNoSeq SEQUENCE_OP expr
// 				;
//
// bind			: LET assignment ( COMMA assignment )* body 														# let
// 				| (ALL | NO | SOME | LONE | ONE | SUM) decl ( COMMA decl )* body 									# bindingQuantifierFormula
// 				;
//
// exprNoSeq		: orExpr
// 				| bind
// 				;
//
// orExpr 			: orExpr (OR_BAR | OR) orExpr
// 				| orExpr (OR_BAR | OR) bind
// 				| equivExpr
// 				; 
//
// equivExpr 		: equivExpr (IFF_ARR | IFF) equivExpr
// 				| equivExpr (IFF_ARR | IFF) bind
// 				| impliesExpr
// 				;
//
// impliesExpr 	: impliesExprClose
//     			| impliesExprOpen
//     			;
//
// impliesExprClose 	: andExpr (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprClose  
// 					| andExpr (RFATARROW | IMPLIES) impliesExprClose ELSE bind             
// 					| andExpr
// 					;
//
// impliesExprOpen 	: andExpr (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprOpen   
//     				| andExpr (RFATARROW | IMPLIES) impliesExpr                            
//     				| andExpr (RFATARROW | IMPLIES) bind                                  
//     				;
//
// andExpr 		: andExpr (AND_AMP | AND) tempBinary
// 				| andExpr (AND_AMP | AND) bind
// 				| tempBinary
// 				;
//
// tempBinary		: tempBinary (UNTIL | SINCE | TRIGGERED | RELEASES) unaryExpr
// 				| tempBinary (UNTIL | SINCE | TRIGGERED | RELEASES) bind
// 				| unaryExpr
// 				;
//
// unaryExpr		: (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | HISTORICALLY | ONCE | BEFORE) unaryExpr
// 				| (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | HISTORICALLY | ONCE | BEFORE) bind
// 				| compareExpr
// 				;
//
// compareExpr		: compareExpr comparison shiftExpr
// 				| (ALL | NO | SOME | LONE | ONE | SET | SEQ ) shiftExpr
// 				| shiftExpr
// 				;
//
// shiftExpr 		: shiftExpr (SHL | SHR | SHA) unionDiffExpr
// 				| shiftExpr (SHL | SHR | SHA) bind
// 				| unionDiffExpr
// 				;
//
// unionDiffExpr	: unionDiffExpr (PLUS | MINUS | FUNADD | FUNSUB) mulExpr
// 				| unionDiffExpr (PLUS | MINUS | FUNADD | FUNSUB) bind
// 				| mulExpr
// 				;
//
// mulExpr			: mulExpr (FUNMUL | FUNDIV | FUNREM) numUnopExpr
// 				| mulExpr (FUNMUL | FUNDIV | FUNREM) bind
// 				| numUnopExpr
// 				;
//
// numUnopExpr		: (CARDINALITY | SUM | INT) numUnopExpr
// 				| (CARDINALITY | SUM | INT) bind
// 				| overrideExpr
// 				;
//
// overrideExpr	: overrideExpr REL_OVERRIDE intersectExpr
// 				| overrideExpr REL_OVERRIDE bind
// 				| intersectExpr
// 				;
//
// intersectExpr	: intersectExpr INTERSECTION relationExpr
// 				| intersectExpr INTERSECTION bind
// 				| relationExpr
// 				;
//
// relationExpr 	: domainExpr arrow relationExpr
// 				| domainExpr arrow bind
// 				| domainExpr
// 				;
//
// domainExpr		: domainExpr DOMRESTR rangeExpr 
// 				| domainExpr DOMRESTR bind 
// 				| rangeExpr
// 				;
//
// rangeExpr 		: rangeExpr RNGRESTR joinExpr
// 				| rangeExpr RNGRESTR bind
// 				| joinExpr
// 				; 
//
// // cannot be separate like in CUP, b/c indirect left recursion
// joinExpr		: (DISJ | PRED_TOTALORDER | INT | SUM) LBRACK (expr (COMMA expr)*)? RBRACK
// 				| joinExpr LBRACK (expr (COMMA expr)*)? RBRACK 
// 		 		| joinExpr DOT unopExpr 
// 				| joinExpr DOT bind
// 				| joinExpr DOT (DISJ | PRED_TOTALORDER | INT | SUM) 
// 				| unopExpr
// 				;
//
// unopExpr		: (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) unopExpr
// 				| (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) bind
// 				| unopExpr PRIME
// 				| bind PRIME
// 				| baseExpr
// 				;
//
// baseExpr		: number
// 				| STRING_LITERAL
// 				| IDEN
// 				| THIS
// 				| FUNMIN
// 				| FUNMAX
// 				| FUNNEXT
// 				| LPAREN expr RPAREN
// 				| sigRef
// 				| AT name
// 				| block
// 				| LBRACE decl ( COMMA decl )* body? RBRACE              											
// 				;
//

// ____________________________________

// expr	        : (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) expr                                               		# unaryOpValue
// 				| expr PRIME                                                        								# primeValue // exprVar
// 				| expr DOT expr                                                   									# dotJoin 
//                 | expr LBRACK (expr (COMMA expr)*)? RBRACK                                  						# boxJoin
// 				| expr (DOMRESTR | RNGRESTR) expr                                           						# restrictionValue
// 				| expr arrow expr                      																# arrowValue
// 				| expr INTERSECTION expr                                                   							# intersectionValue
// 				| expr REL_OVERRIDE expr                                                  							# relationOverrideValue
// 				| expr (FUNMUL | FUNDIV | FUNREM) expr																# mulDivRemValue
//                 | CARDINALITY expr                                                         							# cardinalityValue
// 				| expr (PLUS | MINUS | FUNADD | FUNSUB) expr                                             			# unionDiffAddSubValue
// 				| expr (SHL | SHR | SHA) expr 																		# bitShiftValue
//                 | SUM decl ( COMMA decl )* body                                										# sumValue		// pg 289
// 				| LBRACE decl ( COMMA decl )* body? RBRACE              											# comprehensionValue
//
// 				| SEQ expr																							# seqValue
// 				| INT expr																							# castToSigIntValue
//
// 				| cardinalityConstraint expr                    													# cardinalityConstraintFormula
//                 | expr comparison expr 																				# comparisonFormula
//                 | (NOT_EXCL | NOT | ALWAYS | EVENTUALLY | AFTER | BEFORE | HISTORICALLY | ONCE) expr  				# unaryFormula
//                 | expr (UNTIL | RELEASES | SINCE | TRIGGERED) expr            										# binaryFormula
//                 | expr (AND_AMP | AND) expr                                   										# andFormula
// 				| <assoc=right> expr {inImpliesRHS()}? ELSE expr                                     				# elseFormula
// 				| <assoc=right> expr (RFATARROW | IMPLIES) {pushImpliesRHS(true);} expr {popImpliesRHS();}      	# impliesFormula
//                 | expr (IFF_ARR | IFF) expr                                   										# iffFormula
//                 | expr (OR_BAR | OR) expr                                       									# orFormula
// 				| LET assignment ( COMMA assignment )* body 														# let
// 				| bindingQuantifier decl ( COMMA decl )* body 														# bindingQuantifierFormula
//                 | expr SEQUENCE_OP expr                                               								# sequenceFormula
//
//                 | LPAREN expr RPAREN                                                     							# parenthesis                
//                 | LBRACE expr RBRACE                                                     							# parenthesis                
//                 | block                                                            									# exprBlock
//
//                 | AT name                                                         									# atnameValue
//                 | qname META                                                        								# metaValue 
// 				| qname                                                            									# qnameValue
// 				| (NONE | UNIV | IDEN | PRED_TOTALORDER | DISJ | SUM |
// 						THIS | INT | SIGINT | STEPS | SEQ_INT | STRING |												
// 						FUNMIN | FUNMAX | FUNNEXT | number | STRING_LITERAL) 										# builtinValue	 // exprConstant and exprVar
//                 ;

