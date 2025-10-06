grammar Alloy;

@header {
	package antlr.generated;
}

@parser::members {
	// for ITE
	private final java.util.Deque<Boolean> _inImpliesRHSStack = new java.util.ArrayDeque<>();
	{_inImpliesRHSStack.push(Boolean.FALSE);}
	private boolean inImpliesRHS() { return _inImpliesRHSStack.peek(); }
	private void pushImpliesRHS(boolean v) { _inImpliesRHSStack.push(v); }
	private void popImpliesRHS() { _inImpliesRHSStack.pop(); }


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

importPara      : PRIVATE? OPEN qname ( LBRACK sigRefs? RBRACK )? ( AS name )? ;

sigPara         : sigQualifier* SIG names sigIn? LBRACE COMMA? (varDecl ( COMMA* varDecl )*)? COMMA? RBRACE block? ;
varDecl         : VAR? PRIVATE? decl 
				| PRIVATE? DISJ? names EQUAL DISJ? expr
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

funPara         : PRIVATE? FUN ( sigRef DOT)?  name arguments? COLON multiplicity? expr expr;
arguments       : LPAREN ( decl ( COMMA decl )* COMMA? )? RPAREN
                | LBRACK ( decl ( COMMA decl )* COMMA? )? RBRACK
                ;

assertPara      : ASSERT (name | STRING_LITERAL)? block ;

macroPara       : PRIVATE? LET name ( LBRACK names? RBRACK )? (block | (EQUAL expr))
				| PRIVATE? LET name ( LPAREN names? RPAREN )? (block | (EQUAL expr))
				;


labelCommandPara: (name COLON)? commandDecl ;
commandDecl     : (CHECK | RUN) name? ( qname | block ) scope? (EXPECT number)? ( RFATARROW commandDecl )?;
scope           : FOR number ( BUT typescope ( COMMA typescope )* )* 
                | FOR typescope ( COMMA typescope )*
                ;
typescope       : EXACTLY? number (DOT DOT (number (COLON number)?)?)? 
					(qname | SIGINT | INT | SEQ | STRING | STEPS | NONE) ;




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
                | SUM decl ( COMMA decl )* (block | (BAR expr))                                						# sumValue		// pg 289
				| LBRACE decl ( COMMA decl )* ( block | (BAR expr) )? RBRACE              							# comprehensionValue

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
				| LET name EQUAL expr ( COMMA name EQUAL expr )* ( (BAR expr) | block )  							# let
				| bindingQuantifier decl ( COMMA decl )* ( block | (BAR expr) ) 									# bindingQuantifierFormula
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

block           : LBRACE expr* RBRACE ;

arrow			: multiplicity? RARROW multiplicity? ;
comparison 		: (NOT_EXCL | NOT)? (IN | EQUAL | LT | GT | LE | EL | GE) ;	

// x: lone S in declarations
cardinality     : LONE | ONE | SOME | SET ; // LONEOF, ONEOF, SOMEOF, SETOF
decl            : DISJ? names COLON DISJ? cardinality? expr  ;

// no S means is the relation S empty
cardinalityConstraint		: LONE |  ONE | SOME | NO | SET ; 

// some x: e | F means is F true for some binding of the variable x
bindingQuantifier		: LONE | ONE | SOME | NO | ALL ; 

multiplicity    : LONE | ONE | SOME |  SET ;


number          : ({prevTokenIsAllowed()}? MINUS)? NUMBER ;

moduleArg       : (EXACTLY? name ) ;

qname           : ID | ((ID | THIS) SLASH ID);
qnames          : qname ( COMMA qname )* ;
name            : ID;
names          	: name ( COMMA name )* ;





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

