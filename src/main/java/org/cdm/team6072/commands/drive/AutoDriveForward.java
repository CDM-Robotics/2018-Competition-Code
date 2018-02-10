package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.DriveSys;

public class AutoDriveForward extends Command {

    public AutoDriveForward() {
        requires(DriveSys.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("auto drive forward execute");
        DriveSys driveSys = DriveSys.getInstance();
        driveSys.arcadeDrive(10,0);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
