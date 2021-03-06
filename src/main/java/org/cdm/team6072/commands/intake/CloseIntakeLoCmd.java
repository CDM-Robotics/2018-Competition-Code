package org.cdm.team6072.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakePneumaticsSys;

public class CloseIntakeLoCmd extends Command {



    public CloseIntakeLoCmd() {
        requires(IntakePneumaticsSys.getInstance());
    }


    @Override
    protected void execute() {
        System.out.println("CloseIntakeLoCmd: exec");
        IntakePneumaticsSys.getInstance().CloseIntakeLo();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
