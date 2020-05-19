enum WordType{
    TERMINAL, NON_TERMINAL
}

public class Word{
    private final WordType wordType;
    private final String w;
    public Word(String w){
            wordType = stringToWordType(w);
            this.w = w;
    }

    private static WordType stringToWordType(String w){
        return w.toUpperCase().equals(w)? WordType.TERMINAL : WordType.NON_TERMINAL;
    }

    private boolean isTerminal(){
        return wordType == WordType.TERMINAL;
    }
    private boolean isNonTerminal(){
        return wordType == WordType.NON_TERMINAL;
    }
    
    @Override 
    public boolean equals(Object o){
        return  o != null && o instanceof Word && o.toString().equals(this.toString()); 
    }

    @Override
    public int hashCode(){ return w.hashCode() + 31; }

    @Override
    public String toString(){ return w; }
}
