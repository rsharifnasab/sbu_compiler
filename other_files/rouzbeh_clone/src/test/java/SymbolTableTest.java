

import java.io.*;
import java.nio.file.*;
import java.net.*;

import static org.junit.Assert.*;
import org.junit.*;

import compiler.scanner.*;
import compiler.scanner.token.*;

import compiler.symtab.*;

import compiler.util.*;


public class SymbolTableTest{

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



}
