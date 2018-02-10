package org.cdm.team6072;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.cdm.team6072.commands.drive.ArcadeDriveCmd;
import org.cdm.team6072.subsystems.DriveSys;
import org.cdm.team6072.subsystems.ElevatorSys;
import org.cdm.team6072.subsystems.Navigator;
//import org.cdm.team6072.subsystems.GearSlider;

public class Robot extends IterativeRobot {


    private DriveSys mDriveSys = DriveSys.getInstance();
    private Navigator mNavx = Navigator.getInstance();
    private ElevatorSys mElevatorSys = ElevatorSys.getInstance();

    //private MotionProfileManager profile = new MotionProfileManager(DriveSys.getInstance().getmLeftMaster());


    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();;


    @Override
    public void robotInit() {
        System.out.println("6072: robotInit");
        mControlBoard = ControlBoard.getInstance();
        mDriveSys = DriveSys.getInstance();
    }

    @Override
    public void disabledInit() {
        //DriveSys.getInstance().setupProfile();
    }


    /**
     * The WPILib has a race condition that may lead to the Talons not being correctly
     * configured. This is a work around noted in the TalonSRX software manual 21.18
     */
//    @Override
//    public void disabledPeriodic() {
//        if (mDriveSys != null) {
//            mDriveSys.disabledPeriodic();
//        }
//    }


    //  TELEOP MODE  ---------------------------------------------------------------
    private ArcadeDriveCmd mDriveCmd;

    @Override
    public void teleopInit() {
        System.out.println("6072: teleop init");
        mDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(mDriveCmd);

       //DriveSys.getInstance().getMotionProfileManager().startMotionProfile();
    }

//    public void disabledPeriodic() {
//        mDriveSys.arcadeDrive(0,0);
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

        // MOTION PROFILING
//        mElevatorSys.updateTalonRequiredMPState();
//        mElevatorSys.getMPController().control();


//        DriveSys.getInstance().updateTalonRequiredMPState();
//        DriveSys.getInstance().getMotionProfileManager().control();

//        ElevatorSys.getInstance().masterTalonTest();
    }

    //  AUTONOMOUS MODE  ---------------------------------------------------------------

    @Override

    public void autonomousInit() {
        //super.autonomousInit();
        System.out.println("auto init (6072)");
        /*AutoDriveForward cmd = new AutoDriveForward();
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(cmd);*/
    }

    @Override
    public void autonomousPeriodic() {
    }



    //  TEST MODE  ---------------------------------------------------------------

    private int mCounter;


    @Override
    public void testInit() {
        System.out.println("testInit: --------------------");
        mCounter = 0;
        LiveWindow.add(DriveSys.getInstance());
        mDriveCmd = new ArcadeDriveCmd(mControlBoard.drive_stick);
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(mDriveCmd);
    }

    @Override public void testPeriodic() {
        System.out.println("test periodic called");
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

        LiveWindow.run();
    }




}