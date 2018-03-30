package org.cdm.team6072.autonomous.routines.subroutines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.arm.ArmMoveTo135;
import org.cdm.team6072.commands.arm.ArmMoveTo45;
import org.cdm.team6072.commands.arm.ArmMoveToIntake;
import org.cdm.team6072.commands.elevator.ElvMoveToSwitchCmd;
import org.cdm.team6072.commands.intake.CloseIntakeHiCmd;
import org.cdm.team6072.commands.intake.RunIntakeWheelsCmd;
import org.cdm.team6072.subsystems.IntakeMotorSys;

public class PositionSwitchShooter extends CommandGroup {

    public PositionSwitchShooter() {
        System.out.println("PositionSwitchShooter:  ");
        addSequential(new CloseIntakeHiCmd());
        addSequential(new ElvMoveToSwitchCmd());
        addSequential(new ArmMoveToIntake());
        System.out.println("about to fire");
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
    }


}
