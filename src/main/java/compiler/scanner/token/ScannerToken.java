package compiler.scanner.token;

import java.util.*;

public enum ScannerToken implements Token{
    UNRECOGNIZED, // ye chi ajib
    BOOL_PARAN,


    INCLUDE,

    //OTHER
    FUNCTION,RETURN,START,

    //LANGUAGE STRUCTURE
    INT_LIT,LAN_INT,
    LONG_LIT,LAN_LONG,
    BOOL_LIT_T,BOOL_LIT_F,LAN_BOOL,
    CHAR_LIT,LAN_CHAR,
    FLOAT_LIT,LAN_FLOAT,
    DOUBLE_LIT,LAN_DOUBLE,
    STRING_LIT,LAN_STRING,
    VOID,
    DEFAULT,
    NEW,
    OF,
    UNTIL,
    IDENTIFIER,//USED FOR VARIABLES AND FUNCTION IN PROGRAM
    SEMICOLON,
    IF,THEN,ELSE,
    SWITCH,CASE,COLON,
    BEGIN,END,
    FOR,REPEAT,FOREACH,IN,
    BREAK,CONTINUE,
    PLUS_ASSIGN,STAR_ASSIGN,
    MINUS_ASSIGN,DIVIDE_ASSIGN,MODULE_ASSIGN,
    CONST_KEYWORD,COMMA,
    AUTO,RECORD,
    OPEN_CURLY_BRACK,CLOSE_CURLY_BRACK,
    OPEN_BRACK,CLOSE_BRACK,
    OPEN_PRANTS,CLOSE_PRANTS,

    //COMPARISON OPERATORS
    GREATER_THAN,GREATER_THAN_EQUAL,
    SMALLER_THAN,SMALLER_THAN_EQUAL,
    COND_EQUAL,COND_UNEQUAL,
    COND_AND,
    COND_OR,
    COND_XOR,
    COND_NOT,

    //EXPRESSION OPERATORS
    EQUAL,
    PLUS,D_PLUS,
    MINUS,D_MINUS,
    STAR,D_STAR,
    DIVISION,D_DIVISION,
    MODULO,

    //BITWISE EXPRESSION OPERATORS
    AND,
    OR,
    XOR,

    //LANGUAGE NATIVE FUNCTIONS
    // SIZE_OF, //TODO
    PRINT_LN,
    INPUT,

 //   PREDEFINED_ID,

    EOF;


    static final Map<String, ScannerToken> ntv =
        new HashMap<>();

    static {

        ntv.put("include", INCLUDE);

        ntv.put("$", EOF);
        ntv.put("<=", SMALLER_THAN_EQUAL);
        ntv.put("auto", AUTO);
        ntv.put("float", LAN_FLOAT);
        ntv.put("else", ELSE);
        ntv.put("continue", CONTINUE);
        ntv.put("function", FUNCTION );
        ntv.put("(?", BOOL_PARAN);
        ntv.put("record", RECORD);
        ntv.put("id",IDENTIFIER);
        ntv.put("if",IF);
        ntv.put("icv", INT_LIT);
        ntv.put("case", CASE);
        ntv.put("char_literal", CHAR_LIT);
        ntv.put("==",  COND_EQUAL);
        ntv.put("--", D_MINUS);
        ntv.put("new", NEW);
        ntv.put("void", VOID);
        ntv.put("in",  IN);
        ntv.put("%", MODULO);
        ntv.put("double", LAN_DOUBLE);
        ntv.put("&", AND);
        ntv.put("(", OPEN_PRANTS);
        ntv.put(")", CLOSE_PRANTS);
        ntv.put("*", STAR);
        ntv.put("+", PLUS);
        ntv.put("-", MINUS);
        ntv.put("/", DIVISION);
        ntv.put("-=", MINUS_ASSIGN);
        ntv.put("input", INPUT);
//TODO        ntv.put("user_id", );
//TODO        ntv.put("boolean_id", );
    //   ntv.put("PRE_TYPE", PREDEFINED_ID);
   //    ntv.put("PRE_TYPE", IDENTIFIER);
//
//
        ntv.put("true", BOOL_LIT_T);
        ntv.put(":", COLON);
        ntv.put(";", SEMICOLON);
        ntv.put("!=", COND_UNEQUAL);
        ntv.put("<", SMALLER_THAN);
        ntv.put("begin", BEGIN);
        ntv.put("=", EQUAL);
        ntv.put(">", GREATER_THAN);
        ntv.put(">=", GREATER_THAN_EQUAL);
        ntv.put("string", LAN_STRING);
        ntv.put("bool", LAN_BOOL);
        ntv.put("const", CONST_KEYWORD);
        ntv.put("for", FOR);
        ntv.put("long", LAN_LONG);
        ntv.put("long_literal", LONG_LIT);
        ntv.put("switch", SWITCH);
        ntv.put("foreach", FOREACH);
        ntv.put("default", DEFAULT);
        ntv.put("not", COND_NOT);
        ntv.put("println", PRINT_LN);
        ntv.put("and", AND);
        ntv.put("of", OF);
        ntv.put("repeat", REPEAT);
        ntv.put("float_literal", FLOAT_LIT);
        ntv.put("end", END);
        ntv.put("xor", XOR);
        ntv.put("[", OPEN_BRACK);
        ntv.put("double_literal", DOUBLE_LIT);
        ntv.put("]", CLOSE_BRACK);
        ntv.put("^", XOR);
        ntv.put("^", XOR);
        ntv.put("++", D_PLUS);
        ntv.put("or", COND_OR);
        ntv.put("break", BREAK);
       // TODO ntv.put("start", START);
        ntv.put("false", BOOL_LIT_F);
        ntv.put("int", LAN_INT);
        ntv.put("string_literal", STRING_LIT);
        ntv.put("string_literal", STRING_LIT);
        ntv.put("comma", COMMA);
        ntv.put("+=", PLUS_ASSIGN);
        ntv.put("char", LAN_CHAR);
        ntv.put("until", UNTIL);

        ntv.put("{", OPEN_CURLY_BRACK);
        ntv.put("|", OR);
        ntv.put("}", CLOSE_CURLY_BRACK);
        ntv.put("return", RETURN);

        // TODO: add to parser
        ntv.put("*=", PLUS_ASSIGN);
        ntv.put("/=", PLUS_ASSIGN);
    }
}


// bugs:
//     assign,
//     print just accept literal
//     *=
//     /=
