package org.cdm.team6072;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.cdm.team6072.autonomous.GameChooser;
import org.cdm.team6072.commands.drive.*;
import org.cdm.team6072.subsystems.*;
import util.ControlledLogger;
import util.Logger;


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

    // set up SmartDash for choosing auto

    private   SendableChooser mChooser ;

    private Command mCmdSwitch;
    private Command mCmdScaleLeft;
    private Command mCmdScaleRight;


    // ********************************************** //
    // ENABLE/DISABLE MODE
    // ********************************************* //
    @Override
    public void robotInit() {
        mDriveSys = DriveSys.getInstance();
        mElevatorSys = ElevatorSys.getInstance();
        mIntakeMotorSys = IntakeMotorSys.getInstance();
        mArmSys = ArmSys.getInstance();

        mDriveSys.setSensorStartPosn();
        mElevatorSys.setSensorStartPosn();
        mArmSys.setSensorStartPosn();

        // must initialize nav system here for the navX-MXP
        NavXSys.getInstance();

        // set up chooser
        // Dashboard default is set in file
        //       C:\Users\Public\Documents\FRC folder\FRC DS Data Storage.ini
        // Jars are in
        //      C:\Users\David\wpilib\tools

//        SmartDashboard.putBoolean("AllowCross", true);
//        SmartDashboard.putBoolean("RunScale", false);
//        SmartDashboard.putNumber("StartBox1_3", 1);

        GameChooser gameChooser = new GameChooser();
        mCmdSwitch = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SWITCH, GameChooser.STARTBOX.CENTER, GameChooser.ALLOWCROSSFIELD.Yes);
        mCmdScaleLeft = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SCALE, GameChooser.STARTBOX.LEFT, GameChooser.ALLOWCROSSFIELD.No);
        mCmdScaleRight = gameChooser.chooseCmdGrp(GameChooser.CHOOSER.RUN_SCALE, GameChooser.STARTBOX.RIGHT, GameChooser.ALLOWCROSSFIELD.No);
        mChooser = new SendableChooser();
        mChooser.addDefault("Switch  Center", mCmdSwitch);
        mChooser.addObject( "Scale   Left    NO cross", mCmdScaleLeft);
        mChooser.addObject( "Scale   Right   NO cross", mCmdScaleRight);

        SmartDashboard.putData(mChooser);

        SmartDashboard.putData("Switch cmd:", mCmdSwitch);
    }

    @Override
    public void disabledInit() {
        Logger.getInstance().printBanner("DISABLED INIT");
        // ensure the talons are not in MotionMagic or hold modes
        mElevatorSys.resetTalon();
        mArmSys.resetTalon();
    }

    public void disabledPeriodic() {
        new ControlledLogger().print(100, new Runnable() {
            @Override
            public void run() {
                mArmSys.printPosn("Rob.dis");
            }
        });
    }

    // ******************************************** //
    // TELEOP MODE
    // ******************************************* //
    @Override
    public void teleopInit() {
        Logger.getInstance().printBanner("TELEOP INIT");

        double startBox = SmartDashboard.getNumber("StartBox1_3", -1);

        System.out.println("teleopInit: start box: " + startBox);

        NavXSys.getInstance().zeroYawHeading();

        ArcadeDriveCmd  mArcadeDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(mArcadeDriveCmd);

        SmartDashboard.putData("TI Switch cmd:", mCmdSwitch);

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


    //******************************************** //
    // AUTONOMOUS MODE
    //******************************************* //
    @Override
    public void autonomousInit() {
        super.autonomousInit();
        Logger.getInstance().printBanner("AUTO INIT");

        DriverStation ds = DriverStation.getInstance();
        //CameraManager.getInstance().runCameras();

        NavXSys.getInstance().zeroYawHeading();

        CommandGroup cmdGroup = (CommandGroup)mChooser.getSelected();
        cmdGroup.start();
    }



    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }


    //******************************************* //
    // TEST MODE
    //****************************************** //
    @Override
    public void testInit() {
        Logger.getInstance().printBanner("TEST INIT");
    }

    @Override public void testPeriodic() {
        Scheduler.getInstance().run();
    }

}