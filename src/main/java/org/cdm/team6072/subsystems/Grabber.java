package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;
import util.CrashTracker;

public class Grabber extends Subsystem {
    private static Grabber mInstance;

    private Solenoid pneumaticsController;

    public static Grabber getInstance() {
        if (mInstance == null) {
            mInstance = new Grabber();
        }
        return mInstance;
    }

    private Grabber() {
        CrashTracker.logMessage("Grabber subsystem initialized");
        this.pneumaticsController = new Solenoid(RobotConfig.SOLENOID_CONTROL);
        this.pneumaticsController.free();
    }

    public Solenoid getPneumaticsController() {
        return pneumaticsController;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
