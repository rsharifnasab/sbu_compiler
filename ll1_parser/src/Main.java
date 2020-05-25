import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.println("welcome to this program!");
        System.out.println("enter the file name");
        String filename = input.next();

        LL1 parser = new LL1(filename);
        parser.grammer.build_RHS();

        //--test
        Word test = new Word("EXP");
        System.out.println(parser.is_nullable(test));




        //---------------------



    }




}
