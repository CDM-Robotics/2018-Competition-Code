package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
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

    public static final int kCTREUnitsPerRotation = 4096; // 4096;
    public static final int kUnitsPerRotation = kCTREUnitsPerRotation;
    
    // inches of elevator travel per complete rotation of encoder
    // gear is 1 inch diameter
    public static final double kDistancePerRotation = 1.75 * Math.PI;
    public static final int kUnitsPerInch = (int)Math.round(kUnitsPerRotation / kDistancePerRotation);

    // scale height in native units from nominal base posn
    // the arm assy is on a 2:1 gearing from the drive motor
    private static int INTAKE_POSN = 3 * kUnitsPerInch / 2;
    private static int SWITCH_POSN_UNITS = 35 * kUnitsPerInch / 2;
    private static int SCALELO_POSN_UNITS = 70 * kUnitsPerInch / 2;
    private static int SCALEHI_POSN_UNITS = 90 * kUnitsPerInch / 2;

    // if using Position PID, measure these values as delat from the absolute start position
    // then change the values in the moveToXXX() methods
    private static int INTAKE_POSN_DELTA = 3 * kUnitsPerInch / 2;
    private static int SWITCH_POSN_UNITS_DELTA = 35 * kUnitsPerInch / 2;
    private static int SCALELO_POSN_UNITS_DELTA = 70 * kUnitsPerInch / 2;
    private static int SCALEHI_POSN_UNITS_DELAT = 90 * kUnitsPerInch / 2;


    /**
     * Base trajectory period to add to each individual
     * trajectory point's unique duration.  This can be set
     * to any value within [0,255]ms.
     */
    public static final int kBaseTrajPeriodMs = 0;

    /**
     * Specify the direction the elevator should move
     *  Up = Talon.setInverted(false)
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

    // talon setup  -------------------------------------------------------------------------------

    private WPI_TalonSRX mTalon;

    private static final boolean TALON_INVERT = true;  //  The sensor position must move in a positive direction as the motor controller drives positive output (and LEDs are green)
    private static final boolean TALON_SENSOR_PHASE = true; // true inverts the sensor
    private static final int TALON_FORWARD_LIMIT = -1;
    private static final int TALON_REVERSE_LIMIT = -1;


    // set the allowable closed-loop error, Closed-Loop output will be
    // neutral within this range. See Table in Section 17.2.1 for native units per rotation.
    private static final int TALON_ALLOWED_CLOSELOOP_ERROR = 0;
    public static final double kNeutralDeadband = 0.01;  // Motor deadband, set to 1%.

    // Which PID slot to pull gains from.  Starting 2018, you can choose
    //from 0,1,2 or 3.  Only the first two (0,1) are visible in web-based configuration.
    public static final int kPIDSlot_Move = 1;
    public static final int kPIDSlot_Hold = 0;
    public static final int kPIDSlot_2 = 2;
    public static final int kPIDSlot_3 = 3;

     // Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops.
    public static final int kPIDLoopIdx = 0;

    // paramter to the configXXX() methods. Set to non-zero to have talon wait to check and report error
    public static final int kTimeoutMs = 10;

    private MotionProfileController mMPController;
    private PIDConfig mPIDConfig;

    // set up the top and bottom limit switches and the related counters
    private DigitalInput mTopSwitch;
    private Counter mTopCounter;
    private DigitalInput mBotSwitch;
    private Counter mBotCounter;


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
        try {

            mTalon = new WPI_TalonSRX(RobotConfig.ELEVATOR_TALON);
            mTalon.setName(String.format("%d: Elevator", RobotConfig.ELEVATOR_TALON));

            this.initLimitSwitches();

            // in case we are in magic motion or position hold mode
            mTalon.set(ControlMode.PercentOutput, 0);
            mTalon.setSensorPhase(TALON_SENSOR_PHASE);
            mTalon.setInverted(TALON_INVERT);
            mTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
            mTalon.configNeutralDeadband(kNeutralDeadband, kTimeoutMs);

            this.initSoftLimits();

            mTalon.configOpenloopRamp(0.1, kTimeoutMs);
            mTalon.setNeutralMode(NeutralMode.Brake);

            // see setup for motion magic in s/w manual 12.6
            mTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, kTimeoutMs);
            mTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, kTimeoutMs);

            this.initCurrentLimits();
            this.initDeadbandNominalConfig();


            // bot to top 29000 in 1 sec = 2900 ticks per 100 ms
            mTalon.configMotionCruiseVelocity(3500, kTimeoutMs);        // 2900
            mTalon.configMotionAcceleration(2500, kTimeoutMs);      // 2000
            mTalon.configAllowableClosedloopError(kPIDSlot_Move, TALON_ALLOWED_CLOSELOOP_ERROR, kTimeoutMs);

            this.initPIDValues();
            setSensorStartPosn();


        } catch (Exception ex) {
            System.out.println("************************** ElevatorSys.ctor Ex: " + ex.getMessage());
        }
    }


    // P gain is specified in motor output unit per error unit.
    //      For example, a value of 102 is ~9.97% (which is 102/1023) motor output per 1 unit of Closed-Loop Error.
    // I gain is specified in motor output unit per integrated error.
    //      For example, a value of 10 equates to ~0.97% for each accumulated error (Integral Accumulator).
    //      Integral accumulation is done every 1ms.
    private void initPIDValues() {

        // Move slot configuration
        mTalon.config_kF(kPIDSlot_Move, .4429, kTimeoutMs);        // 0.4429
        mTalon.config_kP(kPIDSlot_Move, 0.14, kTimeoutMs);      // 0.14
        mTalon.config_kI(kPIDSlot_Move, 0.0, kTimeoutMs);
        mTalon.config_kD(kPIDSlot_Move, 0.0, kTimeoutMs);

        // Hold slot configuration
        mTalon.configAllowableClosedloopError(kPIDSlot_Hold, TALON_ALLOWED_CLOSELOOP_ERROR, kTimeoutMs);
        mTalon.config_kF(kPIDSlot_Hold, 0.0, kTimeoutMs);        // normally 0 for position hold but putting in small 0.1 damps oscillation
        mTalon.config_kP(kPIDSlot_Hold, 2.0, kTimeoutMs);        // kP_turn 1.0 used on elevator 2018-02-17
        mTalon.config_kI(kPIDSlot_Hold, 0.0, kTimeoutMs);
        mTalon.config_kD(kPIDSlot_Hold, 20.0, kTimeoutMs);
    }

    private void initSoftLimits() {
        mTalon.configForwardSoftLimitThreshold(TALON_FORWARD_LIMIT, kTimeoutMs);
        mTalon.configForwardSoftLimitEnable(false, kTimeoutMs);
        mTalon.configReverseSoftLimitThreshold(TALON_REVERSE_LIMIT, kTimeoutMs);
        mTalon.configReverseSoftLimitEnable(false, kTimeoutMs);
    }

    private void initLimitSwitches() {
        // set up the limit switches - counter is used to detect switch closing because might be too fast
        mTopSwitch = new DigitalInput(RobotConfig.ELEVATOR_SWITCH_TOP);
        mTopCounter = new Counter(mTopSwitch);
        mBotSwitch = new DigitalInput(RobotConfig.ELEVATOR_SWITCH_BOT);
        mBotCounter = new Counter(mBotSwitch);
        mTopCounter.reset();
        mBotCounter.reset();
        System.out.println("ElevatorSys.ctor:  topSw: " + mTopSwitch.getChannel() + "  botSw: " + mBotSwitch.getChannel());
    }

    private void initCurrentLimits() {
        // set up current limits
        mTalon.configContinuousCurrentLimit(30, kTimeoutMs);
        mTalon.configPeakCurrentLimit(40, kTimeoutMs);
        mTalon.configPeakCurrentDuration(200, kTimeoutMs);
        mTalon.enableCurrentLimit(true);
    }

    private void initDeadbandNominalConfig() {
        // the nominal values are used in closed loop when in deadband
        mTalon.configNominalOutputForward(0, kTimeoutMs);
        mTalon.configNominalOutputReverse(0, kTimeoutMs);
        mTalon.configPeakOutputForward(1.0, kTimeoutMs);
        mTalon.configPeakOutputReverse(-1.0, kTimeoutMs);
    }


    /**
     * Ensure we are not in MotionMagic or PositionHold mode
     */
    public void resetTalon() {
        mTalon.set(ControlMode.PercentOutput, 0);
    }


    //  grab the 360 degree position of the MagEncoder's absolute position, and set the relative sensor to match.
    // should only be called on robot.init
    public void setSensorStartPosn() {
        mTalon.getSensorCollection().setPulseWidthPosition(0, kTimeoutMs);
        mBasePosn = mTalon.getSensorCollection().getPulseWidthPosition();
        int absolutePosition = mBasePosn;
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
    private boolean mFindBaseComplete = false;

    private void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (Exception ex) {}
    }


    /**
     * Move carefully to the bottom and then set position accordingly
     */
    public void findBase() {

        printPosn("findBase.start");
        mFindBaseComplete = false;
        int startPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        int curPosn = startPosn;
        int loopCtr = 0;
        mTalon.set(ControlMode.PercentOutput, 0.3);
        while (curPosn < startPosn + 100) {
            sleep(10);
            curPosn = mTalon.getSensorCollection().getPulseWidthPosition();
            if (++loopCtr % 10 == 0) {
                printPosn("findbase.moveUp_" + loopCtr);
            }
        }
        //now move down until we hit the bottom switch
        loopCtr = 0;
        initBotSwitch();
        int lastPosn = curPosn + 500;
        mTalon.set(ControlMode.PercentOutput, -0.3);
        // give it time to get moving
        sleep(10);
        while (Math.abs(lastPosn - curPosn) > 20) {
            // we are still moving down
            lastPosn = curPosn;
            sleep(20);
            curPosn = mTalon.getSensorCollection().getPulseWidthPosition();
            if (++loopCtr % 1 == 0) {
                printPosn("findBase.moveDown_" + loopCtr);
            }
        }
        setSensorStartPosn();
        mTalon.set(ControlMode.PercentOutput, 0);
        //holdPosn();
        printPosn("findBase.exit");
        mFindBaseComplete = true;
    }
