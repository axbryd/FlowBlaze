/** (xtrac XTRA Lang compiler) - XFSM for TRAnsport protocol Language compiler
*   Author Giacomo Belocchi gbelocchi [at] cnit [dot] it
*
*   This file contains the Lexer and the Parser grammar, for xtrac (the XTRA Lang compiler).
*/
grammar xtra;

// =====================================================================================================================
//
//                                                 P   A   R   S   E   R
//
// =====================================================================================================================
// Main program definition

program: (packet_field_definition|file_import|constant_definition)* (stage_program | state_program) EOF;
stage_program: (register_definition|custom_event_definition|macro_action_definition)* stage_statement+;
state_program: (register_definition|custom_event_definition|macro_action_definition)* state_statement+;
// =====================================================================================================================
// Macro action && custom event definition

macro_action_definition: MACRO_ACTION_DEF_KEYWORD identifier LBRACE body RBRACE; // Action action_name {action_body}
custom_event_definition: CUSTOM_DEF_EVENT_KEYWORD identifier ASSIGNMENT condition (COMMA condition)* SEMICOLON; // Event custom_event = reg0 != reg1;
// =====================================================================================================================
// Stage statement

stage_statement: STAGE_DEF_KEYWORD identifier stage_body; // Stage label { ... State [initial] state_label {... on(...){...}* ...}}
stage_body: LBRACE (register_definition|custom_event_definition|macro_action_definition)* body? state_statement+ RBRACE;
// =====================================================================================================================
// State statement

state_statement: STATE_DEF_KEYWORD INITIAL_MODIFIER? identifier state_body; // State [initial] state_label {... on(...){...}* ...}
state_body: LBRACE body? event_statement* RBRACE; // Can contains a parallel block, actions and event statements
                                                                                                        // {... on(...){...}* ...}
// =====================================================================================================================
// Event statement

event_statement: EVENT_KEYWORD LPARENT identifier RPARENT event_body; // on(event_name){... if(...){...}* ...}
event_body: LBRACE body? condition_statement* RBRACE; //  Can contains a parallel block, actions
                                                      // and condition statements {... if(...){...}* ...}
// =====================================================================================================================
// Condition statement

condition_statement: CONDITIONS_KEYWORD LPARENT condition (COMMA condition)* RPARENT condition_body; // if(...){...}
condition_body: LBRACE body RBRACE; // Can contains a parallel block and actions {...}
condition: term EQ term // A single condition
         | term LEQ term
         | term GEQ term
         | term NEQ term
         | term GRET term
         | term LESS term
         ;
// =====================================================================================================================
// Call, actions && parallel blocks

body: (action | serial)*; // Basic body (made of actions and/or parallel blocks)
serial: SERIAL_KEYWORD LBRACE action+ RBRACE; // Identify a block of actions to be executed in serial
action: call // Action call or assignment
      | assignment
      ;
call: ID LPARENT term* (COMMA term)* RPARENT SEMICOLON; // Call (of a primitive or a macro action)
// =====================================================================================================================
// Assignment and expressions

assignment: identifier ASSIGNMENT expr SEMICOLON; // An assignment (identifier = expr)
expr: term MUL term // An expresison used in assignment (i.e. term {+, -, *, /, %} term, term, {max, min}(term, term))
    | term DIV term
    | term PLUS term
    | term MINUS term
    | term MOD term
    | (MAX|MIN) LPARENT term COMMA term RPARENT
    | term
    ;
// =====================================================================================================================
// Terms (ids, indexed objs and field access)

term: field_elem // A term used in conditions and expressions
    | identifier
    | integer
    | addr
    | index_access
    ;
index_access: field_elem INDEX; // Indexing an idexable object (obj[from:to])
field_elem: ID DOT ID; // Used to access to a field from a field container object (i.e. pkt header, event field)
identifier: ID; // Used to declare something with a label (register, state...)
// =====================================================================================================================
// File import

