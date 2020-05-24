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

    private static WordType stringToWordType(String w){ ///-->if a word is totally uppercase return terminal
        return w.toUpperCase().equals(w)? WordType.NON_TERMINAL : WordType.TERMINAL;
    }

    public boolean isTerminal(){
        return wordType == WordType.TERMINAL;
    }
    public boolean isNonTerminal(){
        return wordType == WordType.NON_TERMINAL;
    }
    
    @Override 
    public boolean equals(Object o){
        return o instanceof Word && o.toString().equals(this.toString());
    }

    @Override
    public int hashCode(){ return w.hashCode() + 31; }

    @Override
    public String toString(){ return w; }
}
