package compiler.scanner.token;

import java.util.*;

public enum GraphToken implements Token{
    ALAKI;
   // OTHER,
   // MAIN;



    static final Map<String, GraphToken> nameToValueMap =
        new HashMap<String, GraphToken>();

    static {
        for (GraphToken value : EnumSet.allOf(GraphToken.class)) {
            nameToValueMap.put(value.name(), value);
        }
    }

}
