package compiler.util;

import org.objectweb.asm.*;
import java.io.*;

import static org.objectweb.asm.Opcodes.*;

public class CodeWrite {

    public static String OUTCLASS_NAME;
    public static File OUTPUT_FILE;
    public static final String SUPER_CLASS = "java/lang/Object";

    public static ClassWriter mainCLW;
    public static ClassWriter structCLW;
    public static MethodVisitor mVisit;

    public static void setOutput(File output) {
        OUTPUT_FILE = output;
        OUTCLASS_NAME = Utils.getClassNameFromFile(output);

        //initializeClass();
        //writePrintFunction();
      //  writeSizeofFunction();
    }


    public static void initializeClass(){ // 1+1 

        Logger.log("Initializing code generator");
        mainCLW = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        mainCLW.visit(V1_8, ACC_PUBLIC, OUTCLASS_NAME, null, SUPER_CLASS, null);

        mVisit = mainCLW.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mVisit.visitCode();
        mVisit.visitVarInsn(ALOAD, 0);
        mVisit.visitMethodInsn(INVOKESPECIAL, SUPER_CLASS, "<init>", "()V", false);
        mVisit.visitInsn(RETURN);
        mVisit.visitMaxs(1, 1);
        mVisit.visitEnd();
    }


    public static void writePrintFunction(){
        // print integer
        mVisit = mainCLW.visitMethod(ACC_PUBLIC | ACC_STATIC, "print", "(I)V", null, null);
        mVisit.visitCode();
        mVisit.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mVisit.visitVarInsn(ILOAD, 0);
        mVisit.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
        mVisit.visitInsn(RETURN);
        mVisit.visitMaxs(1, 1);
        mVisit.visitEnd();

        // print long long
        mVisit = mainCLW.visitMethod(ACC_PUBLIC | ACC_STATIC, "print", "(J)V", null, null);
        mVisit.visitCode();
        mVisit.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mVisit.visitVarInsn(LLOAD, 0);
        mVisit.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
        mVisit.visitInsn(RETURN);
        mVisit.visitMaxs(1, 1);
        mVisit.visitEnd();

        // print float
        mVisit = mainCLW.visitMethod(ACC_PUBLIC | ACC_STATIC, "print", "(F)V", null, null);
        mVisit.visitCode();
        mVisit.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mVisit.visitVarInsn(FLOAD, 0);
        mVisit.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(F)V", false);
        mVisit.visitInsn(RETURN);
        mVisit.visitMaxs(1, 1);
        mVisit.visitEnd();

        //print double
        mVisit = mainCLW.visitMethod(ACC_PUBLIC | ACC_STATIC, "print", "(D)V", null, null);
        mVisit.visitCode();
        mVisit.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mVisit.visitVarInsn(DLOAD, 0);
        mVisit.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(D)V", false);
        mVisit.visitInsn(RETURN);
        mVisit.visitMaxs(1, 1);
        mVisit.visitEnd();


        //print String
        mVisit = mainCLW.visitMethod(ACC_PUBLIC | ACC_STATIC, "print", "(Ljava/lang/String;)V", null, null);
        mVisit.visitCode();
        mVisit.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mVisit.visitVarInsn(ALOAD, 0);
        mVisit.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        mVisit.visitInsn(RETURN);
        mVisit.visitMaxs(2, 1);
        mVisit.visitEnd();
    }

    public static void end(){
        Logger.log("writing generated code into output file");
        mainCLW.visitEnd();
        writeAndCheckClass(mainCLW.toByteArray(), OUTPUT_FILE, OUTCLASS_NAME );
        Logger.close();
    }

    public static void writeStructureClassCode(String record_out){
        Logger.log("writing record to file");
        File record_out_file = new File( 
                    OUTPUT_FILE.getParentFile().getName(),
                    record_out+".class"
                );
        writeAndCheckClass(structCLW.toByteArray(), record_out_file, record_out); 
    }

    private static void writeAndCheckClass(byte[] b,File location, String className){
        finalCheckClass(b,className);
        try(OutputStream out = new FileOutputStream(location)){
            out.write(b);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    
    @SuppressWarnings("rawtypes")
    private static Class finalCheckClass(byte[] b, String className) {
    // Override defineClass (as it is protected) and define the class.
    Class clazz = null;
    try {
      ClassLoader loader = ClassLoader.getSystemClassLoader();
      Class cls = Class.forName("java.lang.ClassLoader");
      java.lang.reflect.Method method =
          cls.getDeclaredMethod(
              "defineClass", 
              new Class[] { String.class, byte[].class, int.class, int.class });

      // Protected method invocation.
      method.setAccessible(true);
      try {
        Object[] args = 
            new Object[] { className, b, Integer.valueOf(0), Integer.valueOf(b.length)};
        clazz = (Class) method.invoke(loader, args);
        clazz.getConstructor().newInstance();
      } finally {
        method.setAccessible(false);
      }
    } catch (Throwable e) {
        Logger.error("compilation failed");
    }
    System.out.println("check doneeeeeeee");

    return clazz;
  }

}
