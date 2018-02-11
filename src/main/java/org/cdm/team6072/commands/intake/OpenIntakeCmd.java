package org.cdm.team6072.commands.intake;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;


public class OpenIntakeCmd extends Command {

    public OpenIntakeCmd() {
        requires(IntakeMotorSys.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("OpenIntakeCmd: exec");
        IntakeMotorSys.getInstance().OpenGrabber();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
