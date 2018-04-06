package org.cdm.team6072.subsystems;

import java.util.ArrayList;
import util.Logger;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.DoubleSolenoid;

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

    /**
     * When configuring talon, the configXXX() methods have a timeout param.
     * Recommended value when config during init is 10mSec, because each call witll wait to ensure value set correctly
     * During run time, want the value ot be 0 to avoid blocking main thread
     */
    private DriveSys() {
        System.out.println("6072: DriveSys constructor");

        try {
            mLeft_Master = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Master.setSafetyEnabled(false);
            mLeft_Master.configOpenloopRamp(0.1 , 10);
            mLeft_Master.setNeutralMode(NeutralMode.Brake);
            mLeft_Master.setSensorPhase(true);

            mLeft_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_SLAVE0);
            mLeft_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Slave0.setInverted(false);

            mRight_Master = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Master.setSafetyEnabled(false);
            mRight_Master.configOpenloopRamp(0.1, 10);
            mRight_Master.setNeutralMode(NeutralMode.Brake);

            mRight_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_SLAVE0);
            mRight_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Slave0.setInverted(false);

            mRoboDrive = new DifferentialDrive(mLeft_Master, mRight_Master);

            mMasterTalons.add(mRight_Master);
            mMasterTalons.add(mLeft_Master);

            mSol_GearShift = new DoubleSolenoid(RobotConfig.PCM_ID, RobotConfig.DRIVE_GEAR_FWD_LO, RobotConfig.DRIVE_GEAR_REV_HI);
            setGearHi();

            // used for motion profiling and autonomous management
            //mMotionProfileManager = new MotionProfileManager(mMasterTalons);

            mAhrs = NavXSys.getInstance().getAHRS();
            mAdsState = AdsState.Straight;      // set arcadeDriveStraight to drive straight state
            initGyroPID();
            mGyroPID.setSetpoint(0);
            mGyroPID.enable();

            initDrivePID();

            SmartDashboard.putData(mGyroPID);
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
        setDefaultCommand(new ArcadeDriveCmd(ControlBoard.getInstance().drive_stick));
