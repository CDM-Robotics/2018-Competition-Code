package org.cdm.team6072.commands.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;


public class StopGrabberWheelsCmd extends Command {

    public StopGrabberWheelsCmd() {
        requires(IntakeMotorSys.getInstance());
    }

    protected void execute() {
        IntakeMotorSys.getInstance().stopWheels();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
