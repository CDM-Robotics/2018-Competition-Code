package org.cdm.team6072.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.profiles.MotionProfileController;
import org.cdm.team6072.profiles.PIDConfig;
import org.omg.IOP.TAG_MULTIPLE_COMPONENTS;
import util.CrashTracker;

/**
 * ArmSys has a single talon used to move the arm through an arc of +- 80 degrees from horizontal
 *
 * The IntakeMotorSys is attached to teh end of the ArmSys, and is used to hold the cubes.
 */
public class ArmSys extends Subsystem {


    /**
     * Specify the direction the ARM should move
     */
    public static enum Direction {
        Up,
        Down
    }


    // start position is with cube loaded and arm folded all in
    //private static int POSN_START = -1180;

    // measure PW posn at power up - assume we are in the start posn
    private int mPosn_START;

    // intake position

//    private static int POSN_INTAKE = 2690;
//    private static int POSN_SHOOT45 = 1473;
//    private static int POSN_SHOOT135 = -400;

    // positions are sensor units to move a given angle from START position
    private static int POSN_START_DELTA = 0;
    private static int POSN_INTAKE_DELTA = 4100;
    private static int POSN_SHOOT45_DELTA = 2998;
    private static int POSN_SHOOT135_DELTA = 1172;


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

    private WPI_TalonSRX mTalon;
    private static final boolean TALON_INVERT = true;
    //  The sensor position must move in a positive direction as the motor controller drives positive output (and LEDs are green)
    //      true inverts the sensor
    private static final boolean TALON_SENSOR_PHASE = true;

    private static final int TALON_FORWARD_LIMIT = 1000;

    private static final int TALON_REVERSE_LIMIT = 0;

    private static final boolean TALON_ENABLE_SOFT_LIMIT = false;

    /*
     * set the allowable closed-loop error, Closed-Loop output will be
     * neutral within this range. See Table in Section 17.2.1 for native units per rotation.
     */
    private static final int TALON_ALLOWED_CLOSELOOP_ERROR = 220;


    /**
     * Which PID slot to pull gains from.  Starting 2018, you can choose
     * from 0,1,2 or 3.  Only the first two (0,1) are visible in web-based configuration.
     */
    private static final int kPIDSlot_Move = 0;
    private static final int kPIDSlot_Hold = 1;
    private static final int kPIDSlot_2 = 2;
    private static final int kPIDSlot_3 = 3;

    private MotionProfileController mMPController;

    private PIDConfig mPIDConfig;

    // set up the top and bottom limit switches and the related counters
    private DigitalInput mTopSwitch;
    private Counter mTopCounter;
    private DigitalInput mBotSwitch;
    private Counter mBotCounter;


    private static ArmSys mInstance = null;
    public static ArmSys getInstance() {
        if (mInstance == null) {
            mInstance = new ArmSys();
        }
        return mInstance;
    }


