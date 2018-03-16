package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.profiles.IMotionProfile;
import org.cdm.team6072.profiles.MotionProfileController;
import org.cdm.team6072.profiles.PIDConfig;
import util.CrashTracker;



public class ElevatorSys extends Subsystem {

    /**
     * How many sensor units per rotation.
     * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
     */
    public static final int kCTREUnitsPerRotation = 1024; // 4096;
    public static final int kAndyMarkUnitsPerRotation = 80;
    
    public static final int kUnitsPerRotation = kCTREUnitsPerRotation;
    
    // inches of elevator travel per complete rotation of encoder
    // gear is 1 inch diameter
    public static final double kDistancePerRotation = 1.25 * Math.PI;

    public static final int kUnitsPerInch = (int)Math.round(kUnitsPerRotation / kDistancePerRotation);


    // scale height in native units from nominal base posn
    private static int BASE_POSN = 1 * kUnitsPerInch;
    private static int SWITCH_POSN_UNITS = 24 * kUnitsPerInch;
    private static int SCALELO_POSN_UNITS = 36 * kUnitsPerInch;
    private static int SCALEHI_POSN_UNITS = 70 * kUnitsPerInch;



    /**
     * Which PID slot to pull gains from.  Starting 2018, you can choose
     * from 0,1,2 or 3.  Only the first two (0,1) are visible in web-based configuration.
     */
    public static final int kPIDSlot_Move = 0;
    public static final int kPIDSlot_Hold = 0;
    public static final int kPIDSlot_2 = 2;
    public static final int kPIDSlot_3 = 3;
    /**
     * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops.
     * For now we just want the primary one.
     */
    public static final int kPIDLoopIdx = 0;
    /**
     * set to zero to skip waiting for confirmation, set to nonzero to wait
     * and report to DS if action fails.
     */
    public static final int kTimeoutMs = 10;

    /**
     * Base trajectory period to add to each individual
     * trajectory point's unique duration.  This can be set
     * to any value within [0,255]ms.
     */
    public static final int kBaseTrajPeriodMs = 0;

    /**
     * Motor deadband, set to 1%.
     */
    public static final double kNeutralDeadband = 0.01;


    /* choose so that Talon does not report sensor out of phase */
    public static boolean kSensorPhase = true;

    /* choose based on what direction you want to be positive,
        this does not affect motor invert. */
    public static boolean kMotorInvert = false;

    /**
     * Specify the direction the elevator should move
     */
    public static enum Direction {
        Up,
        Down
    }

    /**
     * Specify the target position we want to reach.
     * Might be replaced by an enum or some other way of specifying desired state
     */
    private static double mTarget;

    /**
     * Log the sensor position at power up - use this as the base reference for positioning.
     */
    private int mBasePosn;


    private WPI_TalonSRX mTalon;

    private MotionProfileController mMPController;

    private PIDConfig mPIDConfig;

    // set up the top and bottom limit switches and the related counters
    private DigitalInput mTopSwitch;
    private Counter mTopCounter;
    private DigitalInput mBotSwitch;
    private Counter mBotCounter;


    private boolean mSensorPhase = false;  // checked 2018-02-16
    private boolean mMotorInvert = false;


    // singleton constructor  -------------------------------------------------

    private static ElevatorSys mInstance;

    public static ElevatorSys getInstance() {
        if (mInstance == null) {
            mInstance = new ElevatorSys();
        }
        return mInstance;
    }


