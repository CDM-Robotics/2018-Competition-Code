package org.cdm.team6072.subsystems;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.HashMap;


public class CameraManager {

    public static CameraManager mInstance;

    private UsbCamera cam0;
    private HashMap<String, Thread> camThreads;

    private CameraManager() {
        this.camThreads = new HashMap<String, Thread>();
    }

    public static CameraManager getInstance() {
        if (mInstance == null) {
            mInstance = new CameraManager();
        }
        return mInstance;
    }

    private void initializeCameras() {
        camThreads.put("Cam0", new Thread(() -> {
            cam0 = CameraServer.getInstance().startAutomaticCapture(0);
            cam0.setResolution(640, 480);
        }));
    }

    public void runCameras() {
        // start each camera thread
        for (String id: camThreads.keySet()) {
            this.camThreads.get(id).start();
        }
    }

    public void runFilter() {

        Thread filterThread = new Thread(() -> {
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Blurr", 640, 480);

            Mat source = new Mat();
            Mat output = new Mat();

            while (!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        });
        filterThread.start();

    }

}
