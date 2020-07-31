package compiler.util;

import java.util.*;
import java.io.*;

public class Logger {


    /**
      a String just containing escape
     **/
    public static final String ESCAPE = ""+(char) 27 ;

    /**
      ansi character for reset
      if we print this, all setting of colored text will be reverted
      we print this after printing colored text
     **/
    public static final String Reset = ESCAPE + "[0m";

    public static final String Red = ESCAPE + "[31m";
    public static final String Blue = ESCAPE +  "[94m";
    public static final String Green = ESCAPE +  "[32m";
    public static final String Yellow = ESCAPE +  "[93m";
    public static final String White = ESCAPE +  "[97m";

    /**
      in case of pritning this, current line of terminal will be cleaned!
     **/
    public static final String ENTER_CLEARER = "\033[H\033[2J";
    public static final String CLEARER = "\033[H\033[2J\033[3J";
    /**
      print the toPrint string with the specified color
      we get color with the color enum
      and after printing, we reset terminal printing color
     **/
    public static void print(String toPrint, String color){
        String colorer = White;
        switch (color.toUpperCase()){
            case "RED":
                colorer = Red; break;
            case "BLUE":
                colorer = Blue; break;
            case "GREEN":
                colorer = Green; break;
            case "YELLOW":
                colorer = Yellow; break;
            default:
                colorer = White;
        }
        System.out.println( colorer + toPrint + Reset );
        System.out.flush();
    }



    private static final String LOG_FILE = "log.txt";


    private static FileWriter writer = null;

    private static void makeWriterOk() {
        if(writer != null) return; // write is ok
        try {
            writer = new FileWriter(LOG_FILE,true);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logToFile(String log) {
        makeWriterOk();
        try {
            log = log + System.lineSeparator();
            writer.write(log);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String log){
        print(log, "BLUE");
        Logger.logToFile(log);
    }

    public static void error(String error) {
        error = "(Error) " + error;
        print(error,"RED");
        Logger.logToFile(error);
        throw new RuntimeException(error);
    }

    public static void close() {
        if(writer == null) return;
        try{
            writer.close();
            writer = null;
            File logged = new File(LOG_FILE);
            if(logged.exists())
                logged.delete();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
