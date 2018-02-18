package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;



public class IntakePneumaticsSys extends Subsystem {


    public static enum SolenoidType {
        GRABBER
    }


    // IMPORTANT NOTE
    // DoubleSolenoid class abstracts away the PCM. It's being detected by WPILib
    private DoubleSolenoid mGrabberSolenoid;



    private Compressor mCompressor;


    private static IntakePneumaticsSys mInstance;
    public static IntakePneumaticsSys getInstance() {
        if (mInstance == null) {
            mInstance = new IntakePneumaticsSys();
        }
        return mInstance;
    }

    private IntakePneumaticsSys() {
        //mGrabberSolenoid = new DoubleSolenoid(RobotConfig.INTAKE_OPEN_SOLENOID_ON, RobotConfig.INTAKE_OPEN_SOLENOID_OFF);
        // the compressor will automatically stop when the pressure gets too high
        System.out.println("IntakePneumaticsSys.ctor  --------------------------------------------------------");
        mCompressor = new Compressor(RobotConfig.PMC_ID);
        mCompressor.start();
    }


    @Override
    protected void initDefaultCommand() {

    }



    public void OpenIntake() {
        System.out.println("IntakeMotorSys.OpenIntake: exec");
        turnSolenoidOff(IntakePneumaticsSys.SolenoidType.GRABBER);
    }


    public void CloseIntake() {
        System.out.println("IntakeMotorSys.CloseIntake: exec");
        turnSolenoidOn(IntakePneumaticsSys.SolenoidType.GRABBER);
    }


    private void turnSolenoidOn(SolenoidType type) {
        changeMode(type, true);
    }


    private void turnSolenoidOff(SolenoidType type) {
        changeMode(type, false);
    }


    private void changeMode(SolenoidType type, Boolean on) {
        DoubleSolenoid.Value val;
        System.out.println("IntakePneumaticsSys.changeMode: exec");
        if (on) {
            val = DoubleSolenoid.Value.kForward;
        } else {
            val = DoubleSolenoid.Value.kReverse;
        }

        if (type == SolenoidType.GRABBER) {
            mGrabberSolenoid.set(val);
        }
    }





}
