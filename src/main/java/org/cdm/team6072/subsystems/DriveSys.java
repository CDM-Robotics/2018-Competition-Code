package org.cdm.team6072.subsystems;

import java.util.ArrayList;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.DriverStation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.commands.drive.ArcadeDriveCmd;
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

//            initAHRS();
//
//            initGyroPID();
//            mGyroPID.setSetpoint(0);
//            mGyroPID.enable();
//            SmartDashboard.putData(mGyroPID);
        }
        catch (Exception ex) {
            System.out.println("Exception in DriveSys ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }
    }


    /*
     * raise P constant until controller oscillates. If oscillation too much,
     * lower constant a bit raise D constant to damp oscillation, causing it to
     * converge. D also slows controller's approach to setpoint so will need to
     * tweak balance of P and D if P + D are tuned and it oscillates +
     * converges, but not to correct setpoint, increase I = steady-state error -
     * positive, nonzero integral constant will cause controller to correct for
     * it
     */
    static final double kP = 0.03;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;
    /* This tuning parameter indicates how close to "on target" the    */
    /* PID Controller will attempt to get.                             */
    static final double kToleranceDegrees = 2.0f;


    private AHRS  mAhrs;
    private PIDController mGyroPID;
    private PIDOutReceiver mGyroPIDOut;

    private void initAHRS() {
        try {
          /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
          /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
          /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
            mAhrs = new AHRS(SPI.Port.kMXP);
            mAhrs.reset();
            mAhrs.zeroYaw();
        } catch (RuntimeException ex ) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
        }
        while (mAhrs.isCalibrating()) {
            sleep(10);
        }
        System.out.println("DriveSys.initAHRS: YawAxis: " + mAhrs.getBoardYawAxis().board_axis.getValue()
                + "  firmware: " + mAhrs.getFirmwareVersion()
                + "  isConnected: " + mAhrs.isConnected()
        );
    }

    private void initGyroPID() {
        mGyroPIDOut = new PIDOutReceiver();
        mGyroPID = new PIDController(kP, kI, kD, kF, mAhrs, mGyroPIDOut);
        mGyroPID.setInputRange(-180.0f,  180.0f);
        mGyroPID.setOutputRange(-1.0, 1.0);
        // Makes PIDController.onTarget() return True when PIDInput is within the Setpoint +/- the absolute tolerance.
        mGyroPID.setAbsoluteTolerance(kToleranceDegrees);
        // Treats the input ranges as the same, continuous point rather than two boundaries, so it can calculate shorter routes.
        // For example, in a gyro, 0 and 360 are the same point, and should be continuous.
        // Needs setInputRanges.
        mGyroPID.setContinuous(true);
        mGyroPID.setName("DriveSys.GyroPID");
        System.out.println("DriveSys.initGyroPID:  AHRS.SrcType: " + mAhrs.getPIDSourceType().name());
    }



    private void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (Exception ex) {}
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
        setDefaultCommand(new ArcadeDriveCmd(ControlBoard.getInstance().drive_stick));
    }


    /**
     * Implement the tank drive method for the RobotDrive
     * Allows external access for commands without exposing the RobotDrive object
     * @param left
     * @param right
     */
    public void tankDrive(double left, double right) {
        //System.out.println("Drivetrain.tankDrive: " + left + "      " + right);
        mRoboDrive.tankDrive( -1.0*left, -1.0*right, true);
    }

    private int mLoopCnt = 0;
    public void arcadeDrive(double mag, double yaw) {
        mRoboDrive.arcadeDrive(-mag, -yaw, true);
        if (mLoopCnt++ % 50 == 0) {
            System.out.println("DriveSys.arcadeDrive: mag: " + mag + "    yaw: " + yaw );
//                    + "  navAngle: " + mAhrs.getAngle() + "  navYaw: " + mAhrs.getYaw()
//                    + "  PIDOut: " + mGyroPIDOut.getVal() + "  PID.kP: " + mGyroPID.getP());

            SmartDashboard.putNumber("DriveSys.arc.mag", mag);
            SmartDashboard.putNumber("DriveSys.arc.yaw", yaw);
//            SmartDashboard.putNumber("DriveSys.arc.navAngle", mAhrs.getAngle());
//            SmartDashboard.putNumber("DriveSys.arc.navYaw",  mAhrs.getYaw());
//            SmartDashboard.putNumber("DriveSys.arc.PIDOut", mGyroPIDOut.getVal());
//            SmartDashboard.putNumber("DriveSys.arc.PID_kP",  mGyroPID.getP());
        }
    }

    private void printStatus() {

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
