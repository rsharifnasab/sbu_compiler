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

        Grammer grammer = new Grammer(filename);
        grammer.build_RHS();

        System.out.println("RHS table: ");
        grammer.display_RHS();


        //---------------------



    }




}
