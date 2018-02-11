package org.cdm.team6072.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

import java.util.HashMap;


public class CameraManager {

    public static CameraManager mInstance;

    private UsbCamera cam0;
    private HashMap<String, Thread> camThreads;

    private CameraManager() {
        this.camThreads = new HashMap<String, Thread>();
    }

    public CameraManager getInstance() {
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

}
