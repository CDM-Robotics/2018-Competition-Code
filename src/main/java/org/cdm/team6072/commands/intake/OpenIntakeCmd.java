package org.cdm.team6072.commands.intake;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import org.cdm.team6072.subsystems.IntakePneumaticsSys;


public class OpenIntakeCmd extends Command {

    public OpenIntakeCmd() {
        requires(IntakeMotorSys.getInstance());
    }

    @Override
    protected void execute() {
        System.out.println("OpenIntakeCmd: exec");
        IntakePneumaticsSys.getInstance().OpenIntake();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
