import java.util.*;
import java.util.stream.*;


public class ProductionRule {
    static int count = 0;

    public final int num;
    final String [] rule;
    final Word leftSide;
    final List<Word> rightSide;


    boolean visited;


    public ProductionRule(String rule){
        this.num = count++;
        this.rule = rule.trim().split(":");

        // set left side
        this.leftSide = new Word( this.rule[0].trim() );

        // set right side
        this.rightSide = Arrays.stream(this.rule[1].trim().split(" "))
            .map(a -> new Word(a))
            .collect(Collectors.toList());

        this.visited = false;
    }

    //------------------------------------------------------------

    @Override
    public String toString(){ return Integer.toString(num); }

}
