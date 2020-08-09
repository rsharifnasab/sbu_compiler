package compiler.ast.type;

import compiler.util.*;
import compiler.symtab.dscp.AbstractDescriptor;

public class Type {

    public static final Type BOOL = new Type("Z");
    public static final Type CHAR = new Type("C");
    public static final Type INT = new Type("I");
    public static final Type LONG = new Type("J");
    public static final Type FLOAT = new Type("F");
    public static final Type DOUBLE = new Type("D");
    public static final Type VOID = new Type("V");
    public static final Type STRING = new Type("Ljava/lang/String;");
    public static final Type AUTO = new Type("A");
    public static final Type BOOL_ARRAY = new Type("[Z");
    public static final Type CHAR_ARRAY = new Type("[C");
    public static final Type INT_ARRAY = new Type("[I");
    public static final Type LONG_ARRAY = new Type("[J");
    public static final Type FLOAT_ARRAY = new Type("[F");
    public static final Type DOUBLE_ARRAY = new Type("[D");

    private String type;

    public Type(String type) {
        this.type = type;
    }

    public String typeName() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type1 = (Type) o;
        return type.equals(type1.type);
    }

    @Override
    public String toString() {
        return type;
    }

    public static void inferType(AbstractDescriptor descriptor, Type type) {
        if (descriptor.getType() == AUTO)
            descriptor.setType(type);
    }

    public static Type toArray(Type t) {
        if (t == BOOL)
            return BOOL_ARRAY;
        else if (t == CHAR)
            return CHAR_ARRAY;
        else if (t == INT)
            return INT_ARRAY;
        else if (t == LONG)
            return LONG_ARRAY;
        else if (t == FLOAT)
            return FLOAT_ARRAY;
        else if (t == DOUBLE)
            return DOUBLE_ARRAY;
        else
            Logger.error("this type can't be used in arrays");
        return null;
    }

    public static Type toSimple(Type t) {
        if (t == BOOL_ARRAY)
            return BOOL;
        else if (t == CHAR_ARRAY)
            return CHAR;
        else if (t == INT_ARRAY)
            return INT;
        else if (t == LONG_ARRAY)
            return LONG;
        else if (t == FLOAT_ARRAY)
            return FLOAT;
        else if (t == DOUBLE_ARRAY)
            return DOUBLE;
        else
            Logger.error("invalid array type");
        return null;
    }


}
