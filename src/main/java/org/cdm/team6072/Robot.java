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

import org.cdm.team6072.commands.drive.*;
import org.cdm.team6072.commands.auton.*;
import org.cdm.team6072.subsystems.*;




/**
 * Use TimedRobot as it has better control over the loop timing
 */
public class Robot extends IterativeRobot {


    private DriveSys mDriveSys;
    private ElevatorSys mElevatorSys;
    private IntakePneumaticsSys mPneuSys;
    private IntakeMotorSys mIntakeMotorSys;
    private PowerDistributionPanel mPDP;

    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();

    private CommandGroup mAutonCmdGrp;
    private SendableChooser<Integer> mChooser = new SendableChooser<>();


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
        mDriveSys.setSensorStartPosn();
        mElevatorSys = ElevatorSys.getInstance();
        mIntakeMotorSys = IntakeMotorSys.getInstance();
        mPDP = new PowerDistributionPanel(RobotConfig.PDP_ID);

        // must initialize nav system here for the navX-MXP
        NavXSys.getInstance();

        // set up the autonomous options on dashboard
        mChooser.addDefault("Option  1: PF Drive Straight",          1);
        mChooser.addObject( "Option  2: PF Straight then 90",        2);
//        mChooser.addObject( "Option  3: Cross line -  Inner",                      3);
//        mChooser.addObject( "Option  4: Left Outer -  Switch - Cross Field",       4);
//        mChooser.addObject( "Option  5: Left Outer -  Switch - Don't cross Field", 5);
        SmartDashboard.putData("Auto mode", mChooser);
    }

    @Override
    public void disabledInit() {
        //DriveSys.getInstance().setupProfile();
    }


    //  TELEOP MODE  ---------------------------------------------------------------


    private int mTelopLoopCtr = 0;

    @Override
    public void teleopInit() {
        System.out.println("6072: teleop init");
        mTelopLoopCtr = 0;
        mElevatorSys.setSensorStartPosn();
    }

    private int mDisLoopCnt = 0;
    public void disabledPeriodic() {
        if (++mDisLoopCnt % (50 * 5) == 0) {
            mDriveSys.logPosn("Robot.disabled");
        }
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

        // update PDP stats every half second
//        if (++mTelopLoopCtr % 50 == 0) {
//            //logNavX();
//            Logging();
//        }
    }




    //  AUTONOMOUS MODE  ---------------------------------------------------------------


    private PathFinderDriveSys mPathFinderDriveSys;


    /**
     * Details on what is provided by field management system
     * https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details
     *
     * Data is a 3 char string with the side for:
     *      switch
     *      scale
     *      far switch
     * For example:  LRL
     *      left switch
     *      right scale
     *      left far switch
     *
     *
     The DriverStation class can provide information on what alliance color the robot is.
     When connected to FMS this is the alliance color communicated to the DS by the field.
     When not connected, the alliance color is determined by the Team Station dropdown box on the Operation tab of the DS software.

     https://www.chiefdelphi.com/forums/showthread.php?t=163822 - problems with
     We had this problem our rookie year in 2016. We found by making sure we selected the auto mode after the robot
     was connected to the field solved our problem. Even if you have already selected the mode you want,
     click off of it and back on it after the robot was connected.
     Make sure you only have one instance of the SmartDashboard open.
     If there are multiple instances open, each with a different auton selected,
     you will get mixed results. Happened to us last weekend.

     */

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        System.out.println("auto init (6072)  ------------------------------------------------------------");

        int option = mChooser.getSelected();

        // how to get what color we are - could be useful for object recog
        DriverStation.Alliance color;
        color = DriverStation.getInstance().getAlliance();
        if(color == DriverStation.Alliance.Blue){
        }
        String gameData = "";
