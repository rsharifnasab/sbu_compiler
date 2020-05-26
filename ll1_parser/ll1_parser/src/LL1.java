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
    return Stream.concat(
                grammer // avalin gheire nullable
                  .prodRules.stream()
                  .filter( a -> a.leftSide.equals(w) ) // left hand side is w
                  .map( a ->
                        a.rightSide
                              .stream()
                              .filter( b -> !isNullable(b) )
                              .findFirst()
                              .orElse(Word.lambda)
                  )
                  ,

                grammer // hame ye nullable haye ghabl az avalin gheire nullable
                  .prodRules.stream()
                  .filter( a -> a.leftSide.equals(w) ) // left hand side is w
                  .flatMap( a ->
                        a.rightSide
                              .stream()
                              .takeWhile(this::isNullable)
                  )

      ).collect( Collectors.toSet() );

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

    if (w.equals(Word.terminator))
        set.add(w);
      set.addAll( grammer.prodRules.stream()
              .filter(a -> a.rightSide.contains(w))
              .map(a -> a.rightSide)
              .map(a -> a.get(a.indexOf(w) + 1))
              .filter(Word::isTerminal)
              .collect(Collectors.toSet())
      );

      grammer.prodRules.stream()
              .filter(a -> a.rightSide.contains(w))
              .map(a -> a.rightSide)
              .map(a -> a.get(a.indexOf(w) + 1))
              .filter(Word::isNonTerminal)
              .forEach(a -> set.addAll(first(a)));




      return set;
  }




   public boolean isNullable(Word w){
      if(w.isNullable == null)
         w.isNullable = grammer
            .prodRules
            .stream()
            .filter( a -> a.leftSide.equals(w) ) // left hand side is w
            .anyMatch( // har kodum az ghavaed boud okeye
                  a -> a.rightSide
                        .stream()
                        .allMatch( // hame word hash nullable bashe
                              b -> isNullable(b)
                        )
            );

    return w.isNullable;
  }
}
