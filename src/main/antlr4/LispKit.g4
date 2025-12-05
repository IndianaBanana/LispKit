grammar LispKit;

@parser::header {
    package org.banana.grammar;
}

@lexer::header {
    package org.banana.grammar;
}

// ----------------------
// Program
// ----------------------
program : sExpr EOF ;

// ----------------------
// S-expressions
// ----------------------
sExpr
    : atom
    | list
    ;

list : LPAREN sExpr* RPAREN ;

// ----------------------
// Atoms
// ----------------------
atom
    : NUMBER
    | symbolic_atom
    ;

symbolic_atom
    : QUOTE | CAR | CDR | CONS | ATOM
    | ADD | SUB | MUL | DIV | REM | LEQ | EQUAL
    | COND | LAMBDA | LET | LETREC
    | IDENT

    ;

// ----------------------
// Lexer (case-insensitive keywords)
// ----------------------
QUOTE   : [Qq][Uu][Oo][Tt][Ee];
CAR     : [Cc][Aa][Rr];
CDR     : [Cc][Dd][Rr];
CONS    : [Cc][Oo][Nn][Ss];
ATOM    : [Aa][Tt][Oo][Mm];

ADD     : [Aa][Dd][Dd];
SUB     : [Ss][Uu][Bb];
MUL     : [Mm][Uu][Ll];
DIV     : [Dd][Ii][Vv];
REM     : [Rr][Ee][Mm];
LEQ     : [Ll][Ee][Qq];
EQUAL   : [Ee][Qq][Uu][Aa][Ll];

COND    : [Cc][Oo][Nn][Dd];
LAMBDA  : [Ll][Aa][Mm][Bb][Dd][Aa];
LET     : [Ll][Ee][Tt];
LETREC  : [Ll][Ee][Tt][Rr][Ee][Cc];

LPAREN  : '(' ;
RPAREN  : ')' ;

NUMBER  : '-'? [0-9]+ ;

IDENT   : [A-Za-z][A-Za-z0-9]* ;

WS      : [ \t\r\n]+ -> skip ;