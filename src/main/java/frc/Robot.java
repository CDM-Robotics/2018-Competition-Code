package frc;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.subsystems.DriveTrain;
import frc.subsystems.GearSlider;
import frc.subsystems.Climber;

public class Robot extends IterativeRobot {
    private DriveTrain driveTrain;
    private GearSlider slider;
    private Climber climber;

    @Override
    public void robotInit() {
        super.robotInit();

        // get subsystems to tie them together
        driveTrain = DriveTrain.getInstance();
        slider = GearSlider.getInstance();
        climber = Climber.getInstance();
    }

    @Override
    public void teleopInit() {
        super.teleopInit();



    }

    @Override public void autonomousInit() {

        super.autonomousInit();
    }


}