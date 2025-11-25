grammar Dash ;

@header {
	package antlr.generated;
}

@lexer::members {
	public boolean dashMode = false; 
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

alloyFile: alloyParagraph* ;

dashParagraph : stateRoot ;

paragraph
	: dashParagraph
	| alloyParagraph
	;

dashFile: paragraph* ;

// ____________________________________
// stateRoot

stateRoot 	: STATE name LBRACE stateItem* RBRACE ;

stateItem 	: ENV? EVENT qnames LBRACE RBRACE									# dashEventDecls
			| ENV? names COLON expr1 											# dashVarDecls
			| names COLON BUF LBRACK (qname | SIGINT) RBRACK					# dashBufferDecls
			| TRANS name LBRACE transItem* RBRACE								# dashTrans
			| INIT block														# dashInit
			| INVARIANT qname? block											# dashInv
			| PRED name LBRACE expr1 RBRACE										# dashPred
			| DEF? STATE name LBRACE stateItem* RBRACE							# dashState
			| DEF? CONC name (LBRACK qname RBRACK)? LBRACE stateItem* RBRACE	# dashConcState
			;

transItem	: ON dashRef1														# dashOnRef1
			| ON dashRef2														# dashOnRef2
			| SEND dashRef1														# dashSendRef1
			| SEND dashRef2														# dashSendRef2
			| WHEN expr1														# dashWhen
			| DO expr1															# dashDo
			| FROM dashRef1 													# dashFromRef1
			| FROM dashRef2 													# dashFromRef2
			| GOTO dashRef1 													# dashGotoRef1
			| GOTO dashRef2 													# dashGotoRef2
			;


// ____________________________________
// Paragraph

alloyParagraph  : modulePara
                | importPara
                | macroPara
                | sigPara
				| enumPara
                | factPara 
				| funPara
                | predPara 
                | assertPara
                | commandPara
                ;

modulePara      : MODULE qname ( LBRACK moduleArg (COMMA moduleArg)* RBRACK )? ;
moduleArg       : (EXACTLY? qname ) ;

importPara      : PRIVATE? OPEN qname ( LBRACK sigRefs? RBRACK )? ( AS qname )? ;

sigPara         : sigQualifier* SIG qnames sigRel? LBRACE COMMA? (decl ( COMMA* decl )*)? COMMA? RBRACE block? ;
sigQualifier    : VAR | ABSTRACT | PRIVATE | LONE | ONE | SOME ;
sigRel			: EXTENDS sigRef 					# extendSigIn
				| IN sigRef (PLUS sigRef)* 			# inSigIn
				| EQUAL sigRef (PLUS sigRef)* 		# equalSigIn
				;
sigRef			: (qname | UNIV | STRING | STEPS | SIGINT | SEQ_INT | NONE) ;
sigRefs			: sigRef (COMMA sigRef)* ;

enumPara        : PRIVATE? ENUM qname LBRACE qnames? RBRACE;

factPara        : FACT (qname | STRING_LITERAL)? block ;

predPara        : PRIVATE? PRED ( sigRef DOT)? qname arguments? block ;

funPara         : PRIVATE? FUN ( sigRef DOT)?  qname arguments? COLON multiplicity? expr1 block;
arguments       : LPAREN ( decls COMMA? )? RPAREN
                | LBRACK ( decls COMMA? )? RBRACK
                ;

assertPara      : ASSERT (qname | STRING_LITERAL)? block ;

macroPara       : PRIVATE? LET qname ( LBRACK qnames? RBRACK )? (block | (EQUAL expr1))
				| PRIVATE? LET qname ( LPAREN qnames? RPAREN )? (block | (EQUAL expr1))
				;

commandPara		: commandDecl (RFATARROW commandDecl)* ;
commandDecl     : (CHECK | RUN) qname? ( qname | block ) scope? (EXPECT number)? ;
scope           : FOR number ( BUT typescope ( COMMA typescope )* )* 
                | FOR typescope ( COMMA typescope )*
                ;
typescope       : EXACTLY? number (DOT DOT (number (COLON number)?)?)? 
					(qname | SIGINT | INT | SEQ | STRING | STEPS) ;

// ____________________________________
// Expr

bind			: LET assignment ( COMMA assignment )* body 										# let
				| (ALL | NO | SOME | LONE | ONE | SUM) decls body 									# quantificationExpr
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

dashRef1			: (name SLASH)* name LBRACK (expr1 COMMA)* expr1 RBRACK SLASH name ;
dashRef2			: (name SLASH)* name ;

baseExpr		: {((DashLexer)this._input.getTokenSource()).dashMode}? dashRef1							# dashRef1Expr
				| number																					# numberExpr
				| STRING_LITERAL																			# strLiteralExpr
				| IDEN																						# idenExpr
				| THIS																						# thisExpr
				| FUNMIN																					# funMinExpr
				| FUNMAX																					# funMaxExpr
				| FUNNEXT																					# funNextExpr
				| LPAREN expr1 RPAREN																		# parenExpr
				| sigRef																					# sigRefExpr
				| AT qname																					# atNameExpr
				| block																						# blockExpr
				| LBRACE declMul (COMMA declMul)* body? RBRACE              								# comprehensionExpr
				;

transExpr		: (TRANSPOSE | TRANS_CLOS | REFL_TRANS_CLOS) (transExpr | baseExpr | bind) ;											

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
decl            : declMul
				| declExact
				;
declMul			: VAR? PRIVATE? DISJ? qnames COLON DISJ? (LONE | ONE | SOME | SET)? expr1 ; // LONEOF, ONEOF, SOMEOF, SETOF
declExact		: PRIVATE? qnames EQUAL expr1 ; // EXACTLYOF
decls			: decl (COMMA decl)* ;



// ____________________________________
// Expr Helpers

number          : ({prevTokenIsAllowed()}? MINUS)? NUMBER ;
qname           : name 											# simpleQname
				| (SEQ | THIS | name) SLASH name (SLASH name)* 	# qualifiedQname
				;
qnames          : qname ( COMMA qname )* ;
name            : ID;
names 			: name ( COMMA name )* ;


assignment		: qname EQUAL expr1 ;
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

// ____________________________________
// Dash

STATE           : { this.dashMode }? 'state'      ;
ENV             : { this.dashMode }? 'env'        ;
EVENT           : { this.dashMode }? 'event'      ;
BUF          	: { this.dashMode }? 'buf'     	  ;
TRANS           : { this.dashMode }? 'trans'      ;
ENTER           : { this.dashMode }? 'enter'      ;
EXIT           	: { this.dashMode }? 'exit'       ;
INIT            : { this.dashMode }? 'init'       ;
INVARIANT       : { this.dashMode }? 'invariant'  ;
DEF             : { this.dashMode }? 'default'    ;
CONC            : { this.dashMode }? 'conc'       ;
ON              : { this.dashMode }? 'on'         ;
SEND            : { this.dashMode }? 'send'       ;
WHEN            : { this.dashMode }? 'when'       ;
DO              : { this.dashMode }? 'do'         ;
FROM            : { this.dashMode }? 'from'       ;
GOTO            : { this.dashMode }? 'goto'       ;


// ____________________________________
// Alloy

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

TRANSPOSE : '~'  ;
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
DISJ       : 'disj' | 'disjoint' ;
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