    /**
     * The CTRE Magnetic Encoder is actually two sensor interfaces packaged into one (pulse width
     * and quadrature encoder). Therefore the sensor provides two modes of use: absolute and relative.
     * The advantage of absolute mode is having a solid reference to where a mechanism is without
     * re-tare-ing or re-zero-ing the robot. The advantage of the relative mode is the faster update
     * rate. However both values can be read/written at the same time. So a combined strategy of
     * seeding the relative position based on the absolute position can be used to benefit from the
     * higher sampling rate of the relative mode and still have an absolute sensor position
     *
     * All config* routines in the C++/Java require a timeoutMs parameter. When set to a non-zero value,
     * the config routine will wait for an acknowledgement from the device before returning.
     * If the timeout is exceeded, an error code is generated and a Driver Station message is produced.
     * When set to zero, no checking is performed (identical behavior to the CTRE v4 Toolsute).
     *
     * Measured sensor velocity at max motor was 1180 units/100 mSec
     * Motor output is scaled from -1023 to +1023
     */
    private ElevatorSys() {
        CrashTracker.logMessage("ElevatorSys.ctor: initializing");
        double peakOut = 0.75;           // 1.0 is max
        try {
            // set up the limit switches - counter is used to detect switch closing because might be too fast
            mTopSwitch = new DigitalInput(RobotConfig.ELEVATOR_SWITCH_TOP);
            mTopCounter = new Counter(mTopSwitch);
            mBotSwitch = new DigitalInput(RobotConfig.ELEVATOR_SWITCH_BOT);
            mBotCounter = new Counter(mBotSwitch);
            mTopCounter.reset();
            mBotCounter.reset();
            System.out.println("ElevatorSys.ctor:  topSw: " + mTopSwitch.getChannel() + "  botSw: " + mBotSwitch.getChannel());

            mTalon = new WPI_TalonSRX(RobotConfig.ELEVATOR_TALON);
            mTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
            mTalon.setSensorPhase(mSensorPhase);
            mTalon.configNeutralDeadband(kNeutralDeadband, kTimeoutMs);

            // set up current limits
            mTalon.configContinuousCurrentLimit(10, 0);
            mTalon.configPeakCurrentLimit(15, 0);
            mTalon.configPeakCurrentDuration(100, 0);
            //mTalon.enableCurrentLimit(true);

            mTalon.configOpenloopRamp(0.25, 10);

            // set slot zero for position hold closed loop
            mTalon.configNominalOutputForward(0, kTimeoutMs);
            mTalon.configNominalOutputReverse(0, kTimeoutMs);
            mTalon.configPeakOutputForward(peakOut, kTimeoutMs);
            mTalon.configPeakOutputReverse(-peakOut, kTimeoutMs);

            // init PID for moving
//            mTalon.config_kF(kPIDSlot_Move, 0.867, kTimeoutMs);
//            mTalon.config_kP(kPIDSlot_Move, 0.255, kTimeoutMs);
//            mTalon.config_kI(kPIDSlot_Move, 0.0, kTimeoutMs);
//            mTalon.config_kD(kPIDSlot_Move, 0.0, kTimeoutMs);
//            sleep(10);      // wait for the params to hit
//
//            // init PID for hold
//            mTalon.config_kF(kPIDSlot_Hold, 0.0, kTimeoutMs);             // normally 0 for position hold
//            mTalon.config_kP(kPIDSlot_Hold, 1.0, kTimeoutMs);             // kP 1.0 used on elevator 2018-02-17
//            mTalon.config_kI(kPIDSlot_Hold, 0.0, kTimeoutMs);
//            mTalon.config_kD(kPIDSlot_Hold, 0.0, kTimeoutMs);
//            try {
//                Thread.sleep(100);
//            }
//            catch (Exception ex) {
//            }
            /*
             * set the allowable closed-loop error, Closed-Loop output will be
             * neutral within this range. See Table in Section 17.2.1 for native units per rotation.
             */
            mTalon.configAllowableClosedloopError(kPIDLoopIdx, 0, kTimeoutMs);

            setSensorStartPosn();

            // test  motion profile --------------
//            System.out.println("ElevatorSys.setMPProfile:  setting Talon control mode to MotionProfile ");
//            mTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
//            System.out.println("ElevatorSys.setMPProfile:  back from setting Talon ");

            // test magic motion

        } catch (Exception ex) {
            System.out.println("************************** ElevatorSys.ctor Ex: " + ex.getMessage());
        }
    }


