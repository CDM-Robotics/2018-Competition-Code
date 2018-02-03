package util;

import java.io.*;
import java.util.ArrayList;

public class FileParser {
    private static FileParser mInstance;

    public static FileParser getInstance() {
        if (mInstance == null) {
            mInstance = new FileParser();
        }
        return mInstance;
    }

    private FileParser() {
    }

    public double [][] parsePointsCsv(String path) throws IOException {
        String line="";
        BufferedReader reader = new BufferedReader(new InputStreamReader(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        }));
        double [][] points = new double[400][3];

        int i = 0;
        System.out.println("parsing points");
        System.out.println((int)reader.lines().count());
        System.out.println(reader.readLine());
        while((line = reader.readLine()) != null) {
            String[] items = line.split(",");
            System.out.println(items);
            points[i] = new double[]{Double.parseDouble(items[0]), Double.parseDouble(items[1]), Double.parseDouble(items[2])};
            i++;
        }
        return points;
    }
}
