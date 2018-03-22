package org.cdm.team6072.autonomous.routines.subroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.arm.ArmMoveTo45;
import org.cdm.team6072.commands.elevator.ElvMoveToSwitchCmd;

public class PositionSwitchShooter extends CommandGroup {

    public PositionSwitchShooter() {
        addParallel(new ElvMoveToSwitchCmd());
        addParallel(new ArmMoveTo45());
    }


}
