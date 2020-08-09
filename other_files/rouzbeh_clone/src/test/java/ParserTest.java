import java.io.*;
import java.nio.file.*;
import java.net.*;

import static org.junit.Assert.*;
import org.junit.*;

import compiler.parser.*;
import compiler.scanner.*;
import compiler.codegen.*;

import compiler.util.Utils;

public class ParserTest{

    Parser p;

    public static Parser l(String nptS,String codeS) throws Exception{
        InputStream nptFile = Utils.getInputStreamFromResource(nptS);
        InputStream code   = Utils.getInputStreamFromResource(codeS);
        File cgFile = new File("/tmp.txt");

        assertNotNull(nptFile);
        assertNotNull(code);

        var lexical = new Lexical(code);
        var codegen = new CodeGen(lexical);

        return new Parser(lexical,codegen,nptFile);
    }

    @Test
    public void testCons() throws Exception{
        p = l("simple_parser/parse_table_1.npt","simple_parser/code1.txt");
        assertNotNull(p);
    }

    @Test
    public void sampleParseTest() throws Exception{
        p = l("simple_parser/parse_table_1.npt","simple_parser/code1.txt");
        p.parse();
    }

    @Test
    public void tokenTest() throws Exception{
        p = l("simple_parser/parse_table_2.npt", "simple_parser/code2.txt");
        p.parse();
    }
/*


    @Test
    public void ourPT1() throws Exception{
        p = l("pt.npt","1.txt");
        p.parse();
    }


    @Test
    public void ourPT2() throws Exception{
        p = l("pt.npt","2.txt");
        p.parse();
    }


    @Test
    public void ourPT3() throws Exception{
        p = l("pt.npt","3.txt");
        p.parse();
    }


    @Test
    public void ourPT4() throws Exception{
        p = l("pt.npt","4.txt");
        p.parse();
    }

    @Test
    public void ourPT5() throws Exception{
        p = l("pt.npt","5.txt");
        p.parse();
    }
    */


    @Test
    public void ourPT6() throws Exception{
        p = l("pt.npt","6.txt");
        p.parse();
    }

}
