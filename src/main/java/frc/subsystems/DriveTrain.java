package frc.subsystems;
//package frc.SpeedControllerArray;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.ControlBoard;
import frc.RobotConfig;
import frc.commands.TankDrive;
import frc.loops.Loop;


/**
 * Created by Cole on 9/3/17.
 */
public class DriveTrain extends Subsystem {
    // singleton instance
    public static DriveTrain instance = new DriveTrain();

    private boolean mIsBreakMode;

    public static DriveTrain getInstance() {
        return instance;
    }

    public enum DriveControlState {
        GO_FORWARD,
        GO_BACKWARD,
        TURN_TO_HEADING,
        VELOCITY_SETPOINT
    }

    private DriveControlState driveState;

    // loop run for state tracking in either teleop or autonomous mode
    private final Loop mainLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            synchronized (DriveTrain.this) {

            }
        }

        @Override
        public void onLoop(double timestamp) {
            synchronized (DriveTrain.this) {
                switch (driveState) {
                    case VELOCITY_SETPOINT:
                        return;
                    case GO_FORWARD:
                        goForward();
                        return;
                    case GO_BACKWARD:
                        goBackward();
                        return;
                    case TURN_TO_HEADING:
                        turnToHeading();
                        return;
                }
            }
        }

        @Override
        public void onStop(double timestamp) {
            // go to neutral or something
        }
    };

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

    //SpeedController leftSide = new SpeedControllerArray(leftSideMotors);


    // front left motor, rear left, front right, rear right
    //RobotDrive drive = new RobotDrive(leftSideMotors[2], leftSideMotors[0], rightSideMotors[2], rightMotors[0]);

    // shifter (changing gear)
    DoubleSolenoid shifter = new DoubleSolenoid(1,0,1);

    // encoders for drive train (source A, source B, reverse direction, encoding type)
    //Encoder leftEncoder = new Encoder(RobotConfig.ENCODER_LEFT_1, RobotConfig.ENCODER_LEFT_2, false, Encoder.EncodingType.k4X);
    //Encoder rightEncoder = new Encoder(RobotConfig.ENCODER_RIGHT_1, RobotConfig.ENCODER_RIGHT_2, false, Encoder.EncodingType.k4X);


    // Initialization
    private DriveTrain() {
        super("Drivetrain system");
        System.out.println("Drive train initialized");
        // Reverse motors so that they all spin in the correct direction
        //drive.setInvertedMotor(MotorType.kFrontLeft, true);
        //drive.setInvertedMotor(MotorType.kFrontRight, true);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new TankDrive());
    }

    private void turnToHeading() {

    }

    private void goForward() {
        System.out.println("Go forward");
        Command fwd = new Command() {
            @Override
            protected boolean isFinished() {
                return false;
            }

            protected void execute() {
                //drive.tankDrive();
                //drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 5);
            }
        };
    }

    private void goBackward() {

    }

    public synchronized void setHighGear() {
        shifter.set(DoubleSolenoid.Value.kForward);
    }

    public synchronized void setBrakeMode(boolean on)
    {
        if (mIsBreakMode != on) {
            //TODO
        }
    }

    // ips is inches per second
    public synchronized void setVelocitySetpoint(double left_ips, double right_ips) {
        driveState = DriveControlState.VELOCITY_SETPOINT;

        // update each motor speed
        for (int i = 0; i < leftMotors.length; i++) {
            leftMotors[1].set(inchesPerSecondToRpm(left_ips));
            rightMotors[1].set(inchesPerSecondToRpm(right_ips));
        }
    }

    private static double rotationsToInches(double rotations) {
        return rotations * (RobotConfig.DRIVE_WHEEL_DIAMETER * Math.PI);
    }

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