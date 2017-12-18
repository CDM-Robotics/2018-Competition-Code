package org.cdm.team6072;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.cdm.team6072.commands.TankDriveCmd;
import org.cdm.team6072.subsystems.DriveTrain2018;
//import org.cdm.team6072.subsystems.GearSlider;

public class Robot extends IterativeRobot {


    private DriveTrain2018 mDriveTrain;
    //private GearSlider mSlider;
    //private Climber climber;

    // ControlBoard holds the operator interface code such as JoyStick
    private ControlBoard mControlBoard;


    @Override
    public void robotInit() {
        System.out.println("6072: robot initialized");

        mControlBoard = ControlBoard.getInstance();

        // Create the drive train subsystem, which had code to handle the drive motors
        // Currently this sets a default command of drive forward and passes it to the
        // scheduler. The scheduler is then called in teleoPerodic
        // The command ties itself to
        mDriveTrain = DriveTrain2018.getInstance();

        //mSlider = GearSlider.getInstance();
        //climber = Climber.getInstance();
        //dTrain = new Drivetrain();
    }

    @Override
    public void disabledInit() {
    }



    private TankDriveCmd mDriveCmd;

    @Override
    public void teleopInit() {
        System.out.println("6072 2018 2: teleop init");
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

        // driveTrain.goForward()
        // asynch positionArm
        // driveTrain.moveLeft()
    }

}