package frc;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.subsystems.DriveTrain;

public class Robot extends IterativeRobot {
    private DriveTrain driveTrain = DriveTrain.getInstance();

    @Override
    public void robotInit() {
        super.robotInit();

        // get subsystems to tie them together
        driveTrain.getInstance();


    }

    @Override
    public void teleopInit() {
        super.teleopInit();



    }

    @Override public void autonomousInit() {

        super.autonomousInit();
    }


}