//        setDefaultCommand(new TankDriveCmd(ControlBoard.getInstance().drive_stick));
    }


    private void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (Exception ex) {}
    }


    /**
     * Set the quad posn to same as PW posn.
     * Left motor sensor goes -ve when driving forward
     * This should only be called from Robot.Init because of the time delays
     */
    public void setSensorStartPosn() {

        mLeft_Master.getSensorCollection().setPulseWidthPosition(0, 10);
        mRight_Master.getSensorCollection().setPulseWidthPosition(0, 10);
        int leftPosn = mLeft_Master.getSensorCollection().getPulseWidthPosition();
        int rightPosn = mRight_Master.getSensorCollection().getPulseWidthPosition();

        /* mask out overflows, keep bottom 12 bits */
        int leftAbsPosition = leftPosn  & 0xFFF;
        int rightAbsPosn = rightPosn & 0xFFF;

        mLeft_Master.setSelectedSensorPosition(-leftAbsPosition, 0, 10);
        mRight_Master.setSelectedSensorPosition(rightAbsPosn, 0, 10);
        //mTalon.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
        // setSelected takes time so wait for it to get accurate print
        sleep(100);
        logPosn("DS.setStart");
    }

    public int getLeftSensPosn() {
        return mLeft_Master.getSensorCollection().getPulseWidthPosition();
    }




    // gear shifting code  ----------------------------------------------------------------------------------

    private DoubleSolenoid mSol_GearShift;
    private boolean mIsHiGear = true;

    /**
     * Provide a command to toggle between lo and hi gear
     *
     * need to think about velocity check before shifting
     */
    public void toggleGear() {
        if (mIsHiGear) {
            setGearLo();
        }
        else {
            setGearHi();
        }
    }

    private void setGearLo() {
        Logger.getInstance().printRobotAction("DriveSys.setGearLo");
        mSol_GearShift.set(DoubleSolenoid.Value.kForward);
        mIsHiGear = false;
    }

    private void setGearHi() {
        Logger.getInstance().printRobotAction("DriveSys.setGearHi");
        mSol_GearShift.set(DoubleSolenoid.Value.kReverse);
        mIsHiGear = true;
    }


    // driving  ---------------------------------------------------------------------------------------------

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
        mRoboDrive.arcadeDrive(-mag, yaw, false);
        if (mLoopCnt++ % 10 == 0) {
            //System.out.println("DriveSys.arcadeDrive: mag: " + mag + "    yaw: " + yaw + "  navYaw: " + mAhrs.getYaw());
            //printPosn("arcadeDrive");
        }
    }

    public void logPosn(String caller) {
        int lSens = mLeft_Master.getSelectedSensorPosition(0);
        int lQuad = mLeft_Master.getSensorCollection().getQuadraturePosition();
        int lPW = mLeft_Master.getSensorCollection().getPulseWidthPosition();
        int rSens = mRight_Master.getSelectedSensorPosition(0);
        int rQuad = mRight_Master.getSensorCollection().getQuadraturePosition();
        int rPW = mRight_Master.getSensorCollection().getPulseWidthPosition();

        String msg = String.format("%s:  lSens: %5d lQuad: %5d lPW: %5d  rSens: %5d  rQuad: %5d  rPW: %5d ", caller, lSens, lQuad, lPW, rSens, rQuad, rPW);
        System.out.println(msg);
    }

    private enum AdsState {
        Straight,
        OperatorTurn
    }
    private AdsState mAdsState;

    private static final int kAdsStateDriveToOpTurn = 5;     // roughly 100mS assuming called every 20 mS
    private static final int kAdsStateOpTurnToDrive = 25;    // roughly 200mS assuming called every 20 mS

    private int mAdsStateDriveToOpTurnCtr;
    private int mAdsStateOpTurnToDriveCtr;

    // hold the last n directions recorded, to be averaged on state transition
    private double[] mAdsDirns = new double[kAdsStateOpTurnToDrive];
    private int mAdsDirnsIndex = 0;

    /**
     * Use the gyro PID to assist in driving straight.
     * Idea is
     *      if in state drive
     *          and operator does a firm yaw for a period,
     *          then
     *              treat as a normal turn (disable gyro)
     *      If in state OpTurn
     *          and yaw is inside deadband for a period,
     *          then
     *              average the direction over the period
     *              treat that as the direction we want
     *              use gyro to drive straight in that direction
     * If yaw > kOperatorTurn for k
     *
     * mAhrs.getAngle() returns the accumulated yaw angle since yaw reset
     * @param mag
     * @param yaw
     */
    public void arcadeOperatorDriveStraight(double mag, double yaw) {

        boolean inDeadband = yaw <= 0.05;
        mAdsDirns[mAdsDirnsIndex] = mAhrs.getCompassHeading();
        mAdsDirnsIndex = (mAdsDirnsIndex + 1) % kAdsStateOpTurnToDrive;
        if (mAdsState == AdsState.Straight) {
            if (inDeadband) {
                // keep driving according to gyro - need to get PID correction
                mAdsStateDriveToOpTurnCtr = 0;
                yaw = mGyroPIDOut.getVal();
            }
            else {
                // check how long, maybe transition to OpTurn
                mAdsStateDriveToOpTurnCtr++;
                if (mAdsStateDriveToOpTurnCtr == kAdsStateDriveToOpTurn) {
                    mAdsStateOpTurnToDriveCtr++;
                    mAdsState = AdsState.OperatorTurn;
                    System.out.println("DriveSys: arcDrvSt: transition to operator turn");
                }
            }
        }
        else {
            // we are in operator turn
            if (inDeadband) {
                // might have completed turn - check how long, maybe transition to drive straight
                mAdsStateOpTurnToDriveCtr++;
                if (mAdsStateOpTurnToDriveCtr == kAdsStateOpTurnToDrive) {
                    // transition to drive straight - average the direction, set gyro
                    // do not actually use the PID this iteration - give it time to settle
                    double avgDirn = 0;
                    for (int i = 0; i < kAdsStateOpTurnToDrive; i++) {
                        avgDirn += mAdsDirns[i];
                    }
                    avgDirn = avgDirn / kAdsStateOpTurnToDrive;
                    mGyroPID.setSetpoint(avgDirn);
                    mAdsState = AdsState.Straight;
                    System.out.println("DriveSys: arcDrvSt: transition to drive straight - angle: " + avgDirn);
                }
                else {
                    // keep turning
                }
            }
            else {
                // not in deadband - keep turning
            }
        }
        mRoboDrive.arcadeDrive(-mag, yaw, false);
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
        for (int i = 0; i < mMasterTalons.size(); i++) {
            this.mMasterTalons.get(i).set(ControlMode.MotionProfile, setOutput.value);
        }
    }


    // autonomous driving ---------------------------------------------------------------------------

    // calculate allowed dist error in inches
    private static final int ALLOWED_DISTERR = (int) (6 / (Math.PI * 6) * 4096);

    private static double DRIVE_NEGBOUND = -0.12;
    private static double DRIVE_POSBOUND = 0.12;

    private static double TURN_NEGBAND = -0.19;
    private static double TURN_POSBAND = 0.19;

    static final double kF_drive = 2.0;
    static final double kP_drive = 0.5;
    static final double kI_drive = 0.0;
    static final double kD_drive = 1.50;

    /*
     * raise P constant until controller oscillates. If oscillation too much,
     * lower constant a bit raise D constant to damp oscillation, causing it to
     * converge. D also slows controller's approach to setpoint so will need to
     * tweak balance of P and D if P + D are tuned and it oscillates +
     * converges, but not to correct setpoint, increase I = steady-state error -
     * positive, nonzero integral constant will cause controller to correct for
     * it
     */
    private static final double kP_turn = 0.04;
    private static final double kI_turn = 0.00;
    private static final double kD_turn = 0.1;
    private static final double kF_turn = 0.00;

    /* This tuning parameter indicates how close to "on target" the    */
    /* PID Controller will attempt to get.                             */
    static final double kToleranceDegrees = 2.0f;

    private int mStartPosn;
    private int mTargetDist;
    private int mTargPosn;

    /* This tuning parameter indicates how close to "on target" the    */
    /* PID Controller will attempt to get.                             */
    private PIDController mDrivePID;
    private PIDOutReceiver mDrivePIDOut;
    private PIDSourceTalonPW mTalonPIDSource;

    private void initDrivePID() {
        mDrivePIDOut = new PIDOutReceiver();
        mTalonPIDSource = new PIDSourceTalonPW(mRight_Master);
        mDrivePID = new PIDController(kP_drive, kI_drive, kD_drive, kF_drive, mTalonPIDSource, mDrivePIDOut);
        //mDrivePID.setInputRange(-180.0f,  180.0f);
        mDrivePID.setOutputRange(-0.8, 0.8);
        // Makes PIDController.onTarget() return True when PIDInput is within the Setpoint +/- the absolute tolerance.
        mDrivePID.setAbsoluteTolerance(ALLOWED_DISTERR);
        // Treats the input ranges as the same, continuous point rather than two boundaries, so it can calculate shorter routes.
        // For example, in a Drive, 0 and 360 are the same point, and should be continuous. Needs setInputRanges.
        mDrivePID.setContinuous(false);
        mDrivePID.setName("DriveSys.DrivePID");
        Logger.getInstance().printRobotAction("DriveSys.initDrivePID");
    }


    // move distance in feet
    // wheel diameter is 6 inches (0.5 feet), so one rev = PI * 0.5 feet
    // distance to go in encoder ticks
    public void startMoveDistance(float distInFeet) {
        mTargetDist = (int) ((distInFeet / (Math.PI * 0.5)) * 4096);
        double dist = ((distInFeet / (Math.PI * 0.5)) * 4096);
        System.out.printf("DS.startMoveDist: distInFeet: %.3f   mTargdist:  %d   floatDist: %.3f  \r\n", distInFeet, mTargetDist, dist);
        mStartPosn = mRight_Master.getSensorCollection().getPulseWidthPosition();
        mTargPosn = mStartPosn + mTargetDist;
        mHitTarg = false;
        mMoveDistLoopCnt = 0;
        mLastErr = 99999999;
        mDrivePID.setSetpoint(mTargPosn);
        mDrivePID.enable();
    }


    // set this true when get within ERR of target, to prevent further driving
    private boolean mHitTarg = false;
    private int mLastErr;
    private int mMoveDistLoopCnt;

    /**
     * Called by the command exec loop
     * Stop if with error bound of taarget, OR if error is growing - we went past target
     */
