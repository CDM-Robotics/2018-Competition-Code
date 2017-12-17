package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.SpeedControllerArray;


/**
 * Created by Cole on 9/3/17.
 */
public class DriveTrain extends Subsystem {

    // singleton instance
    private static DriveTrain mInstance;

    public static DriveTrain getInstance() {
        if (mInstance == null) {
            mInstance = new DriveTrain();
        }
        return mInstance;
    }

    private boolean mIsBreakMode;

    public enum DriveControlState {
        GO_FORWARD,
        GO_BACKWARD,
        TURN_TO_HEADING,
        VELOCITY_SETPOINT
    }

    private DriveControlState mDriveState;

    // LEFT motors on the drive train
    VictorSP leftMotors[] = {
            new VictorSP(RobotConfig.LEFT_MOTOR_1),
            new VictorSP(RobotConfig.LEFT_MOTOR_2),
            new VictorSP(RobotConfig.LEFT_MOTOR_3)
    };

    // RIGHT motors on drive train
    VictorSP rightMotors[] = {
            new VictorSP(RobotConfig.RIGHT_MOTOR_1),
            new VictorSP(RobotConfig.RIGHT_MOTOR_2),
            new VictorSP(RobotConfig.RIGHT_MOTOR_3)
    };

    SpeedController[] leftSideMotors = { leftMotors[1], leftMotors[2] };
    SpeedController[] rightSideMotors = { rightMotors[1], rightMotors[2] };

    SpeedController leftSide = new SpeedControllerArray(leftSideMotors);
    SpeedController rightSide = new SpeedControllerArray(rightSideMotors);

    // RobotDrive is an FRC class that handles basic driving for robot
    // This should not be public - means external objects are modifying state
    public RobotDrive drive;

    // shifter (changing gear)
    DoubleSolenoid shifter = new DoubleSolenoid(1,0,1);

    // Initialization
    private DriveTrain() {
        System.out.println("6072: Drive train constructor");
        mDriveState = DriveControlState.GO_FORWARD;
        this.drive = new RobotDrive(leftSide, leftSideMotors[0], rightSide, rightMotors[0]);
    }


    @Override
    public void initDefaultCommand() {
        System.out.println("6072: init default command");
        teleopDrive();
    }


    // create a new command as an anonymous object, and pass it to the scheduler
    //
    // Taranis:   Y is 1   X is 0
    // Joystick:  Y is 1  X  is 0
    // Gamepad:   LY = 1   RY = 5  LTrigger = 2  RTrigger = 3  LX = 0  RX = 4
    private void teleopDrive() {
        System.out.println("6072: Go forward");
        Command driveCommand = new Command() {
            @Override
            protected boolean isFinished() {
                return false;
            }

            protected void execute() {
                System.out.println("executing DriveTrain.teleopDrive.tankDrive");

                // gamepad
                //drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 5);
                // 3D Pro Joystick
                //drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 0);
                // Taranis
                drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 0);
            }
        };
        Scheduler.getInstance().add(driveCommand);
    }



    private void turnToHeading() {
    }


    private void goBackward() {
    }



    public synchronized void setHighGear() {
        shifter.set(DoubleSolenoid.Value.kForward);
    }

    public synchronized void setBrakeMode(boolean on)
    {
        if (mIsBreakMode != on) {
        }
    }

    // ips is inches per second
    public synchronized void setVelocitySetpoint(double left_ips, double right_ips) {
        mDriveState = DriveControlState.VELOCITY_SETPOINT;

        // update each motor speed
        for (int i = 0; i < leftMotors.length; i++) {
            leftMotors[1].set(inchesPerSecondToRpm(left_ips));
            rightMotors[1].set(inchesPerSecondToRpm(right_ips));
        }
    }

    private static double rotationsToInches(double rotations) {
        return rotations * (RobotConfig.DRIVE_WHEEL_DIAMETER * Math.PI);
    }
            //TODO

    private static double rpmToInchesPerSec(double rpm) {
        return rotationsToInches(rpm)/60;
    }

    private static double inchesToRotations(double inches) {
        return inches/(RobotConfig.DRIVE_WHEEL_DIAMETER * Math.PI);
    }
    private static double inchesPerSecondToRpm(double ips) {
        return inchesToRotations(ips) * 60;
    }

}