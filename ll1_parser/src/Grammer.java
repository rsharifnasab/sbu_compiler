import java.io.*;
import java.util.*;

public class Grammer{

    public ArrayList <ProductionRule> grm_rules;
    public ArrayList <Stack<String>> RHS; //---->stack of strings for storing each rule's right hand side

    public Grammer(String filename) throws FileNotFoundException {
        this.grm_rules = new ArrayList<>();
        this.RHS   = new ArrayList<>();
        File grammerFile = new File(filename);
        Scanner fileScan = new Scanner(grammerFile);

        int i = 0;
        //--->read the file line by line and construct production rules
        while (fileScan.hasNextLine()){
           String temp = fileScan.nextLine();
           ProductionRule pr = new ProductionRule(i);
           pr.rule = temp.trim().split(":");

           grm_rules.add(pr);

            i++;
        }

    }


    //-----> method to assemble the rhs table
    public void build_RHS(){

        Stack<String> stack;
        for (ProductionRule pr : grm_rules) {

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
