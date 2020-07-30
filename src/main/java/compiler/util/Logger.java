package compiler.util;

import java.util.*;
import java.io.*;

public class Logger {

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

    public static void log(String log) {
        makeWriterOk();
        try {
            log = log + System.lineSeparator();
            //System.err.print(log);
            writer.write(log);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void error(String error) {
        error = "(Error) " + error;
        Logger.log(error);
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
