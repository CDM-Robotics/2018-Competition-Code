package org.cdm.team6072.commands.grabber;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Grabber;


public class OpenGrabberCmd extends Command {

    public OpenGrabberCmd() {
        requires(Grabber.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("OpenGrabberCmd: exec");
        Grabber.getInstance().OpenGrabber();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
