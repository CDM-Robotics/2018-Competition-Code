package util;

public class Logger {
    private static Logger mInstance;

    private Logger() {

    }

    public static Logger getInstance() {
        if (mInstance == null) {
            mInstance = new Logger();
        }
        return mInstance;
    }

    public void printBanner(String message) {
        System.out.println("*********************************");
        System.out.println("   " + message);
        System.out.println("*********************************");
    }

    public void printRobotAction(String message) {
        System.out.println(">>> " + message);
    }

    public void printError(String message) {
        System.out.println("!!!!!!!! " + message + " !!!!!!!!");
    }
}
