import java.util.*;
import java.util.stream.*;

public class ProductionRule {
    static int count = 0;

    public final int num;
    final String [] rule;
    final Word leftSide;
    final List<Word> rightSide;

    public ProductionRule(String rule){
        this.num = count++;
        this.rule = rule.trim().split(":");

        this.leftSide = new Word( this.rule[0].trim() );
        this.rightSide = Arrays.stream(this.rule[1].trim().split(" "))
            .map(Word::new)
            .collect(Collectors.toList());
    }


    @Override
    public String toString(){ return Integer.toString(num); }

}
