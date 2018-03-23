package org.cdm.team6072.autonomous.routines.subroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.arm.ArmMoveTo45;
import org.cdm.team6072.commands.elevator.ElvMoveToSwitchCmd;
import org.cdm.team6072.commands.intake.CloseIntakeHiCmd;

public class PositionSwitchShooter extends CommandGroup {

    public PositionSwitchShooter() {
        addSequential(new CloseIntakeHiCmd());
        addParallel(new ElvMoveToSwitchCmd());
        //addParallel(new ArmMoveTo45());
    }


}
