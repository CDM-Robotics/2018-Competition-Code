package frc;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.subsystems.DriveTrain;

public class Robot extends IterativeRobot {
    private DriveTrain driveTrain = DriveTrain.getInstance();

    @Override
    public void robotInit() {
        super.robotInit();

    }

    @Override
    public void teleopInit() {
        super.teleopInit();

        //driveTrain
    }

    @Override public void autonomousInit() {

        super.autonomousInit();
    }


}