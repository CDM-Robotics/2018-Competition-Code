package org.cdm.team6072;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.cdm.team6072.commands.drive.ArcadeDriveCmd;
import org.cdm.team6072.subsystems.DriveTrain;
import org.cdm.team6072.subsystems.Elevator;
import org.cdm.team6072.subsystems.Navigator;
//import org.cdm.team6072.subsystems.GearSlider;

public class Robot extends IterativeRobot {


    private DriveTrain mDriveTrain = DriveTrain.getInstance();
    private Navigator mNavx = Navigator.getInstance();
    private Elevator elevator = Elevator.getInstance();


    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();;


    @Override
    public void robotInit() {
        System.out.println("6072: robotInit");
        mControlBoard = ControlBoard.getInstance();
        mDriveTrain = DriveTrain.getInstance();
    }

    @Override
    public void disabledInit() {
    }


    /**
     * The WPILib has a race condition that may lead to the Talons not being correctly
     * configured. This is a work around noted in the TalonSRX software manual 21.18
     */
//    @Override
//    public void disabledPeriodic() {
//        if (mDriveTrain != null) {
//            mDriveTrain.disabledPeriodic();
//        }
//    }


    //  TELEOP MODE  ---------------------------------------------------------------


    private ArcadeDriveCmd mDriveCmd;

    @Override
    public void teleopInit() {
        System.out.println("6072 2018: teleop init");
        mDriveCmd = new ArcadeDriveCmd(mControlBoard.usb0_stick);
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(mDriveCmd);
    }

//    public void disabledPeriodic() {
//        mDriveTrain.arcadeDrive(0,0);
//    }

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
    }

    //  AUTONOMOUS MODE  ---------------------------------------------------------------

    @Override public void autonomousInit() {
        super.autonomousInit();
    }



    //  TEST MODE  ---------------------------------------------------------------

    private int mCounter;


    @Override
    public void testInit() {
        System.out.println("testInit: --------------------");
        mCounter = 0;
        LiveWindow.add(DriveTrain.getInstance());
    }

    @Override public void testPeriodic() {
        System.out.println("test periodic called");
        if (mCounter < 500) {
            mCounter++;
            //mDriveTrain.arcadeDrive(-0.4,0);
        }
        if (mCounter%5==0 && mCounter<500) {
            System.out.println(ControlBoard.getInstance().usb0_stick.getY() + "       " + ControlBoard.getInstance().usb0_stick.getZ());
            SmartDashboard.putNumber("Counter: ", mCounter);
        }
        if (mCounter==500) {
            disabledPeriodic();
        }
        LiveWindow.run();
    }



}