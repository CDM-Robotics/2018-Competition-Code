package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToScaleHiCmd;

public class GoToScale extends CommandGroup {

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    public GoToScale(int startBox, ALLIANCE_SIDE side) {
        switch (startBox) {
            case 1:
                goFromPosOneToLeft();
            case 3:
                goFromPosThreeToRight();
        }
    }

    private void goFromPosOneToLeft() {
        addSequential(new DriveDistCmd(20));
        addSequential(new DriveDistCmd(5));
        addParallel(new ElvMoveToScaleHiCmd());

    }

    private void goFromPosThreeToRight() {
        addSequential(new DriveDistCmd(20));
        addSequential(new DriveDistCmd(5));
        addParallel(new ElvMoveToScaleHiCmd());
    }
}
