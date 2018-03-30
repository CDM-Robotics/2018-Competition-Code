package org.cdm.team6072;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.cdm.team6072.autonomous.GameChooser;
import org.cdm.team6072.autonomous.routines.GoToScale;
import org.cdm.team6072.autonomous.routines.GoToSwitch;
import org.cdm.team6072.autonomous.routines.tests.TestSwitchRoutine;
import org.cdm.team6072.commands.drive.*;
import org.cdm.team6072.commands.auton.*;
import org.cdm.team6072.subsystems.*;
import util.ControlledLogger;


/**
 * Use TimedRobot as it has better control over the loop timing
 */
public class Robot extends TimedRobot {


    private DriveSys mDriveSys;
    private ElevatorSys mElevatorSys;
    private IntakePneumaticsSys mPneuSys;
    private IntakeMotorSys mIntakeMotorSys;
    private PowerDistributionPanel mPDP;
    private ArmSys mArmSys;

    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();
    private UsbCamera cam;


    /**
     * Initialize all the subsystems in Robot init
     */
    @Override
    public void robotInit() {
        System.out.println("6072: robotInit");
//        CameraManager.getInstance().runCameras();
//        CameraManager.getInstance().runFilter();
        mDriveSys = DriveSys.getInstance();
        mElevatorSys = ElevatorSys.getInstance();
        mIntakeMotorSys = IntakeMotorSys.getInstance();
        mArmSys = ArmSys.getInstance();

        mDriveSys.setSensorStartPosn();
        mElevatorSys.setSensorStartPosn();
        mArmSys.setSensorStartPosn();

        //mPDP = new PowerDistributionPanel(RobotConfig.PDP_ID);

        // must initialize nav system here for the navX-MXP
        NavXSys.getInstance();

    }

    @Override
    public void disabledInit() {
        // ensure the talons are not in MotionMagic or hold modes
        mElevatorSys.resetTalon();
        mArmSys.resetTalon();

        // control debug printing
        System.out.println("6072: disable init -------------------------------------------------");
    }


    public void disabledPeriodic() {

        new ControlledLogger().print(100, new Runnable() {
            @Override
            public void run() {
                mArmSys.printPosn("Rob.dis");
            }
        });

    }


    //  TELEOP MODE  ---------------------------------------------------------------


    private int mTelopLoopCtr = 0;

    @Override
    public void teleopInit() {
        System.out.println("6072: teleop init -------------------------------------------------");
        mTelopLoopCtr = 0;

        NavXSys.getInstance().zeroYawHeading(); // TESTING PURPOSES

        ArcadeDriveCmd  mArcadeDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(mArcadeDriveCmd);

       // CameraManager.getInstance().runCameras();
    }



    /**
     * teleopPeriodic is called about every 20mSec
     * We call the scheduler to cause all commands that have been scheduled to run
     * A command is typically placed on the scheduler in response to operator input
     *  e.g. a button press
     */
    @Override
    public void teleopPeriodic() {
        // must call the scheduler to run
        Scheduler.getInstance().run();

        // reset the disabled loop print count to allow a few prints when disabled
        // update PDP stats every half second
        new ControlledLogger().print(200, new Runnable() {
            @Override
            public void run() {
                NavXSys.getInstance().outputAngles();
            }
        });
    }


    //  AUTONOMOUS MODE  ---------------------------------------------------------------


    private PathFinderDriveSys mPathFinderDriveSys;


    @Override
    public void autonomousInit() {
        super.autonomousInit();

        DriverStation ds = DriverStation.getInstance();
        CameraManager.getInstance().runCameras();

        System.out.println("auto init (6072)  ------------------------------------------------------------");
        NavXSys.getInstance().zeroYawHeading();

        GameChooser autoChooser = new GameChooser();
        autoChooser.runChooser();
    }


    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }


    //  TEST MODE  ---------------------------------------------------------------

    private int mCounter;

    private TestDriveForward mTestFwdCmd;
    private TestDriveGyro mTestDriveGyro;


    @Override
    public void testInit() {
        System.out.println("testInit: --------------------");
    }

    @Override public void testPeriodic() {
        Scheduler.getInstance().run();

    }


    // UTILS  ----------------------------------------------------------------------------------------


    private void Logging() {
        Path logFile;

        try {

            double elvCurrent = 0; //mPDP.getCurrent(RobotConfig.ELEVATOR_TALON_PDP);
            double armCurrent = 0; //mPDP.getCurrent(RobotConfig.ARM_TALON_PDP);
            double driveLeftCurrent = 0; //mPDP.getCurrent(RobotConfig.DRIVE_LEFT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_LEFT_SLAVE0_PDP);
            double driveRightCurrent = 0; //mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_SLAVE0_PDP);

            SmartDashboard.putNumber("PDP.ElevCurrent", elvCurrent);
            SmartDashboard.putNumber("PDP.ArmElevCurrent", armCurrent);
            SmartDashboard.putNumber("PDP.DriveLeftCurrent", driveLeftCurrent);
            SmartDashboard.putNumber("PDP.DriveRightCurrent", driveRightCurrent);

            String logmsg = String.format("lCur: %.3f, rCur: %.3f, elvCur: %.3f, armCur: %.3f", driveLeftCurrent, driveRightCurrent, elvCurrent, armCurrent);

        }
        catch (Exception ex) {
            System.out.println( "*******  Logging ex: "+ ex.getClass().getName() + "   msg: " + ex.getMessage() + " ");
        }
    }



}