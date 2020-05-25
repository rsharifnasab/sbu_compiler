import java.io.*;
import java.util.*;

public class Grammer{

    public ArrayList <ProductionRule> prod_rules;
    public ArrayList <Stack<String>> RHS; //---->stack of strings for storing each rule's right hand side

    public Grammer(String filename) throws FileNotFoundException {
        this.prod_rules = new ArrayList<>();
        this.RHS = new ArrayList<>();

        Scanner fileScan = new Scanner(new File(filename));


        //--->read the file line by line and construct production rules
        while (fileScan.hasNextLine()){

           ProductionRule pr = new ProductionRule(
                fileScan.nextLine()
           );
           //-------

           prod_rules.add(pr);

        }

    }


    //-----> method to assemble the rhs table
    public void build_RHS(){

        Stack<String> stack;
        for (ProductionRule pr : prod_rules) {

            String[] temp = pr.rule[1].trim().split(" ");
            stack = new Stack<>();

            for (int i = temp.length - 1; i >= 0 ; i--) {
                stack.push(temp[i]);

            }

            this.RHS.add(stack);

            }

        }


        public void display_RHS(){

            for (Stack<String> x : RHS) {
                for (String str: x) {
                    System.out.print(str+" , ");
                }
                System.out.println();
            }

        }



}
