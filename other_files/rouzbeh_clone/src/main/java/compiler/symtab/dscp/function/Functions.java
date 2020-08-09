package compiler.symtab.dscp.function;

import compiler.ast.type.Type;
import compiler.symtab.dscp.variable.VariableDescriptor;

import java.util.*;

public class Functions {

    private static Functions instance = new Functions();

    private final List<FunctionDescriptor> ALL_FUNCTIONS = new ArrayList<>();

    {
        List<VariableDescriptor> parameterList;
        FunctionDescriptor printInt = new FunctionDescriptor();
        VariableDescriptor printIntParam = new VariableDescriptor();
        parameterList = new ArrayList<>();
        printIntParam.setName("a");
        printIntParam.setConst(false);
        printIntParam.setType(Type.INT);
        printInt.setName("print");
        printInt.setCompleteDCL(true);
        printInt.setReturnType(Type.VOID);
        printInt.setParameterTypes(Type.INT);
        parameterList.add(printIntParam);
        printInt.setParameters(parameterList);

        FunctionDescriptor printLong = new FunctionDescriptor();
        VariableDescriptor printLongParam = new VariableDescriptor();
        parameterList = new ArrayList<>();
        printLongParam.setName("a");
        printLongParam.setConst(false);
        printLongParam.setType(Type.LONG);
        printLong.setName("print");
        printLong.setCompleteDCL(true);
        printLong.setReturnType(Type.VOID);
        printLong.setParameterTypes(Type.LONG);
        parameterList.add(printLongParam);
        printLong.setParameters(parameterList);

        FunctionDescriptor printFloat = new FunctionDescriptor();
        VariableDescriptor printFloatParam = new VariableDescriptor();
        parameterList = new ArrayList<>();
        printFloatParam.setName("a");
        printFloatParam.setConst(false);
        printFloatParam.setType(Type.FLOAT);
        printFloat.setName("print");
        printFloat.setCompleteDCL(true);
        printFloat.setReturnType(Type.VOID);
        printFloat.setParameterTypes(Type.FLOAT);
        parameterList.add(printFloatParam);
        printFloat.setParameters(parameterList);

        FunctionDescriptor printDouble = new FunctionDescriptor();
        VariableDescriptor printDoubleParam = new VariableDescriptor();
        parameterList = new ArrayList<>();
        printDoubleParam.setName("a");
        printDoubleParam.setConst(false);
        printDoubleParam.setType(Type.DOUBLE);
        printDouble.setName("print");
        printDouble.setCompleteDCL(true);
        printDouble.setReturnType(Type.VOID);
        printDouble.setParameterTypes(Type.DOUBLE);
        parameterList.add(printDoubleParam);
        printDouble.setParameters(parameterList);

        FunctionDescriptor printString = new FunctionDescriptor();
        VariableDescriptor printStringParam = new VariableDescriptor();
        parameterList = new ArrayList<>();
        printStringParam.setName("a");
        printStringParam.setConst(false);
        printStringParam.setType(Type.STRING);
        printString.setName("print");
        printString.setCompleteDCL(true);
        printString.setReturnType(Type.VOID);
        printString.setParameterTypes(Type.STRING);
        parameterList.add(printStringParam);
        printString.setParameters(parameterList);

        FunctionDescriptor len = new FunctionDescriptor();
        VariableDescriptor lenParam = new VariableDescriptor();
        parameterList = new ArrayList<>();
        lenParam.setName("a");
        lenParam.setConst(false);
        lenParam.setType(Type.STRING);
        len.setName("len");
        len.setCompleteDCL(true);
        len.setReturnType(Type.INT);
        len.setParameterTypes(Type.STRING);
        parameterList.add(lenParam);
        len.setParameters(parameterList);

        ALL_FUNCTIONS.add(printInt);
        ALL_FUNCTIONS.add(printLong);
        ALL_FUNCTIONS.add(printFloat);
        ALL_FUNCTIONS.add(printDouble);
        ALL_FUNCTIONS.add(printString);
        ALL_FUNCTIONS.add(len);

        //TODO add input
    }

    private Functions() {
    }

    public static Functions getInstance() {
        return instance;
    }

    public void addFunction(FunctionDescriptor descriptor) {
        ALL_FUNCTIONS.add(descriptor);
    }

    public boolean contains(String id, Type... parameterTypes) {
        FunctionDescriptor temp = new FunctionDescriptor();
        temp.setName(id);
        temp.setParameterTypes(parameterTypes);
        return ALL_FUNCTIONS.contains(temp);
    }

    public boolean containsName(String id) {
        for (FunctionDescriptor function : ALL_FUNCTIONS)
            if (function.getName().equals(id))
                return true;
        return false;
    }

    public FunctionDescriptor get(String id, Type... parameterTypes) {
        FunctionDescriptor temp = new FunctionDescriptor();
        temp.setName(id);
        temp.setParameterTypes(parameterTypes);
        return ALL_FUNCTIONS.get(ALL_FUNCTIONS.indexOf(temp));
    }

}