    private void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (Exception ex) {}
    }



    //  grab the 360 degree position of the MagEncoder's absolute position, and set the relative sensor to match.
    public void setSensorStartPosn() {

        // might be in a PID hold mode, so get out of it
        mTalon.set(ControlMode.PercentOutput, 0);
        sleep(20);
        mTalon.getSensorCollection().setPulseWidthPosition(0, 10);
        sleep(20);
        mBasePosn = mTalon.getSensorCollection().getPulseWidthPosition();
        int absolutePosition = mBasePosn;

        /* mask out overflows, keep bottom 12 bits */
        absolutePosition &= 0xFFF;
        if (mSensorPhase)
            absolutePosition *= -1;
        if (mMotorInvert)
            absolutePosition *= -1;
        /* set the quadrature (relative) sensor to match absolute */
        mTalon.setSelectedSensorPosition(absolutePosition, 0, kTimeoutMs);
        //mTalon.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
        // setSelected takes time so wait for it to get accurate print
        sleep(5);
        printPosn("setStart");
    }



    @Override
    protected void initDefaultCommand() {

    }


    public void initTopSwitch() {
        mTopCounter.reset();
    }

    public boolean topSwitchSet() {
        return mTopCounter.get() > 0;
    }

    public void initBotSwitch() {
        mBotCounter.reset();
    }

    public boolean botSwitchSet() {
        return mBotCounter.get() > 0;
    }


    // moveTo Base command implementation       --------------------------------------------------------------


    public void initMoveToBase() {
        initBotSwitch();
        initForMove();
    }


    /**
     * Move carefully to the bottom and then set position accordingly
     */
