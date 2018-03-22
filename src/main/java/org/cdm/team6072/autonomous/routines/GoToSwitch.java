package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToSwitchCmd;
import org.cdm.team6072.subsystems.DriveSys;

public class GoToSwitch extends CommandGroup {

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    private float midChannelDist = (float) 4.9;
    private float midChannelToSwitchDist = 3;

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

    private void goFromPosThreeToRight() {
        addSequential(new DriveDistCmd(12));
        addSequential(new DriveTurnYawCmd(-90));
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd((float)2.95));
    }

    // go straight, 90 degree turn to right, straight, 90 degree turn left, straight to finish at switch
    private void goFromPosTwoToRight() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd((float)5.7));
        addSequential(new DriveTurnYawCmd(-90));
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosOneToRight() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd((float)10.5));
        addSequential(new DriveTurnYawCmd(-90));
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosThreeToLeft() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd((float)10.5));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosTwoToLeft() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd((float)5.7));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosOnetoLeft() {
        addSequential(new DriveDistCmd(12));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd((float)2.95));
    }

    public void testSwitch() {
        addSequential(new DriveDistCmd(1));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd(1));
    }
}
