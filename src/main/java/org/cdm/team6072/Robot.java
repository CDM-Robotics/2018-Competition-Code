package org.cdm.team6072;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.cdm.team6072.commands.TankDriveCmd;
import org.cdm.team6072.subsystems.DriveTrain2018;
import edu.wpi.first.wpilibj.*;
import org.cdm.team6072.subsystems.Navigator;
//import org.cdm.team6072.subsystems.GearSlider;

public class Robot extends IterativeRobot {


    private DriveTrain2018 mDriveTrain = DriveTrain2018.getInstance();
    private Navigator mNavx = Navigator.getInstance();
    //private GearSlider mSlider;
    //private Climber climber;

    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard  = ControlBoard.getInstance();;


    @Override
    public void robotInit() {
        System.out.println("6072: robotInit");
       // mControlBoard = ControlBoard.getInstance();
        //mDriveTrain = DriveTrain2018.getInstance();
        //mSlider = GearSlider.getInstance();
        //climber = Climber.getInstance();
        //dTrain = new Drivetrain();
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



    private TankDriveCmd mDriveCmd;

    @Override
    public void teleopInit() {
        System.out.println("6072 2018: teleop init");
        mDriveCmd = new TankDriveCmd(mControlBoard.usb0_stick);
        Scheduler.getInstance().removeAll();
        Scheduler.getInstance().add(mDriveCmd);
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
    }



    @Override public void autonomousInit() {
        super.autonomousInit();
    }

}