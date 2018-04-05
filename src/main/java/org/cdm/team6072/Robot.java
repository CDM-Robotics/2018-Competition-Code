package org.cdm.team6072;

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
import sun.rmi.runtime.Log;
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

        NavXSys.getInstance().zeroYawHeading();

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

        GameChooser autoChooser = new GameChooser();
        autoChooser.runChooser();
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