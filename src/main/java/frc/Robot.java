package frc;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.subsystems.DriveTrain;
//import frc.subsystems.GearSlider;
//import frc.subsystems.Climber;

public class Robot extends IterativeRobot {
    private DriveTrain driveTrain = DriveTrain.getInstance();
    //private GearSlider slider;
    //private Climber climber;
    public ControlBoard oi = ControlBoard.getInstance();


    @Override
    public void robotInit() {
        super.robotInit();
        // get subsystems to tie them together
        driveTrain = DriveTrain.getInstance();
        //slider = GearSlider.getInstance();
        //climber = Climber.getInstance();
        //dTrain = new Drivetrain();
    }

    @Override
    public void disabledInit() {

    }



    @Override
    public void teleopInit() {
        System.out.print("teleop started");
        super.teleopInit();



    }


    @Override public void autonomousInit() {
        super.autonomousInit();
    }

}