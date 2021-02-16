package kz.edu.astanait.application.loggers;

import java.io.*;

public class LogWriter {
    public static void writeLog(File file, String logMessage){
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(logMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
