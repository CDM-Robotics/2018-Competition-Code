package frc;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Core.java
 *
 * A singleton class which contains useful methods such as log, warn, etc...
 */

class Core {

    // Constants:
    private static final String LOG_DIR = "./logs/";
    private static final SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat LINE_FORMAT = new SimpleDateFormat("hh:mm:ss");

    // Properties:
    private BufferedWriter writer = null;

    // Constructor:
    private Core() {
        // Creating logs directory if one doesn't already exist.
        File logDir = new File(LOG_DIR);
        if (!logDir.isDirectory())
            if (!logDir.mkdir())
                throw new RuntimeException("Couldn't create logs directory.");

        // Generating log name.
        String today = LOG_FORMAT.format(new Date());

        //File[] matchingLogs = logDir.listFiles((dir, name) -> name.startsWith(today));

        /*if (matchingLogs == null) {
        File[] matchingLogs = logDir.listFiles((dir, name) -> name.startsWith(today));

        if (matchingLogs == null) {
            throw new RuntimeException("Couldn't list logs in logs directory.");
        }

        int logCount = matchingLogs.length;

        String logName = today + "-" + (logCount + 1) + ".log";

        // Creating log buffer.
        try {
            writer = new BufferedWriter(new FileWriter(LOG_DIR + logName, true));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create FileWriter instance.");
        }*/
        }
    }

    // Singleton Configuration:

    private static final Core instance = new Core();

    static Core getInstance() {
        return instance;
    }

    // Methods:
    private void append(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to log.");
        }
    }

    private void print(String type, String message) {
        String line = "[" + LINE_FORMAT.format(new Date()) + " " + type + "]: " + message;
        System.out.println(line); // Prints to console.
        append(line); // Prints to log.
    }

    void log(String message) {
        print("LOG", message);
    }

    void warn(String message) {
        print("WRN", message);
    }

}
