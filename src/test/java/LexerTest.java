
import java.io.*;
import java.nio.file.*;
import java.net.*;

import static org.junit.Assert.*;
import org.junit.*;

import compiler.scanner.*;
import compiler.scanner.token.*;
import compiler.util.*;


public class LexerTest{

    Lexical scanner = null;

    @Before
    public void makeScannerNull(){ scanner = null; }
    
    
    private void f(String add) throws IOException{ 
       scanner = new Lexical(
               Utils.getInputStreamFromResource(add)
            );
    }

    @Test
    public void contructTest() throws IOException {
        assertNull( scanner );
        f("scanner/scanner_test_1.txt");
        assertNotNull( scanner );
    }


    @Test
    public void test2_types() throws Exception{
        f("scanner/scanner_test_1.txt");
        assertEquals(scanner.nextToken(),ScannerToken.FLOAT_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.FLOAT_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.DOUBLE_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.CHAR_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.CHAR_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.CHAR_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.STRING_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.STRING_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.STRING_LIT.toString());
        assertEquals(scanner.nextToken(),ScannerToken.STRING_LIT.toString());
    }

    @Test
    public void code2() throws Exception{
        f("scanner/scanner_test_2.txt");
        assertEquals(scanner.nextToken(),ScannerToken.IF.toString());
        assertEquals(scanner.nextToken(),ScannerToken.IDENTIFIER.toString());
        assertEquals(scanner.nextToken(),ScannerToken.COND_EQUAL.toString());
        assertEquals(scanner.nextToken(),ScannerToken.STRING_LIT.toString());

    }


    @Test
    public void code1() throws Exception{
        f("simple_parser/code1.txt");
        assertEquals(scanner.nextToken(),ScannerToken.SEMICOLON.toString());
    }


}
