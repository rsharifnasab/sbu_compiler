package compiler.scanner;

import java.util.Optional;

import compiler.scanner.token.*;

public class Symbol {

    public final String content;
    public final Token token;

    private Symbol(Token token, String content) {
        this.content = content;
        this.token = token;
    }

    public static Symbol of(Token tt){
        return new Symbol(tt,null);
    }

    public static Symbol of(Token tt, String content){
        return new Symbol(tt,content);
    }

    public Optional<String> content(){
        return Optional.ofNullable(content);
    }

    @Override
    public String toString(){
        return "sym "
            + token.toString()+ ", "+content();
    }

}

