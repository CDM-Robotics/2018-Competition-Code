package org.cdm.team6072.autonomous.routines.tests;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.routines.subroutines.PositionSwitchShooter;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;

public class TestSwitchRoutine extends CommandGroup {

    public TestSwitchRoutine() {
        testSwitch();
    }

    public void testSwitch() {
        addSequential(new DriveDistCmd(1));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd(1));
    }
}
