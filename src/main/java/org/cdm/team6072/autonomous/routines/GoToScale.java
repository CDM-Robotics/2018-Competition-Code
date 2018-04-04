package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToScaleHiCmd;

public class GoToScale extends CommandGroup {

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    public GoToScale(int startBox, ALLIANCE_SIDE side) {
        startBox = 2;
        switch (startBox) {
            case 1:
                goFromPosOneToLeft();
            case 2:
                goFromPosTwoToRight();
            case 3:
                goFromPosThreeToRight();
        }
    }

    private void goFromPosTwoToRight() {
        addSequential(new DriveDistCmd((float)1.5));
        addSequential(new DriveTurnYawCmd(45), 2);
        addSequential(new DriveDistCmd(this.inchesToFeet(154)));
        addSequential(new DriveTurnYawCmd(0), 2);
        addSequential(new DriveDistCmd(this.inchesToFeet(208)));
        addSequential(new DriveTurnYawCmd(-90), 2);
        addSequential(new DriveDistCmd((float)0.1));
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

    public float inchesToFeet(int inches) {
        System.out.println("inches to feet: " + (float)(inches/12));
        return (float)(inches/12);
    }

}
