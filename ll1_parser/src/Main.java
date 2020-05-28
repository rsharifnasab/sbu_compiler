
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


        switch (filename) {
            case "../in.txt":

                System.out.println(parser.follow(new Word("S"))); // $
                System.out.println(parser.follow(new Word("EXP"))); // $, ) 
                System.out.println(parser.follow(new Word("EXP'")));// $, )
                System.out.println(parser.follow(new Word("TERM"))); // $, ), +, -
                System.out.println(parser.follow(new Word("TERM'")));// $, ), +, -
                System.out.println(parser.follow(new Word("FACTOR"))); // $, ), *,+, -, /
                System.out.println(parser.follow(new Word("ID"))); // $, ), *,+, -, /
                System.out.println(parser.follow(new Word("ID'")));// $, ), *,+, -, /

                
                break;

            case "../in2.txt":

                System.out.println(parser.first(new Word("E"))); // (, id
                System.out.println(parser.first(new Word("T"))); // (, id
                System.out.println(parser.first(new Word("F"))); // (, id

                System.out.println(parser.first(new Word("E'"))); // #, +
                System.out.println(parser.first(new Word("T'"))); // #, *

                System.out.println(parser.follow(new Word("E"))); // $, )
                System.out.println(parser.follow(new Word("E'")));// $, )
                System.out.println(parser.follow(new Word("T"))); // $, ), +
                System.out.println(parser.follow(new Word("T'")));// $, ), +
                System.out.println(parser.follow(new Word("F"))); // $, ), *, +

                System.out.println(parser.isNullable(new Word("E'"))); //true
                System.out.println(parser.first(new Word("E'"))); // #, +
                
                break;
            default:
                System.out.println("nothing to test here");
        } // end switch

    } // end main method 

} // end class 
