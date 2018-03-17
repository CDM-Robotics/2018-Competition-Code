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

    private static boolean TALON_INVERTED_LEFT_IN = false;
    private static boolean TALON_INVERTED_RIGHT_IN = true;
    private static boolean TALON_INVERTED_LEFT_OUT = true;
    private static boolean TALON_INVERTED_RIGHT_OUT = false;



    public void runWheels(WheelDirn dir, double speed) {
        if (dir == WheelDirn.In) {
            mTalonLeft.setInverted(TALON_INVERTED_LEFT_IN);
            mTalonRight.setInverted(TALON_INVERTED_RIGHT_IN);
        }
        else {
            mTalonLeft.setInverted(TALON_INVERTED_LEFT_OUT);
            mTalonRight.setInverted(TALON_INVERTED_RIGHT_OUT);
        }
        mTalonLeft.set(ControlMode.PercentOutput, speed);
        mTalonRight.set(ControlMode.PercentOutput, speed);
    }


    /**
     * On stop, set wheels in very slow
     */
    public void stopWheels() {
        mTalonLeft.setInverted(TALON_INVERTED_LEFT_IN);
        mTalonRight.setInverted(TALON_INVERTED_RIGHT_IN);
        mTalonLeft.set(ControlMode.PercentOutput, 0.1);
        mTalonRight.set(ControlMode.PercentOutput, 0.1);
        // In Position mode, output value is in encoder ticks or an analog value, depending on the sensor.
//        double curPosnLeft = mTalonLeft.getSelectedSensorPosition(0);
//        mTalonLeft.set(ControlMode.Position, curPosnLeft);
//        double curPosnRight = mTalonRight.getSelectedSensorPosition(0);
//        mTalonRight.set(ControlMode.Position, curPosnRight);
    }



    //-----------------------------------------------------------------------------------
    //
    //  Opening and closing the intake is actually controlled by the pneumatic system




}
