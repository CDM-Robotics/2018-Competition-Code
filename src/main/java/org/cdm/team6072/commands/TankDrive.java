package org.cdm.team6072.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.subsystems.DriveTrain;


public class TankDrive extends Command {

    public TankDrive() {
        requires(DriveTrain.getInstance());
    }
    protected void execute() {
        System.out.println("6072: execute called");
        DriveTrain driveTrain = DriveTrain.getInstance();
        driveTrain.drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 5);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