//
//
//    private boolean mMoveToBaseComplete = false;
//
    public boolean findBaseComplete() {
        return mFindBaseComplete;
    }

    // ------------- code for open loop target  -----------------------------------------------
    public void initForMove() {
        mLastSensPosn = mTalon.getSelectedSensorPosition(0);
        mLastQuadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        mTalon.selectProfileSlot(kPIDSlot_Move,0);
        mMoveLoopCtr = 0;
        initBotSwitch();
        initTopSwitch();
        printPosn("initForMove");
    }

    private int mMoveLoopCtr  = 0;
    private int holdPos = 0;

    public void move(Direction dir, double speed) {
        if (topSwitchSet() || botSwitchSet()) {
            System.out.println("*****************  ElvSys.move:  switch hit:  top:" + mTopCounter.get() + "  bot: " + mBotCounter.get());
            mTalon.set(ControlMode.PercentOutput, 0);;
            return;
        }
        if (dir == Direction.Down) {
            speed = -speed;
            System.out.println("dirn down  " + speed);
        }
        else {
            System.out.println("dirn up  " + speed);
        }
        mTalon.set(ControlMode.PercentOutput, speed);
        if (++mMoveLoopCtr % 5 == 0) {
            printPosn("ElvSys.move");
        }
    }


    public void initStop() {
        mTalon.set(ControlMode.PercentOutput, 0);
        holdPos = getCurPosn();
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

    private TalonWatchdog mWatchdog;

    /**
     * Switch to using closed loop position hold at current position
     */
    private void holdPosn() {
        // select the hold PID slot
        mTalon.selectProfileSlot(kPIDSlot_Hold,0);

        double curPosn = mTalon.getSelectedSensorPosition(0);
        //double curPosn = mTalon.getSensorCollection().getPulseWidthPosition();
        //double curPosn = holdPos;
        printPosn("holdPosn.before");
        int loopCnt = 0;
        // In Position mode, output value is in encoder ticks or an analog value, depending on the sensor.
        mTalon.set(ControlMode.Position, curPosn);
        if (mWatchdog != null) {
            // cancel the existing watchdog, set a new one
            mWatchdog.Cancel();
        }
        //mWatchdog = TalonWatchdog.SetWatchdog(mTalon, 3, kPIDLoopIdx, TALON_ALLOWED_CLOSELOOP_ERROR + 50);

        boolean notFinished = true;
        double lastErr = -1;

//        while (notFinished && loopCnt < 50) {
//            try {
//                Thread.sleep(1);
//                double curError = mTalon.getClosedLoopError(0);
//                if (lastErr != -1) {
//                    notFinished = (Math.abs(curError) > TALON_ALLOWED_CLOSELOOP_ERROR);
//                }
//                lastErr = curError;
//                loopCnt++;
//                //if (loopCnt % 2 == 0) {
////                    printPosn("holdPosn.after_" + curPosn +"_" + loopCnt );
//                //}
//            } catch (Exception ex) {
//            }
//        }
        printPosn("holdPosn.FINISH_" + curPosn +"_" + loopCnt );
    }
    
    
    // move to the standard target positions  ---------------------------------------------------------------------

    /**
     * Move to the scale height from whatever our current position is
     */
    public void moveToIntake() {
        moveToTarget(INTAKE_POSN);
    }
    public boolean moveToIntakeComplete() {
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


    /**
     * mbasePosn is based on the sensor pulsewidth posn
     * @param targPosn
     */
    private void moveToTarget(int targPosn) {
        Direction dir;

        int distToMove = targPosn - getCurPosn();
        if (distToMove >= 0) {
            dir = Direction.Up;
        }
        else {
            dir = Direction.Down;
        }
        System.out.println("ElvSys.moveToTarget:  mBasePosn: " + mBasePosn + "  targPosn: " + targPosn + "  curPosn: " + getCurPosn() + "  distToMove: " + distToMove);

        // change here to switch to using Position PID
        // NOTE NOTE - magic move is using targPosn, but Position is using a delta
        initForMagicMove();
        magicMove(dir, Math.abs(targPosn));
    }
    
    public boolean moveToTargetComplete() {
        return magicMoveComplete();
    }



    // code for magic move  --------------------------------------------------------------------------

    private double mMMTargetPosn = -1;

    private boolean mMMStarted;
    private int mMMStartPosn;

    /**
     * Measured max vel was 1180 native units per 100 mSec
     *  kF = 1023 / 1180 = 0.867
     *  Set max cruise to 0.5 * 1180 = 885
     */
    public void initForMagicMove() {
        // set acceleration and cruise velocity - see documentation
        mTalon.selectProfileSlot(kPIDSlot_Move, 0);
        mMMStartPosn = getCurPosn();
        mMMStarted = false;
        mLoopCtr = 0;
        printPosn("initForMagicMove");
    }


    private int mLoopCtr = 0;

    /**
     * Use Magic motion profile to move to the specified target posn
     * @param dir
     * @param targPosn
     */
    private void magicMove(Direction dir, double targPosn) {
//        double targetDist;
        if (!mMMStarted) {
            mTalon.set(ControlMode.MotionMagic, targPosn);
            mMMStarted = true;
        }
    }

    /**
     * Called from the command.execute method - just print out the current move position occassionally
     */
    public void magicMoveStatus() {
//        if (++mLoopCtr % 5 == 0) {
//            printPosn("MM_" + mLoopCtr);
//        }
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
     * NOTE: MotionMagic will essentially turn into position hold at the end
     */
    public void magicStop() {
 //       mTalon.set(ControlMode.PercentOutput, 0);
        //holdPosn();
        printPosn("magicMoveStop -------- ");
    }


    // ********************************************************************** //
    // POSITION MODE FOR MOVING TO TARGET
    // ********************************************************************** //

    private int mPosn_START;
    private int mCalcTarg;


    private void moveToTargetPosn(int targPosnDelta) {
        Direction dir;

        mTalon.selectProfileSlot(kPIDSlot_Move,0);
        mCalcTarg = mPosn_START + targPosnDelta;
        mMMStartPosn = getCurPosn();
        System.out.println("ElvSys.moveToTarget:  mPosn_START: " + mPosn_START + "  delta: " + targPosnDelta  + "  calcTarg: " + mCalcTarg+ "  curPosn: " + getCurPosn());
        mMMStarted = false;
        mLoopCtr = 0;
        mTalon.set(ControlMode.Position, mCalcTarg);
        mMMStarted = true;
    }


    public boolean moveToTargetPosnComplete() {
        double curVel = mTalon.getSelectedSensorVelocity(kPIDSlot_Move);
        int curPosn = Math.abs(getCurPosn());
        boolean end =  (curVel == 0) && (Math.abs(mCalcTarg - curPosn) < 300);
        if (end) {
            printPosn("moveToTargetComplete -------- ");
        }
        return end;
    }



    // end  moving using Position mode instead of magic move  ----------------------------------------


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
//            sleep(1);
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


    /**
     *
     * @return the current absolute position - pulsewidth
     */
    private int getCurPosn() {
        return mTalon.getSelectedSensorPosition(0);
    }


    private double mLastSensPosn;
    private double mLastQuadPosn;

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
        int relDelta = absSensPosn - mBasePosn;
        int quadDelta = quadPosn - mBasePosn;
        mLastSensPosn = absSensPosn;
        double closedLoopErr = mTalon.getClosedLoopError(0);

        mLastQuadPosn = quadPosn;
        System.out.println("ElvSys." + caller + ":  base: " + mBasePosn + "  sens: " + sensPosnSign + absSensPosn + "  rDelta: " + relDelta
                + "  quad: " + quadPosn  + "  qDelta: " + quadDelta + "  pw: " + pwPosn + "  clErr: " + closedLoopErr);

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
