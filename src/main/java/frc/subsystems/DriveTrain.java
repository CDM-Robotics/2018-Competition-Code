package frc.subsystems;
//package frc.SpeedControllerArray;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.VictorSP;
import frc.RobotConfig;

/**
 * Created by Cole on 9/3/17.
 */
public class DriveTrain {

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

    SpeedController leftSpeedControllers[] = leftMotors;
    SpeedController rightSpeedControllers[] = rightMotors;

    RobotDrive drive = new RobotDrive(leftSpeedControllers, rightSpeedControllers);
}