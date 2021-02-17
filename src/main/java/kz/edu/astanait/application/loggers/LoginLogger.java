package kz.edu.astanait.application.loggers;

import kz.edu.astanait.application.models.User;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginLogger implements Runnable {
    private final File file;
    private final User user;

    public LoginLogger(User user) {
        file = new File("C:/Users/troy9/IdeaProjects/Assignment3_AJ2/src/main/java/kz/edu/astanait/application/loggers/files/login.log");
        this.user = user;
    }

    @Override
    public void run() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String logMsg = '[' + dateFormat.format(currentDate)
                + " " + user.getId()
                + " " + user.getUsername()
                + " " + user.getRole().getName()
                + "]";

        LogWriter.writeLog(file, logMsg);
    }
}
