package util;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class ControlledLogger {

    int count = 0;
    public ControlledLogger() {

    }


    public void print(int strike, String message) {
        count++;
        if (count % strike == 0) {
            System.out.println(message);
        }
    }

    public void print(int strike, Runnable func) {
        count++;
        if (count % strike == 0) {
            try {
                func.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

