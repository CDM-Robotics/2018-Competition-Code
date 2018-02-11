/**
 * Example logic for firing and managing motion profiles.
 * This example sends MPs, waits for them to finish
 * Although this code uses a CANTalon, nowhere in this module do we changeMode() or call set() to change the output.
 * This is done in Robot.java to demonstrate how to change control modes on the fly.
 *
 * The only routines we call on Talon are....
 *
 * changeMotionControlFramePeriod
 *
 * getMotionProfileStatus
 * clearMotionProfileHasUnderrun     to get status and potentially clear the error flag.
 *
 * pushMotionProfileTrajectory
 * clearMotionProfileTrajectories
 * processMotionProfileBuffer,   to push/clear, and process the trajectory points.
 *
 * getControlMode, to check if we are in Motion Profile Control mode.
 *
 * Example of advanced features not demonstrated here...
 * [1] Calling pushMotionProfileTrajectory() continuously while the Talon executes the motion profile, thereby keeping it going indefinitely.
 * [2] Instead of setting the sensor position to zero at the start of each MP, the program could offset the MP's position based on current position.
 */
package org.cdm.team6072.profiles;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.Notifier;
import com.ctre.phoenix.motion.*;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import util.CrashTracker;

public class MotionProfileController {

    /**
     * The status of the motion profile executer and buffer inside the Talon.
     * Instead of creating a new one every time we call getMotionProfileStatus
     */
    private MotionProfileStatus mMPStatus = new MotionProfileStatus();


    /** additional cache for holding the active trajectory point */
    private double mATP_Pos =0;
    private double mATP_Vel =0;
    private double mATP_Heading =0;

    /**
     * reference to the talon we plan on manipulating. We will not changeMode()
     * or call set(), just get motion profile status and make decisions based on
     * motion profile.
     */
    private TalonSRX mTalon;



    private static int DISABLELOOP = -1;

    private enum ControlState {
        STOPPED,
        STARTING,
        RUNNING
    }


    /**
     * State machine to make sure we let enough of the motion profile stream to
     * talon before we fire it.
     */
    private ControlState mControlState = ControlState.STOPPED;


    /**
     * Any time you have a state machine that waits for external events, its a
     * good idea to add a timeout. Set to -1 to disable. Set to nonzero to count
     * down to '0' which will print an error message. Counting loops is not a
     * very accurate method of tracking timeout, but this is just conservative
     * timeout. Getting time-stamps would certainly work too, this is just
     * simple (no need to worry about timer overflows).
     */
    private int _loopTimeout = -1;
    /**
     * Just a state timeout to make sure we don't get stuck anywhere. Each loop
     * is about 20ms.
     */
    private static final int kNumLoopsTimeout = 10;


    /**
     * If start() gets called, this flag is set and in the control() we will
     * service it.
     */
    private boolean mStartRequested = false;

    /**
     * Since the CANTalon.set() routine is mode specific, deduce what we want
     * the set value to be and let the calling module apply it whenever we
     * decide to switch to MP mode.
     */
    private SetValueMotionProfile _requiredTalonMPState = SetValueMotionProfile.Disable;

    /**
     * How many trajectory points do we wait for before firing the motion profile.
     */
    private static final int kMinPointsInTalon = 5;


    /**
     * Profile of points we want to push to Talon
     */
    private IMotionProfile mProfile;

    /**
     * PIConfig loaded from profile passed in
     */
    private PIDConfig mPIDConfig;

    /**
     * Name of this instance for display on ShuffleBoard
     */
    private String mInstanceName;

    /**
     * Define the set of keys we are going to display in ShuffleBoard
     * These should not be static - they need to be instance variables
     */
    private static enum Keys {
        HoldState,
        TopBufferCount,
        BottomBufferCount,
        ActivePointValid,
        IsLast,
        Velocity,
        Position,
        StartPosition,
        Heading,
        EncoderVal,
        PID_kF,
        PID_kP,
        PID_kI,
        PID_kD,
    };



    public static enum MPDirection {
        Positive,
        Negative,
    }

    private MPDirection mDirn = MPDirection.Positive;

    private double mStartPosn = -1;


