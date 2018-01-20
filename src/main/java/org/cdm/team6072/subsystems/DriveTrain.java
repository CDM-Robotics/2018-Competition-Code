package org.cdm.team6072.subsystems;


import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.sun.corba.se.impl.orbutil.closure.Constant;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.autonomous.Constants;
import org.cdm.team6072.autonomous.MotionProfileManager;
import org.cdm.team6072.autonomous.profiles.DrivetrainProfile;

import com.ctre.phoenix.motion.TrajectoryPoint;

import java.util.ArrayList;

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
 *      Right Top        30     // changed 2018-01-13  0 -> 30
 *      Right Front      1
 *      Right Rear      15
 *
 *      We set to top motor on each side as the master for the side, then the other motors as slaves
 */
public class DriveTrain extends PIDSubsystem {

    private WPI_TalonSRX mLeftMaster;
    private WPI_TalonSRX mLeft_Slave0;
    private WPI_TalonSRX mLeft_Slave1;
    private WPI_TalonSRX mRightMaster;
    private WPI_TalonSRX mRight_Slave0;
    private WPI_TalonSRX mRight_Slave1;

    ArrayList<TalonSRX> masters = new ArrayList<TalonSRX>();

    private DifferentialDrive mRoboDrive;
    private MotionProfileManager mMotionProfileManager;


    private static DriveTrain mInstance;
    public static DriveTrain getInstance() {
        if (mInstance == null) {
            mInstance = new DriveTrain();
        }
        return mInstance;
    }

    private DriveTrain() {
        super(34, 20, 55);
        System.out.println("6072: DriveTrain constructor");

        try {
            mLeftMaster = new WPI_TalonSRX(RobotConfig.LEFT_MASTER);
            mLeftMaster.configOpenloopRamp(0.7 , 0);

            mLeft_Slave0 = new WPI_TalonSRX(RobotConfig.LEFT_SLAVE0);
            mLeft_Slave0.set(ControlMode.Follower, RobotConfig.LEFT_MASTER);
            mLeft_Slave0.setInverted(true);
            mLeft_Slave1 = new WPI_TalonSRX(RobotConfig.LEFT_SLAVE1);
            mLeft_Slave1.set(ControlMode.Follower, RobotConfig.LEFT_MASTER);
            mLeft_Slave1.setInverted(true);

            mRightMaster = new WPI_TalonSRX(RobotConfig.RIGHT_MASTER);
            mRightMaster.configOpenloopRamp(0.7, 0);

            mRight_Slave0 = new WPI_TalonSRX(RobotConfig.RIGHT_SLAVE0);
            mRight_Slave0.set(ControlMode.Follower, RobotConfig.RIGHT_MASTER);
            mRight_Slave0.setInverted(true);
            mRight_Slave1 = new WPI_TalonSRX(RobotConfig.RIGHT_SLAVE1);
            mRight_Slave1.set(ControlMode.Follower, RobotConfig.RIGHT_MASTER);
            mRight_Slave1.setInverted(true);

            mRoboDrive = new DifferentialDrive(mLeftMaster, mRightMaster);

            masters.add(mRightMaster);
            masters.add(mLeftMaster);

            // used for motion profiling and autonomous management
            mMotionProfileManager = new MotionProfileManager(masters);
        }
        catch (Exception ex) {
            System.out.println("Exception in DriveTrain ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }

    }



    public void setupProfile() {
        // temporarily setting the profile here
        this.mMotionProfileManager.loadMotionProfile(new DrivetrainProfile());

        for (int i=0; i < this.masters.size(); i++) {
            this.masters.get(i).configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
            this.masters.get(i).setSensorPhase(true);
            this.masters.get(i).configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

            this.masters.get(i).config_kF(0, 0.076, Constants.kTimeoutMs);
            this.masters.get(i).config_kP(0, 2, Constants.kTimeoutMs);
            this.masters.get(i).config_kI(0, 0, Constants.kTimeoutMs);
            this.masters.get(i).config_kD(0,20, Constants.kTimeoutMs);

            //this.masters.get(i).setControlFramePeriod(10, Constants.kTimeoutMs);
            this.masters.get(i).setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
        }
    }

    public void updateProfileValue() {
        SetValueMotionProfile setOutput = this.mMotionProfileManager.getSetValue();

        System.out.println("val: " + setOutput.value);

        for (int i=0; i < masters.size(); i++) {
            this.masters.get(i).set(ControlMode.MotionProfile, setOutput.value);
        }
    }



    @Override
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
        System.out.println("DriveTrain: init default command");
    }


    /**
     * Implement the tank drive method for the RobotDrive
     * Allows external access for commands without exposing the RobotDrive object
     * @param left
     * @param right
     */
    public void tankDrive(double left, double right) {
        System.out.println("Drivetrain.tankDrive: " + left + "      " + right);
        getPIDController().setEnabled(false); // disable PID controller while manually driving
        mRoboDrive.tankDrive(left, right);
    }

    public void arcadeDrive(double mag, double turn) {
        mRoboDrive.arcadeDrive(-mag, -turn, true);
        System.out.println("Drivetrain.arcadeDrive: " + mag + "      " + turn);
    }

    public MotionProfileManager getMotionProfileManager() {
        return this.mMotionProfileManager;
    }

    @Override
    protected double returnPIDInput() {
        return 0;
    }

    @Override
    protected void usePIDOutput(double output) {

    }
}
