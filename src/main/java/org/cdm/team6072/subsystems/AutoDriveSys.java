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
        new Waypoint(-28, 0, 0)
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

        System.out.println("AutoDriveSys.initSys: left seg:" + left.segments[0].x + "  " + left.segments[0].y + "  " + left.segments[0].position);
        this.leftFollower = new EncoderFollower(left);
        this.leftFollower.configureEncoder(mLeft_Master.getSelectedSensorPosition(0), 1024, wheelWidthMeters);
        this.leftFollower.configurePIDVA(1.0, 0.0, 0.0, 1/1.7, 0);

        Trajectory right = modifier.getRightTrajectory();
        System.out.println("AutoDriveSys.initSys: right seg:" + right.segments[0].x + "  " + right.segments[0].y + "  " + right.segments[0].position);
        this.rightFollower = new EncoderFollower(right);
        this.rightFollower.configureEncoder(mRight_Master.getSelectedSensorPosition(0), 1024, wheelWidthMeters);
        this.rightFollower.configurePIDVA(1.0, 0.0, 0.0, 1/1.17, 0);
    }

    private Trajectory getTrajectory() {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60);
        Trajectory trajectory = Pathfinder.generate(this.straightLine, config);
        return trajectory;
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

    public void advanceTrajectory() {
       // boolean keepAlive = true;
       // while (keepAlive) {
        int leftSensPosn = mLeft_Master.getSelectedSensorPosition(0);
        int rightSensPosn = mRight_Master.getSelectedSensorPosition(0);
        double leftOutput = leftFollower.calculate(leftSensPosn);
        double rightOutput = rightFollower.calculate(rightSensPosn);

        double gyro_heading = NavSys.getInstance().getHeading();
        double desired_heading = Pathfinder.r2d(leftFollower.getHeading());

        //System.out.println("AutoDriveSys.advTraj: leftSensPosn: " + leftSensPosn + "  rightSensPosn: " + rightSensPosn + "  output -> " + leftOutput + ", right output -> " + rightOutput + "  heading -> " + gyro_heading + ", desired -> " + desired_heading);
       //System.out.println("Encoder check; " + mLeft_Mas)

        double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
        double turn = 0.8 * (-1.0/80.0) * angleDifference;

//            mLeft_Master.set(leftOutput);
//            mRight_Master.set(rightOutput);
        mRoboDrive.tankDrive(leftOutput, rightOutput);
       // }
    }

}

