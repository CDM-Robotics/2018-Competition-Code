package org.cdm.team6072.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.profiles.Constants;
import org.cdm.team6072.profiles.IMotionProfile;
import org.cdm.team6072.profiles.MotionProfileController;
import org.cdm.team6072.profiles.PIDConfig;
import util.CrashTracker;



public class ElevatorSys extends Subsystem {

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


    private WPI_TalonSRX mElevatorTalon;

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
     and quadrature encoder). Therefore the sensor provides two modes of use: absolute and relative.
     The advantage of absolute mode is having a solid reference to where a mechanism is without
     re-tare-ing or re-zero-ing the robot. The advantage of the relative mode is the faster update
     rate. However both values can be read/written at the same time. So a combined strategy of
     seeding the relative position based on the absolute position can be used to benefit from the
     higher sampling rate of the relative mode and still have an absolute sensor position
     */
    private ElevatorSys() {
        CrashTracker.logMessage("ElevatorSys Subsystem initializing");
        try {
            mElevatorTalon = new WPI_TalonSRX(RobotConfig.ELEVATOR_TALON);
            mElevatorTalon.getSensorCollection().setQuadraturePosition(0, 10);
            mElevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
            mElevatorTalon.setSensorPhase(true);
            mElevatorTalon.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);

            // test  motion profile --------------
//            System.out.println("ElevatorSys.setMPProfile:  setting Talon control mode to MotionProfile ");
//            mElevatorTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
//            System.out.println("ElevatorSys.setMPProfile:  back from setting Talon ");

            // test magic motion

        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
    }


    @Override
    protected void initDefaultCommand() {

    }



    // ------------  code for using a motion profile  ----------------------------------------------

    /**
     * Specify the motion profile to use
     * @param profile
     */
    public void setMPProfile(IMotionProfile profile) {
        System.out.println("ElevatorSys.setMPProfile:  setting up ");

        mMPController = new MotionProfileController("ElevatorMP", mElevatorTalon, profile, MotionProfileController.MPDirection.Positive);
        mPIDConfig = profile.getPIDConfig();

        //this.mMasterTalons.get(i).setControlFramePeriod(10, Constants.kTimeoutMs);
        mElevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
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
//        mElevatorTalon.vv
//    }






    // ------------- code for open loop target  -----------------------------------------------

    public void setTarget(double target) {
        mTarget = target;
    }


    /**
     * Thhis needs to return true when the elevator has reached the target position,
     * or if the elevator has hit a limit switch
     * @return
     */
    public boolean targetReached() {
        return false;
    }

    public void resetSystemState() {
        mElevatorTalon.setInverted(false);
    }


    public void move(Direction dir, double speed) {
        if (dir == Direction.Up) {
            mElevatorTalon.setInverted(false);
        }
        else {
            mElevatorTalon.setInverted(true);
        }
        mElevatorTalon.set(ControlMode.PercentOutput, speed);
    }

    public void stop() {
        mElevatorTalon.set(0);
    }


    // --------------------------------------------------------------------

    /**
     * Utility method to allow us to get the encoder ticks at max speed, and check encoder phase
     */
    public void masterTalonTest() {
        //this.mElevatorTalon.set(ControlMode.Velocity, 1);
        //mElevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
        mElevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        mElevatorTalon.setSensorPhase(false);
        SmartDashboard.putNumber("Encoder posn", mElevatorTalon.getSensorCollection().getQuadraturePosition());
        mElevatorTalon.set(ControlMode.PercentOutput, 1);
    }






}
