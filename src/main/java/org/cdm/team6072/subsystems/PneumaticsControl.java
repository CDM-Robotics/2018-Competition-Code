package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import org.cdm.team6072.RobotConfig;

public class PneumaticsControl {
    private static PneumaticsControl mInstance;

    private Compressor compressor;
    private Solenoid grabberSolenoid;

    public static enum SolenoidType {
        GRABBER
    }

    public static PneumaticsControl getInstance() {
        if (mInstance == null) {
            mInstance = new PneumaticsControl();
        }
        return mInstance;
    }

    private PneumaticsControl() {
        compressor = new Compressor(RobotConfig.COMPRESSOR);
        compressor.setClosedLoopControl(false);

        grabberSolenoid = new Solenoid(RobotConfig.SOLENOID_CONTROL);
    }


    private void changeMode(SolenoidType type, Boolean on) {
        if (type == SolenoidType.GRABBER) {
            grabberSolenoid.set(true);
        }
    }

    public void turnSolenoidOn(SolenoidType type) {
        changeMode(type, true);
    }

    public void turnSolenoidOff(SolenoidType type)
    {
        changeMode(type, false);
    }
}
