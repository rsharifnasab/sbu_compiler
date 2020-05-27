import java.io.*;
import java.util.*;
import java.util.stream.*;

public class LL1 {
  public final Grammer grammer;
  public final ParseTable pt;

  public LL1 (String filename) throws IOException {
    this.grammer = new Grammer(filename);
    this.pt = new ParseTable(grammer);
  }

  public ParseTable run(){
    throw new RuntimeException("not implemented yet");
  }


  public Set<Word> first(Word w){ //todo
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
              ).filter(Word::isTerminal).collect(Collectors.toSet());

      ///---->if the the first not nullable was non terminal recursively call first on that
              grammer.prodRules.stream()
                      .filter(a -> a.leftSide.equals(w))
                      .map(a -> a.rightSide
                                      .stream()
                                      .dropWhile(this::isNullable)
                                      .findFirst()
                                      .orElse(Word.lambda)
                      ).filter(Word::isNonTerminal)
                      .forEach(a -> set.addAll(first(a)));//---> recursively call on each non terminal


      return set;

  }

  private Set<ProductionRule> whoContainsMe(Word w){
        return grammer.prodRules
                  .stream()
                  .filter( a -> a.rightSide.contains(w))
                  .collect(Collectors.toSet());
 }




  public Set<Word> follow(Word w){ //‌ُ TODO
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
              .filter(a -> a.rightSide.get(a.rightSide.indexOf(w) + 1).isNonTerminal() &&
                      isNullable(a.rightSide.get(a.rightSide.indexOf(w) + 1)))

              .map(a -> follow(a.leftSide)).findFirst()
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







      return set;
  }




   public boolean isNullable(Word w) {

       if (w.equals(Word.lambda))
           return true;
       else if(w.isTerminal())
           return false;

       else {
           boolean flag = false;
           int temp =  (int)grammer.prodRules.stream()
                   .filter(a -> a.leftSide.equals(w))
                   .map(a -> a.rightSide)
                   .filter(a -> a.stream().allMatch(this::isNullable)
                   ).count();


           if(temp > 0)
           {flag =  true;}


           return flag;
       }

   }



}

