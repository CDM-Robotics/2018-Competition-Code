package org.cdm.team6072.commands.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Grabber;
import org.cdm.team6072.subsystems.PneumaticsControl;

public class CloseGrabberCmd extends Command {

    public CloseGrabberCmd() {
        requires(Grabber.getInstance());
    }

    @Override
    protected void execute() {
        PneumaticsControl.getInstance().turnSolenoidOn(PneumaticsControl.SolenoidType.GRABBER);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
