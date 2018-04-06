package org.cdm.team6072.autonomous.routines.subroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.arm.ArmMoveToIntake;
import org.cdm.team6072.commands.elevator.ElvMoveToIntakeCmd;
import org.cdm.team6072.commands.intake.CloseIntakeLoCmd;
import org.cdm.team6072.commands.intake.IntakeRunWheelsInLoCmd;
import org.cdm.team6072.commands.intake.RunIntakeWheelsCmd;
import org.cdm.team6072.subsystems.IntakeMotorSys;

public class PositionIntake extends CommandGroup {

    // arm intake pos, close low, wheels in low
    public PositionIntake() {
        addSequential(new ElvMoveToIntakeCmd(), 2);
        //addParallel(new ArmMoveToIntake());
        addParallel(new CloseIntakeLoCmd(), 1);
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
    }
}
