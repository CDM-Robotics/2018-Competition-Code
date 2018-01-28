package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.autonomous.Constants;
import org.cdm.team6072.autonomous.MotionProfile;
import org.cdm.team6072.autonomous.MotionProfileController;
import org.cdm.team6072.autonomous.profiles.DrivetrainProfile;
import org.cdm.team6072.autonomous.profiles.PIDConfig;
import util.CrashTracker;

public class Elevator extends Subsystem {

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

    private static Elevator mInstance;
    public static Elevator getInstance() {
        if (mInstance == null) {
            mInstance = new Elevator();
        }
        return mInstance;
    }

    private Elevator() {
        CrashTracker.logMessage("Elevator Subsystem initializing");
        try {
            mElevatorTalon = new WPI_TalonSRX(RobotConfig.ELEVATOR_TALON);
            mElevatorTalon.getSensorCollection().setQuadraturePosition(0, 10);
            mElevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
            mElevatorTalon.setSensorPhase(true);
            mElevatorTalon.configNeutralDeadband(Constants.kNeutralDeadband, Constants.kTimeoutMs);
            //mElevatorTalon.set(ControlMode.MotionProfile, 1);

           //mElevatorTalon.set(ControlMode.Current, ControlMode.Current.value);
            /*mElevatorTalon.set(ControlMode.MotionProfile, ControlMode.MotionProfile.value);
            mElevatorTalon.configOpenloopRamp(2, 0);*/
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
    public void setMPProfile(MotionProfile profile) {
        System.out.println("Elevator.setMPProfile:  setting up ");
        System.out.println("device (encoder): " + this.mElevatorTalon.getSensorCollection().toString());
        mMPController = new MotionProfileController("ElevatorMP", mElevatorTalon, profile);
        mPIDConfig = profile.getPIDConfig();

        //this.masters.get(i).setControlFramePeriod(10, Constants.kTimeoutMs);
        mElevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
    }

    /**
     * Start the motion profile running
     */
    public void startMotionProfile() {
        mMPController.startMotionProfile();
    }

    public boolean isProfileComplete() {
        return mMPController.isComplete();
    }

    public void runProfile() {
        mMPController.control();
    }


    private void updateTalonRequiredMPState() {
        SetValueMotionProfile setOutput = this.mMPController.getRequiredTalonMPState();

        //System.out.println("Elevator.updateTalonRequiredMPState: elevator val: " + setOutput.value);
        mElevatorTalon.set(ControlMode.MotionProfile, setOutput.value);
    }






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
