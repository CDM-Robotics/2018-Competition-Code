package org.cdm.team6072.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakePneumaticsSys;

public class CloseIntakeHiCmd extends Command {



    public CloseIntakeHiCmd() {
        requires(IntakePneumaticsSys.getInstance());
    }


    @Override
    protected void execute() {
        System.out.println("CloseIntakeHiCmd: exec");
        IntakePneumaticsSys.getInstance().CloseIntakeHi();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