//        while (gameData.length() < 3) {
//            sleep(10);
//        }
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() < 3) {
            System.out.println("**********************  NO GAME DATA  *********************************************");
        }
        char switchSide = gameData.charAt(0);
        char scaleSide =  gameData.charAt(1);
        char farSwitchSide = gameData.charAt(2);
        mAutonCmdGrp = new CommandGroup();
        switch (option) {
            case 1:
                mAutonCmdGrp.addSequential(new PF_AutoDriveStraightCmd());
                break;
            case 2:
                mAutonCmdGrp.addSequential(new PF_AutoDriveStraightBendCmd());
                break;
//            case 3:
//                mAutonCmd = new Auto03();
//                break;
//            case 4:
//                if ((switchSide == 'L') && (scaleSide == 'L')) {
//                    mAutonCmd = new Auto05L();
//                } else if ((switchSide == 'L') && (scaleSide == 'R')) {
//                    mAutonCmd = new Auto05L();
//                } else if ((switchSide == 'R') && (scaleSide == 'L')) {
//                    mAutonCmd = new Auto02();
//                } else if ((switchSide == 'R') && (scaleSide == 'R')) {
//                    mAutonCmd = new Auto02();
//                }
//                break;
            default:
                mAutonCmdGrp.addSequential(new PF_AutoDriveStraightCmd());;
                break;
        }
        mAutonCmdGrp.start();
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
        mCounter = 0;
//        mArcadeDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
//        Scheduler.getInstance().removeAll();
//        Scheduler.getInstance().add(mArcadeDriveCmd);
    }

    @Override public void testPeriodic() {
        Scheduler.getInstance().run();
        /*if (mCounter < 500) {
            mCounter++;
            mDriveSys.arcadeDrive(-0.4,0);

        }
        if (mCounter%5==0 && mCounter<500) {
            System.out.println(ControlBoard.getInstance().drive_stick.getY() + "       " + ControlBoard.getInstance().drive_stick.getZ());
            SmartDashboard.putNumber("Counter: ", mCounter);
        }*/
        /*if (mCounter==500) {

            disabledPeriodic();
        }*/

    }


    // UTILS  ----------------------------------------------------------------------------------------


    private void Logging() {
        Path logFile;

        try {
//            logFile = FileSystems.getDefault().getPath("/home/lvuser/logs", "PDP_Log.csv");
//            System.out.println("-------  Logging: path: " + logFile.toString() + " : " + logFile.toAbsolutePath().toString());
//            if (Files.notExists(logFile)) {
//                logFile = Files.createFile(logFile);
//                List<String> line = new ArrayList<String>();
//                line.add("time, DriveLeft, DriveRight, Elev, Arm");
//                Files.write(logFile, line, StandardCharsets.UTF_8);
//            }

            double elvCurrent = 0; //mPDP.getCurrent(RobotConfig.ELEVATOR_TALON_PDP);
            double armCurrent = 0; //mPDP.getCurrent(RobotConfig.ARM_TALON_PDP);
            double driveLeftCurrent = mPDP.getCurrent(RobotConfig.DRIVE_LEFT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_LEFT_SLAVE0_PDP);
            double driveRightCurrent = mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_SLAVE0_PDP);

            SmartDashboard.putNumber("PDP.ElevCurrent", elvCurrent);
            SmartDashboard.putNumber("PDP.ArmElevCurrent", armCurrent);
            SmartDashboard.putNumber("PDP.DriveLeftCurrent", driveLeftCurrent);
            SmartDashboard.putNumber("PDP.DriveRightCurrent", driveRightCurrent);

            String logmsg = String.format("lCur: %.3f, rCur: %.3f, elvCur: %.3f, armCur: %.3f", driveLeftCurrent, driveRightCurrent, elvCurrent, armCurrent);

//            System.out.println(logmsg);

            //SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
//            Date now = new Date();
//            List<String> line = new ArrayList<String>();
//            line.add(logmsg);
//            Files.write(logFile, line, StandardCharsets.UTF_8);
        }
        catch (Exception ex) {
            System.out.println( "*******  Logging ex: "+ ex.getClass().getName() + "   msg: " + ex.getMessage() + " ");
        }
    }



}