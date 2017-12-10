package org.cdm.team6072.commands;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.subsystems.DriveTrain;


/**
 * Define a command for driving
 */
public class TankDriveCmd extends Command {


    /**
     * Specify the the command requires the DriveTrain subsystem
     */
    public TankDriveCmd() {
        requires(DriveTrain.getInstance());
    }

    /**
     * Execute is called by the scheduler until the command returns finished
     * or the OI stops requesting - for example if the whileHeld() button command is used
     */
    protected void execute() {
        System.out.println("6072: execute called");
        DriveTrain driveTrain = DriveTrain.getInstance();
        driveTrain.drive.tankDrive(ControlBoard.getInstance().stick, 1, ControlBoard.getInstance().stick, 5);
    }


    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {
        return false;
    }
}