file_import: FILE_IMPORT file_name (COMMA file_name)* SEMICOLON; // import "./headers/tcp.xtrah";
file_name: FILENAME;
// =====================================================================================================================
// Packet field definition

packet_field_definition: PACKET_FIELD_KEYWORD identifier LBRACE packet_field_entry+ RBRACE; // PacketField eth { dst = pkt.metadata[xx:xx]; ... }
packet_field_entry: identifier ASSIGNMENT index_access SEMICOLON; // e.g. dst = pkt.metadata[xx:xx];
// =====================================================================================================================
// Register definition

register_definition: REGISTER_DEF_KEYWORD GLOBAL_MODIFIER? identifier (COMMA identifier)* SEMICOLON; // Register [global] reg0 [, regi]*;
// =====================================================================================================================
// Constant definition
constant_definition: CONSTANT_DEF_KEYWORD const_assignement (COMMA const_assignement)* SEMICOLON; // Constant const0 = 0 [, constj = i ]*;
const_assignement: identifier ASSIGNMENT (integer|addr);
// =====================================================================================================================
// Immediate types

integer: DECIMAL_INTEGER // An integer immediate in decimal or hex base
       | HEX_INTEGER
       ;
addr: IPV4_ADDR // A network address, can be an ipv4, ipv6 or mac address
    | IPV6_ADDR
    | MAC_ADDR
    ;
// =====================================================================================================================
//
//                                                   L   E   X   E   R
//
// =====================================================================================================================
// Here comes what to ignore in the source file (spaces, newlines, tabs and comment blocks)

WS: [ \t\r\n]+ -> skip; // Ignoring spaces, tabs, returns and newlines
COMMENT: '/*' (COMMENT|.)*? '*/' -> skip; // Ignoring C-style block comments
LINE_COMMENT: '//' .*? '\n' -> skip; // Ignoring C-style line comments
// =====================================================================================================================
// XTRA Lang keywords

// --- Modifiers (e.g. initial, global)
GLOBAL_MODIFIER: 'global'; // Make the register scope global
INITIAL_MODIFIER: 'initial'; // Mark the state as initial

// --- Definitions (define a XTRA Lang object with a name label)
REGISTER_DEF_KEYWORD: 'Register'; // Used to declare register(s)
CONSTANT_DEF_KEYWORD: 'Constant'; // Used to declare constant(s) (they'll be treated like define in C)
STATE_DEF_KEYWORD: 'State'; // Used to declare a state and begin state statement
CUSTOM_DEF_EVENT_KEYWORD: 'Event'; // Used to define a custom event
MACRO_ACTION_DEF_KEYWORD: 'Action'; // Used to define a macro action
STAGE_DEF_KEYWORD: 'Stage'; // Used to define a stage
PACKET_FIELD_KEYWORD: 'PacketField'; // Used to define packet fields

// -- XTRA Lang statement blocks (define a XTRA Lang object which has no name)
CONDITIONS_KEYWORD: 'if'; // Used to begin a condition(s) statement
EVENT_KEYWORD: 'on'; // Used to begin a event statement
SERIAL_KEYWORD: 'serial'; // Used to define a block of actions which has to be executed in serial
FILE_IMPORT: 'import'; // Used to import files, i.e. packet fields definition files
// =====================================================================================================================
// Basic types tokens (ipv{4, 6}_addr, mac_addr, hex_integer, decimal_integer, identifier, index)

