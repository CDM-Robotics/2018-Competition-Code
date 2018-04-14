package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.intake.CloseIntakeHiCmd;
import org.cdm.team6072.commands.intake.RunIntakeWheelsCmd;
import org.cdm.team6072.subsystems.IntakeMotorSys;

public class GoToExchange extends CommandGroup {

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    // Field Layout
    // ------------------------              --------------------- \\
    // -    1        L                L                  1  -      \\
    // -   (E)                                                     \\
    // -    2     (switch)         (scale)               2  -      \\
    // -                                                   (E)     \\
    // -    3        R                R                  3  -      \\
    // -----------------------               --------------------- \\

    public GoToExchange(int startBox) {
        switch (startBox) {
            case 1:
                return;
            case 2:
                return;
            case 3:
                return;
        }

    }

    private void twoCubeExchangeFromPosTwo() {
        addSequential(new DriveTurnYawCmd(-19));
        addSequential(new DriveDistCmd(95));
        addSequential(new DriveTurnYawCmd(19));
        addSequential(new DriveDistCmd(7));
        addParallel(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
        addSequential(new CloseIntakeHiCmd());

    }


}
