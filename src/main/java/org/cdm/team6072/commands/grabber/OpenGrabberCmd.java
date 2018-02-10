package org.cdm.team6072.commands.grabber;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;


public class OpenGrabberCmd extends Command {

    public OpenGrabberCmd() {
        requires(IntakeMotorSys.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("OpenGrabberCmd: exec");
        IntakeMotorSys.getInstance().OpenGrabber();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
