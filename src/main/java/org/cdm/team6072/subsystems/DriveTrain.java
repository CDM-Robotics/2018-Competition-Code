package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Encoder;
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


    /**
     *  The PWM signal used to control the Victor SP should be between 1-2ms in duration
     *  with a center (neutral) pulse of 1.5ms and a period between 2.9-100ms.
     *  The PWM period is how fast the robot controller can send a new PWM pulse.
     *  The amount of time between the rising edge of one PWM pulse to the next PWM pulse
     *  should not be less than 2.9ms or greater than 100ms.
     *
     *  Calibration
     *  The calibration of a Victor SP is essentially the scale of PWM input signal to output voltage.
     *  Different controllers may have different “max” and “min” PWM signals that may not correspond
     *  to the same Victor SP outputs. Calibrating the Victor SP allows it to adjust for these
     *  differences so that a “max” signal results in a “max” output.
     *  Calibrating can also correct issues caused by joysticks or gamepads with off-center neutral outputs.
     *  The Victor SP’s default calibration is compatible with the roboRIO control system
     *
     *  Brake & Coast Modes
     *  When a neutral PWM signal is applied to the Victor SP in Brake mode, the motor will resist rotation,
     *  especially high speed rotation. This is accomplished by essentially shorting the motor leads, which
     *  causes a Back Electromotive Force (Back-EMF) to resist the rotation of the motor.
     *  Brake mode does not have any effect when the motor is not rotating, but can make a large difference
     *  in robot behavior when used on a motor attached to a high reduction gearbox.
     *  Brake mode does not impact performance when a non-neutral PWM signal is applied.
     *  When a neutral PWM signal is applied to the Victor SP in Coast mode,
     *  Back-EMF will not be generated, so the motor’s rotation will not be affected by the Victor SP.
     *
     *  Switching between Brake & Coast: To switch between Brake and Coast mode,
     *  simply push the B/C CAL button at any time.
     *  The Victor SP is in
     *      Brake mode when the button is illuminated red and
     *      Coast when the red light is turned off.
     *  Brake/Coast settings are saved even if power is removed from the Victor SP.
     *
     * Note that the VictorSP uses the following bounds for PWM values.
     * These values should work reasonably well for most controllers, but if users experience issues
     * such as asymmetric behavior around the deadband or inability to saturate the controller in
     * either direction, calibration is recommended.
     * The calibration procedure can be found in the VictorSP User Manual available from CTRE.
     * - 2.004ms = full "forward"
     * - 1.52ms = the "high end" of the deadband range
     * - 1.50ms = center of the deadband range (off)
     * - 1.48ms = the "low end" of the deadband range
     * - .997ms = full "reverse"
     *
     * ctor channel - The PWM channel that the VictorSP is attached to. 0-9 are on-board, 10-19 are on the MXP port
     */

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

    private Encoder encoder1;
    private Encoder encoder2;

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
                drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 0);
                // Taranis
                //drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 0);
            }
        };
        Scheduler.getInstance().add(driveCommand);
    }

    // distance is in meters
    private void autonomousDriveDistance(float distance) {
        System.out.println("driving distance");
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