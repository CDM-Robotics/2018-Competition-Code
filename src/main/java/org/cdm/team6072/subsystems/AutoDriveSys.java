package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.cdm.team6072.RobotConfig;
import jaci.pathfinder.followers.EncoderFollower;
import java.io.File;

import java.util.ArrayList;

public class AutoDriveSys extends Subsystem {

    private WPI_TalonSRX mLeft_Master;
    private WPI_TalonSRX mLeft_Slave0;
    private WPI_TalonSRX mRight_Master;
    private WPI_TalonSRX mRight_Slave0;

    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;

    private int leftEncoderPos = 0;
    private int rightEncodePos = 0;

    ArrayList<TalonSRX> mMasterTalons = new ArrayList<TalonSRX>();

    private DifferentialDrive mRoboDrive;

    /**
     * https://www.chiefdelphi.com/forums/showthread.php?p=1691279
     *
     * The coordinate system is just standard x, y, and theta. Note that theta must be in radians.
     * The units of x and y don’t matter as long as you are consistent.
     * If you provide coordinates in feet, you must provide velocity and acceleration in ft/s and ft/s^2,
     * and your output will be in feet and ft/s.
     *
     * The way it currently works is like so:
     X+ is forward from where your robot starts.
     X- is backward from where your robot starts.
     Y+ is to the right of your robot where it starts.
     Y- is to the left of your robot where it starts.
     Angle (theta) is your desired robot heading in radians, which you can convert to/from degrees with the r2d and d2r functions provided by Pathfinder.

     Positive headings are going from X+ towards Y+,
     Negative Headings from X+ to Y-.
     As for the actual following of the heading, that depends on where your gyroscope is zero'd to.

     As with anything, these coordinates are useless unless you have the follower code to interpret them.
     X and Y coordinate directions can be flipped by just sending them to different motor outputs, for example

     You can orient the coordinate plane any way you want, as long as you are consistent.
     Since, as I mentioned above, angle is measured counterclockwise from the +x axis,
     a starting waypoint of (0,0,0) for (x,y,theta) will correspond to a robot at the origin facing in the +x direction.

     If you do not like this, and would rather have the robot face in the +y direction,
     all you need to do is start with a waypoint of (0,0,pi/2) instead.
     */

    // waypoint positions are in meters
    // angles in degrees but d2r method converts to radians
    private Waypoint[] points = new Waypoint[] {
      new Waypoint(-4, -1, Pathfinder.d2r(-45)),
      new Waypoint(-2 ,-2, 0),
      new Waypoint(0,0,0)
    };

    private Waypoint[] sinewave = new Waypoint[] {
            new Waypoint(2, 2, Pathfinder.d2r(0)),
            new Waypoint(4, -2, Pathfinder.d2r(0)),
            new Waypoint(6, 2, Pathfinder.d2r(0)),
            new Waypoint(8, -2, Pathfinder.d2r(0)),
    };




    private static AutoDriveSys mInstance;
    public static AutoDriveSys getInstance() {
        if (mInstance == null) {
            mInstance = new AutoDriveSys();
        }
        return mInstance;
    }

    private AutoDriveSys() {
        System.out.println("6072: DriveSys constructor");

        try {
            mLeft_Master = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Master.configOpenloopRamp(0 , 0);
            mLeft_Master.setNeutralMode(NeutralMode.Brake);

            mLeft_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_SLAVE0);
            mLeft_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Slave0.setInverted(false);

            mRight_Master = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Master.configOpenloopRamp(0, 0);
            mLeft_Master.setNeutralMode(NeutralMode.Brake);

            mRight_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_SLAVE0);
            mRight_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Slave0.setInverted(false);

            mRoboDrive = new DifferentialDrive(mLeft_Master, mRight_Master);

