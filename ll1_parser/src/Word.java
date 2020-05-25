
public class Word{
    public final WordType wordType;
    private final String content;

    public Word(String content){
            wordType = stringToWordType(content);
            this.content = content;
    }

    private static WordType stringToWordType(String content){ ///-->if a word is totally lowercase return erminal
        return content.toLowerCase().equals(content)?  WordType.TERMINAL : WordType.NON_TERMINAL;
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
    public int hashCode(){ return content.hashCode() + 31; }

    @Override
    public String toString(){ return content; }
}
