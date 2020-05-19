import java.util.*;

public class LL1 {
    public final Grammer grammer;

    public LL1 (String filename){
        this.grammer = new Grammer(filename);
    }

    public ParseTable run(){
        throw new RuntimeException("not implemented yet");
    }

    public Set<Word> first(Word w){
        if(w.isTerminal()) throw new IllegalArgumentException("first should only calculated for non-terminals");

        throw new RuntimeException("not implemented yet");
    }


}