            mMasterTalons.add(mRight_Master);
            mMasterTalons.add(mLeft_Master);

        }
        catch (Exception ex) {
            System.out.println("Exception in AutoDriveSys ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }
    }


    // 26 inches converted to meters
    public static double TRACKWIDTH = 26 * 2.54 / 100;

    public static double WHEELDIAM = 6 * 2.54 / 100;

    public static double MAX_VELOCITY = 5.0;           // meters per sec

    public static double MAX_ACCEL = 4.0;              // meters per sec per sec




    /**
     * Configure the PID/VA Variables for the Follower
     *  kp The proportional term. This is usually quite high (0.8 - 1.0 are common values)
     *  ki The integral term. Currently unused.
     *  kd The derivative term. Adjust this if you are unhappy with the tracking of the follower. 0.0 is the default
     *  kv The velocity ratio. This should be 1 over your maximum velocity @ 100% throttle.
     *           This converts m/s given by the algorithm to a scale of -1..1 to be used by your
     *           motor controllers
     *  ka The acceleration term. Adjust this if you want to reach higher or lower speeds faster. 0.0 is the default
     *
     * Configure the Encoders being used in the follower.
     *  initial_position      The initial 'offset' of your encoder. This should be set to the encoder value just
     *                              before you start to track
     *  ticks_per_revolution  How many ticks per revolution the encoder has
     *  wheel_diameter        The diameter of your wheels (or pulleys for track systems) in meters
     *
     */
    public void setTrajectory(Trajectory traj) {

        NavXSys.getInstance().zeroYawHeading();

        TankModifier modifier = new TankModifier(traj);
        modifier.modify(TRACKWIDTH);

        Trajectory left = modifier.getLeftTrajectory();
        this.leftFollower = new EncoderFollower(left);
        this.leftFollower.configureEncoder(getLeftSens(), 1024, WHEELDIAM);
        this.leftFollower.configurePIDVA(1.0, 0.0, 0.0, 1/MAX_VELOCITY, 0);

        Trajectory right = modifier.getRightTrajectory();
        this.rightFollower = new EncoderFollower(right);
        this.rightFollower.configureEncoder(getRightSens(), 1024, WHEELDIAM);
        this.rightFollower.configurePIDVA(1.0, 0.0, 0.0, 1/MAX_VELOCITY, 0);

        for (int i = 0; i < right.segments.length; i++) {
            System.out.println("Auto.setTraj: left seg:" + left.segments[i].x + "  " + left.segments[i].y + "  " + left.segments[i].velocity + "  " + left.segments[i].heading);
            System.out.println("Auto.setTraj: right seg:" + right.segments[i].x + "  " + right.segments[i].y + "  " + right.segments[i].velocity + "  " + right.segments[i].heading);
        }
    }



    /**
     * Set lef tand right talons to the same sensor position
     */
    private void setSensorStartPosn() {
        mLeft_Master.getSensorCollection().setPulseWidthPosition(0, 10);
        int absolutePosition = mLeft_Master.getSensorCollection().getPulseWidthPosition();

        /* mask out overflows, keep bottom 12 bits */
        absolutePosition &= 0xFFF;
//        if (mSensorPhase)
//            absolutePosition *= -1;
//        if (mMotorInvert)
//            absolutePosition *= -1;
        /* set the quadrature (relative) sensor to match absolute */
        mLeft_Master.setSelectedSensorPosition(absolutePosition, 0, 10);
        mRight_Master.setSelectedSensorPosition(absolutePosition, 0, 10);
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
        System.out.println("AutoDriveSys: init default command");
    }

    private int getLeftSens() {
        //return mLeft_Master.getSelectedSensorPosition(0);
        return Math.floorMod(mLeft_Master.getSensorCollection().getPulseWidthPosition(), 1024);
    }

    /**
     * left and right motors spin in opposite directions so reverse one sensor
     * @return
     */
    private int getRightSens() {
        //return -mRight_Master.getSelectedSensorPosition(0);
        return -Math.floorMod(mRight_Master.getSensorCollection().getPulseWidthPosition(), 1024);
    }


    private int mLoopCtr = 0;


    /**
     * Output from the follower.calculate is:
            double distance_covered = ((double)(encoder_tick - encoder_offset) / encoder_tick_count) * wheel_circumference;
             Trajectory.Segment seg = trajectory.get(segment);
             double error = seg.position - distance_covered;
             double calculated_value =
                             kp * error +                                    // Proportional
                             kd * ((error - last_error) / seg.dt) +          // Derivative
                             (kv * seg.velocity + ka * seg.acceleration);    // V and A Terms

        Have to normalise the output to (-1 to 1) for the tankdrive
     */

    /**
     * this is called form the command execute() method periodically
     */
    public void advanceTrajectory() {
        if (leftFollower.isFinished()) {
            return;
        }
       // boolean keepAlive = true;
       // while (keepAlive) {
        int leftSensPosn = getLeftSens();
        int rightSensPosn = getRightSens();
        double leftPercent = mLeft_Master.getMotorOutputPercent();
        double leftVolts = mLeft_Master.getMotorOutputVoltage();
        double rightPercent = mRight_Master.getMotorOutputPercent();
        double rightVolts = mRight_Master.getMotorOutputVoltage();

        double follLeft = leftFollower.calculate(leftSensPosn);
        double follRight = rightFollower.calculate(rightSensPosn);

        double gyro_heading = NavXSys.getInstance().getYawHeading();
        double desired_heading = Pathfinder.r2d(leftFollower.getHeading());

        // Bound an angle (in degrees) to -180 to 180 degrees.
        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;
        double modLeftOutput = follLeft + turn;
        double modRightOutput = follRight - turn;

        System.out.println("AutoDrvSys.advTraj: leftSens: " + leftSensPosn + "  rightSens: " + rightSensPosn ); // + "  ltrag: " + follLeft + "  rtrag: " + follRight + "  gyro: " + gyro_heading + " desired: " + desired_heading + "  turn: " + turn);
        System.out.println("Auto.advTraj: gyro " + gyro_heading + " desired: " + desired_heading + " angDif: " + angleDifference + " turn: " + turn
                + " follL: " + follLeft + " calcL: " + modLeftOutput + " follR: " + follRight + " modR: " + modRightOutput);

        //System.out.println("AutoDrvSys.advTraj: leftPercent: " + leftPercent + "  leftVolts: " + leftVolts + "  rightPercent: " + rightPercent + "  rightVolts: " + rightVolts);
        //System.out.println("Encoder check; " + mLeft_Mas)

        mRoboDrive.tankDrive(-modLeftOutput, -modRightOutput);
       // }
    }

    public boolean trajectoryComplete() {
        return (leftFollower.isFinished() && rightFollower.isFinished());
    }

}

