import java.util.*;

public class ParseTable{

    private final Map<Word, Map<Word,  Set<ProductionRule>  >> innerMap;

    public ParseTable(){
        innerMap = new HashMap<>();
    }

    public void put(Word key1, Word key2, ProductionRule value) {
        throw new RuntimeException("not implemented yet");
    }


    @Override
    public String toString(){
        throw new RuntimeException("not implemented yet");
    }
}
