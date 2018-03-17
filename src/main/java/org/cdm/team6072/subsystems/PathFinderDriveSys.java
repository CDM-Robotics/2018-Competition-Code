package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
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

import java.util.ArrayList;

public class PathFinderDriveSys extends Subsystem {

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





    private static PathFinderDriveSys mInstance;
    public static PathFinderDriveSys getInstance() {
        if (mInstance == null) {
            mInstance = new PathFinderDriveSys();
        }
        return mInstance;
    }

    private PathFinderDriveSys() {
        System.out.println("6072: DriveSys constructor");

        try {
            mLeft_Master = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Master.configOpenloopRamp(0 , 0);
            mLeft_Master.setNeutralMode(NeutralMode.Brake);
            mLeft_Master.setSensorPhase(true);
            mLeft_Master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);

            mLeft_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_SLAVE0);
            mLeft_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Slave0.setInverted(false);

            mRight_Master = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Master.configOpenloopRamp(0, 0);
            mRight_Master.setNeutralMode(NeutralMode.Brake);
            mRight_Master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);

            mRight_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_SLAVE0);
            mRight_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Slave0.setInverted(false);

            mRoboDrive = new DifferentialDrive(mLeft_Master, mRight_Master);

            mMasterTalons.add(mRight_Master);
            mMasterTalons.add(mLeft_Master);

        }
        catch (Exception ex) {
            System.out.println("Exception in PathFinderDriveSys ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }
    }


    // 26 inches converted to meters
    public static double TRACKWIDTH = 26 * 2.54 / 100;

    public static double WHEELDIAM = 6 * 2.54 / 100;

    // measured max speed was about 7.8 feet/sec
    public static double MAX_VELOCITY = 7.8 * 12 * 2.54 /100 ;           // meters per sec

    public static double MAX_ACCEL = 4.0;              // meters per sec per sec

    public static int TICKS_PER_REV = 4096;


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
        this.leftFollower.configureEncoder(getLeftSens(), TICKS_PER_REV, WHEELDIAM);
        this.leftFollower.configurePIDVA(1.0, 0.0, 0.0, 1/MAX_VELOCITY, 0);

        Trajectory right = modifier.getRightTrajectory();
        this.rightFollower = new EncoderFollower(right);
        this.rightFollower.configureEncoder(getRightSens(), TICKS_PER_REV, WHEELDIAM);
        this.rightFollower.configurePIDVA(1.0, 0.0, 0.0, 1/MAX_VELOCITY, 0);

        for (int i = 0; i < right.segments.length; i++) {
            String lmsg = String.format("\"Auto.setTraj: left x:%.3f  y:%.3f  v:%.3f  h:%.3f", left.segments[i].x, left.segments[i].y, left.segments[i].velocity, left.segments[i].heading);
            String rmsg = String.format("\"Auto.setTraj: rght x:%.3f  y:%.3f  v:%.3f  h:%.3f", right.segments[i].x, right.segments[i].y, right.segments[i].velocity, right.segments[i].heading);
            System.out.println(lmsg);
            System.out.println(rmsg);
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
        System.out.println("PathFinderDriveSys: init default command");
    }

    private int getLeftSens() {
        //return mLeft_Master.getSelectedSensorPosition(0);
        return mLeft_Master.getSelectedSensorPosition(0);
    }

    /**
     * left and right motors spin in opposite directions so reverse one sensor
     * @return
     */
    private int getRightSens() {
        //return -mRight_Master.getSelectedSensorPosition(0);
        return mRight_Master.getSelectedSensorPosition(0);
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

        double gyro_heading = NavXSys.getInstance().getAngle();
        double desired_heading = Pathfinder.r2d(leftFollower.getHeading());

        // Bound an angle (in degrees) to -180 to 180 degrees.
        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = -1 * 0.01 * angleDifference;
        double modLeftOutput = follLeft - turn;
        double modRightOutput = follRight + turn;

        //System.out.println("AutoDrvSys.advTraj: leftSens: " + leftSensPosn + "  rightSens: " + rightSensPosn ); // + "  ltrag: " + follLeft + "  rtrag: " + follRight + "  gyro: " + gyro_heading + " desired: " + desired_heading + "  turn: " + turn);

        String msg = String.format("Auto.advTraj: l: %d  r: %d  gyro %.2f  des: %.2f  aDif: %.3f  turn: %.3f  follLeft: %.3f  follRight: %.3f  calcLeft: %.3f  calcRight: %.3f",
                leftSensPosn, rightSensPosn,   gyro_heading, desired_heading, angleDifference, turn,  follLeft, follRight, modLeftOutput, modRightOutput);
        System.out.println(msg);

        //System.out.println("AutoDrvSys.advTraj: leftPercent: " + leftPercent + "  leftVolts: " + leftVolts + "  rightPercent: " + rightPercent + "  rightVolts: " + rightVolts);
        //System.out.println("Encoder check; " + mLeft_Mas)

        // for tank drive, -ve and -ve will go forward
        mRoboDrive.tankDrive(-modLeftOutput / 2, -modRightOutput / 2);
       // }
    }

    public boolean trajectoryComplete() {
        return (leftFollower.isFinished() && rightFollower.isFinished());
    }

}

