grammar ProposalTemplate;

/*
 * Parser Rules
 */

proposal: languageDecl info videoLink insuranceAmount crmManagerName locationInfo droneList figureList EOF;

languageDecl: LANGUAGE COLON language;
language: PORTUGUESE #portugueseVersion | ENGLISH #englishVersion;

info: (representativeName)? companyName address vatNumber showProposalNumber date;

representativeName: REPRESENTATIVE COLON NAME+;
companyName: COMPANY COLON NAME+;
address: ADDRESS COLON street SEMICOLON postalCode SEMICOLON city SEMICOLON country;
street: NAME (NAME | WORD)*;
postalCode: POSTALCODE;
city: NAME+;
country: NAME+;
vatNumber: VAT COLON VATNUMBER;
showProposalNumber: PROPOSALNUMBER COLON NUMBER;
date: DATEPROPOSAL COLON DATE;

videoLink: VIDEO COLON URL;

insuranceAmount: INSURANCE COLON NUMBER CURRENCY?;

crmManagerName: CRM COLON NAME+;

locationInfo: gpsEvent dateEvent timeEvent durationEvent;

gpsEvent: EVENTGPS COLON coordinates;
coordinates: SIGNED_NUMBER SEMICOLON SIGNED_NUMBER;
dateEvent: EVENTDATE COLON DATE;
timeEvent: EVENTTIME COLON TIME;
durationEvent: EVENTDURATION COLON NUMBER;

droneList: beginDrone droneItem+ end;
beginDrone: DEF DRONE;
droneItem: droneModel HYPHEN droneQuantity;
droneModel: identifier;
droneQuantity: NUMBER;

figureList: beginFigure figureItem+ end;
beginFigure: DEF FIGURE;
figureItem: figurePosition HYPHEN figureCode;
figurePosition: SIGNED_NUMBER SEMICOLON SIGNED_NUMBER SEMICOLON SIGNED_NUMBER;
figureCode: identifier;

end: END;
identifier: NAME | WORD | CODE | NUMBER;

/*
 * Lexer Rules
 */

WS: [ \t\r\n]+ -> skip ;

fragment LOWERCASE : [a-zà-ÿ] ;
fragment UPPERCASE : [A-ZÀ-Ý] ;
fragment LETTER    : LOWERCASE | UPPERCASE ;
fragment DIGIT     : [0-9] ;

COLON: ':';
SEMICOLON: ';';
HYPHEN : '-';

LANGUAGE: 'Language';
PORTUGUESE: 'PT';
ENGLISH: 'EN';
REPRESENTATIVE: 'Representative';
COMPANY: 'Company';
ADDRESS: 'Address';
VAT: 'VAT';
PROPOSALNUMBER: 'Proposal Number';
DATEPROPOSAL: 'Date';
VIDEO: 'Video';
INSURANCE: 'Insurance Amount';
CRM: 'CRM Manager';
EVENTGPS: 'Event GPS';
EVENTDATE: 'Event Date';
EVENTTIME: 'Event Time';
EVENTDURATION: 'Event Duration';
DRONE: 'Drone';
FIGURE: 'Figure';
DEF: 'Def';
END: 'End';

STRING: '"' ('""' | ~'"')* '"';

DATE : (('0'?[1-9]) | ('1'DIGIT) | ('2'DIGIT) | ('3'[0-1])) '-'
       (('0'?[1-9]) | ('1'[0-2])) '-'
       DIGIT DIGIT DIGIT DIGIT;

POSTALCODE : DIGIT+ '-' DIGIT+ ;

TIME : (('0'[0-9]) | ('1'[0-9]) | ('2'[0-3])) ':' ([0-5][0-9]) ;

URL : ('http' | 'https') '://' ~[ \t\r\n]+ ;

CURRENCY : '€' | 'EUR' | '$' | 'USD' ;

VATNUMBER : LETTER LETTER DIGIT DIGIT DIGIT DIGIT DIGIT DIGIT DIGIT (DIGIT)? (DIGIT)? (DIGIT)? (DIGIT)?;

NUMBER : DIGIT+;

NAME : UPPERCASE (LETTER)* ;

WORD : (LETTER)+ ;

CODE : (LETTER | DIGIT)+ ;

SIGNED_NUMBER : ('+' | '-')? NUMBER+ ('.' NUMBER+)? ;
