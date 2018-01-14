package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;
import util.CrashTracker;

public class Grabber extends Subsystem {

    /**
     * Set direction wheels on grabber are to run
     */
    public static enum WheelDirn {
        In,
        Out
    }


    private Solenoid mPneumaticsController;

    private WPI_TalonSRX mTalonLeft;
    private WPI_TalonSRX mTalonRight;

    private static Grabber mInstance;
    public static Grabber getInstance() {
        if (mInstance == null) {
            mInstance = new Grabber();
        }
        return mInstance;
    }

    private Grabber() {
        CrashTracker.logMessage("Grabber subsystem initialized");
        mPneumaticsController = new Solenoid(RobotConfig.SOLENOID_CONTROL);
        mPneumaticsController.free();
        mTalonLeft = new WPI_TalonSRX(RobotConfig.GRABBER_TALON_LEFT);
        mTalonLeft.setNeutralMode(NeutralMode.Brake);
        mTalonRight = new WPI_TalonSRX(RobotConfig.GRABBER_TALON_RIGHT);
        mTalonRight.setNeutralMode(NeutralMode.Brake);
    }

    public Solenoid getPneumaticsController() {
        return mPneumaticsController;
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
    }




}
