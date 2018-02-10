package org.cdm.team6072.commands.grabber;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import org.cdm.team6072.subsystems.IntakePneumaticsSys;

public class CloseGrabberCmd extends Command {



    public CloseGrabberCmd() {

        requires(IntakeMotorSys.getInstance());
        requires(IntakePneumaticsSys.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("CloseGrabberCmd: exec");
        IntakeMotorSys.getInstance().CloseGrabber();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
