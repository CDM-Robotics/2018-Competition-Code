package org.cdm.team6072.autonomous.routines.subroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.arm.ArmMoveTo45;
import org.cdm.team6072.commands.elevator.ElvMoveToScaleHiCmd;

public class PositionScaleShooter extends CommandGroup {

    public PositionScaleShooter() {
        addParallel(new ElvMoveToScaleHiCmd());
        addParallel(new ArmMoveTo45());
    }
}
