grammar DSLPlugin;

/*
 * Parser Rules
 */

program: droneType+ variablesBlock? figuresBlock block* EOF;

droneType: DRONETYPE variableName SEMICOLON;

variablesBlock: variableDecl+;

variableDecl: POSITION variableName EQUALS (vectorExpression) SEMICOLON #instancePosition |
            VELOCITY variableName EQUALS (numberExpression) SEMICOLON #instanceVelocity |
            DISTANCE variableName EQUALS (numberExpression) SEMICOLON #instanceDistance;

figuresBlock: figureDecl+;

figureDecl: LINE variableName LEFTPARENTHESIS vectorExpression COMMA numberExpression COMMA variableName RIGHTPARENTHESIS SEMICOLON #instanceLine |
            RECTANGLE variableName LEFTPARENTHESIS vectorExpression COMMA numberExpression COMMA numberExpression COMMA variableName RIGHTPARENTHESIS SEMICOLON #instanceRectangle |
            CIRCLE variableName LEFTPARENTHESIS vectorExpression COMMA numberExpression COMMA variableName RIGHTPARENTHESIS SEMICOLON #instanceCircle |
            CIRCUMFERENCE variableName LEFTPARENTHESIS vectorExpression COMMA numberExpression COMMA variableName RIGHTPARENTHESIS SEMICOLON #instanceCircumference;

block: beforeBlock | groupBlock | afterBlock | pauseStmt | commandStmt;

beforeBlock: BEFORE blockBody ENDBEFORE;

afterBlock: AFTER blockBody ENDAFTER;

groupBlock: GROUP blockBody ENDGROUP;

blockBody: block*;

pauseStmt: PAUSE LEFTPARENTHESIS NUMBER RIGHTPARENTHESIS SEMICOLON;

commandStmt: variableName '.' methodCall SEMICOLON;

methodCall: MOVE LEFTPARENTHESIS (vectorExpression) COMMA (numberExpression) COMMA (numberExpression) RIGHTPARENTHESIS #instanceMove
            | MOVEPOS LEFTPARENTHESIS (vectorExpression) COMMA (numberExpression) RIGHTPARENTHESIS #instanceMovePos
            | ROTATE LEFTPARENTHESIS (vectorExpression) COMMA (vectorExpression) COMMA (numberExpression) COMMA (numberExpression) RIGHTPARENTHESIS #instanceRotate
            | LIGHTSON LEFTPARENTHESIS COLORNAME RIGHTPARENTHESIS #instanceLightsOn
            | LIGHTSOFF #instanceLightsOff ;

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

DRONETYPE: 'DroneType';

POSITION: 'Position';
VELOCITY: 'Velocity';
DISTANCE: 'Distance';

LINE: 'Line';
RECTANGLE: 'Rectangle';
CIRCLE: 'Circle';
CIRCUMFERENCE: 'Circumference';

BEFORE: 'before';
ENDBEFORE: 'endbefore';
AFTER: 'after';
ENDAFTER: 'endafter';
GROUP: 'group';
ENDGROUP: 'endgroup';

PAUSE: 'pause';

MOVE: 'move';
MOVEPOS: 'movePos';
ROTATE: 'rotate';
LIGHTSON: 'lightsOn';
LIGHTSOFF: 'lightsOff';

COLORNAME: 'RED' | 'GREEN' | 'BLUE' | 'YELLOW' | 'WHITE' | 'BLACK';

IDENTIFIER: LETTER (LETTER | DIGIT)*;

LINE_COMMENT: '//' ~[\r\n]* -> skip;

BLOCK_COMMENT: '/*' .*? '*/' -> skip;