    /**
     * @param controllerName - define the display name for this instance
     * @param talon  -  reference to Talon object to control.
     * @param motionProfile - the motion profile that we want to run on the talon
     * @param dirn - direction the device is to move in
     */
    public MotionProfileController(String controllerName, TalonSRX talon, IMotionProfile motionProfile, MPDirection dirn) {
        mTalon = talon;
        mProfile = motionProfile;
        mDirn = dirn;
        mStartPosn = mTalon.getSelectedSensorPosition(0);
        mInstanceName = controllerName + "_";

        // initialize the SmatDashboard display for this MP
        SmartDashboard.putString(mInstanceName + Keys.HoldState, "NOT SET");
        SmartDashboard.putNumber(mInstanceName + Keys.TopBufferCount, -1);
        SmartDashboard.putNumber(mInstanceName + Keys.BottomBufferCount, -1);
        SmartDashboard.putBoolean(mInstanceName + Keys.ActivePointValid, false);
        SmartDashboard.putBoolean(mInstanceName + Keys.IsLast, false);
        SmartDashboard.putNumber(mInstanceName + Keys.Velocity, -1);
        SmartDashboard.putNumber(mInstanceName + Keys.Position, -1);
        SmartDashboard.putNumber(mInstanceName + Keys.StartPosition, mStartPosn);
        SmartDashboard.putNumber(mInstanceName + Keys.Heading, -1);
        SmartDashboard.putNumber(mInstanceName + Keys.EncoderVal, -1);

        // configure the PID params from the profile
        if (mProfile.getPIDConfig() != null) {
            mPIDConfig = mProfile.getPIDConfig();
            mTalon.selectProfileSlot(mPIDConfig.SlotId, 0);
            mTalon.config_kF(mPIDConfig.SlotId, mPIDConfig.kF, Constants.kTimeoutMs);
            mTalon.config_kP(mPIDConfig.SlotId, mPIDConfig.kP, Constants.kTimeoutMs);
            mTalon.config_kI(mPIDConfig.SlotId, mPIDConfig.kI, Constants.kTimeoutMs);
            mTalon.config_kD(mPIDConfig.SlotId, mPIDConfig.kD, Constants.kTimeoutMs);
            SmartDashboard.putNumber(mInstanceName + Keys.PID_kF, mPIDConfig.kF);
            SmartDashboard.putNumber(mInstanceName + Keys.PID_kP, mPIDConfig.kP);
            SmartDashboard.putNumber(mInstanceName + Keys.PID_kI, mPIDConfig.kI);
            SmartDashboard.putNumber(mInstanceName + Keys.PID_kD, mPIDConfig.kD);
        }
		// set the control frame rate and the notifer to half the base TP duration in profile
        mTalon.changeMotionControlFramePeriod(mProfile.getBaseTPDurationMs() / 2);
        mTalonPushPointsToBottomBuffer.startPeriodic(mProfile.getBaseTPDurationMs() / 2 / 1000);
    }


    /**
     * Called by application to signal Talon to start the buffered MP (when it's able to).
     */
    public void startMotionProfile() {
        CrashTracker.logMessage("MotionProfileController.startMotionProfile");
        mStartRequested = true;
        mIsComplete = false;
    }


    private boolean mIsComplete = false;

    public boolean isComplete() {
        if (mIsComplete) {
            CrashTracker.logMessage("MotionProfileController.isComplete TRUE  ----------------------------------------");
        }
        return mIsComplete;
    }


    /**
     * Called by application to request change in Talon state
     * @return the output value to pass to Talon's set() routine. 0 for disable
     *         motion-profile output, 1 for enable motion-profile, 2 for hold
     *         current motion profile trajectory point.
     */
//    public SetValueMotionProfile getRequiredTalonMPState() {
//        return _requiredTalonMPState;
//    }


    /**
     * Called to clear Motion profile buffer and reset state info during
     * disabled and when Talon is not in MP control mode.
     */
    public void reset() {
		/*
		 * Let's clear the buffer just in case user decided to disable in the
		 * middle of an MP, and now we have the second half of a profile just
		 * sitting in memory.
		 */
        mTalon.clearMotionProfileTrajectories();
		/* When we do re-enter motionProfile control mode, stay disabled. */
        //_requiredTalonMPState = SetValueMotionProfile.Disable;
        mTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
		/* When we do start running our state machine start at the beginning. */
        mControlState = ControlState.STOPPED;
        _loopTimeout = DISABLELOOP;
		/*
		 * If application wanted to start an MP before, ignore and wait for next button press
		 */
        mStartRequested = false;
        mIsComplete = false;
    }


    // -------------------------  moving Points to Talon  -------------------------------------------