    private ArmSys() {
        CrashTracker.logMessage("ArmSys.ctor: initializing");
        boolean sensorPhase = false;  // checked 2018-02-16
        boolean motorInvert = false;
        double peakOut = 1.0;           // 1.0 is max
        try {
            // set up the limit switches - counter is used to detect switch closing because might be too fast
            mTopSwitch = new DigitalInput(RobotConfig.ARM_SWITCH_TOP);
            mTopCounter = new Counter(mTopSwitch);
            mBotSwitch = new DigitalInput(RobotConfig.ARM_SWITCH_BOT);
            mBotCounter = new Counter(mBotSwitch);
            mTopCounter.reset();
            mBotCounter.reset();
            System.out.println("ArmSys.ctor:  topSw: " + mTopSwitch.getChannel() + "  botSw: " + mBotSwitch.getChannel());

            mTalon = new WPI_TalonSRX(RobotConfig.ARM_TALON);
            mTalon.setName(String.format("%d: Arm", RobotConfig.ARM_TALON));
            // in case we are in magic motion or position hold mode
            mTalon.set(ControlMode.PercentOutput, 0);
            mTalon.setSensorPhase(TALON_SENSOR_PHASE);
            mTalon.setInverted(TALON_INVERT);

            mTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
            mTalon.configNeutralDeadband(kNeutralDeadband, kTimeoutMs);

            //mTalon.configForwardSoftLimitThreshold(TALON_FORWARD_LIMIT, kTimeoutMs);
            //mTalon.configForwardSoftLimitEnable(TALON_ENABLE_SOFT_LIMIT, kTimeoutMs);
            //mTalon.configReverseSoftLimitThreshold(TALON_REVERSE_LIMIT, kTimeoutMs);
            //mTalon.configReverseSoftLimitEnable(TALON_ENABLE_SOFT_LIMIT, kTimeoutMs);

            mTalon.configOpenloopRamp(0.1, 10);

            // set slot zero for position hold closed loop
            mTalon.configNominalOutputForward(0, kTimeoutMs);
            mTalon.configNominalOutputReverse(0, kTimeoutMs);
            mTalon.configPeakOutputForward(peakOut, kTimeoutMs);
            mTalon.configPeakOutputReverse(-peakOut, kTimeoutMs);

            // set acceleration and cruise velocity - see documentation
            // measured velocity 300 units / 100 mSec
            mTalon.configMotionCruiseVelocity(150, kTimeoutMs);
            mTalon.configMotionAcceleration(1000, kTimeoutMs);
            /*
             * set the allowable closed-loop error, Closed-Loop output will be
             * neutral within this range. See Table in Section 17.2.1 for native units per rotation.
             */
            mTalon.configAllowableClosedloopError(0, TALON_ALLOWED_CLOSELOOP_ERROR, kTimeoutMs);
            // do not use closed loop ramp - interfers with PID
            //mTalon.configClosedloopRamp(0.1, kTimeoutMs);

            // set PID values for postion hold closed loop
            mTalon.config_kF(kPIDSlot_Hold, 0.0, kTimeoutMs);             // 1023/1180
            mTalon.config_kP(kPIDSlot_Hold, 0.8, kTimeoutMs);  // original val 0.8            // 1023 / 400  * 0.1
            mTalon.config_kI(kPIDSlot_Hold, 0.0, kTimeoutMs);
            mTalon.config_kD(kPIDSlot_Hold, 0.0, kTimeoutMs);

            // PID values for moving
            mTalon.config_kF(kPIDSlot_Move, 0.0, kTimeoutMs);             // 1023/1180
            mTalon.config_kP(kPIDSlot_Move, 0.5, kTimeoutMs);             // 1023 / 400  * 0.1
            mTalon.config_kI(kPIDSlot_Move, 0.0, kTimeoutMs);
            mTalon.config_kD(kPIDSlot_Move, 5.0, kTimeoutMs);

            setSensorStartPosn();

        } catch (Exception ex) {
            System.out.println("************************** ArmSys.ctor Ex: " + ex.getMessage());
        }
    }

    /**
     * Ensure we are not in MotionMagic or PositionHold mode
     */
    public void resetTalon() {
        mTalon.set(ControlMode.PercentOutput, 0);
    }


    @Override
    public void initDefaultCommand() {
    }



