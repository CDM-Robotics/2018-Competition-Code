package org.cdm.team6072.commands.intake;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;


public class StopIntakeWheelsCmd extends Command {

    public StopIntakeWheelsCmd() {
        requires(IntakeMotorSys.getInstance());
    }

    protected void execute() {
        IntakeMotorSys.getInstance().stopWheels();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
