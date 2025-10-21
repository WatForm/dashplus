grammar Alloy;

@header {
	package antlr.generated;
}

@parser::members {
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

// ____________________________________

alloyFile
    : paragraph*
    ;


// ____________________________________
// Paragraph

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

enumPara        : PRIVATE? ENUM name LBRACE names? RBRACE;

factPara        : FACT (name | STRING_LITERAL)? block ;

predPara        : PRIVATE? PRED ( sigRef DOT)? name arguments? block ;

funPara         : PRIVATE? FUN ( sigRef DOT)?  name arguments? COLON multiplicity? expr1 block;
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
// Expr

bind			: LET assignment ( COMMA assignment )* body 														# let
				| (ALL | NO | SOME | LONE | ONE | SUM) decl ( COMMA decl )* body 									# quantificationExpr
				;

expr1			: bind																				# bindExpr
				| expr1 (IFF_ARR | IFF)  expr1														# iffExpr
				| expr1 (IFF_ARR | IFF)  bind														# iffBindExpr
				| expr1 (OR_BAR | OR) expr1															# orExpr
				| expr1 (OR_BAR | OR) bind															# orBindExpr
				| <assoc=right> expr1 SEQUENCE_OP expr1												# stateSeqExpr
				| expr1 SEQUENCE_OP bind															# stateSeqBindExpr
				| impliesExpr																		# impExprOpenOrClose
				;


impliesExpr 	: impliesExprClose																	# impExprCloseFromImplies
    			| impliesExprOpen																	# impExprOpenFromImplies
    			;

impliesExprClose 	: expr2 (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprClose  			# iteCloseExpr
					| expr2 (RFATARROW | IMPLIES) impliesExprClose ELSE bind             			# iteBindCloseExpr
					| expr2																			# expr2FromImpClose
					;

impliesExprOpen 	: expr2 (RFATARROW | IMPLIES) impliesExprClose ELSE impliesExprOpen   			# iteOpenExpr
    				| expr2 (RFATARROW | IMPLIES) impliesExpr                            			# impExpr
    				| expr2 (RFATARROW | IMPLIES) bind                                  			# impBindExpr
    				;

baseExpr		: number																					# numberExpr
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

transExpr		: (TRANS | TRANS_CLOS | REFL_TRANS_CLOS) (transExpr | baseExpr | bind) ;											

primeExpr		: primeExpr PRIME																			
				| (baseExpr | transExpr | bind) PRIME													
				;

expr2			: baseExpr																					# baseExprFromExpr2
				| transExpr																					# transExprFromExpr2
				| primeExpr																					# primeExprFromExpr2
				| expr2 DOT (DISJ | PRED_TOTALORDER | INT | SUM | baseExpr | transExpr | primeExpr | bind) 	# dotExpr
				| expr2 LBRACK (expr1 (COMMA expr1)*)? RBRACK 												# bracketExpr
				| (DISJ | PRED_TOTALORDER | INT | SUM) LBRACK (expr1 (COMMA expr1)*)? RBRACK				# bracketBuiltinExpr
				| expr2 RNGRESTR expr2																		# rangExpr
				| expr2 RNGRESTR bind																		# rangBindExpr
				| expr2 DOMRESTR expr2																		# domExpr
				| expr2 DOMRESTR bind																		# domBindExpr
				| <assoc=right> expr2 arrow expr2															# arrowExpr
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
				;

// ____________________________________
// Misc

block           : LBRACE expr1* RBRACE ;
decl            : DISJ? names COLON DISJ? (LONE | ONE | SOME | SET)? expr1 ;// LONEOF, ONEOF, SOMEOF, SETOF
names          	: name ( COMMA name )* ;



// ____________________________________
// Expr Helpers

number          : ({prevTokenIsAllowed()}? MINUS)? NUMBER ;
qname           : name 										# simpleQname
				| (SEQ | THIS | name) SLASH name (SLASH name)* 	# qualifiedQname
				;
name            : ID;


assignment		: name EQUAL expr1 ;
body			: block  		# blockBody
				| BAR expr1 # barBody
				;

arrow			: multiplicity? RARROW multiplicity? ;
comparison 		: (NOT_EXCL | NOT)? (IN | EQUAL | LT | GT | LE | EL | GE) ;	

// x: lone S in declarations
// cardinality     : LONE | ONE | SOME | SET ; // LONEOF, ONEOF, SOMEOF, SETOF

// no S means is the relation S empty
// cardinalityConstraint		: LONE |  ONE | SOME | NO | SET ; 

// some x: e | F means is F true for some binding of the variable x
// bindingQuantifier		: LONE | ONE | SOME | NO | ALL ; 

multiplicity    : LONE | ONE | SOME |  SET ;



// ____________________________________
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


DOT : '.' | '::' ; // see Alloy.lex

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

// ____________________________________

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

// ____________________________________

ID              : [\p{L}\p{Lo}_%][\p{L}\p{Lo}_"0-9%]*;
NUMBER          : [0-9]+ | '0x' [0-9A-Fa-f]+ | '0b' [10]+ ;
STRING_LITERAL  : '"' ( ~["\\] | '\\' . )* '"' ;

