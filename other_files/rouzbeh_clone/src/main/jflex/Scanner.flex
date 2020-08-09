/* this part will be added to java files directly! */
package compiler.scanner;

import java.io.*;

import compiler.scanner.token.*;

%%
 /* * * * * options and macros (declerations) * * * * * * * */


%class MyScanner //name of output class

%public // class is public, why? accessible out of package!
%final // class is final, why anyone should inherit an autogenrated class?


%{
    StringBuffer stringLiteral = new StringBuffer();

    private Symbol of(ScannerToken token){
        return Symbol.of(token,yytext());
    }
%}


%buffer 100000
%unicode //input file use the last version of unicode!

%line // line counting: current line can be accesssed with the variable yyline
%column // column counting: same as line!

 /* settings of next() function */
%function nextToken // name of function (instead of yylex)
%type Symbol //define output of out next() function

 /* define states of our machine */
%state STRING_STATE
%state CHAR_STATE


 /* white spaces */
LineTerminator = \r|\n|\r\n // support linux line ending and windows line ending
InputCharacter = [^\r\n] // evething except line terminator is input character!

Tab = \t
WhiteSpace = [ ]|{Tab}


 /*   *   *   *   *  *  comments  *   *   *   *    *   *  */
CStyleComment = "/*"~"*/"
OneLineComment = "//" {InputCharacter}* {LineTerminator}
Comment = {CStyleComment}|{OneLineComment}


Letter = [A-Za-z]
Digit = [0-9]
Underscore = "_"
SingleQ = '
Identifier = {Underscore}* {Letter} ({Letter}|{Digit}|{Underscore})*


 /* * * * * *  numbers * * * * * * */
ESign = (\-)
Sign = (\+|\-)?
NoSignDecimal = [0-9]+
DecimalInt = {ESign}{NoSignDecimal}
DecimalLong = {DecimalInt}[L]
HexaDecimal = {Sign}[0][xX][0-9a-fA-F]+
 // IntegerNumber ={DecimalInt}|{DecimalLong}|{HexaDecimal}

Num = {DoubleNumber}|{DecimalInt}|{NoSignDecimal}
DoubleNumber = {Sign}(\.{Digit}+) | {Sign}({Digit}+\.) |{Sign}({Digit}+\.{Digit}+)
FloatNumber = {Num}[fF]

Ee = (e|E)
ScientificNumber = {Num}{Ee}{Sign}{DecimalInt}

 // RealNumber = {FloatNumber} | {DoubleNumber} | {ScientificNumber}





 /* * * * * * * * lexical rules * * * * * ** * * * */
%%


