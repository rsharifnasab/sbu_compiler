import java.util.*;
import java.util.stream.*;


public class ParseTable{

    private final Map<Word, Integer> satrIndex =  new HashMap<>();
    private final Map<Word, Integer> sotunIndex = new HashMap<>();
    int satrCount = 0;
    int sotunCount = 0;

    private final ProductionRule[][] arr;

    public ParseTable(Grammer g){
          int satrCount = (int) g.prodRules
                  .stream()
                  .flatMap( a-> a.rightSide.stream() )
                  .filter( a -> a.isNonTerminal() )
                  .count();

          int  sotunCount = (int) g.prodRules
                    .stream()
                    .flatMap( a-> a.rightSide.stream() )
                    .filter(a -> a.isTerminal())
                    .count();

            arr = new ProductionRule[satrCount][sotunCount]; // not sure
    }


    public void put(Word key1, Word key2, ProductionRule value) {
        if(key1.isTerminal()) throw new IllegalArgumentException("while putting in parse table key1 should be non-terminal");
        if(key2.isNonTerminal()) throw new IllegalArgumentException("while putting in parse table key2 should be terminal");

        arr[satr(key1)][sotun(key2)] = value;
    }

    private int satr(Word w){
        return satrIndex.computeIfAbsent(w,
                k -> satrIndex.size()
            );
    }
    private int sotun(Word w){
        return sotunIndex.computeIfAbsent(w,
                k -> sotunIndex.size()
            );
    }

    @Override
    public String toString(){
          return "\nthis is pt arr: \n"
          +Arrays.deepToString(arr)
          + "\n\n this is satr map"
          + satrIndex.toString()
          + "\n\n this is sotun map"
          + sotunIndex.toString()
          ;
        //throw new RuntimeException("not implemented yet");
    }
}
