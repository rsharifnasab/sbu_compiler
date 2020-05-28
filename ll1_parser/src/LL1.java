import java.io.*;
import java.util.*;
import java.util.stream.*;

public class LL1 {
    public final Grammer grammer;

    public LL1 (String filename) throws IOException {
        this.grammer = new Grammer(filename);
    }

    public ParseTable run(){ // why method name should be "run"?
        return createParseTable();
    }
    
    private Set<Word> firstOfProdRule(ProductionRule pr){
        Set<Word> ans = new HashSet<>();
        for( var w : pr.rightSide ){
            if (isNullable(w) ) continue;
            if( w.isNonTerminal() )
                ans.addAll(first(w));
            else{
                ans.add(w);
            }
            break; //todo
        }
        return ans;
    }
    
    private boolean isRuleNullable(ProductionRule pr){
        for( var w : pr.rightSide)
            if(! isNullable(w)) return false;
        return true;
    }

    private Set<Word> predict(ProductionRule pr){
        Set<Word> ans = new HashSet<>();

        for( var w : pr.rightSide ){
            if(isNullable(w)){
                if(w.equals(Word.lambda) ) {
                    ans.addAll(follow(pr.leftSide));
                } else ans.addAll(first(w));
            }
            else{
                if(w.isTerminal()) 
                    ans.add(w);
                else
                    ans.addAll(first(w));
                break;
            }
        }
        return ans;
    }


    public ParseTable createParseTable(){
        ParseTable pt = new ParseTable(grammer);

        for( var pr : this.grammer.prodRules ){
            var prodRulePredict = predict(pr);
            var leftNonTerminal =  pr.leftSide;
            for( var w : prodRulePredict ){
                pt.put(leftNonTerminal,w,pr);
            }
        }
        return pt;
    }

    public String firstsToString(){
        return this.grammer.prodRules.stream()
            .map( a -> a.leftSide )
            .distinct()
            .map( a->
                    "first(" + a.toString() + ")= " + first(a).toString() + "\n"
                )
            .reduce( (a,b) -> a+b )
            .orElse("error in printing firsts: no non-terminal found");
    }


    public String followsToString(){
        return this.grammer.prodRules.stream()
            .map( a -> a.leftSide )
            .distinct()
            .map( a->
                    "follow(" + a.toString() + ")= " + follow(a).toString() + "\n"
                )
            .reduce( (a,b) -> a+b )
            .orElse("error in printing follows: no non-terminal found");
    }



    public Set<Word> first(Word w){ 
        if(w.isTerminal()) throw new IllegalArgumentException
            ("first should only calculated for non-terminals");

        // first not nullable was terminal add that to the set
        Set<Word> set = grammer.prodRules.stream()
            .filter(a -> a.leftSide.equals(w)) // left hand side is w
            .map(a -> a.rightSide
                    .stream()
                    .dropWhile(this::isNullable)
                    .findFirst()
                    .orElse(Word.lambda)
                ).filter(Word::isTerminal)
            .collect(Collectors.toSet());

        ///---->if the the first not nullable was non terminal recursively call first on that
        grammer.prodRules.stream()
            .filter(a -> a.leftSide.equals(w))
            .map(a -> a.rightSide
                    .stream()
                    .dropWhile(this::isNullable)
                    .findFirst()
                    .orElse(Word.lambda)
                ).filter(Word::isNonTerminal)
            .forEach(a -> set.addAll(first(a))); //---> recursively call on each non terminal

        return set;
    }




    public Set<Word> follow(Word w){ 
        if(w.isTerminal()) throw new IllegalArgumentException("follow should only calculated for non-terminals");

        Set<Word> set = new HashSet<>();
        if (w.equals(grammer.startSymbol))
            set.add(Word.terminator);

        //case one : non terminal after w
        grammer.prodRules.stream()
            .filter(a -> a.rightSide.contains(w))
            .filter(a -> a.rightSide.lastIndexOf(w) != a.rightSide.size() - 1)
            .map(a -> a.rightSide.get(a.rightSide.indexOf(w) + 1))
            .filter(Word::isNonTerminal)
            .forEach(a -> set.addAll(first(a)));


        //--->next word after w is nullable --> T' : * F T' | # ----> follow(F) contains follow(T')
        grammer.prodRules.stream()
            .filter(a -> a.rightSide.contains(w))
            .filter(a -> a.rightSide.lastIndexOf(w) != a.rightSide.size() - 1)
            .filter(a ->
                    a.rightSide.get(a.rightSide.indexOf(w) + 1).isNonTerminal()
                    && isNullable(a.rightSide.get(a.rightSide.indexOf(w) + 1))
                   )
            .map(a -> follow(a.leftSide))
            .findFirst()
            .ifPresent(set::addAll);

        //case two : terminal after w
        grammer.prodRules.stream()
            .filter(a -> a.rightSide.contains(w))
            .filter(a -> a.rightSide.lastIndexOf(w) != a.rightSide.size() - 1)
            .map(a -> a.rightSide.get(a.rightSide.indexOf(w) + 1))
            .filter(Word::isTerminal)
            .forEach(set::add);

        //case three : nullable after w --> iterate past nullable words until and find the first of the first !nullable
        grammer.prodRules.stream()
            .filter(a -> a.rightSide.contains(w))
            .filter(a -> a.rightSide.lastIndexOf(w) != a.rightSide.size() - 1)
            .map(a -> a.rightSide.get(a.rightSide.indexOf(w) + 1))
            .filter(Word::isNonTerminal)
            .dropWhile(this::isNullable)
            .map(this::first)
            .findFirst().ifPresent(set::addAll);


        //case four : w is the last word of the right side of the rule
        grammer.prodRules.stream()
            .filter(a -> a.rightSide.contains(w))
            .filter(a -> a.rightSide.lastIndexOf(w) == a.rightSide.size() - 1)
            .map(a -> follow(a.leftSide))
            .findFirst().ifPresent(set::addAll);

        set.remove(Word.lambda);
        return set;
    }



    public boolean isNullable(Word w) {
        if(w.isTerminal())
            return w.equals(Word.lambda);

        return grammer.prodRules.stream()
            .filter(a -> a.leftSide.equals(w))
            .map(a -> a.rightSide)
            .anyMatch(
                    a -> a.stream().allMatch(this::isNullable)
                );
    }

    private Set<ProductionRule> whoContainsMe(Word w){
        return grammer.prodRules
            .stream()
            .filter( a -> a.rightSide.contains(w))
            .collect(Collectors.toSet());
    }
}