<YYINITIAL> {

   {Comment}        { return nextToken(); }




  /* keywords */
    "function" {return of(ScannerToken.FUNCTION);}
    "return" {return of(ScannerToken.RETURN);}
    "void" {return of(ScannerToken.VOID);}
    // "start" {return of(ScannerToken.START);}
    // "sizeof" {return of(ScannerToken.SIZE_OF);}
    // "println" {return of(ScannerToken.PRINT_LN);}
    "input" {return of(ScannerToken.INPUT);}
    "if" {return of(ScannerToken.IF);}
    "then" {return of(ScannerToken.THEN);}
    "else" {return of(ScannerToken.ELSE);}
    "switch" {return of(ScannerToken.SWITCH);}
    "case" {return of(ScannerToken.CASE);}
    "begin" {return of(ScannerToken.BEGIN);}
    "end" {return of(ScannerToken.END);}
    "for" {return of(ScannerToken.FOR);}
    "repeat" {return of(ScannerToken.REPEAT);}
    "foreach" {return of(ScannerToken.FOREACH);}
    "in" {return of(ScannerToken.IN);}
    "break" {return of(ScannerToken.BREAK);}
    "continue" {return of(ScannerToken.CONTINUE);}
    "float" {return of(ScannerToken.LAN_FLOAT);}
    "double" {return of(ScannerToken.LAN_DOUBLE);}
    "long" {return of(ScannerToken.LAN_LONG);}
    "int" {return of(ScannerToken.LAN_INT);}
    "bool" {return of(ScannerToken.LAN_BOOL);}
    "char" {return of(ScannerToken.LAN_CHAR);}
    "string" {return of(ScannerToken.LAN_STRING);}
    "new" {return of(ScannerToken.NEW);}
    "default" {return of(ScannerToken.DEFAULT);}
    "until" {return of(ScannerToken.UNTIL);}
    "of" {return of(ScannerToken.OF);}
    "+=" {return of(ScannerToken.PLUS_ASSIGN);}
    "*=" {return of(ScannerToken.STAR_ASSIGN);}
    "-=" {return of(ScannerToken.MINUS_ASSIGN);}
    "%=" {return of(ScannerToken.MODULE_ASSIGN);}
    "const" {return of(ScannerToken.CONST_KEYWORD);}
    "auto" {return of(ScannerToken.AUTO);}
    "record" {return of(ScannerToken.RECORD);}
    ">=" {return of(ScannerToken.GREATER_THAN_EQUAL);}
    "<=" {return of(ScannerToken.SMALLER_THAN_EQUAL);}
    "==" {return of(ScannerToken.COND_EQUAL);}
    "!=" {return of(ScannerToken.COND_UNEQUAL);}
    "and" {return of(ScannerToken.COND_AND);}
    "or" {return of(ScannerToken.COND_OR);}
    "xor" {return of(ScannerToken.COND_XOR);}
    "not" {return of(ScannerToken.COND_NOT);}
    "=" {return of(ScannerToken.EQUAL);}
    "++" {return of(ScannerToken.D_PLUS);}
    "--" {return of(ScannerToken.D_MINUS);}
    "**" {return of(ScannerToken.D_STAR);}

    "+" {return of(ScannerToken.PLUS);}
    "-" {return of(ScannerToken.MINUS);}
    "*" {return of(ScannerToken.STAR);}
    "/" {return of(ScannerToken.DIVISION);}
    "%" {return of(ScannerToken.MODULO);}
    "&" {return of(ScannerToken.AND);}
    "|" {return of(ScannerToken.OR);}
    "^" {return of(ScannerToken.XOR);}
    ">" {return of(ScannerToken.GREATER_THAN);}
    "<" {return of(ScannerToken.SMALLER_THAN);}
    "{" {return of(ScannerToken.OPEN_CURLY_BRACK);}
    "}" {return of(ScannerToken.CLOSE_CURLY_BRACK);}
    "[" {return of(ScannerToken.OPEN_BRACK);}
    "]" {return of(ScannerToken.CLOSE_BRACK);}
    "(" {return of(ScannerToken.OPEN_PRANTS);}
    ")" {return of(ScannerToken.CLOSE_PRANTS);}
    "," {return of(ScannerToken.COMMA);}
    ":" {return of(ScannerToken.COLON);}
    ";" {return of(ScannerToken.SEMICOLON);}


    "(?" {return of(ScannerToken.BOOL_PARAN);}


 {Identifier}  {return Symbol.of(ScannerToken.IDENTIFIER, yytext());}

 {FloatNumber}      {return Symbol.of(ScannerToken.FLOAT_LIT, yytext().toLowerCase().replace("f",""));}
 {DoubleNumber}     {return Symbol.of(ScannerToken.DOUBLE_LIT, yytext());}


 {DecimalInt}  {return Symbol.of(ScannerToken.INT_LIT, yytext()); }
 {NoSignDecimal} {return Symbol.of(ScannerToken.INT_LIT, yytext()); }
 {DecimalLong} {return Symbol.of(ScannerToken.LONG_LIT, yytext()); }
 {HexaDecimal} {return Symbol.of(ScannerToken.INT_LIT,
        "" + Integer.parseInt(yytext().toLowerCase().replace("0x",""),16)
        ); }


 {ScientificNumber} {return Symbol.of(ScannerToken.DOUBLE_LIT,
                        ""+Double.parseDouble(yytext())
                             ); }


 {WhiteSpace}     { return nextToken(); }
 {LineTerminator} { return nextToken(); }

  /* jump to another state: String */
 "\""  {stringLiteral.setLength(0); yybegin(STRING_STATE); }

  /* jump to another state: Character */
 "\'"  { yybegin(CHAR_STATE); }

 <<EOF>> { return Symbol.of(ScannerToken.EOF); }



 [^]  { return Symbol.of(ScannerToken.UNRECOGNIZED, yytext()); }

}

 /* * * * * * * *  * * * State : S T R I N G * * * * * * * */
<STRING_STATE> {
    /* end of string */
 "\""  { yybegin( YYINITIAL ); return Symbol.of(ScannerToken.STRING_LIT, stringLiteral.toString()); }

  /* for \" */
    "\\\""  { stringLiteral.append(yytext());}
    "\\t" { stringLiteral.append('\t'); }
    "\\n" { stringLiteral.append('\n'); }
    "\\r" { stringLiteral.append('\r'); }
    "\\"  { stringLiteral.append('\\'); }

    /* any other char in string */
    [^\n\r\"\\]+  { stringLiteral.append( yytext() ); }
}

 /* * * * * * * *  * * * State : CHARACTER * * * * * * * */
<CHAR_STATE> {

    "\\\""{SingleQ}  { yybegin(YYINITIAL); return Symbol.of(ScannerToken.CHAR_LIT, yytext()); }
    "\\t"{SingleQ} { yybegin(YYINITIAL);
            return Symbol.of(ScannerToken.CHAR_LIT, "\t");}
    "\\n"{SingleQ} { yybegin(YYINITIAL);
            return Symbol.of(ScannerToken.CHAR_LIT,  "\n" );}
    "\\r"{SingleQ} { yybegin(YYINITIAL);
            return Symbol.of(ScannerToken.CHAR_LIT, "\r" );}
    "\\"{SingleQ}  { yybegin(YYINITIAL);
            return Symbol.of(ScannerToken.CHAR_LIT, "\\" );}

    /* any other char */
    .{SingleQ} { yybegin(YYINITIAL); return Symbol.of(ScannerToken.CHAR_LIT, yytext().replace("\'","")); }
}
