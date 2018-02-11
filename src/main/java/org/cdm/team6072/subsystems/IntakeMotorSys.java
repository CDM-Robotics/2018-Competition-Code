package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;
import util.CrashTracker;

public class IntakeMotorSys extends Subsystem {

    /**
     * Set direction wheels on intake are to run
     */
    public static enum WheelDirn {
        In,
        Out
    }

    public static enum GrabberPosition {
        Open,
        Closed
    }


    private WPI_TalonSRX mTalonLeft;
    private WPI_TalonSRX mTalonRight;

    private static IntakeMotorSys mInstance;
    public static IntakeMotorSys getInstance() {
        if (mInstance == null) {
            mInstance = new IntakeMotorSys();
        }
        return mInstance;
    }

    private IntakeMotorSys() {
        CrashTracker.logMessage("IntakeMotorSys subsystem initialized");

        mTalonLeft = new WPI_TalonSRX(RobotConfig.INTAKE_TALON_LEFT);
        mTalonLeft.setNeutralMode(NeutralMode.Brake);
        mTalonRight = new WPI_TalonSRX(RobotConfig.INTAKE_TALON_RIGHT);
        mTalonRight.setNeutralMode(NeutralMode.Brake);
    }

    @Override
    protected void initDefaultCommand() {

    }


    public void runWheels(WheelDirn dir) {
        if (dir == WheelDirn.In) {
            mTalonLeft.setInverted(false);
            mTalonRight.setInverted(true);
        }
        else {
            mTalonLeft.setInverted(true);
            mTalonRight.setInverted(false);
        }
        mTalonLeft.set(ControlMode.PercentOutput, 0.5);
        mTalonRight.set(ControlMode.PercentOutput, 0.5);
    }


    public void stopWheels() {
        mTalonLeft.set(ControlMode.PercentOutput, 0);
        mTalonRight.set(ControlMode.PercentOutput, 0);
        mTalonRight.setNeutralMode(NeutralMode.Brake);
        mTalonLeft.setNeutralMode(NeutralMode.Brake);
    }


    //-----------------------------------------------------------------------------------
    //
    //  Opening and closing the intake is actually controlled by the pneumatic system


    public void OpenGrabber() {
        System.out.println("IntakeMotorSys.OpenGrabber: exec");
       IntakePneumaticsSys.getInstance().turnSolenoidOff(IntakePneumaticsSys.SolenoidType.GRABBER);
    }


    public void CloseGrabber() {
        System.out.println("IntakeMotorSys.CloseGrabber: exec");
       IntakePneumaticsSys.getInstance().turnSolenoidOn(IntakePneumaticsSys.SolenoidType.GRABBER);
    }



}