    /**
     * Lets create a periodic task to funnel our trajectory points into our talon.
     * It doesn't need to be very accurate, just needs to keep pace with the motion
     * profiler executer.  Now if you're trajectory points are slow, there is no need
     * to do this, just call mTalon.processMotionProfileBuffer() in your teleop loop.
     * Generally speaking you want to call it at least twice as fast as the duration
     * of your trajectory points.  So if they are firing every 20ms, you should call
     * every 10ms.
     */
    class PeriodicRunnable implements java.lang.Runnable {
        public void run() {  mTalon.processMotionProfileBuffer();    }
    }
    Notifier mTalonPushPointsToBottomBuffer = new Notifier(new PeriodicRunnable());


    /**
     * Find enum value if supported.
     * @param durationMs
     * @return enum equivalent of durationMs
     */
    private TrajectoryDuration GetTrajectoryDuration(int durationMs) {
		/* create return value */
        TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
		/* convert duration to supported type */
        retval = retval.valueOf(durationMs);
		/* check that it is valid */
        if (retval.value != durationMs) {
            CrashTracker.logMessage("Trajectory Duration not supported - use configMotionProfileTrajectoryPeriod instead");
        }
		/* pass to caller */
        return retval;
    }


    /** Start filling the MPs to all of the involved Talons. */
    private void startFilling() {
		/* since this example only has one talon, just update that one */
        startFilling(mProfile.getPoints(), mProfile.getPoints().length);
    }


    /**
     * Profile is array
     *      position   velocity     duration mSec
     * @param profile
     * @param totalCnt
     */
    private void startFilling(double[][] profile, int totalCnt) {

		/* did we get an underrun condition since last time we checked ? */
        if (mMPStatus.hasUnderrun) {
			/* better log it so we know about it */
            //Instrumentation.OnUnderrun();
			/*
			 * clear the error. This flag does not auto clear, this way we never miss logging it.
			 */
            System.out.println(mInstanceName + ": has underrrun");
            mTalon.clearMotionProfileHasUnderrun(0);
        }
		/*
		 * just in case we are interrupting another MP and there is still buffer points in memory, clear it.
		 */
        mTalon.clearMotionProfileTrajectories();

		/* set the base trajectory period to zero, use the individual trajectory period below */
        //mTalon.configMotionProfileTrajectoryPeriod(Constants.kBaseTrajPeriodMs, Constants.kTimeoutMs);

        TrajectoryPoint point = new TrajectoryPoint();
        for (int i = 0; i < totalCnt; ++i) {
            double positionRot = profile[i][0];
            double velocityRPM = profile[i][1];

            if (mDirn == MPDirection.Positive) {
                positionRot = mStartPosn + positionRot;
            }
            else {
                positionRot = mStartPosn - positionRot;
            }

			/* for each point, fill our structure and pass it to API */
            point.position = positionRot * mProfile.getUnitsPerRotation(); //Convert Revolutions to Units
            point.velocity = velocityRPM * mProfile.getUnitsPerRotation() / 600.0; //Convert RPM to Units/100ms
            point.headingDeg = 0; /* future feature - not used in this example*/
            point.profileSlotSelect0 = mProfile.getPIDConfig().SlotId; /* which set of gains would you like to use [0,3]? */
            point.profileSlotSelect1 = mProfile.getPIDConfig().SlotId; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */
            point.timeDur = GetTrajectoryDuration((int)profile[i][2]);
            point.zeroPos = false;
            point.zeroPos = (i == 0); /* set this to true on the first point */
            point.isLastPoint = ((i + 1) >= totalCnt); /* set this to true on the last point  */

            if ((i % 100 == 0) || point.isLastPoint) {
                System.out.println(mInstanceName + "MPE:  push point i: " + i + "  pos: " + point.position + " vel:" + point.velocity + "  ISLAST: " + point.isLastPoint);
            }
            CrashTracker.logMessage("MotionProfileController.startFilling: (Buffer) pos rot: " + positionRot + ", velocity RPM: " + velocityRPM);
            mTalon.pushMotionProfileTrajectory(point);
        }
    }


    // -------------------------------------------------------




