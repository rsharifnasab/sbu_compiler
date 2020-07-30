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
Sign = (\+|\-)?
DecimalInt = {Sign}[0-9]+
DecimalLong = {DecimalInt}[L]
HexaDecimal = {Sign}[0][xX][0-9a-fA-F]+
 // IntegerNumber ={DecimalInt}|{DecimalLong}|{HexaDecimal}

Num = {FloatNumber}|{DecimalInt}
DoubleNumber = {Sign}(\.{Digit}+) | {Sign}({Digit}+\.) |{Sign}({Digit}+\.{Digit}+)
FloatNumber = {DoubleNumber}[fF]

Ee = (e|E)
ScientificNumber = {Num}{Ee}{Sign}{DecimalInt} 

 // RealNumber = {FloatNumber} | {DoubleNumber} | {ScientificNumber}





 /* * * * * * * * lexical rules * * * * * ** * * * */
%%

 
<YYINITIAL> { 

   {Comment}        { return nextToken(); }

  /* keywords */
    "function" {return Symbol.of(ScannerToken.FUNCTION);}
    "return" {return Symbol.of(ScannerToken.RETURN);}
    "void" {return Symbol.of(ScannerToken.VOID);}
    // "start" {return Symbol.of(ScannerToken.START);}
    // "sizeof" {return Symbol.of(ScannerToken.SIZE_OF);}
    "println" {return Symbol.of(ScannerToken.PRINT_LN);}
    "input" {return Symbol.of(ScannerToken.INPUT);}
    "if" {return Symbol.of(ScannerToken.IF);}
    "then" {return Symbol.of(ScannerToken.THEN,null);}
    "else" {return Symbol.of(ScannerToken.ELSE);}
    "switch" {return Symbol.of(ScannerToken.SWITCH);}
    "case" {return Symbol.of(ScannerToken.CASE);}
    "begin" {return Symbol.of(ScannerToken.BEGIN);}
    "end" {return Symbol.of(ScannerToken.END);}
    "for" {return Symbol.of(ScannerToken.FOR);}
    "repeat" {return Symbol.of(ScannerToken.REPEAT);}
    "foreach" {return Symbol.of(ScannerToken.FOREACH);}
    "in" {return Symbol.of(ScannerToken.IN);}
    "break" {return Symbol.of(ScannerToken.BREAK);}
    "continue" {return Symbol.of(ScannerToken.CONTINUE);}
    "float" {return Symbol.of(ScannerToken.LAN_FLOAT);}
    "double" {return Symbol.of(ScannerToken.LAN_DOUBLE);}
    "long" {return Symbol.of(ScannerToken.LAN_LONG);}
    "int" {return Symbol.of(ScannerToken.LAN_INT);}
    "bool" {return Symbol.of(ScannerToken.LAN_BOOL);}
    "char" {return Symbol.of(ScannerToken.LAN_CHAR);}
    "string" {return Symbol.of(ScannerToken.LAN_STRING);}
    "new" {return Symbol.of(ScannerToken.NEW);}
    "default" {return Symbol.of(ScannerToken.DEFAULT);}
    "until" {return Symbol.of(ScannerToken.UNTIL);}
    "of" {return Symbol.of(ScannerToken.OF);}
    "+=" {return Symbol.of(ScannerToken.PLUS_ASSIGN);}
    "*=" {return Symbol.of(ScannerToken.STAR_ASSIGN);}
    "-=" {return Symbol.of(ScannerToken.MINUS_ASSIGN);}
    "%=" {return Symbol.of(ScannerToken.MODULE_ASSIGN);}
    "const" {return Symbol.of(ScannerToken.CONST_KEYWORD);}
    "auto" {return Symbol.of(ScannerToken.AUTO);}
    "record" {return Symbol.of(ScannerToken.RECORD);}
    ">=" {return Symbol.of(ScannerToken.GREATER_THAN_EQUAL);}
    "<=" {return Symbol.of(ScannerToken.SMALLER_THAN_EQUAL);}
    "==" {return Symbol.of(ScannerToken.COND_EQUAL);}
    "!=" {return Symbol.of(ScannerToken.COND_UNEQUAL);}
    "and" {return Symbol.of(ScannerToken.COND_AND);}
    "or" {return Symbol.of(ScannerToken.COND_OR);}
    "xor" {return Symbol.of(ScannerToken.COND_XOR);}
    "not" {return Symbol.of(ScannerToken.COND_NOT);}
    "=" {return Symbol.of(ScannerToken.EQUAL);}
    "++" {return Symbol.of(ScannerToken.D_PLUS);}
    "--" {return Symbol.of(ScannerToken.D_MINUS);}
    "**" {return Symbol.of(ScannerToken.D_STAR);}
    
    "+" {return Symbol.of(ScannerToken.PLUS);}
    "-" {return Symbol.of(ScannerToken.MINUS);}
    "*" {return Symbol.of(ScannerToken.STAR);}
    "/" {return Symbol.of(ScannerToken.DIVISION);}
    "%" {return Symbol.of(ScannerToken.MODULO);}
    "&" {return Symbol.of(ScannerToken.AND);}
    "|" {return Symbol.of(ScannerToken.OR);}
    "^" {return Symbol.of(ScannerToken.XOR);}
    ">" {return Symbol.of(ScannerToken.GREATER_THAN);}
    "<" {return Symbol.of(ScannerToken.SMALLER_THAN);}
    "{" {return Symbol.of(ScannerToken.OPEN_CURLY_BRACK);}
    "}" {return Symbol.of(ScannerToken.CLOSE_CURLY_BRACK);}
    "[" {return Symbol.of(ScannerToken.OPEN_BRACK);}
    "]" {return Symbol.of(ScannerToken.CLOSE_BRACK);}
    "(" {return Symbol.of(ScannerToken.OPEN_PRANTS);}
    ")" {return Symbol.of(ScannerToken.CLOSE_PRANTS);}
    "," {return Symbol.of(ScannerToken.COMMA);}
    ":" {return Symbol.of(ScannerToken.COLON);}
    ";" {return Symbol.of(ScannerToken.SEMICOLON);}


    "(?" {return Symbol.of(ScannerToken.BOOL_PARAN);}


 {Identifier}  {return Symbol.of(ScannerToken.IDENTIFIER, yytext());}


 {DoubleNumber}     {return Symbol.of(ScannerToken.DOUBLE_LIT, yytext());}
 {FloatNumber}      {return Symbol.of(ScannerToken.FLOAT_LIT, yytext().toLowerCase().replace("f",""));}
 {ScientificNumber} {return Symbol.of(ScannerToken.DOUBLE_LIT,
                        ""+Double.parseDouble(yytext())
                             ); }

 {DecimalInt}  {return Symbol.of(ScannerToken.INT_LIT, yytext()); }
 {DecimalLong} {return Symbol.of(ScannerToken.LONG_LIT, yytext()); } 
 {HexaDecimal} {return Symbol.of(ScannerToken.INT_LIT,
        "" + Integer.parseInt(yytext().toLowerCase().replace("0x",""),16)
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

