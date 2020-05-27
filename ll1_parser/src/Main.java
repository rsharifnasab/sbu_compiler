
import java.io.*;
import java.util.*;

public class Main {

    private static String getFileName(String[] args){
        if(args.length == 1) return args[0];
        System.err.println("enter input filename");
        return new java.util.Scanner(System.in).next();
    }

    public static void main(String[] args) throws IOException {

        System.out.println("welcome to this program!");
        String filename = getFileName(args);

        LL1 parser = new LL1(filename);


        System.out.println(parser.isNullable(new Word("E'")));
        System.out.println(parser.first(new Word("E'")));
        System.out.println(parser.follow(new Word("E'")));


       // System.out.println(parser.first(new Word("ID")));
       // System.out.println(parser.first(new Word("S")));
        //System.out.println(parser.follow(new Word("TERM")));

        /*parser.pt.put(new Word("EXP"),new Word("+"),
            new ProductionRule("EXP : TERM EXP'")
        );
        System.out.println( parser.pt.toString() );*/
    }

}
