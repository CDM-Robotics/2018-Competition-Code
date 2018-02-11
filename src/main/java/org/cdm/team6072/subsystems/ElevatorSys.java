package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
     * Using CTRE Magnetic Encoder.
     *
     * @link https://github.com/CrossTheRoadElec/Phoenix-Documentation#what-are-the-units-of-my-sensor
     */
    public static final double kSensorUnitsPerRotation = 4096;

    public static final double kAndyMarkUnitsPerRotation = 80;

    /**
     * Which PID slot to pull gains from.  Starting 2018, you can choose
     * from 0,1,2 or 3.  Only the first two (0,1) are visible in web-based configuration.
     */
    public static final int kSlotIdx = 0;

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


    private WPI_TalonSRX mTalon;

    private MotionProfileController mMPController;

    private PIDConfig mPIDConfig;


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
     */
    private ElevatorSys() {
        CrashTracker.logMessage("ElevatorSys Subsystem initializing");
        try {
            mTalon = new WPI_TalonSRX(RobotConfig.ELEVATOR_TALON);
//            mTalon.getSensorCollection().setQuadraturePosition(0, 10);
            mTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, kPIDLoopIdx, kTimeoutMs);
            mTalon.setSensorPhase(true);
            mTalon.setSelectedSensorPosition(0, 0, 10);
            mTalon.configNeutralDeadband(kNeutralDeadband, kTimeoutMs);

            // set slot zero for position hold closed loop
            /* set the peak and nominal outputs, 12V means full */
            mTalon.configNominalOutputForward(0, kTimeoutMs);
            mTalon.configNominalOutputReverse(0, kTimeoutMs);
            mTalon.configPeakOutputForward(1, kTimeoutMs);
            mTalon.configPeakOutputReverse(-1, kTimeoutMs);
            /*
             * set the allowable closed-loop error, Closed-Loop output will be
             * neutral within this range. See Table in Section 17.2.1 for native units per rotation.
             */
            mTalon.configAllowableClosedloopError(0, kPIDLoopIdx, kTimeoutMs);

		    /* set closed loop gains in slot0, typically kF stays zero. */
            mTalon.config_kF(kPIDLoopIdx, 0.0, kTimeoutMs);
            mTalon.config_kP(kPIDLoopIdx, 0.2, kTimeoutMs);
            mTalon.config_kI(kPIDLoopIdx, 0.0, kTimeoutMs);
            mTalon.config_kD(kPIDLoopIdx, 0.0, kTimeoutMs);

            /*
             * grab the 360 degree position of the MagEncoder's absolute position, and set the relative sensor to match.
             */
            int absolutePosition = mTalon.getSensorCollection().getPulseWidthPosition();
		    /* mask out overflows, keep bottom 12 bits */
            absolutePosition &= 0xFFF;
            if (kSensorPhase)
                absolutePosition *= -1;
            if (kMotorInvert)
                absolutePosition *= -1;
		    /* set the quadrature (relative) sensor to match absolute */
            mTalon.setSelectedSensorPosition(absolutePosition, kPIDLoopIdx, kTimeoutMs);
            double curPosn = mTalon.getSelectedSensorPosition(0);
            System.out.println("ElevatorSys.ctor: sensor abs posn: " + absolutePosition  +  "  sensor rel posn: " + curPosn);

            // test  motion profile --------------
//            System.out.println("ElevatorSys.setMPProfile:  setting Talon control mode to MotionProfile ");
//            mTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
//            System.out.println("ElevatorSys.setMPProfile:  back from setting Talon ");

            // test magic motion

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
    }


    @Override
    protected void initDefaultCommand() {

    }



    // ------------- code for open loop target  -----------------------------------------------

    public void setTarget(double target) {
        mTarget = target;
    }


    /**
     * Thhis needs to return true when the elevator has reached the target position,
     * or if the elevator has hit a limit switch
     *
     * @return
     */
    public boolean targetReached() {
        return false;
    }

    public void resetSystemState() {
        mTalon.setInverted(false);
    }

    private int mCounter = 0;
    private double mLastPosn;

    public void initForMove() {
        mCounter = 0;
        mLastPosn = mTalon.getSelectedSensorPosition(0);
        System.out.println("ElevatorSys.initForMove: cur posn: " + mLastPosn + "   ----------------------" );
    }

    public void move(Direction dir, double speed) {
        if (dir == Direction.Up) {
            mTalon.setInverted(false);
        } else {
            mTalon.setInverted(true);
        }
        mTalon.set(ControlMode.PercentOutput, speed);
        if (++mCounter % 10 == 0) {
            double curPosn = mTalon.getSelectedSensorPosition(0);
            double delta = curPosn - mLastPosn;
            mLastPosn = curPosn;
            System.out.println("ElevatorSys.move: cur posn: " + curPosn + "   delta: "  + delta);
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
        double curPosn = mTalon.getSelectedSensorPosition(0);
        // In Position mode, output value is in encoder ticks or an analog value, depending on the sensor.
        mTalon.set(ControlMode.Position, curPosn);
        System.out.println("ElevatorSys.holdPosn: cur posn: " + mTalon.getSelectedSensorPosition(0) + "   ----------------------" );
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


}
