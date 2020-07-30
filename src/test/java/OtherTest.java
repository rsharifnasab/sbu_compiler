
import java.io.*;
import java.nio.file.*;
import java.net.*;

import static org.junit.Assert.*;
import org.junit.*;

import compiler.util.*;

public class OtherTest{

    @After
    public void deleteLog(){
        Logger.close();
    }

    @Test
    public void loggerTest(){
        Logger.log("yohho, Im testing logger");
    }

    @Test
    public void loggNullTest(){
        Logger.log("another test for logger");
        Logger.close();
        Logger.log("after null and close");
    }

}
