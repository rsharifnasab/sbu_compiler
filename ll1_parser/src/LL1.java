import java.io.*;
import java.util.*;
import java.util.stream.*;

public class LL1 {
  public final Grammer grammer;

  public LL1 (String filename) throws IOException {
    this.grammer = new Grammer(filename);
  }

  public ParseTable run(){
    throw new RuntimeException("not implemented yet");
  }


  public Set<Word> first(Word w){ //todo
    if(w.isTerminal()) throw new IllegalArgumentException
      ("first should only calculated for non-terminals");
    return
            Stream.concat(
                  grammer // avalin gheire nullable
                  .prod_rules
                  .stream()
                  .filter( a -> a.leftSide.equals(w) ) // left hand side is w
                  .map( a->
                        a.rightSide
                              .stream()
                              .filter( b -> !is_nullable(b) )
                              .findFirst()
                              .orElse(Word.lambda)
                  )
                  ,

                  grammer // hame ye nullable ha
                  .prod_rules
                  .stream()
                  .filter( a -> a.leftSide.equals(w) ) // left hand side is w
                  .flatMap( a->
                        a.rightSide
                              .stream()
                              .takeWhile( b -> is_nullable(b) )
                  )

      ).collect( Collectors.toSet() );

  }



  public Set<Word> follow(Word w){
    if(w.isTerminal()) throw new IllegalArgumentException("follow should only calculated for non-terminals");

    throw new RuntimeException("not implemented yet");
  }




   public boolean is_nullable(Word w){
      if(w.isNullable == null)
         w.isNullable = grammer
            .prod_rules
            .stream()
            .filter( a -> a.leftSide.equals(w) ) // left hand side is w
            .anyMatch( // har kodum az ghavaed boud okeye
                  a -> a.rightSide
                        .stream()
                        .allMatch( // hame word hash nullable bashe
                              b -> is_nullable(b)
                        )
            );

    return w.isNullable;
  }
}
