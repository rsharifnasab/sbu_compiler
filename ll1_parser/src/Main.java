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

        //--test
        Word test = new Word("EXP");
        parser.grammer.printRHS();
        System.out.println(parser.isNullable(test));
        System.out.println(parser.first(test));
        //System.out.println(parser.follow(test));

        System.out.println(parser.first(new Word("ID")));
        System.out.println(parser.first(new Word("ID'")));

        parser.pt.put(new Word("EXP"),new Word("+"),
            new ProductionRule("EXP : TERM EXP'")
        );
        System.out.println( parser.pt.toString() );
    }

}
