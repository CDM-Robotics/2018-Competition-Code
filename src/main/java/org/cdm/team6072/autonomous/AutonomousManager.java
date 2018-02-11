package org.cdm.team6072.autonomous;

public class AutonomousManager {
    private static AutonomousManager mInstance;


    public static AutonomousManager getInstance() {
        if (mInstance == null) {
            mInstance = new AutonomousManager();
        }
        return mInstance;
    }

    private AutonomousManager() {
    }
}
