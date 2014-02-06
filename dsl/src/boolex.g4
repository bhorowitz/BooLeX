grammar BooLeX;

module
    : circuitdeclaration* EOF
    ;

circuitdeclaration
    : Circuit Identifier LeftParen identifierList? RightParen (assignment)* outStatement End
    ;

identifierList
    : Identifier
    | identifierList ',' Identifier
    ;

expressionList
    : booleanExpression
    | expressionList ',' booleanExpression
    ;

assignment
    : identifierList Assign booleanExpression
    ;

outStatement : Out expressionList ;

circuitCall
    : Identifier LeftParen identifierList? RightParen
    ;

booleanFactor
    : circuitCall
    | Identifier
    | BooleanValue
    | '(' booleanExpression ')'
    ;

booleanPostNot
    : booleanFactor
    | booleanPostNot PostNot
    ;

booleanPreNot
    : booleanPostNot
    | Not booleanPreNot
    ;

booleanOr
    : booleanPreNot
    | booleanPreNot Or booleanPreNot
    | booleanPreNot Nor booleanFactor
    ;

booleanXor
    : booleanOr
    | booleanOr Xor booleanOr
    | booleanOr XNor booleanOr
    ;

booleanExpression
    : booleanXor
    | booleanXor And booleanXor
    | booleanXor NAnd booleanXor
    ;

Circuit: 'circuit';
Out: 'out' ;
End: 'end' ;
And: 'and' | '*' ;
Or: 'or' | '+' ;
Not: 'not' | '-' ;
PostNot: '\'' ;
Xor: 'xor' | '^' ;
NAnd: 'nand' ;
Nor: 'nor' ;
XNor: 'xnor' ;

BooleanValue
    : 'true'
    | 'false'
    | '1'
    | '0'
    ;

Assign: '=' ;
LeftParen : '(' ;
RightParen : ')' ;
LeftBracket : '[' ;
RightBracket : ']' ;

Identifier
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

Whitespace
    : [ \t]+
      -> skip
    ;

Newline
    : ( '\r' '\n'?
      | '\n'
      ) -> skip
    ;

LineComment
    : '#' ~[\r\n]*
      -> skip
    ;