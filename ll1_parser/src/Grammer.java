import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.stream.*;

public class Grammer{

    public List <ProductionRule> prod_rules;
    public List <Stack<String>> RHS; //---->stack of strings for storing each rule's right hand side

    public Grammer(String filename) throws IOException {

        //--->read the file line by line and construct production rules
        prod_rules = Files.lines(Paths.get(filename))
              .map( a -> new ProductionRule(a) )
              .collect( Collectors.toList() );

        build_RHS();
    }


    //-----> method to assemble the rhs table
    public void build_RHS(){
        this.RHS = new ArrayList<>();

        Stack<String> stack = new Stack<>();
        for (ProductionRule pr : prod_rules) {
            stack.clear();

            String[] temp = pr.rule[1].trim().split(" ");
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
