package org.cdm.team6072.autonomous.routines.subroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.arm.ArmMoveToIntake;
import org.cdm.team6072.commands.elevator.ElvMoveToIntakeCmd;
import org.cdm.team6072.commands.intake.CloseIntakeLoCmd;
import org.cdm.team6072.commands.intake.IntakeRunWheelsInLoCmd;

public class PositionIntake extends CommandGroup {

    // arm intake pos, close low, wheels in low
    public PositionIntake() {
        addSequential(new ElvMoveToIntakeCmd());
        addParallel(new ArmMoveToIntake());
        addParallel(new CloseIntakeLoCmd());
        addParallel(new IntakeRunWheelsInLoCmd());
    }
}
