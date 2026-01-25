grammar DroneLanguagePlugin;

/*
 * Parser Rules
 */

program: variablesBlock? instructionsBlock EOF;

variablesBlock: variableDecl+;

variableDecl: POINT variableName EQUALS (vectorExpression) SEMICOLON #instancePoint |
            POINT variableName EQUALS (arrayVectorExpression) SEMICOLON #instanceArrayPoint |
            VECTOR variableName EQUALS (vectorExpression) SEMICOLON #instanceVector |
            LINEARVELOCITY variableName EQUALS (numberExpression) SEMICOLON #instanceLinVelocity |
            ANGULARVELOCITY variableName EQUALS (numberExpression) SEMICOLON #instanceAngVelocity |
            DISTANCE variableName EQUALS (numberExpression) SEMICOLON #instanceDistance |
            TIME variableName EQUALS (numberExpression) SEMICOLON #instanceTime ;

instructionsBlock: instruction+;

instruction
    : TAKEOFF LEFTPARENTHESIS (numberExpression) COMMA (numberExpression) RIGHTPARENTHESIS SEMICOLON #instanceTakeOff
    | LAND LEFTPARENTHESIS (numberExpression) RIGHTPARENTHESIS SEMICOLON # instanceLand
    | MOVE LEFTPARENTHESIS (vectorExpression) COMMA (numberExpression) RIGHTPARENTHESIS SEMICOLON # instanceMove1
    | MOVE LEFTPARENTHESIS (vectorExpression) COMMA (numberExpression) COMMA (numberExpression) RIGHTPARENTHESIS SEMICOLON #instanceMove2
    | MOVEPATH LEFTPARENTHESIS (arrayVectorExpression) COMMA (numberExpression) RIGHTPARENTHESIS SEMICOLON #instanceMovePath
    | MOVECIRCLE LEFTPARENTHESIS (vectorExpression) COMMA (numberExpression) COMMA (numberExpression) RIGHTPARENTHESIS SEMICOLON #instanceMoveCircle
    | HOOVER LEFTPARENTHESIS (numberExpression) RIGHTPARENTHESIS SEMICOLON #instanceHoover
    | LIGHTSON (LEFTPARENTHESIS COLORNAME RIGHTPARENTHESIS)? SEMICOLON #instanceLightsOn
    | LIGHTSOFF LEFTPARENTHESIS RIGHTPARENTHESIS SEMICOLON #instanceLightOff
    | BLINK LEFTPARENTHESIS (numberExpression) RIGHTPARENTHESIS SEMICOLON #instanceBlink ;

vectorExpression: vectorExpression op=(MULT | DIV) vectorExpression # mulDivVec
                | vectorExpression op=(PLUS | MINUS) vectorExpression # addSubVec
                | vector #instanceVectorExp
                | variableName #instanceVarVector;
vector: LEFTPARENTHESIS numberExpression COMMA numberExpression COMMA numberExpression RIGHTPARENTHESIS;

numberExpression: numberExpression op=(MULT | DIV) numberExpression # mulDivNum
                | numberExpression op=(PLUS | MINUS) numberExpression # addSubNum
                | numberVar #intanceNumber
                | variableName #instanceVarNumber;

numberVar: (op=(PLUS|MINUS))? (NUMBER | PI);

arrayVectorExpression: LEFTPARENTHESIS arrayVector RIGHTPARENTHESIS #instanceArrayVector
                        | variableName #instanceArrayVarVector;
arrayVector: vectorExpression (COMMA vectorExpression)*;

variableName: IDENTIFIER;

/*
 * Lexer Rules
 */

fragment LETTER    : [a-zà-ÿ] | [A-ZÀ-Ý] ;
fragment DIGIT     : [0-9] ;

WS: [ \t\r\n]+ -> skip;

NUMBER: DIGIT+ ('.' DIGIT+)?;

SEMICOLON: ';';
COMMA: ',';
EQUALS: '=';
LEFTPARENTHESIS: '(';
RIGHTPARENTHESIS: ')';

PI: 'PI';
PLUS : '+';
MINUS: '-';
MULT : '*';
DIV  : '/';

POSITION: 'Position';
POINT: 'Point';
VECTOR: 'Vector';
LINEARVELOCITY: 'LinearVelocity';
ANGULARVELOCITY: 'AngularVelocity';
DISTANCE: 'Distance';
TIME: 'Time';

TAKEOFF: 'takeOff';
LAND: 'land';
MOVE: 'move';
MOVEPATH: 'movePath';
MOVECIRCLE: 'moveCircle';
HOOVER: 'hoover';
LIGHTSON: 'lightsOn';
LIGHTSOFF: 'lightsOff';
BLINK: 'blink';

COLORNAME: 'RED' | 'GREEN' | 'BLUE' | 'YELLOW' | 'WHITE' | 'BLACK';

IDENTIFIER: LETTER (LETTER | DIGIT)*;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip;