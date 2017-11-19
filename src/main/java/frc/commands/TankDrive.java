package frc.commands;
import edu.wpi.first.wpilibj.command.Command;
import frc.ControlBoard;
import frc.subsystems.DriveTrain;

public class TankDrive extends Command {

    protected void execute() {
        System.out.println("execute called");
        DriveTrain driveTrain = DriveTrain.getInstance();
        driveTrain.drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 5);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
