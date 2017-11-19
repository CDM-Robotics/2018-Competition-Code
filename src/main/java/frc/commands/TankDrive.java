package frc.commands;
import edu.wpi.first.wpilibj.command.Command;

public class TankDrive extends Command {

    protected void execute() {
        System.out.println("execute called");
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
