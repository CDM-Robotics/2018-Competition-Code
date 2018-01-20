package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.DriveTrain;

public class AutoDriveForward extends Command {

    public AutoDriveForward() {
        requires(DriveTrain.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("auto drive forward execute");
        DriveTrain driveTrain = DriveTrain.getInstance();
        driveTrain.arcadeDrive(10,0);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
