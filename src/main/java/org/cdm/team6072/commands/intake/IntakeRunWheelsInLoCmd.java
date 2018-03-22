package org.cdm.team6072.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;


public class IntakeRunWheelsInLoCmd extends Command {

    public IntakeRunWheelsInLoCmd() {
        requires(IntakeMotorSys.getInstance());
    }

    protected void execute() {
        IntakeMotorSys.getInstance().runWheelsInLo();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
