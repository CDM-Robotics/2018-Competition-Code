package org.cdm.team6072.commands.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Grabber;

public class CloseGrabberCmd extends Command {

    public CloseGrabberCmd() {
        requires(Grabber.getInstance());
    }

    @Override
    protected void execute() {
        Grabber.getInstance().getPneumaticsController().set(true);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
