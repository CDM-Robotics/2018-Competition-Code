package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.subsystems.DriveSys;

public class GoToSwitch extends CommandGroup {

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    // startBox options are 1, 2, 3
    public GoToSwitch(int startBox, ALLIANCE_SIDE side) {
        System.out.println("GoToSwitch: startBox -> " + startBox + ", side -> " + side);

        switch (startBox) {
            case 1:
                // do something
                if (side == ALLIANCE_SIDE.LEFT) {
                    goFromPosOnetoLeft();
                } else {
                    goFromPosOneToRight();
                }
            case 2:
                // do something
                if (side == ALLIANCE_SIDE.LEFT) {
                    goFromPosTwoToLeft();
                } else {
                    goFromPosTwoToRight();
                }
            case 3:
                // do something
                if (side == ALLIANCE_SIDE.LEFT) {
                    goFromPosThreeToLeft();
                } else {
                    goFromPosThreeToRight();
                }
        }
    }

    public void goFromPosThreeToRight() {
        addSequential(new DriveDistCmd(10));
        addSequential(new DriveTurnYawCmd(45));
        addSequential(new DriveDistCmd(2));
    }

    // go straight, 90 degree turn to right, straight, 90 degree turn left, straight to finish at switch
    public void goFromPosTwoToRight() {
        addSequential(new DriveDistCmd(8));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd(5));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd(3));
    }

    public void goFromPosOneToRight() {
        addSequential(new DriveDistCmd(8));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd(10));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd(3));
    }

    public void goFromPosThreeToLeft() {
        addSequential(new DriveDistCmd(8));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd(10));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd(3));
    }

    public void goFromPosTwoToLeft() {
        addSequential(new DriveDistCmd(8));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd(5));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd(3));
    }

    public void goFromPosOnetoLeft() {
        addSequential(new DriveDistCmd(10));
        addSequential(new DriveTurnYawCmd(45));
        addSequential(new DriveDistCmd(3));
    }
}