    //  grab the 360 degree position of the MagEncoder's absolute position, and set the relative sensor to match.
    // should only be called on robot.init
    public void setSensorStartPosn() {
        mTalon.getSensorCollection().setPulseWidthPosition(0, kTimeoutMs);
        mPosn_START = mTalon.getSensorCollection().getQuadraturePosition();
        int absolutePosition = mPosn_START;
        /* mask out overflows, keep bottom 12 bits */
        absolutePosition &= 0xFFF;
        if (TALON_SENSOR_PHASE)
            absolutePosition *= -1;
        if (TALON_INVERT)
            absolutePosition *= -1;
        /* set the quadrature (relative) sensor to match absolute */
        mTalon.setSelectedSensorPosition(absolutePosition, 0, kTimeoutMs);
        printPosn("setStart");
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


    private int mCounter = 0;


    // manual move -----------------------------------------------------------------------

    public void initForMove() {
        mCounter = 0;
        mStopMode = StopMode.Moving;
        mLastRelPosn = mTalon.getSelectedSensorPosition(0);
        mLastQuadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        initBotSwitch();
        initTopSwitch();
        mTalon.selectProfileSlot(kPIDSlot_Move, 0);
        printPosn("initForMove");
    }

    public void move(ArmSys.Direction dir, double speed) {
        if (topSwitchSet() || botSwitchSet()) {
            System.out.println("*****************  ArmSys.move:  switch hit  top:" + mTopCounter.get() + "  bot: " + mBotCounter.get());
            mTalon.set(ControlMode.PercentOutput, 0);
            return;
        }
        if (dir == Direction.Down) {
            speed = -speed;
        }
        mTalon.set(ControlMode.PercentOutput, speed);
        if (++mCounter % 5 == 0) {
            printPosn("move");
        }
    }


    // manual stop -----------------------------------------------------

    private enum StopMode {
        Moving,
        Stopping,
        StopComplete,
    }

    private StopMode mStopMode = StopMode.Moving;

    private double mLastError = 0;
    private int mStopCallCount = 0;


    /**
     * Start the stop process, at end move to closed loop position hold
     */
    public void initStop() {
        mTalon.set(ControlMode.PercentOutput, 0);
    }

    public void stopping() {
        double output = mTalon.getMotorOutputPercent();
        double outVolts = mTalon.getMotorOutputVoltage();
        System.out.printf("ElvSys.stopping:  output%%: %.2f    volts: %.3f  \r\n", output, outVolts);
    }

    public boolean stopComplete() {

        double output = mTalon.getMotorOutputPercent();
        if (output < 0.1) {
            System.out.printf("ElvSys.stopComplete:  output%%: %.2f    \r\n", output);
            holdPosn();
            return true;
        }
        return false;
    }


    /**
     * Switch to using closed loop position hold at current position
     */
    private void holdPosn() {
        // select the hold PID slot
        mTalon.selectProfileSlot(kPIDSlot_Hold, 0);

        double curPosn = mTalon.getSelectedSensorPosition(0);
        //double curPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        printPosn("holdPosn");
        int loopCnt = 0;
        mTalon.set(ControlMode.Position, curPosn);
//        if (mWatchdog != null) {
//            // cancel the existing watchdog, set a new one
//            mWatchdog.Cancel();
//        }
        //mWatchdog = TalonWatchdog.SetWatchdog(mTalon, 3, kPIDLoopIdx, TALON_ALLOWED_CLOSELOOP_ERROR + 50);
    }


    public void xxstopping() {
        boolean finished;
        double curPosn;

        curPosn = mTalon.getSelectedSensorPosition(0);
        if (mStopMode == StopMode.Moving) {

            mTalon.set(ControlMode.PercentOutput, 0);
            printPosn("Arm.stop.before");
            // In Position mode, output value is in encoder ticks or an analog value, depending on the sensor.
            mTalon.selectProfileSlot(kPIDSlot_Hold, 0);
            mTalon.set(ControlMode.Position, curPosn);
            mLastError = -1;
            mStopCallCount = 0;
            mStopMode = StopMode.Stopping;
        }
        else if (mStopMode == StopMode.Stopping) {
            // see if the hold is complete
            double curError = mTalon.getClosedLoopError(0);
            if (mLastError != -1) {
                finished = (Math.abs(curError - mLastError) < 100);
                if (finished) {
                    mStopMode = StopMode.StopComplete;
                    //System.out.println("Arm.stop.StopComplete   curErr: " + curError + "   lastErr: " + mLastError );
                    printPosn("Arm.stop.StopComplete_" + mStopCallCount);
                }
            }
            if (++mStopCallCount % 5 == 0) {
                printPosn("Arm.stop.after_" + mStopCallCount);
            }
            mLastError = curError;
        }
        else {
            printPosn("Arm.stop.  STOPPED  ----------------------------------------------" );
        }
    }

    public boolean xxstopComplete() {
        return mStopMode == StopMode.StopComplete;
    }



    // target moving        --------------------------------------------------------------------


    /**
     *
     * @return the current absolute position - pulsewidth
     */
    private int getCurPosn() {
        //return mTalon.getSensorCollection().getPulseWidthPosition();
        return mTalon.getSensorCollection().getQuadraturePosition();
    }


    public void moveToStart() {
        moveToTarget(POSN_START_DELTA);
    }
    public boolean moveToStartComplete() {
        return moveToTargetComplete();
    }

    public void moveToIntake() {
        moveToTarget(POSN_INTAKE_DELTA);
    }
    public boolean moveToIntakeComplete() {
        return moveToTargetComplete();
    }

    public void moveToShoot45() {
        moveToTarget(POSN_SHOOT45_DELTA);
    }
    public boolean moveToShoot45Complete() {
        return moveToTargetComplete();
    }

    public void moveToShoot135() {
        moveToTarget(POSN_SHOOT135_DELTA);
    }
    public boolean moveToShoot135Complete() {
        return moveToTargetComplete();
    }


    private int mCalcTarg;
    private boolean mMMStarted;
    private int mLoopCtr = 0;

    private void moveToTarget(int targPosnDelta) {
        Direction dir;

        mTalon.selectProfileSlot(kPIDSlot_Move,0);
        mCalcTarg = mPosn_START + targPosnDelta;
//        mCalcTarg = mPosn_START + targPosnDelta - getCurPosn();
        mMMStartPosn = getCurPosn();
        System.out.println("ArmSys.moveToTarget:  mPosn_START: " + mPosn_START + "  delta: " + targPosnDelta  + "  calcTarg: " + mCalcTarg+ "  curPosn: " + getCurPosn());
        mMMStarted = false;
        mLoopCtr = 0;
        mTalon.set(ControlMode.Position, mCalcTarg);
        mMMStarted = true;
    }


    public boolean moveToTargetComplete() {
        double curVel = mTalon.getSelectedSensorVelocity(kPIDSlot_Move);
        int curPosn = Math.abs(getCurPosn());
        boolean end =  (curVel == 0) && (Math.abs(mCalcTarg - curPosn) < 300);
        if (end) {
            printPosn("moveToTargetComplete -------- ");
        }
        return end;
    }


    public void moveStatus() {
        if (++mLoopCtr % 5 == 0) {
            printPosn("MS_" + mLoopCtr);
        }
    }


    // implement target move manually ---------------------------------------------------------

    // full range from start to intake is about 3900 ticks

    private static int TICKS_PER_DEGREE = (int)(3900 / 180);

    // calculate allowed dist error in inches
    private static final int ALLOWED_DISTERR = 5 * TICKS_PER_DEGREE;
    private float mDistance;

    private int mStartPosn;
    private int mTargetDist;
    private int mTargPosn;

    // set to +1 for forward, -1 for back
    private int mMotorDirn = 1;

    // set this true when get within ERR of target, to prevent further driving
    private boolean mHitTarg = false;
    private int mLastErr;
    private int mMoveDistLoopCnt;


    // use a PW delta from START posn to get target
    public void startMoveTarget(int targPosnDelta) {
        mHitTarg = false;
        mCalcTarg = mPosn_START - targPosnDelta;
        double curPosn = getCurPosn();
        if (mCalcTarg > curPosn) {
            mMotorDirn = 1;
        }
        else {
            mMotorDirn = -1;
        }
        mTalon.set(ControlMode.PercentOutput, mMotorDirn * 0.8);
        System.out.println("AS.startMoveTarget:  mPosn_START: " + mPosn_START + "  delta: " + targPosnDelta  + "  calcTarg: " + mCalcTarg+ "  curPosn: " + getCurPosn());
        mHitTarg = false;
        mMoveDistLoopCnt = 0;
        mLastErr = 99999999;
    }


    /**
     * Called by the command exec loop
     * Stop if with error bound of target, OR if error is growing - we went past target
     */
    public boolean moveTargetExec() {

        int curPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        int curErr = Math.abs(mTargPosn - curPosn);
        mHitTarg = (curErr < ALLOWED_DISTERR) || curErr > mLastErr;
        if (mMoveDistLoopCnt++ % 5 == 0) {
            //System.out.printf("AS.moveTargetExec: start: %d   cur: %d   targ: %d  curErr: %d  lastErr: %d  \r\n", mStartPosn, curPosn, mTargPosn, curErr, mLastErr);
            System.out.println("Arm.moveTargetExec: curPos: " + getCurPosn() + ", target: " + mMMTargetPosn + ", distLeft: " + (mTargPosn - getCurPosn()));
        }
        mLastErr = curErr;
        if (mHitTarg) {
            mTalon.set(ControlMode.PercentOutput, 0);
            holdPosn();
        }
        else {
            // start slowing down if within 15 degrees of target
            if (curErr < 15*TICKS_PER_DEGREE) {
                mTalon.set(ControlMode.PercentOutput, mMotorDirn * 0.3);
            }
        }
        return mHitTarg;
    }





    // utils ---------------------------------------------------------------------------------------


    //  shuffleboard setup ---------------------------------------------------


    private double mLastRelPosn;
    private double mLastQuadPosn;
    
    private double mMMStartPosn;
    private double mMMTargetPosn;

    public void printPosn(String caller) {
        int sensPosn = mTalon.getSelectedSensorPosition(0);
        String sensPosnSign = "(+)";
        int absSensPosn = Math.abs(sensPosn);

        if (sensPosn < 0) {
            // absSensPosn is negative if moving down
            sensPosnSign = "(-)";
        }
        int quadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        int pwPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        int pwDelta = pwPosn - mPosn_START;
        double pwVel = mTalon.getSensorCollection().getPulseWidthVelocity();
        int relDelta = absSensPosn - mPosn_START;
        int quadDelta = quadPosn - mPosn_START;
        double vel = mTalon.getSensorCollection().getQuadratureVelocity();
        double mout = mTalon.getMotorOutputPercent();
        double voltOut = mTalon.getMotorOutputVoltage();
        mLastRelPosn = absSensPosn;
        double closedLoopErr = mTalon.getClosedLoopError(0);

        mLastQuadPosn = quadPosn;
//        System.out.println("ArmSys." + caller + ":    topSwitch: " + mTopCounter.get() + "   botSwitch: " + mBotCounter.get());
//        System.out.println("ArmSys." + caller + ":    Vel: " + vel + "  pwVel: " + pwVel + "  MotorOut: " + mout  +  "  voltOut: " + voltOut+ "  clErr: " + closedLoopErr);
        System.out.println("ArmSys." + caller + "  base: " + mPosn_START + "  sens: " + sensPosnSign + absSensPosn + ", currPos: " + getCurPosn()
                + "  quad: " + quadPosn  +  "  pw: " + pwPosn + "  clErr: " + closedLoopErr);
        //shuffleBd();
    }


    private void shuffleBd() {
        double curRelPosn = Math.abs(mTalon.getSelectedSensorPosition(0));
        double quadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        double quadVel = mTalon.getSensorCollection().getQuadratureVelocity();
        double pwPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        double pwVel = mTalon.getSensorCollection().getPulseWidthVelocity();

        double mout = mTalon.getMotorOutputPercent();
        double voltOut = mTalon.getMotorOutputVoltage();
        double closedLoopErr = mTalon.getClosedLoopError(0);

        SmartDashboard.putNumber("Arm/MotorOuput", mout);
        SmartDashboard.putNumber("Arm/VoltOut", voltOut);
        SmartDashboard.putNumber("Arm/SensorPosn", curRelPosn);
        SmartDashboard.putNumber("Arm/QuadPosn", quadPosn);
        SmartDashboard.putNumber("Arm/QuadVel", quadVel);
        SmartDashboard.putNumber("Arm/PulseWPosn", pwPosn);
        SmartDashboard.putNumber("Arm/PulseWVel", pwVel);
        SmartDashboard.putNumber("Arm/MotorOuput", mout);
        SmartDashboard.putNumber("Arm/ClosedLoopErr", closedLoopErr);
    }


}
