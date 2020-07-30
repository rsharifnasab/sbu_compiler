package compiler.util;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import java.io.*;
import java.net.*;
import java.util.*;

public class Utils{

    private final static Scanner scanner = new Scanner(System.in);

    public static String input(String message){
        System.err.println(message);
        return scanner.next();
    }

    public static File getInputFile(String[] args){
        if(args.length == 1 || args.length == 2)
            return new File(args[0]);
        else
            return new File(input("please enter input file path"));
    }

    public static File getOutputFile(String[] args){
        if(args.length == 2)
            return new File(args[1]);
        else
            return new File(input("please enter input file path"));
   }



    // get file from resources folder
    public static InputStream getInputStreamFromResource(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        return classLoader.getResourceAsStream(fileName);
        /*
            URI uri = classLoader.getResource(fileName).toURI();
            System.out.println(new File(classLoader.getResource(fileName).getFile()).canRead());
            if(3>2) return new File(classLoader.getResource(fileName).getFile());

            Map<String, String> env = new HashMap<>();
            String[] array = uri.toString().split("!");
            if(array.length==1) return new File(array[0]);

            fs = FileSystems.newFileSystem(URI.create(array[0]), env);
            return fs.getPath(array[1]).toFile();

            //fs.close();
           //return Paths.get(resource.toURI()).toFile();
        } catch(Exception e){
            throw new RuntimeException("cannot open resource "+fileName,e);
        } finally {
            try{if(fs!=null)fs.close();}
            catch(Exception e){e.printStackTrace();} // F java
        }
        */
    }

    public static InputStream getParseTableFile(String inp){
        try{
            return getInputStreamFromResource(inp);
        } catch(Exception e){
            throw new RuntimeException("failed to load parser table from recourses",e);
        }
        /*
        File mehrshad = new File("inputFiles/"+inp);
        if(mehrshad.exists()) return  new InputStream(mehrshad); // other verison
        else throw new RuntimeException("
        */
    }
    
    public static String getClassNameFromFile(File f){
        String name = f.getName();
        if(name.contains(".")) 
            return name.substring(0, name.lastIndexOf('.'));
        else return name;
    }

}