    /**
     * Called every loop.
     */
    public void control() {

		/* Get the motion profile status every loop */
        mTalon.getMotionProfileStatus(mMPStatus);
        SmartDashboard.putNumber(mInstanceName + Keys.TopBufferCount, mMPStatus.topBufferCnt);
        SmartDashboard.putNumber(mInstanceName + Keys.BottomBufferCount, mMPStatus.btmBufferCnt);
        SmartDashboard.putBoolean(mInstanceName + Keys.ActivePointValid, mMPStatus.activePointValid);
        SmartDashboard.putBoolean(mInstanceName + Keys.IsLast, mMPStatus.isLast);
        if (mMPStatus.isLast == true) {
            System.out.println("******************************************************************* isLast ***************");
            SmartDashboard.putBoolean("isLast found", true);
        }
		/*
		 * track time, this is rudimentary but that's okay, we just want to make
		 * sure things never get stuck.
		 */
        if (_loopTimeout != DISABLELOOP) {
			/* timeout active */
            if (_loopTimeout == 0) {
                //  something is wrong. Talon is not present, unplugged, breaker tripped
                //Instrumentation.OnNoProgress();
            } else {
                --_loopTimeout;
            }
        }

		/* first check if we are in MotionProfileBase mode */
        if (mTalon.getControlMode() != ControlMode.MotionProfile) {
			/*
			 * we are not in MP mode. We are probably driving the robot around
			 * using gamepads or some other mode.
			 */
            mControlState = ControlState.STOPPED;
            _loopTimeout = DISABLELOOP;
        } else {
			/*
			 * we are in MP control mode. That means: starting Mps, checking Mp
			 * progress, and possibly interrupting MPs if thats what you want to do.
			 */
            switch (mControlState) {
                case STOPPED: /* wait for application to tell us to start an MP */
                    if (mStartRequested) {
                        mStartRequested = false;

                        //_requiredTalonMPState = SetValueMotionProfile.Disable;
                        mTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
                        startFilling();
						/*
						 * MP is being sent to CAN bus, wait a small amount of time
						 */
                        mControlState = ControlState.STARTING;
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case STARTING:
                    /* wait for MP to stream to Talon, really just the first few points */
					/* do we have a minimum number of points in Talon */
                    if (mMPStatus.btmBufferCnt > kMinPointsInTalon) {
						/* start (once) the motion profile */
                        //_requiredTalonMPState = SetValueMotionProfile.Enable;
                        mTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
						/* MP will start once the control frame gets scheduled */
                        mControlState = ControlState.RUNNING;
                        _loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case RUNNING:
					/*
					 * if talon is reporting things are good, keep adding to our
					 * timeout. Really this is so that you can unplug your talon in
					 * the middle of an MP and react to it.
					 */
//					System.out.println("MotionProfileController.control: state RUNNING: mMPStatus.isUndern: " + mMPStatus.isUnderrun
//                                + "  mMPStatus,active: " + mMPStatus.activePointValid
//                                + "  mMPStatus.isLast: " + mMPStatus.isLast);
                    if (mMPStatus.isUnderrun == false) {
                        _loopTimeout = kNumLoopsTimeout;
                    }

					/*
					 * If we are executing an MP and the MP finished, start loading
					 * another. We will go into hold state so robot servo's position.
					 */
                    if ((mMPStatus.isLast) && mMPStatus.activePointValid) { //mMPStatus.activePointValid && mMPStatus.isLast) {
						/*
						 * because we set the last point's isLast to true, we will get here when the MP is done
						 */
                        //_requiredTalonMPState = SetValueMotionProfile.Disable;
                        mTalon.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
                        mControlState = ControlState.STOPPED;
                        _loopTimeout = DISABLELOOP;
                        mIsComplete = true;
                    }
                    break;
            }

            mATP_Heading = mTalon.getActiveTrajectoryHeading();
            mATP_Pos  = mTalon.getActiveTrajectoryPosition();
            mATP_Vel = mTalon.getActiveTrajectoryVelocity();

            //System.out.println("velocity: " + mATP_Vel + "  pos: " + _pos + " heading: " + mATP_Heading);
            SmartDashboard.putNumber(mInstanceName + Keys.Velocity, mATP_Vel);
            SmartDashboard.putNumber(mInstanceName + Keys.Position, mATP_Pos );
            SmartDashboard.putNumber(mInstanceName + Keys.Heading, mATP_Heading);
            SmartDashboard.putNumber(mInstanceName + Keys.EncoderVal, mTalon.getSelectedSensorPosition(0));
            //SmartDashboard.putString("Motion Profile Underrun" + mMPStatus.hasUnderrun);

			/* printfs and/or logging */
            //Instrumentation.process(mMPStatus, _pos, mATP_Vel, mATP_Heading);
        }
    }





}