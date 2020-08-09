package compiler.scanner.token;

public interface Token{


    static Token of(String value){
        Token scannerToken = ScannerToken.ntv.get(value);
        Token graphToken = GraphToken.nameToValueMap.get(value);

        if(scannerToken == null && graphToken == null)
            throw new RuntimeException("token "+value+" is not not valid");
        else if(scannerToken == null)
            return graphToken;
        else if(graphToken == null)
            return scannerToken;
        else throw new RuntimeException(value+" exists in both enums!");
    }

    static String translate(String input){
        Token token = ScannerToken.ntv.get(input);
        
        if(token!=null) // input exists in enum
            return token.toString(); 
        else return input; // graph name
    }
}
