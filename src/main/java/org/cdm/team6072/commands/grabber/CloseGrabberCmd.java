package org.cdm.team6072.commands.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Grabber;
import org.cdm.team6072.subsystems.PneumaticsControl;

public class CloseGrabberCmd extends Command {



    public CloseGrabberCmd() {

        requires(Grabber.getInstance());
        requires(PneumaticsControl.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("CloseGrabberCmd: exec");
        Grabber.getInstance().CloseGrabber();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
