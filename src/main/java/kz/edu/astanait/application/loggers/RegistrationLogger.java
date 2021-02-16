package kz.edu.astanait.application.loggers;

import kz.edu.astanait.application.models.User;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationLogger implements Runnable {
    private final File file;
    private final User user;

    public RegistrationLogger(User user) {
        file = new File("src/main/java/kz/edu/astanait/application/loggers/files/registration.log");
        this.user = user;
    }

    @Override
    public void run() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        String logMsg = '[' + dateFormat.format(currentDate)
                + " " + user.getId()
                + " " + user.getUsername()
                + "]";

        LogWriter.writeLog(file, logMsg);
    }

}
