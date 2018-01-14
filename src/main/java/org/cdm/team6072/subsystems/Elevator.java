package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.commands.elevator.MoveElevatorCmd;
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
            mElevatorTalon.set(ControlMode.MotionProfile, ControlMode.MotionProfile.value);
            mElevatorTalon.configOpenloopRamp(2, 0);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
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


}
