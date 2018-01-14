package org.cdm.team6072.commands.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Grabber;



public class StopGrabberWheelsCmd extends Command {

    public StopGrabberWheelsCmd() {
        requires(Grabber.getInstance());
    }

    protected void execute() {
        Grabber.getInstance().stopWheels();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
