package frc.subsystems;
//package frc.SpeedControllerArray;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.RobotConfig;

/**
 * Created by Cole on 9/3/17.
 */
public class DriveTrain extends Subsystem {
    // singleton instance
    public static DriveTrain instance = new DriveTrain();

    public static DriveTrain getInstance() {
        return instance;
    }

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
    RobotDrive drive = new RobotDrive(leftMotors[0], leftSideMotors[2], rightMotors[0], rightMotors[2]);

    // shifter (changing gear)
    DoubleSolenoid shifter = new DoubleSolenoid(RobotConfig.GEARBOX_MODULE_NUMBER, RobotConfig.GEARBOX_FORWARD_CHANNEL, RobotConfig.GEARBOX_REVERSE_CHANNEL);

    // encoders for drive train (source A, source B, reverse direction, encoding type)
    Encoder leftEncoder = new Encoder(RobotConfig.ENCODER_LEFT_1, RobotConfig.ENCODER_LEFT_2, false, Encoder.EncodingType.k4X);
    Encoder rightEncoder = new Encoder(RobotConfig.ENCODER_RIGHT_1, RobotConfig.ENCODER_RIGHT_2, false, Encoder.EncodingType.k4X);


    // Initialization
    private DriveTrain() {

        // Reverse motors so that they all spin in the correct direction
        drive.setInvertedMotor(MotorType.kFrontLeft, true);
        drive.setInvertedMotor(MotorType.kFrontRight, true);
    }

    @Override
    protected void initDefaultCommand() {

    }

}