import java.util.ArrayList;

public class ProductionRule {
    public final int num;
    String [] rule;
    Word leftSide;
    ArrayList<Word> rightSide;
    boolean visited;


    public ProductionRule(int num){

        this.num = num;
        this.rule = new String[2];
        this.rightSide = new ArrayList<>();
        this.visited = false;

    }

    //-------setter for left side and right side of the rule-------
    public void setLeft(){

        this.leftSide = new Word( this.rule[0].trim());

    }

    public void setRight(){
        for (String str: this.rule[1].trim().split(" ")) {
            this.rightSide.add(new Word(str));
        }
    }
    //------------------------------------------------------------


    @Override
    public String toString(){ return ""+num; }





}
