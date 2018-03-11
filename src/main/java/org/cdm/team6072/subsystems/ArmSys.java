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
import util.CrashTracker;

/**
 * ArmSys has a single talon used to move the arm through an arc of +- 80 degrees from horizontal
 *
 * ArmSys is attached to the ArmSys, and moves up and dwon with it.
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

    /**
     * Which PID slot to pull gains from.  Starting 2018, you can choose
     * from 0,1,2 or 3.  Only the first two (0,1) are visible in web-based configuration.
     */
    public static final int kPIDSlot_0 = 0;
    public static final int kPIDSlot_1 = 1;
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

    private WPI_TalonSRX mTalon;

    private MotionProfileController mMPController;

    private PIDConfig mPIDConfig;

    // set up the top and bottom limit switches and the related counters
    private DigitalInput mTopSwitch;
    private Counter mTopCounter;
    private DigitalInput mBotSwitch;
    private Counter mBotCounter;

    private int mBasePosn;


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
            mTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
            mTalon.setSensorPhase(sensorPhase);
            mTalon.configNeutralDeadband(kNeutralDeadband, kTimeoutMs);

            mTalon.configOpenloopRamp(0.25, 10);

            // set slot zero for position hold closed loop
            mTalon.configNominalOutputForward(0, kTimeoutMs);
            mTalon.configNominalOutputReverse(0, kTimeoutMs);
            mTalon.configPeakOutputForward(peakOut, kTimeoutMs);
            mTalon.configPeakOutputReverse(-peakOut, kTimeoutMs);
            /*
             * set the allowable closed-loop error, Closed-Loop output will be
             * neutral within this range. See Table in Section 17.2.1 for native units per rotation.
             */
            mTalon.configAllowableClosedloopError(kPIDLoopIdx, 200, kTimeoutMs);

            // set PID values for postion hold closed loop
            mTalon.config_kF(0, 0.0, kTimeoutMs);             // 1023/1180
            mTalon.config_kP(0, 1.5, kTimeoutMs);             // 1023 / 400  * 0.1
            mTalon.config_kI(0, 0.0, kTimeoutMs);
            mTalon.config_kD(0, 1.0, kTimeoutMs);

            //  grab the 360 degree position of the MagEncoder's absolute position, and set the relative sensor to match.
            mTalon.getSensorCollection().setPulseWidthPosition(0, 10);
            int absolutePosition = mTalon.getSensorCollection().getPulseWidthPosition();
            mBasePosn = absolutePosition;

		    /* mask out overflows, keep bottom 12 bits */
            absolutePosition &= 0xFFF;
            if (sensorPhase)
                absolutePosition *= -1;
            if (motorInvert)
                absolutePosition *= -1;
		    /* set the quadrature (relative) sensor to match absolute */
            mTalon.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);
            //mTalon.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
            // setSelected takes time so wait for it to get accurate print
            Thread.sleep(100);
            printPosn("ctor");

            // test  motion profile --------------
//            System.out.println("ArmSys.setMPProfile:  setting Talon control mode to MotionProfile ");
//            mTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
//            System.out.println("ArmSys.setMPProfile:  back from setting Talon ");

            // test magic motion

        } catch (Exception ex) {
            System.out.println("************************** ArmSys.ctor Ex: " + ex.getMessage());
        }
    }


    public void resetStart() {
        int absolutePosition = mTalon.getSensorCollection().getPulseWidthPosition();
        mBasePosn = absolutePosition;
    }



    private void sleep(int milliSecs) {
        try {
            Thread.sleep(milliSecs);
        } catch (Exception ex) {}
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


    @Override
    public void initDefaultCommand() {

    }


    private int mCounter = 0;


    public void initForMove() {
        mCounter = 0;
        mStopMode = StopMode.Moving;
        mLastRelPosn = mTalon.getSelectedSensorPosition(0);
        mLastQuadPosn = mTalon.getSensorCollection().getQuadraturePosition();
        initBotSwitch();
        initTopSwitch();
        		    /* set closed loop gains in slot0, typically kF stays zero. */
//        mTalon.config_kF(kPIDLoopIdx, 0.867, kTimeoutMs);             // 1023/1180
//        mTalon.config_kP(kPIDLoopIdx, 0.0, kTimeoutMs);             // 1023 / 400  * 0.1
//        mTalon.config_kI(kPIDLoopIdx, 0.0, kTimeoutMs);
//        mTalon.config_kD(kPIDLoopIdx, 0.0, kTimeoutMs);
        printPosn("initForMove");
    }

    public void move(ArmSys.Direction dir, double speed) {
        if (topSwitchSet() || botSwitchSet()) {
            System.out.println("*****************  ArmSys.move:  switch hit  top:" + mTopCounter.get() + "  bot: " + mBotCounter.get());
            stop();
            return;
        }
        if (dir == ArmSys.Direction.Up) {
            mTalon.setInverted(true);
        } else {
            mTalon.setInverted(false);
        }
        mTalon.set(ControlMode.PercentOutput, speed);
        if (++mCounter % 5 == 0) {
            printPosn("move");
        }
    }

    private enum StopMode {
        Moving,
        Stopping,
        StopComplete,
    }

    private StopMode mStopMode = StopMode.Moving;

    private double mLastError = 0;
    private int mStopCallCount = 0;

    /**
     * Stop movement and move to closed loop position hold
     */
    public void stop() {
        boolean finished;
        double curPosn;

        curPosn = mTalon.getSelectedSensorPosition(0);
        if (mStopMode == StopMode.Moving) {
            mTalon.set(ControlMode.PercentOutput, 0);
            /* set closed loop gains in slot0, typically kF stays zero. */
//            mTalon.config_kF(0, 0.0, kTimeoutMs);             // 1023/1180
//            mTalon.config_kP(0, 2.0, kTimeoutMs);             // 1023 / 400  * 0.1
//            mTalon.config_kI(0, 0.0, kTimeoutMs);
//            mTalon.config_kD(0, 0.5, kTimeoutMs);
            //double curPosn = Math.abs(mTalon.getSelectedSensorPosition(0));
            printPosn("Arm.stop.before");
            // In Position mode, output value is in encoder ticks or an analog value, depending on the sensor.
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

    public boolean stopComplete() {
        return mStopMode == StopMode.StopComplete;
    }


    /**
     * The position provided is a delta from the designated start posn
     * @param posn
     */
    public void MoteToPosn(int posn) {

    }
    
    
    
    // utils ---------------------------------------------------------------------------------------


    //  shuffleboard setup ---------------------------------------------------


    private double mLastRelPosn;
    private double mLastQuadPosn;
    
    private double mMMStartPosn;
    private double mMMTargetPosn;

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
        mLastRelPosn = absSensPosn;
        double closedLoopErr = mTalon.getClosedLoopError(0);

        mLastQuadPosn = quadPosn;
//        System.out.println("ArmSys." + caller + ":    topSwitch: " + mTopCounter.get() + "   botSwitch: " + mBotCounter.get());
//        System.out.println("ArmSys." + caller + ":    Vel: " + vel + "  pwVel: " + pwVel + "  MotorOut: " + mout  +  "  voltOut: " + voltOut+ "  clErr: " + closedLoopErr);
        System.out.println("ArmSys." + caller + ":   sensPosn: " + sensPosnSign + absSensPosn + "  relDelta: " + relDelta
                + "  quadPosn: " + quadPosn  + "  quadDelta: " + quadDelta + "  pwPosn: " + pwPosn + "  clErr: " + closedLoopErr);
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
