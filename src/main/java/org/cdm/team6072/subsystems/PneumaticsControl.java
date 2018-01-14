package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import org.cdm.team6072.RobotConfig;

public class PneumaticsControl {
    private static PneumaticsControl mInstance;

    // IMPORTANT NOTE
    // DoubleSolenoid class abstracts away the PCM. It's being detected by WPILib
    private DoubleSolenoid mGrabberSolenoid;
    private Compressor mCompressor;

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

        mGrabberSolenoid = new DoubleSolenoid(RobotConfig.GRABBER_OPEN_SOLENOID_ON, RobotConfig.GRABBER_OPEN_SOLENOID_OFF);

        // the compressor will automatically stop when the pressure gets too high
        mCompressor = new Compressor();
        mCompressor.start();
    }


    private void changeMode(SolenoidType type, Boolean on) {
        DoubleSolenoid.Value val;
        if (on) {
            val = DoubleSolenoid.Value.kForward;
        } else {
            val = DoubleSolenoid.Value.kOff;
        }

        if (type == SolenoidType.GRABBER) {
            mGrabberSolenoid.set(val);
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
