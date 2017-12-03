package org.cdm.team6072;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.cdm.team6072.subsystems.DriveTrain;

public class Robot extends IterativeRobot {
    private DriveTrain mDriveTrain;
    //private GearSlider slider;
    //private Climber climber;
    private ControlBoard mOi;


    @Override
    public void robotInit() {
        System.out.println("6072: robot initialized");

        // get subsystems to tie them together
        mDriveTrain = DriveTrain.getInstance();
        mOi = ControlBoard.getInstance();
        //slider = GearSlider.getInstance();
        //climber = Climber.getInstance();
        //dTrain = new Drivetrain();

    }

    @Override
    public void disabledInit() {
    }



    @Override
    public void teleopInit() {
        System.out.println("6072: teleop started");
    }



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