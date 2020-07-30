

import java.io.*;
import java.nio.file.*;
import java.net.*;

import static org.junit.Assert.*;
import org.junit.*;

import compiler.scanner.*;
import compiler.scanner.token.*;
import compiler.scanner.symboltable.*;
import compiler.util.*;


public class SymbolTableTest{
/*
    SymbolTable symTab = null;

    @Before
    public void init(){ 
        this.symTab = new SymbolTable(); 
    }


    private void f(String add) throws IOException{ 
    }

    @Test
    public void contructTest() throws IOException {
        assertNotNull( symTab );
    }


    @Test
    public void testID1() throws Exception{
        Symbol s = Symbol.of(ScannerToken.IDENTIFIER,"salam");
        assertTrue(!symTab.exists(s));
        symTab.addID(s);
        assertEquals("salam",symTab.getLastValue());
        try{
            symTab.getLastValue();
            fail();
        } catch( Exception e){
            // ok
        }
    }

    @Test
    public void testLIT() throws Exception{
        Symbol s = Symbol.of(ScannerToken.FLOAT_LIT,"1.2");
        symTab.addLit(s);
        assertEquals("1.2",symTab.getLastValue());

        
    }
*/


}
