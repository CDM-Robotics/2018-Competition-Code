package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.autonomous.Constants;
import org.cdm.team6072.autonomous.MotionProfileExample;
import util.CrashTracker;

public class Elevator extends Subsystem {

    /**
     * Specify the direction the elevator should move
     */
    public static enum Direction {
        Up,
        Down
    }

    /**
     * Specify the target position we want to reach.
     * Might be replaced by an enum or some other way of specifying desired state
     */
    private static double mTarget;


    private WPI_TalonSRX mElevatorTalon;

    private MotionProfileExample mMotionExample;


    private static Elevator mInstance;
    public static Elevator getInstance() {
        if (mInstance == null) {
            mInstance = new Elevator();
        }
        return mInstance;
    }

    private Elevator() {
        CrashTracker.logMessage("Elevator Subsystem initializing");
        try {
            mElevatorTalon = new WPI_TalonSRX(RobotConfig.ELEVATOR_TALON);
            mElevatorTalon.getSensorCollection().setQuadraturePosition(0, 10);
            //mElevatorTalon.set(ControlMode.MotionProfile, 1);

            mMotionExample = new MotionProfileExample(mElevatorTalon);
            //mElevatorTalon.set(ControlMode.Current, ControlMode.Current.value);
            /*mElevatorTalon.set(ControlMode.MotionProfile, ControlMode.MotionProfile.value);
            mElevatorTalon.configOpenloopRamp(2, 0);*/
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
    }


    public void setupProfile() {
        System.out.println("Elevator.setupProfile:  setting up ");
        System.out.println("device (encoder): " + this.mElevatorTalon.getSensorCollection().toString());

        this.mElevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        this.mElevatorTalon.setSensorPhase(true);
        this.mElevatorTalon.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);
        this.mElevatorTalon.selectProfileSlot(0, 0 );

        this.mElevatorTalon.config_kF(0, 0.0, Constants.kTimeoutMs);
        this.mElevatorTalon.config_kP(0, 0.0, Constants.kTimeoutMs);
        this.mElevatorTalon.config_kI(0, 0.0, Constants.kTimeoutMs);
        this.mElevatorTalon.config_kD(0,0.0, Constants.kTimeoutMs);

        //this.masters.get(i).setControlFramePeriod(10, Constants.kTimeoutMs);
        this.mElevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
    }


    @Override
    protected void initDefaultCommand() {

    }

    public void setTarget(double target) {
        mTarget = target;
    }


    /**
     * Thhis needs to return true when the elevator has reached the target position,
     * or if the elevator has hit a limit switch
     * @return
     */
    public boolean targetReached() {
        return false;
    }

    public void resetSystemState() {
        mElevatorTalon.setInverted(false);
    }


    public void move(Direction dir, double speed) {
        if (dir == Direction.Up) {
            mElevatorTalon.setInverted(false);
        }
        else {
            mElevatorTalon.setInverted(true);
        }
        mElevatorTalon.set(ControlMode.PercentOutput, speed);
    }

    public void stop() {
        mElevatorTalon.set(0);
    }





    public void updateTalonRequiredMPState() {
        SetValueMotionProfile setOutput = this.mMotionExample.getRequiredTalonMPState();

        //System.out.println("Elevator.updateTalonRequiredMPState: elevator val: " + setOutput.value);
        this.mElevatorTalon.set(ControlMode.MotionProfile, setOutput.value);
    }


    public MotionProfileExample getMotionExample() {
        return mMotionExample;
    }


}
