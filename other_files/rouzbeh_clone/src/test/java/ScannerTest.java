import java.io.*;
import java.nio.file.*;
import java.net.*;

import static org.junit.Assert.*;
import org.junit.*;

import compiler.scanner.*;
import compiler.scanner.token.*;
import compiler.util.*;


public class ScannerTest{

    MyScanner scanner = null;

    @Before
    public void makeScannerNull(){ scanner = null; }

    @After
    public void closeScanner() throws IOException{
        if(scanner != null) scanner.yyclose();
    }

    private void s(String inp){
        scanner = new MyScanner(
                new StringReader(inp)
            );
    }

    private void f(String add) throws Exception{
       scanner = new MyScanner(
               new InputStreamReader(Utils.getInputStreamFromResource(add))
            );
    }

    @Test
    public void contructTest() throws IOException {

        assertNull( scanner );
        s("init");
        assertNotNull( scanner );
    }

    @Test
    public void test1() throws IOException{
        s("salam 1 \" in ye string \" ");
        assertEquals(scanner.nextToken().token,ScannerToken.IDENTIFIER);
        assertEquals(scanner.nextToken().token,ScannerToken.INT_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.STRING_LIT);
    }

    @Test
    public void test2_types() throws Exception{
        f("scanner/scanner_test_1.txt");
        assertEquals(scanner.nextToken().token,ScannerToken.FLOAT_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.FLOAT_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.DOUBLE_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.CHAR_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.CHAR_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.CHAR_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.STRING_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.STRING_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.STRING_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.STRING_LIT);
    }


    @Test
    public void test2_values() throws Exception{
        f("scanner/scanner_test_1.txt");
        assertEquals(scanner.nextToken().content,"1.2");
        assertEquals(scanner.nextToken().content,"1.2");
        assertEquals(scanner.nextToken().content,"3.14156");
        assertEquals(scanner.nextToken().content,"s");
        assertEquals(scanner.nextToken().content.length(),"\n".length());
        assertEquals(scanner.nextToken().content,"\r");
        assertEquals(scanner.nextToken().content,"some string");
        assertEquals(scanner.nextToken().content,"some \n \t string");
        assertEquals(scanner.nextToken().content,"\n");
        assertEquals(scanner.nextToken().content,"s");
    }

    @Test
    public void test3() throws IOException{
        s(";; <= ++ + > >=  \'s\' (?");
        assertEquals(scanner.nextToken().token,ScannerToken.SEMICOLON);
        assertEquals(scanner.nextToken().token,ScannerToken.SEMICOLON);
        assertEquals(scanner.nextToken().token,ScannerToken.SMALLER_THAN_EQUAL);
        assertEquals(scanner.nextToken().token,ScannerToken.D_PLUS);
        assertEquals(scanner.nextToken().token,ScannerToken.PLUS);
        assertEquals(scanner.nextToken().token,ScannerToken.GREATER_THAN);
        assertEquals(scanner.nextToken().token,ScannerToken.GREATER_THAN_EQUAL);
        assertEquals(scanner.nextToken().token,ScannerToken.CHAR_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.BOOL_PARAN);
    }

    @Test
    public void test4() throws IOException{
        s("some /* comment */ if //");
        assertEquals(scanner.nextToken().token,ScannerToken.IDENTIFIER);
        assertEquals(scanner.nextToken().token,ScannerToken.IF);
    }

    @Test
    public void test5() throws IOException{
        s("if \n // some comment \n some_id");
        assertEquals(scanner.nextToken().token,ScannerToken.IF);
        assertEquals(scanner.nextToken().token,ScannerToken.IDENTIFIER);
    }

    @Test
    public void test6() throws IOException{
        s("1+2");
        assertEquals(scanner.nextToken().token,ScannerToken.INT_LIT);
        assertEquals(scanner.nextToken().token,ScannerToken.PLUS);
        assertEquals(scanner.nextToken().token,ScannerToken.INT_LIT);
    }

    @Test
    public void test7() throws IOException{
        s("2f");
        assertEquals(scanner.nextToken().token,ScannerToken.FLOAT_LIT);

    }



    @Test
    public void code1() throws Exception{
        f("simple_parser/code1.txt");
        assertEquals(scanner.nextToken().token,ScannerToken.SEMICOLON);
    }


}
