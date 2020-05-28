
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
        

        ParseTable pt = new ParseTable(parser.grammer);
        pt.put(
            new Word("S"),new Word("id"),
            parser.grammer.prodRules.stream().filter( a-> a.leftSide.equals(new Word("S")) ).findAny().get()
         );
        System.err.println(pt);

    } // end main method 

} // end class 
