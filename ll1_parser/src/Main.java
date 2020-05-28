
import java.io.*;
import java.util.*;

public class Main {

    private static String getFileName(String[] args){
        if(args.length == 1) return args[0];
        System.err.println("enter input filename");
        return new java.util.Scanner(System.in).next();
    }

    public static void main(String[] args) throws IOException {

        String filename = getFileName(args);
        LL1 parser = new LL1(filename);
        
        System.out.println(parser.firstsToString());
        System.out.println(parser.followsToString());
        
        System.out.println(parser.run());

    } // end main method 

} // end class 
