package org.cdm.team6072.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakePneumaticsSys;

public class CloseIntakeCmd extends Command {



    public CloseIntakeCmd() {
        requires(IntakePneumaticsSys.getInstance());
    }


    @Override
    protected void execute() {
        System.out.println("CloseIntakeCmd: exec");
        IntakePneumaticsSys.getInstance().CloseIntake();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
