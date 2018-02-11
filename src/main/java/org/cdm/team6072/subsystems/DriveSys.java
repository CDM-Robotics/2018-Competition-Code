package org.cdm.team6072.subsystems;

import java.util.ArrayList;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;


import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.profiles.Constants;
import org.cdm.team6072.autonomous.MotionProfileManager;
import org.cdm.team6072.profiles.drive.DrivetrainProfile;


/**
 * Implement a drive subsystem for the 2018 robot
 * Two motors per side, driving a single shaft per side
 * Each motor controlled by a CANTalon on the CAN bus
 *
 * To configure Talon Device Ids, need to use the NI web browser RoboRio config
 * Requires IE and Silverlight
 * Connect to robot wifi, then browse to http://roborio-6072-frc.local
 *
 * As at 2018-02-10, the Talons have been given device IDs that match the PDP port they are connected to
 *      See RobotConfig for details
 */
public class DriveSys extends Subsystem {

    private WPI_TalonSRX mLeft_Master;
    private WPI_TalonSRX mLeft_Slave0;
    private WPI_TalonSRX mRight_Master;
    private WPI_TalonSRX mRight_Slave0;

    ArrayList<TalonSRX> mMasterTalons = new ArrayList<TalonSRX>();

    private DifferentialDrive mRoboDrive;
    private MotionProfileManager mMotionProfileManager;


    private static DriveSys mInstance;
    public static DriveSys getInstance() {
        if (mInstance == null) {
            mInstance = new DriveSys();
        }
        return mInstance;
    }

    private DriveSys() {
        System.out.println("6072: DriveSys constructor");

        try {
            mLeft_Master = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Master.configOpenloopRamp(0.7 , 0);

            mLeft_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_SLAVE0);
            mLeft_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Slave0.setInverted(false);

            mRight_Master = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Master.configOpenloopRamp(0.7, 0);

            mRight_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_SLAVE0);
            mRight_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Slave0.setInverted(false);

            mRoboDrive = new DifferentialDrive(mLeft_Master, mRight_Master);

            mMasterTalons.add(mRight_Master);
            mMasterTalons.add(mLeft_Master);
            // used for motion profiling and autonomous management
            mMotionProfileManager = new MotionProfileManager(mMasterTalons);
        }
        catch (Exception ex) {
            System.out.println("Exception in DriveSys ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }
    }


    /**
     * Each subsystem may, but is not required to, have a default command
     * which is scheduled whenever the subsystem is idle
     * (the command currently requiring the system completes).
     *  The most common example of a default command is a command for the drivetrain
     *  that implements the normal joystick control. This command may be interrupted
     *  by other commands for specific maneuvers ("precision mode", automatic alignment/targeting, etc.)
     *  but after any command requiring the drivetrain completes the joystick command would be scheduled again.
     */
    public void initDefaultCommand() {
        System.out.println("DriveSys: init default command");
    }


    /**
     * Implement the tank drive method for the RobotDrive
     * Allows external access for commands without exposing the RobotDrive object
     * @param left
     * @param right
     */
    public void tankDrive(double left, double right) {
        //System.out.println("Drivetrain.tankDrive: " + left + "      " + right);
        mRoboDrive.tankDrive(left, right);
    }

    public void arcadeDrive(double mag, double turn) {
        mRoboDrive.arcadeDrive(-mag, -turn, true);
        //System.out.println("DriveSys.arcadeDrive: " + mag + "      " + turn);
    }



    // motion profile code


    public void setupProfile() {
        // temporarily setting the profile here
        mMotionProfileManager.loadMotionProfile(DrivetrainProfile.getInstance());

        for (int i = 0; i < this.mMasterTalons.size(); i++) {
            this.mMasterTalons.get(i).configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
            this.mMasterTalons.get(i).setSensorPhase(true);
            this.mMasterTalons.get(i).configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

            this.mMasterTalons.get(i).config_kF(0, 0.076, Constants.kTimeoutMs);
            this.mMasterTalons.get(i).config_kP(0, 2, Constants.kTimeoutMs);
            this.mMasterTalons.get(i).config_kI(0, 0, Constants.kTimeoutMs);
            this.mMasterTalons.get(i).config_kD(0,20, Constants.kTimeoutMs);

            //this.mMasterTalons.get(i).setControlFramePeriod(10, Constants.kTimeoutMs);
            this.mMasterTalons.get(i).setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
        }
    }

    public void updateProfileValue() {
        SetValueMotionProfile setOutput = this.mMotionProfileManager.getSetValue();

        System.out.println("val: " + setOutput.value);

        for (int i = 0; i < mMasterTalons.size(); i++) {
            this.mMasterTalons.get(i).set(ControlMode.MotionProfile, setOutput.value);
        }
    }





    public MotionProfileManager getMotionProfileManager() {
        return this.mMotionProfileManager;
    }


}
