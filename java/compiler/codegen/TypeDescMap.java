package compiler.codegen;

import java.util.HashMap;
import java.util.Map;

public class TypeDescMap {
    public Map<String,String> map;
    public Map<String,String> mapDCLs;

    public TypeDescMap() {
        map = new HashMap<>();
        mapDCLs = new HashMap<>();
        map.put("void", "V");
        map.put("int", "I");
        map.put("byte", "B");
        map.put("short", "S");
        map.put("char", "C");
        map.put("float", "F");
        map.put("long", "J");
        map.put("double", "D");
        map.put("bool", "Z");
        map.put("string", "Ljava/lang/String;");

    }
    public void makeMapDCLs(String dcl , String value){

        if(dcl.equals("int") && Integer.parseInt(value) < 6){

        }
    }

}
