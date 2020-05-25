import java.io.FileNotFoundException;
import java.util.*;

public class LL1 {
    public final Grammer grammer;

    public LL1 (String filename) throws FileNotFoundException {
        this.grammer = new Grammer(filename);
    }

    public ParseTable run(){
        throw new RuntimeException("not implemented yet");
    }

    public Set<Word> first(Word w){
        Set<Word> set = new HashSet<>();
        if(w.isTerminal()) throw new IllegalArgumentException
                ("first should only calculated for non-terminals");

        for (ProductionRule pr : grammer.prod_rules) {
            if(pr.leftSide.equals(w) && !pr.visited){
                pr.visited = true;
                for (Word word : pr.rightSide) {

                    if(word.isNonTerminal()){
                        if(is_nullable(word)){continue;}
                        else {first(word);}
                    }
                    else{ set.add(word); }
                }


            }

        }
        return set;
    }


    //----------------------------------------------------------------------------------------------------------

    public Set<Word> follow(Word w){
        if(w.isTerminal()) throw new IllegalArgumentException("follow should only calculated for non-terminals");
        //todo
        throw new RuntimeException("not implemented yet");
    }

    //-----------------------------------------
    public boolean is_nullable(Word w){
        boolean flag = true;

        if(w.isTerminal() && !w.toString().equals("#"))
            return false;
        else {

           for (ProductionRule pr : grammer.prod_rules) {

             if(pr.leftSide.equals(w) && !pr.visited){
                pr.visited = true;

                 for (Word word : pr.rightSide) {
                     //if all of them are nullable return true else false
                     if(!is_nullable(word))
                     { flag = false;
                       break;
                     }
                     else {flag = true;}

                 }
              }
           }

        }

        for (ProductionRule pr : grammer.prod_rules) {
            pr.visited = false;
        }



        return flag;
    }
}
