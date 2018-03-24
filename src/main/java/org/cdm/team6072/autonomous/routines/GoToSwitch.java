package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.routines.subroutines.PositionSwitchShooter;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToSwitchCmd;
import org.cdm.team6072.commands.intake.RunIntakeWheelsCmd;
import org.cdm.team6072.subsystems.ArmSys;
import org.cdm.team6072.subsystems.DriveSys;
import org.cdm.team6072.subsystems.IntakeMotorSys;

public class GoToSwitch extends CommandGroup {

    // Field Layout
    // ------------------------              --------------------- \\
    // -    1        L                L                  1  -      \\
    // -    2     (switch)         (scale)               2  -      \\
    // -    3        R                R                  3  -      \\
    // -----------------------               --------------------- \\

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    private float midChannelDist = (float) 12;
    private float midChannelToSwitchDist = 3;

    private ALLIANCE_SIDE mSide;

    // startBox options are 1, 2, 3
    public GoToSwitch(int startBox, ALLIANCE_SIDE side) {
        System.out.println("GoToSwitch: startBox -> " + startBox + ", side -> " + side);

        mSide = side;

        switch (3) {
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
        System.out.println("goFromPos3 called");
        addSequential(new DriveDistCmd(18));
        //addSequential(new DriveTurnYawCmd(-90));
        addSequential(new PositionSwitchShooter());
        /*if (mSide == ALLIANCE_SIDE.RIGHT) {
            System.out.println("goFromPosThreeToRight:");
            addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
        }*/
        //addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
        //addSequential(new DriveDistCmd((float)2.95));
    }

    // go straight, 90 degree turn to right, straight, 90 degree turn left, straight to finish at switch
    private void goFromPosTwoToRight() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd((float)5.7));
        addSequential(new DriveTurnYawCmd(-90));
        addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosOneToRight() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd((float)10.5));
        addSequential(new DriveTurnYawCmd(-90));
        addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosThreeToLeft() {
        addSequential(new DriveDistCmd(midChannelDist));
//        addSequential(new DriveTurnYawCmd(-90));
//        addSequential(new DriveDistCmd((float)10.5));
//        addSequential(new DriveTurnYawCmd(90));
//        addParallel(new PositionSwitchShooter());
//        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosTwoToLeft() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd((float)5.7));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd(midChannelToSwitchDist));
    }

    private void goFromPosOnetoLeft() {
        addSequential(new DriveDistCmd(12));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd((float)2.95));
    }

    public void testSwitch() {
        addSequential(new DriveDistCmd(1));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd(1));
    }
}