//    public void moveToBase() {
//
//        printPosn("moveToBase.start");
//        mMoveToBaseComplete = false;
//        int startPosn = mTalon.getSelectedSensorPosition(0);
//        int curPosn = mTalon.getSelectedSensorPosition(0);
//        int loopCtr = 0;
//        move(Direction.Up, 0.3);
//        while (curPosn < startPosn + 100) {
//            curPosn = Math.abs(mTalon.getSelectedSensorPosition(0));
//            sleep(1);
//            if (++loopCtr % 10 == 0) {
//                printPosn("moveToBase.moveUp_" + loopCtr);
//            }
//        }
//        //now move down until we hit the bottom switch
//        initBotSwitch();
//        move(Direction.Down, 0.3);
//        while (!botSwitchSet()) {
//            sleep(1);
//            if (++loopCtr % 10 == 0) {
//                printPosn("moveToBase.moveDown_" + loopCtr);
//            }
//        }
//        setSensorStartPosn();
//        holdPosn();
//        printPosn("moveToBase.exit");
//        mMoveToBaseComplete = true;
//    }
//
//
//    private boolean mMoveToBaseComplete = false;
//
//    public boolean moveToBaseComplete() {
//        return mMoveToBaseComplete;
//    }



    // ------------- code for open loop target  -----------------------------------------------



    public void resetSystemState() {
        mTalon.setInverted(false);
    }


    public void initForMove() {
        mLastRelPosn = mTalon.getSelectedSensorPosition(kPIDSlot_Move);
        mLastQuadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        mMoveLoopCtr = 0;
        initBotSwitch();
        initTopSwitch();
        printPosn("initForMove");
    }

    private int mMoveLoopCtr  = 0;

    public void move(Direction dir, double speed) {
        if (topSwitchSet() || botSwitchSet()) {
            System.out.println("*****************  ElvSys.move:  switch hit  top:" + mTopCounter.get() + "  bot: " + mBotCounter.get());
            stop();
            return;
        }
        if (dir == Direction.Up) {
            mTalon.setInverted(false);
        } else {
            mTalon.setInverted(true);
        }
        mTalon.set(ControlMode.PercentOutput, speed);
        if (++mMoveLoopCtr % 5 == 0) {
            printPosn("ElvSys.move");
        }
    }

    /**
     * Stop movement and move to closed loop position hold
     */
    public void stop() {
        mTalon.set(ControlMode.PercentOutput, 0);
        holdPosn();
    }

    /**
     * Switch to using closed loop position hold at current position
     */
    private void holdPosn() {
        /* set closed loop gains in slot0, typically kF stays zero. */
        mTalon.config_kF(kPIDSlot_Hold, 0.0, kTimeoutMs);             // normally 0 for position hold
        mTalon.config_kP(kPIDSlot_Hold, 1.0, kTimeoutMs);             // kP 1.0 used on elevator 2018-02-17
        mTalon.config_kI(kPIDSlot_Hold, 0.0, kTimeoutMs);
        mTalon.config_kD(kPIDSlot_Hold, 0.0, kTimeoutMs);
        sleep(50);
        double curPosn = mTalon.getSelectedSensorPosition(kPIDSlot_Hold);
        //double curPosn = Math.abs(mTalon.getSelectedSensorPosition(0));
        printPosn("holdPosn.before");
        // In Position mode, output value is in encoder ticks or an analog value, depending on the sensor.
        mTalon.set(ControlMode.Position, curPosn);
        boolean notFinished = true;
        double lastErr = -1;
        int loopCnt = 0;
        while (notFinished && loopCnt < 50) {
            try {
                Thread.sleep(50);
                double curError = mTalon.getClosedLoopError(kPIDSlot_Hold);
                if (lastErr != -1) {
                    notFinished = (Math.abs(curError - lastErr) > 200);
                }
                lastErr = curError;
                if (++loopCnt % 5 == 0) {
                    printPosn("holdPosn.after_" + curPosn +"_" + loopCnt );
                }
            } catch (Exception ex) {
            }
        }
        printPosn("holdPosn.FINISH_" + curPosn +"_" + loopCnt );
    }
    
    
    // move to the standard target positions  ---------------------------------------------------------------------

    /**
     * Move to the scale height from whatever our current position is
     */
    public void moveToBase() {
        moveToTarget(BASE_POSN);
    }
    public boolean moveToBaseComplete() {
        return moveToTargetComplete();
    }

    public void moveToSwitch() {
        moveToTarget(SWITCH_POSN_UNITS);
    }
    public boolean moveToSwitchComplete() {
        return moveToTargetComplete();
    }

    public void moveToScaleLo() {
        moveToTarget(SCALELO_POSN_UNITS);
    }
    public boolean moveToScaleLoComplete() {
        return moveToTargetComplete();
    }

    public void moveToScaleHi() {
        moveToTarget(SCALEHI_POSN_UNITS);
    }
    public boolean moveToScaleHiComplete() {
        return moveToTargetComplete();
    }

    //------------------------------------------

    /**
     * mbasePosn is based on the sensor pulsewidth posn
     * @param targPosn
     */
    private void moveToTarget(int targPosn) {
        Direction dir;
        
        //int normCurPosn = getCurPosn() - mBasePosn;
        //int distToMove = targPosn - normCurPosn;
        int distToMove = targPosn - getCurPosn();
        if (distToMove >= 0) {
            dir = Direction.Up;
        }
        else {
            dir = Direction.Down;
        }
        System.out.println("ElvSys.moveToTarget:  mBasePosn: " + mBasePosn + "  targPosn: " + targPosn + "  curPosn: " + getCurPosn() + "  distToMove: " + distToMove);
        initForMagicMove();
        magicMove(dir, Math.abs(distToMove));
    }
    
    public boolean moveToTargetComplete() {
        return magicMoveComplete();
    }


    // code for motion magic move  -------------------------------------------------------------------


    private double mMMTargetPosn = -1;

    private boolean mMMStarted;
    private int mMMStartPosn;

    /**
     * Measured max vel was 1180 native units per 100 mSec
     *  kF = 1023 / 1180 = 0.867
     *  Set max cruise to 0.5 * 1180 = 885
     */
    public void initForMagicMove() {
		//  set closed loop gains in slot0
        mTalon.config_kF(kPIDSlot_Move, 0.867, kTimeoutMs);
        mTalon.config_kP(kPIDSlot_Move, 0.255, kTimeoutMs);
        mTalon.config_kI(kPIDSlot_Move, 0.0, kTimeoutMs);
        mTalon.config_kD(kPIDSlot_Move, 0.0, kTimeoutMs);
        sleep(10);      // wait for the params to hit

        // set acceleration and cruise velocity - see documentation
        mTalon.configMotionCruiseVelocity(590, kTimeoutMs);
        mTalon.configMotionAcceleration(500, kTimeoutMs);
//        mLastRelPosn = mTalon.getSelectedSensorPosition(0);
//        mLastQuadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        mMMStartPosn = getCurPosn();
        //mTalon.setSelectedSensorPosition(0, 0, kTimeoutMs);
        mMMStarted = false;
        mLoopCtr = 0;
        printPosn("initForMagicMove");
    }


    private int mLoopCtr = 0;

    /**
     * Use Magic motion profile to move the specified number of rotations up or down
     * @param dir
     * @param distInSensUnits
     */
    private void magicMove(Direction dir, double distInSensUnits) {
        double targetDist;
        if (!mMMStarted) {
            if (dir == Direction.Up) {
                targetDist = distInSensUnits;
            } else {
                targetDist = -1 * distInSensUnits;
            }
            //mMMTargetPosn = targetDist + mMMStartPosn;
            mTalon.set(ControlMode.MotionMagic, targetDist);
            mMMStarted = true;
        }
    }

    public void magicMoveStatus() {
        if (++mLoopCtr % 5 == 0) {
            printPosn("MM_" + mLoopCtr);
        }
    }

    /**
     * Return true when velocity zero and have moved at least 500 units from start
     * @return
     */
    public boolean magicMoveComplete() {
        double curVel = mTalon.getSelectedSensorVelocity(kPIDSlot_Move);
        int curPosn = Math.abs(getCurPosn());
        boolean end =  (curVel == 0) && (Math.abs(mMMStartPosn - curPosn) > 500);
        if (end) {
            printPosn("magicMoveComplete -------- ");
        }
        return end;
    }

    /**
     * Stop movement and move to closed loop position hold
     */
    public void magicStop() {
 //       mTalon.set(ControlMode.PercentOutput, 0);
        holdPosn();
    }


    //  code for fixed move  -----------------------------------------------------------------------

    private boolean mMoveDeltaComplete = false;

    public void moveDelta(Direction dir, double speed, double rotations) {

        mMoveDeltaComplete = false;
        int loopCtr = 0;
        if (dir == Direction.Up) {
            mTalon.setInverted(false);
        } else {
            mTalon.setInverted(true);
        }
        double startPosn = mTalon.getSelectedSensorPosition(0);
        double topEndPosn = startPosn + (rotations * kUnitsPerRotation);
        double botEndPosn = startPosn - (rotations * kUnitsPerRotation);
        mTalon.set(ControlMode.PercentOutput, speed);
        boolean complete = false;
        while (!complete) {
            sleep(1);
            double curPosn = mTalon.getSelectedSensorPosition(0);
            complete = (curPosn > botEndPosn) && (curPosn < topEndPosn);
            if (++loopCtr % 5 == 0) {
                printPosn("moveDelta");
            }
        }
        mMoveDeltaComplete = true;
    }

    public boolean moveDeltaComplete() {
        return mMoveDeltaComplete;
    }



    // ------------  code for using a motion profile  ----------------------------------------------

    /**
     * Specify the motion profile to use
     *
     * @param profile
     */
    public void setMPProfile(IMotionProfile profile) {
        System.out.println("ElevatorSys.setMPProfile:  setting up ");

        mMPController = new MotionProfileController("ElevatorMP", mTalon, profile, MotionProfileController.MPDirection.Positive);
        mPIDConfig = profile.getPIDConfig();

        //this.mMasterTalons.get(i).setControlFramePeriod(10, kTimeoutMs);
        mTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, kTimeoutMs);
}

    /**
     * Start the motion profile running
     */
    public void startMotionProfile() {
        CrashTracker.logMessage("ElevatorSys.startMotionProfile  ");
        mMPController.startMotionProfile();
    }

    public boolean isProfileComplete() {
        return mMPController.isComplete();
    }

    public void runProfile() {
        mMPController.control();
    }


