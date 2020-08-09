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
                  .map( a-> a.leftSide )
                  .filter(Word::isNonTerminal)
                  .distinct()
                  .count(); 

        int  sotunCount = (int) Stream.concat(
                    g.prodRules
                    .stream()
                    .flatMap( a-> a.rightSide.stream() )
                    .filter(Word::isTerminal)
                    .filter( w -> !w.equals(Word.lambda) )
                    ,
                    Stream.of(Word.terminator) 
                )
                .distinct()
                .count();
        //System.err.println("tedad satr(nonterminal):"+satrCount);
        //System.err.println("tedad sotun(nerminal):"+sotunCount);

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
        String keySet = sotunIndex.keySet().stream().map( a-> a.toString()).reduce( (a,b) -> a+ "\t" + b).orElse(" ");
        StringBuilder sb = new StringBuilder("\t"+keySet+"\n");

        for (  Word satrW : satrIndex.keySet() ) {
            sb.append( satrW + "\t");
            for (Word sotunW : sotunIndex.keySet() ) {
                var pr = arr[satr(satrW)][sotun(sotunW)];
              //  var pr = arr[satr(sotunW)][sotun(satrW)];
                sb.append("["+(pr==null?"":pr)+"]\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
