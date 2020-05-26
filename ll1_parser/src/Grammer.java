import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.util.stream.*;

public class Grammer{

  final public List <ProductionRule> prodRules;
  final public List <Stack<Word>> RHS; //---->stack of Words for storing each rule's right hand side

  public Grammer(String filename) throws IOException {

    //--->read the file line by line and construct production rules
    prodRules = Files.lines(Paths.get(filename))
    .map( a -> new ProductionRule(a) )
    .collect( Collectors.toList() );

    RHS = build_RHS();
  }


  //-----> method to assemble the rhs table
  public List<Stack<Word>> build_RHS(){
    List <Stack<Word>> tempRHS = new ArrayList<>();

    for (ProductionRule pr : prodRules) {
      Stack<Word> stack = new Stack<>();

      String[] temp = pr.rule[1].trim().split(" ");
      for (int i = temp.length - 1; i >= 0 ; i--) {
        stack.push( new Word(temp[i]) );
      }
      tempRHS.add(stack);
    }
    return tempRHS;
  }

  public void display_RHS(){

/*    RHS.stream()
        .map( x-> String.join(",",x) )
        .forEach(System.out::println);
*/
    for (Stack<Word> x : RHS) {
      for (Word str: x) {
        System.out.print(str+" , ");
      }
      System.out.println();
    }
  }

}
