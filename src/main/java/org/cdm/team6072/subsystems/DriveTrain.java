package org.cdm.team6072.subsystems;


import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.cdm.team6072.RobotConfig;


/**
 * Implement a drive subsystem for the 2018 robot
 * Three motors per side, driving a single shaft per side
 * Each motor controlled by a CANTalon on the CAN bus
 *
 * To configure Talon Device Ids, need to use the NI web browser RoboRio config
 * Requires IE and Silverlight
 * Connect to robot wifi, then browse to http://roborio-6072-frc.local
 *
 * As at 2017-12-17, the Talons have been given device IDs that match the PDP port they are connected to
 * The configuration is
 *      Motor           Talon ID
 *      Left Top        13
 *      Left Front      14
 *      Left Rear       12
 *
 *      Right Top        0
 *      Right Front      1
 *      Right Rear      15
 *
 *      We set to top motor on each side as the master for the side, then the other motors as slaves
 */
public class DriveTrain extends Subsystem {

    private WPI_TalonSRX mLeftMaster;
    private WPI_TalonSRX mLeft_Slave0;
    private WPI_TalonSRX mLeft_Slave1;
    private WPI_TalonSRX mRightMaster;
    private WPI_TalonSRX mRight_Slave0;
    private WPI_TalonSRX mRight_Slave1;

    private DifferentialDrive mRoboDrive;



    private static DriveTrain mInstance;
    public static DriveTrain getInstance() {
        if (mInstance == null) {
            mInstance = new DriveTrain();
        }
        return mInstance;
    }

    private DriveTrain() {
        System.out.println("6072: DriveTrain constructor");

        try {
            mLeftMaster = new WPI_TalonSRX(RobotConfig.LEFT_MASTER);

            mLeft_Slave0 = new WPI_TalonSRX(RobotConfig.LEFT_SLAVE0);
            mLeft_Slave0.set(ControlMode.Follower, RobotConfig.LEFT_MASTER);
            mLeft_Slave0.setInverted(true);
            mLeft_Slave1 = new WPI_TalonSRX(RobotConfig.LEFT_SLAVE1);
            mLeft_Slave1.set(ControlMode.Follower, RobotConfig.LEFT_MASTER);
            mLeft_Slave1.setInverted(true);

            mRightMaster = new WPI_TalonSRX(RobotConfig.RIGHT_MASTER);

            mRight_Slave0 = new WPI_TalonSRX(RobotConfig.RIGHT_SLAVE0);
            mRight_Slave0.set(ControlMode.Follower, RobotConfig.RIGHT_MASTER);
            mRight_Slave0.setInverted(true);
            mRight_Slave1 = new WPI_TalonSRX(RobotConfig.RIGHT_SLAVE1);
            mRight_Slave1.set(ControlMode.Follower, RobotConfig.RIGHT_MASTER);
            mRight_Slave1.setInverted(true);

            mRoboDrive = new DifferentialDrive(mLeftMaster, mRightMaster);
        }
        catch (Exception ex) {
            System.out.println("Exception in DriveTrain ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }

    }


    /**
     * See WPI_TalonSRX manual section 21.18
     */
//    public void disabledPeriodic() {
//        //System.out.println("6072: disabledPeriodic");
//        mLeft_Slave0.enableBrakeMode(false);
//        mLeft_Slave1.enableBrakeMode(false);
//        mLeftMaster.enableBrakeMode(false);
//        mRight_Slave0.enableBrakeMode(false);
//        mRight_Slave1.enableBrakeMode(false);
//        mRightMaster.enableBrakeMode(false);
//    }


    @Override
    public void initDefaultCommand() {
        System.out.println("DriveTrain: init default command");
    }


    /**
     * Implement the tank drive method for the RobotDrive
     * Allows external access for commands without exposing the RobotDrive object
     * @param left
     * @param right
     */
    public void tankDrive(double left, double right) {

        mRoboDrive.tankDrive(left, right);
    }

    public void arcadeDrive(double mag, double turn) {
        mRoboDrive.arcadeDrive(mag, turn);
    }

}