//    public void moveDistanceExec() {
//
//        int curPosn = mRight_Master.getSensorCollection().getPulseWidthPosition();
//        int curErr = Math.abs(mTargPosn - curPosn);
//        mHitTarg = (curErr < ALLOWED_DISTERR) || curErr > mLastErr;
//        double yaw = mGyroPIDOut.getVal();
//        if (mMoveDistLoopCnt++ % 5 == 0) {
//            System.out.printf("DS.moveDistExec: start: %d   cur: %d   targ: %d   yaw: %.3f  curErr: %d  lastErr: %d  \r\n", mStartPosn, curPosn, mTargPosn, yaw, curErr, mLastErr);
//        }
//        mLastErr = curErr;
//        if (!mHitTarg) {
//            mRoboDrive.arcadeDrive(-0.5, yaw, false);
//        } else {
//            mRoboDrive.arcadeDrive(-0.0, 0, false);
//            System.out.printf("DS.moveDistExec: start: %d   cur: %d   targ: %d   yaw: %.3f  curErr: %d  lastErr: %d  ------------\r\n", mStartPosn, curPosn, mTargPosn, yaw, curErr, mLastErr);
//        }
//    }

    public void moveDistancePIDExec() {
        int curPosn = mRight_Master.getSensorCollection().getPulseWidthPosition();
        double mag = mDrivePIDOut.getVal();
        mag = (mag/1.3); //1.5;      // slow it down
        mag = checkDeadband(mag, DRIVE_NEGBOUND, DRIVE_POSBOUND);
        double yaw = mGyroPIDOut.getVal();
        if (mMoveDistLoopCnt++ % 5 == 0) {
           System.out.printf("DS.moveDistPIDExec: start: %d   cur: %d   targ: %d   mag: %.3f  yaw: %.3f  \r\n", mStartPosn, curPosn, mTargPosn, mag, yaw);
        }
        mRoboDrive.arcadeDrive(mag, yaw, false);       // PROD is +mag,   TEST is -mag
        mHitTarg = mDrivePID.onTarget();
        if (mHitTarg) {
            printPosn("moveDistancePIDExec_hit");
        }
    }

    public boolean moveDistComplete() {
        mHitTarg = mDrivePID.onTarget();
        if (mHitTarg) {
            printPosn("moveDistComplete");
        }
        return mHitTarg;
    }


    private void printPosn(String caller) {
        /*int curPosn = mRight_Master.getSensorCollection().getPulseWidthPosition();
        double yaw = mAhrs.getYaw();
        int dist = curPosn - mStartPosn;
        double distRevs = dist / 4096;
        double distFeet = distRevs * 1.5708;
        double distErr = 0;
        if (mTargPosn != 0) {
            distErr = dist / mTargetDist;
        }
        System.out.printf("DS. %s : start: %d   cur: %d   targ: %d   distEnc: %d   distRevs: %.3f  distFeet: %.3f   distErr: %.2f   yaw:  %.3f \r\n",
                caller, mStartPosn, curPosn, mTargPosn, dist, distRevs, distFeet, distErr, yaw) ;*/
    }





    private AHRS  mAhrs;
    private PIDController mGyroPID;
    private PIDOutReceiver mGyroPIDOut;

    private void initGyroPID() {
        mGyroPIDOut = new PIDOutReceiver();
        mGyroPID = new PIDController(kP_turn, kI_turn, kD_turn, kF_turn, mAhrs, mGyroPIDOut);
        mGyroPID.setInputRange(-180.0f,  180.0f);
        mGyroPID.setOutputRange(-0.5, 0.5);
        // Makes PIDController.onTarget() return True when PIDInput is within the Setpoint +/- the absolute tolerance.
        mGyroPID.setAbsoluteTolerance(kToleranceDegrees);
        // Treats the input ranges as the same, continuous point rather than two boundaries, so it can calculate shorter routes.
        // For example, in a gyro, 0 and 360 are the same point, and should be continuous. Needs setInputRanges.
        mGyroPID.setContinuous(true);
        mGyroPID.setName("DriveSys.GyroPID");
        Logger.getInstance().printRobotAction("DriveSys.initGyroPID");
    }



    private double mTargYaw;
    public void initTurnYaw(int yaw) {
        double curYaw = mAhrs.getYaw();
        System.out.printf("DS.initTurnYaw: curYaw: %.3f   targYaw: %d \r\n", curYaw, yaw);

        mTargYaw = yaw;
        mGyroPID.setSetpoint(yaw);
        mGyroPID.enable();
    }


    public void turnYawExec() {
        int curPosn = mRight_Master.getSensorCollection().getPulseWidthPosition();
        double curYaw = mAhrs.getYaw();
        double pidYaw = mGyroPIDOut.getVal();   // scaled for motor output
        pidYaw = checkDeadband(pidYaw, TURN_NEGBAND, TURN_POSBAND);
        if (mMoveDistLoopCnt++ % 5 == 0) {
            System.out.printf("DS.turnYawExec:  curYaw: %.3f   pidYaw: %.3f   targYaw: %.3f  \r\n", curYaw, pidYaw, mTargYaw);
        }
        mRoboDrive.arcadeDrive(-0, pidYaw, false);
    }

    public boolean turnYawComplete() {
        boolean onTarg =  mGyroPID.onTarget();
        if (onTarg) {
            double curYaw = mAhrs.getYaw();
            System.out.printf("DS.turnYawComplete: final yaw: %.3f  targYaw: %.3f\r\n", curYaw, mTargYaw);
        }
        return onTarg;
    }

    private double checkDeadband(double val, double negBand, double posBand) {
        if (val > 0 && val < posBand) {
            return posBand;
        }
        if (val < 0 && val > negBand) {
            return negBand;
        }
        return val;
    }


    public MotionProfileManager getMotionProfileManager() {
        return this.mMotionProfileManager;
    }
}