//    public void updateTalonRequiredMPState() {
//        SetValueMotionProfile setOutput = this.mMPController.getRequiredTalonMPState();
//        //System.out.println("ElevatorSys.updateTalonRequiredMPState: elevator val: " + setOutput.value);
//        mTalon.vv
//    }



    // --------------------------------------------------------------------

    /**
     * Utility method to allow us to get the encoder ticks at max speed, and check encoder phase
     */
    public void masterTalonTest() {
        //this.mTalon.set(ControlMode.Velocity, 1);
        //mTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        mTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        mTalon.setSensorPhase(false);
        SmartDashboard.putNumber("Encoder posn", mTalon.getSensorCollection().getQuadraturePosition());
        mTalon.set(ControlMode.PercentOutput, 1);
    }


    //  shuffleboard setup ---------------------------------------------------


    /**
     *
     * @return the current absolute position - pulsewidth
     */
    private int getCurPosn() {
        return mTalon.getSensorCollection().getPulseWidthPosition();
    }


    private double mLastRelPosn;
    private double mLastQuadPosn;

    private void printPosn(String caller) {
        int sensPosn = mTalon.getSelectedSensorPosition(0);
        String sensPosnSign = "(+)";
        int absSensPosn = Math.abs(sensPosn);

        if (sensPosn < 0) {
            // absSensPosn is negative if moving down
            sensPosnSign = "(-)";
        }
        int quadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        int pwPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        int pwDelta = pwPosn - mBasePosn;
        double pwVel = mTalon.getSensorCollection().getPulseWidthVelocity();
        int relDelta = absSensPosn - mBasePosn;
        int quadDelta = quadPosn - mBasePosn;
        double vel = mTalon.getSensorCollection().getQuadratureVelocity();
        double mout = mTalon.getMotorOutputPercent();
        double voltOut = mTalon.getMotorOutputVoltage();
        double curOut = mTalon.getOutputCurrent();
        mLastRelPosn = absSensPosn;
        double closedLoopErr = mTalon.getClosedLoopError(0);

        mLastQuadPosn = quadPosn;
//        System.out.println("ArmSys." + caller + ":    topSwitch: " + mTopCounter.get() + "   botSwitch: " + mBotCounter.get());
//        System.out.println("ArmSys." + caller + ":    Vel: " + vel + "  pwVel: " + pwVel + "  MotorOut: " + mout  +  "  voltOut: " + voltOut+ "  clErr: " + closedLoopErr);
//        System.out.println("ElvSys." + caller + ":  base: " + mBasePosn + "   sensPosn: " + sensPosnSign + absSensPosn + "  relDelta: " + relDelta
//                + "  quadPosn: " + quadPosn  + "  quadDelta: " + quadDelta + "  pwPosn: " + pwPosn + "  clErr: " + closedLoopErr);
        System.out.println("  out%: " + mout + "   volt: " + voltOut + "   curOut: " + curOut);
        //shuffleBd();
    }


    private void shuffleBd() {
        double sensPosn = mTalon.getSelectedSensorPosition(0);
        double quadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        double quadVel = mTalon.getSensorCollection().getQuadratureVelocity();
        double pwPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        double pwVel = mTalon.getSensorCollection().getPulseWidthVelocity();

        double motorOut = mTalon.getMotorOutputPercent();
        double voltOut = mTalon.getMotorOutputVoltage();
        double closedLoopErr = mTalon.getClosedLoopError(0);

        SmartDashboard.putNumber("Elev/MotorOuput", motorOut);
        SmartDashboard.putNumber("Elev/VoltOut", voltOut);
        SmartDashboard.putNumber("Elev/SensorPosn", sensPosn);
        SmartDashboard.putNumber("Elev/QuadPosn", quadPosn);
        SmartDashboard.putNumber("Elev/QuadVel", quadVel);
        SmartDashboard.putNumber("Elev/PulseWPosn", pwPosn);
        SmartDashboard.putNumber("Elev/PulseWVel", pwVel);
        SmartDashboard.putNumber("Elev/MotorOuput", motorOut);
        SmartDashboard.putNumber("Elev/ClosedLoopErr", closedLoopErr);
    }

}