IPV6_ADDR: DQUOTES HEX_16 COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON LS32 DQUOTES // Tokenize ipv6 addr
         | DQUOTES DOUBLE_COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON LS32 DQUOTES
         | DQUOTES HEX_16? DOUBLE_COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON LS32 DQUOTES
         | DQUOTES ((HEX_16 COLON)? HEX_16)? DOUBLE_COLON HEX_16 COLON HEX_16 COLON HEX_16 COLON LS32 DQUOTES
         | DQUOTES (((HEX_16 COLON)? HEX_16 COLON)? HEX_16)? DOUBLE_COLON HEX_16 COLON HEX_16 COLON LS32 DQUOTES
         | DQUOTES ((((HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16)? DOUBLE_COLON HEX_16 COLON LS32 DQUOTES
         | DQUOTES (((((HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16)? DOUBLE_COLON LS32 DQUOTES
         | DQUOTES ((((((HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16)? DOUBLE_COLON HEX_16 DQUOTES
         | DQUOTES (((((((HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16 COLON)? HEX_16)? DOUBLE_COLON DQUOTES
         ;

IPV4_ADDR: DQUOTES NOT_ZERO_OCTET DOT OCTET DOT OCTET DOT OCTET DQUOTES // Tokenize an ipv4 addr
         | '"0.0.0.0"'
         ;

MAC_ADDR: DQUOTES HEX_DIGIT HEX_DIGIT? COLON HEX_DIGIT HEX_DIGIT? COLON HEX_DIGIT HEX_DIGIT? // Tokenize a mac addr
          COLON HEX_DIGIT HEX_DIGIT? COLON HEX_DIGIT HEX_DIGIT? COLON HEX_DIGIT HEX_DIGIT? DQUOTES;

HEX_INTEGER: HEX_LITERAL HEX_DIGIT+; // Tokenize an hexadecimal integer

DECIMAL_INTEGER: DIGIT+; // Tokenize a decimal integer

ID: ((LETTER) (LETTER | DIGIT | '_')*) | ('_' (LETTER | DIGIT | '_')+); // Can be componed of letters, digits
                                                                        // and underscores. Can begin only with
                                                                        // a letter or an underscore
INDEX: LBRACK SPACE* DECIMAL_INTEGER SPACE* (COLON SPACE* PLUS? SPACE* DECIMAL_INTEGER SPACE*)? RBRACK; // [DECIMAL_INTEGER:(+)DECIMAL_INTEGER]

FILENAME: DQUOTES (ID|DOT+ (DIV ID|DOT+)*)? (ID|DOT)+ DOT ID DQUOTES; // A filename e.g. "./headers/tcp.xtrah"

// =====================================================================================================================
// Bytes tokenization (used for building addresses (mac and ipv{4, 6})

NOT_ZERO_OCTET: '2' '5' ('0'..'5') // 0 < integer <= 255
              | '2' ('0'..'4') DIGIT
              | '1' DIGIT DIGIT
              | ('1'..'9') DIGIT
              | '1'..'9'
              ;
OCTET: '2' '5' ('0'..'5') // 0 < integer <= 255
     | '2' ('0'..'4') DIGIT
     | '1' DIGIT DIGIT
     | ('1'..'9') DIGIT
     | DIGIT
     ;

LS32: HEX_16 COLON HEX_16 // 32 bit element (in HEX_16:HEX_16 or ipv4 format)
    | IPV4_ADDR
    ;

HEX_16: HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT // 2 bytes in hex
      | HEX_DIGIT HEX_DIGIT HEX_DIGIT
      | HEX_DIGIT HEX_DIGIT
      | HEX_DIGIT
      ;
// =====================================================================================================================
// 'Single element' tokenization (digits, characters and symbols)

COLON: ':';
DOUBLE_COLON: COLON COLON;
DQUOTES: '"';
DOT: '.';

ASSIGNMENT: '=';

PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
MOD: '%';

MAX: 'max';
MIN: 'min';

COMMA: ',';

EQ: '==';
LEQ: '<=';
GEQ: '>=';
NEQ: '!=';
GRET: '>';
LESS: '<';

LPARENT: '(';
RPARENT: ')';
LBRACE: '{';
RBRACE: '}';
LBRACK: '[';
RBRACK: ']';
SEMICOLON: ';';

SPACE: ' ';

HEX_LITERAL: '0x'
           | '0X'
           ;
HEX_DIGIT: DIGIT
         | 'a'..'f'
         | 'A'..'F'
         ;

LETTER: 'a'..'z'
      |'A'..'Z'
      ;

DIGIT: '0'..'9';