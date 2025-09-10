grammar Alloy;

alloyFile
    : paragraph*
    ;

paragraph       : moduleDecl
                | importDecl
                | macroDecl
                | sigDecl
                | factDecl 
                | predDecl 
                | assertDecl
                | labelCommandDecl
                ;

moduleDecl      : 'module' qname ( '[' moduleArg (',' moduleArg)* ']' )? ;
importDecl      : p='private'? 'open' qname ( '[' qnames ']' )? ( 'as' name )? ;
// name should be qname according to CUP
sigDecl         : qualifier* 'sig' names ('extends' extend=qname | 'in' qname ( '+' qname )*)? '{' (varDecl ( ',' varDecl )*)? '}' block? ;
enumDecl        : p='private'? 'enum' name '{' names '}';
// names should be qnames according to CUP
factDecl        : 'fact' name? block ;
predDecl        : p='private'? 'pred' ( qname '.')? name arguments? block ;
funDecl         : p='private'? 'fun' ( qname '.')?  name arguments? ':' multiplicity? value value;
assertDecl      : 'assert' name? block ;
macroDecl       : p='private'? 'let' name ( '[' names ']' )? '='? expr ;

value           : value '.' qname ('[' value (',' value)* ']')?                     # boxValue
                | qname '[' value (',' value)* ']'                                  # boxValue
                | qname                                                             # qnameValue
                | ('~'|'^'|'*') value                                               # unaryOpValue
                | value '\''                                                        # primeValue
                | value '.' value                                                   # joinValue
                | value '[' value (',' value)* ']'                                  # boxJoinValue
                | value ('<:'|':>') value                                           # restrictionValue
                | value multiplicity? '->' multiplicity? value                      # arrowValue
                | value '&' value                                                   # intersectionValue
                | value '++' value                                                  # relationOverrideValue
                | '#' value                                                         # cardinalityValue
                | value ('+'|'-') value                                             # unionDifferenceValue
                | '{' decl ( ',' decl )* ( block | bar ) '}'                        # comprehensionValue
                | 'sum' decl ( ',' decl )* '|' value                                # sumValue        
                | value qname value                                                 # primitiveValue
                | '(' value ')'                                                     # parenthesisValue                
                | '{' value '}'                                                     # parenthesisValue                
                | '@' name                                                          # atnameValue
                | qname '$'                                                         # metaValue 
                | number                                                            # numberValue
                ;
                  
implies         : <assoc=right> formula ('=>'|'implies') expr ('else' expr)? ;

formula         : ('no' | 'lone' | 'one' | 'some' | 'set') value                    # multiplicityFormula
                | value ('!' | 'not')? ('in' | '=' | '<' | '>' | '=<' | '>=') value # comparisonFormula
                | ('!' | 'not' | 'always' | 'eventually' | 'after' | 'before'| 'historically' | 'once' ) formula  # unaryFormula
                | formula ( 'releases' | 'since' | 'triggered' ) formula            # binaryFormula
                | formula ('&&' | 'and')? formula                                   # andFormula
                | formula ('<=>' | 'iff') formula                                   # iffFormula
                | formula ('||'|'or') formula                                       # orFormula
                | ( let | quantification )                                          # letOrQuantificationFormula
                | formula ';' formula                                               # sequenceFormula
                | '(' formula ')'                                                   # parenthesisFormula
                | block                                                             # blockFormula
                | ( value '.' )? qname ('[' value ( ',' value )*  ']' )?            # predicateFormula
                ;
    
expr           : '{' expr '}'
                | value
                | formula
                ;

let             : 'let' name '=' value ( ',' name '=' value )* ( block | bar ) ;
quantification  : ('all'|'no'|'lone'|'one'| 'some') decl ( ',' decl )* ( block | bar )  ;

decl            : disj? names ':' disj? multiplicity? value  ;
varDecl         : var? decl ;
arguments       : '(' ( decl ( ',' decl )* )? ')'
                | '[' ( decl ( ',' decl )* )? ']'
                ;

number          : ('-'|'+')? NUMBER ;

multiplicity    : 'lone' | 'one' | 'some' |  'set' ;
qualifier       : 'var' | 'abstract' | 'private'| 'lone' | 'one' | 'some' ;
disj            : 'disj';
var             : 'var';
check           : 'check' ;
run             : 'run' ;
moduleArg       : ('exactly'? name ) ;

qname           : ID | QNAME;
qnames          : qname ( ',' qname )* ;
name            : ID;
names          : name ( ',' name )* ;


block           : '{' formula? '}' ;
bar             : '|' formula ;  


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
