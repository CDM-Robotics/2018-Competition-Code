package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.commands.drive.ArcadeDriveCmd;
import jaci.pathfinder.followers.EncoderFollower;

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

    private Waypoint[] straightLine = new Waypoint[] {
        new Waypoint(0, 0, 0),
        new Waypoint(2, 0, 0)
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
            mLeft_Master.configOpenloopRamp(0.7 , 0);

            mLeft_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_LEFT_SLAVE0);
            mLeft_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_LEFT_MASTER);
            mLeft_Slave0.setInverted(false);

            mRight_Master = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Master.configOpenloopRamp(0.7, 0);

            mRight_Slave0 = new WPI_TalonSRX(RobotConfig.DRIVE_RIGHT_SLAVE0);
            mRight_Slave0.set(ControlMode.Follower, RobotConfig.DRIVE_RIGHT_MASTER);
            mRight_Slave0.setInverted(false);

            mRoboDrive = new DifferentialDrive(mLeft_Master, mRight_Master);

            mMasterTalons.add(mRight_Master);
            mMasterTalons.add(mLeft_Master);

            initializeSystem();
        }
        catch (Exception ex) {
            System.out.println("Exception in AutoDriveSys ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }
    }


    // 26 inches converted to meters
    private static double WHEELWIDTH = 26 * 2.54 / 100;


    private void initializeSystem() {
        double wheelWidthMeters = WHEELWIDTH;

        Trajectory t = this.getTrajectory();
        System.out.println("AutoDriveSys.initializeSystem trajectory " + t.toString());
        TankModifier modifier = new TankModifier(t);
        modifier.modify(wheelWidthMeters);

        Trajectory left = modifier.getLeftTrajectory();

        for (int i = 0; i < left.segments.length; i++) {
            System.out.println("AutoDriveSys.initSys: left seg:" + left.segments[i].x + "  " + left.segments[i].y + "  " + left.segments[i].position + "  " + left.segments[i].heading);
        }
        this.leftFollower = new EncoderFollower(left);
        this.leftFollower.configureEncoder(getLeftSens(), 1024, wheelWidthMeters);
        this.leftFollower.configurePIDVA(1.0, 0.0, 0.0, 1/1.7, 0);

        Trajectory right = modifier.getRightTrajectory();
        for (int i = 0; i < right.segments.length; i++) {
            System.out.println("AutoDriveSys.initSys: right seg:" + right.segments[i].x + "  " + right.segments[i].y + "  " + right.segments[i].position+ "  " + right.segments[i].heading);
        }
        this.rightFollower = new EncoderFollower(right);
        this.rightFollower.configureEncoder(getRightSens(), 1024, wheelWidthMeters);
        this.rightFollower.configurePIDVA(1.0, 0.0, 0.0, 1/1.17, 0);
    }

    private Trajectory getTrajectory() {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60);
        Trajectory trajectory = Pathfinder.generate(this.straightLine, config);
        return trajectory;
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
        /*setDefaultCommand(new Command() {

            public Command() {
                requires(AutoDriveSys.getInstance());
            }

            @Override
            protected void execute() {
                advanceTrajectory();
            }

            @Override
            protected boolean isFinished() {
                return false;
            }
        });
//        setDefaultCommand(new TankDriveCmd(ControlBoard.getInstance().drive_stick));*/
    }

    private int getLeftSens() {
        return mLeft_Master.getSelectedSensorPosition(0);
    }

    /**
     * left and right motors spin in opposite directions so reverse one sensor
     * @return
     */
    private int getRightSens() {
        return -mRight_Master.getSelectedSensorPosition(0);
    }

    public void advanceTrajectory() {
       // boolean keepAlive = true;
       // while (keepAlive) {
        int leftSensPosn = getLeftSens();
        int rightSensPosn = getRightSens();
        double leftOutput = leftFollower.calculate(leftSensPosn);
        double rightOutput = rightFollower.calculate(rightSensPosn);

        double gyro_heading = NavSys.getInstance().getHeading();
        double desired_heading = Pathfinder.r2d(leftFollower.getHeading());

        System.out.println("AutoDrvSys.advTraj: leftSens: " + leftSensPosn + "  rightSens: " + rightSensPosn + "  lout: " + leftOutput + "  rout: " + rightOutput + "  hdg: " + gyro_heading + " desired: " + desired_heading);
       //System.out.println("Encoder check; " + mLeft_Mas)

        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;

//            mLeft_Master.set(leftOutput);
//            mRight_Master.set(rightOutput);
        mRoboDrive.tankDrive(-leftOutput, -rightOutput);
       // }
    }

}

