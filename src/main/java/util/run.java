package util;

import java.io.IOException;

public class run {

    public static void main(String args[]) {
        try {
            String path = "/Users/trito/Triton-Tech-2018/src/main/resources/MotionProfiles/elevatorUp.csv";
            double[][] points = FileParser.getInstance().parsePointsCsv(path);
            System.out.println(points[1][1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
