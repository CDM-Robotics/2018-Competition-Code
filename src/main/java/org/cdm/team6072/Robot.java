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
        mElevatorSys = ElevatorSys.getInstance();
        mIntakeMotorSys = IntakeMotorSys.getInstance();
        mArmSys = ArmSys.getInstance();

        mDriveSys.setSensorStartPosn();
        mElevatorSys.setSensorStartPosn();
        mArmSys.setSensorStartPosn();

        //mPDP = new PowerDistributionPanel(RobotConfig.PDP_ID);

        // must initialize nav system here for the navX-MXP
        NavXSys.getInstance();

        // set up the autonomous options on dashboard
        mChooser.addDefault("Option  1: Run TEST Auton",      1);
        mChooser.addObject( "Option  2: Run to Switch",       2);
        mChooser.addObject( "Option  3: Run to Scale",        3);
//        mChooser.addObject( "Option  4: Left Outer -  Switch - Cross Field",       4);
//        mChooser.addObject( "Option  5: Left Outer -  Switch - Don't cross Field", 5);
        SmartDashboard.putData("Auto mode", mChooser);
    }

    @Override
    public void disabledInit() {
        // ensure the talons are not in MotionMagic or hold modes
        mElevatorSys.resetTalon();
        mArmSys.resetTalon();

        // control debug printing
        System.out.println("6072: disable init -------------------------------------------------");
        mDisLoopPrintCnt = 0;
    }


    private int mDisLoopMAXcnt = 3;
    private int mDisLoopPrintCnt=0;
    private int mDisLoopCnt = 0;

    public void disabledPeriodic() {

        if ((++mDisLoopCnt % (50 * 2) == 0) && (mDisLoopPrintCnt < mDisLoopMAXcnt)) { // limiting the log output
//            mDriveSys.logPosn("Rob.dis");
//            mElevatorSys.printPosn("Rob.dis");
            mArmSys.printPosn("Rob.dis");
//            System.out.println();
            mDisLoopPrintCnt++;
        }
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
        if (++mTelopLoopCtr % 200 == 0) {
//            //logNavX()
            NavXSys.getInstance().outputAngles();
        }
    }


    //  AUTONOMOUS MODE  ---------------------------------------------------------------


    private PathFinderDriveSys mPathFinderDriveSys;


    @Override
    public void autonomousInit() {
        DriverStation ds = DriverStation.getInstance();
        CameraManager.getInstance().runCameras();

        super.autonomousInit();
        System.out.println("auto init (6072)  ------------------------------------------------------------");
        NavXSys.getInstance().zeroYawHeading();

        GameChooser autoChooser = new GameChooser();
        autoChooser.runChooser();
    }

    public void initSwitchRoutine(int startBox, char switchSide) {
        GoToSwitch switchRoutine;
        GoToSwitch.ALLIANCE_SIDE side = null;

        if (switchSide == 'L') {
            side = GoToSwitch.ALLIANCE_SIDE.LEFT;
        } else if (switchSide == 'R') {
            side = GoToSwitch.ALLIANCE_SIDE.RIGHT;
        }
        switchRoutine = new GoToSwitch(startBox, side);
        switchRoutine.start();
    }

    public void initScaleRoutine(int startBox, char scaleSide) {
        GoToScale switchRoutine;
        GoToScale.ALLIANCE_SIDE side = null;

        if (scaleSide == 'L') {
            side = GoToScale.ALLIANCE_SIDE.LEFT;
        } else if (scaleSide == 'R') {
            side = GoToScale.ALLIANCE_SIDE.RIGHT;
        }
        switchRoutine = new GoToScale(startBox, side);
        switchRoutine.start();
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
            double driveLeftCurrent = 0; //mPDP.getCurrent(RobotConfig.DRIVE_LEFT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_LEFT_SLAVE0_PDP);
            double driveRightCurrent = 0; //mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_SLAVE0_PDP);

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