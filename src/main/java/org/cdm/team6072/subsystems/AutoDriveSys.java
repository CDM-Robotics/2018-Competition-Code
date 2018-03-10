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
    private Waypoint[] points = new Waypoint[] {
      new Waypoint(-4, -1, Pathfinder.d2r(-45)),
      new Waypoint(-2 ,-2, 0),
      new Waypoint(0,0,0)
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
        }
        catch (Exception ex) {
            System.out.println("Exception in AutoDriveSys ctor: " + ex.getMessage() + "\r\n" + ex.getStackTrace());
        }
    }

    private void initializeSystem() {
        double wheelWidthMeters = 0.5;

        TankModifier modifier = new TankModifier(this.getTrajectory());
        modifier.modify(wheelWidthMeters);

        Trajectory left = modifier.getLeftTrajectory();
        this.leftFollower = new EncoderFollower(left);
        this.leftFollower.configureEncoder(this.leftEncoderPos, 4096, wheelWidthMeters);
        this.leftFollower.configurePIDVA(1.0, 0.0, 0.0, 1/3, 0);

        Trajectory right = modifier.getRightTrajectory();
        this.rightFollower = new EncoderFollower(right);
        this.rightFollower.configureEncoder(this.rightEncodePos, 4096, wheelWidthMeters);
        this.rightFollower.configurePIDVA(1.0, 0.0, 0.0, 1/3, 0);
    }

    private Trajectory getTrajectory() {
        Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60);
        Trajectory trajectory = Pathfinder.generate(this.points, config);
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
        System.out.println("DriveSys: init default command");
        setDefaultCommand(new Command() {
            @Override
            protected void execute() {
                advanceTrajectory();
            }

            @Override
            protected boolean isFinished() {
                return false;
            }
        });
//        setDefaultCommand(new TankDriveCmd(ControlBoard.getInstance().drive_stick));
    }

    public void advanceTrajectory() {
       // boolean keepAlive = true;
       // while (keepAlive) {
            double leftOutput = leftFollower.calculate(this.leftEncoderPos);
            double rightOutput = rightFollower.calculate(this.rightEncodePos);

            System.out.println("AutoDriveSys.startControlLoop: left output -> " + leftOutput + ", right output -> " + rightOutput);

            double gyro_heading = 0.0;
            double desired_heading = Pathfinder.r2d(leftFollower.getHeading());

            double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
            double turn = 0.8 * (-1.0/80.0) * angleDifference;

            mLeft_Master.set(leftOutput);
            mRight_Master.set(rightOutput);
       // }
    }

}

