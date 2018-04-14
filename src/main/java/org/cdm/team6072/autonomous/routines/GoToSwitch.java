package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.GameChooser;
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

    private float midChannelDist = (float) 12;
    private float midChannelToSwitchDist = 3;

    private GameChooser.ALLIANCE_SIDE mSide;
    GameChooser.STARTBOX mStartBox;
    GameChooser.ALLOWCROSSFIELD mAllowCross;


    // startBox options are LEFT, CENTER, RIGhT
    // but currently hard wired to CENTER
    public GoToSwitch(GameChooser.STARTBOX startBox, GameChooser.ALLIANCE_SIDE side,  GameChooser.ALLOWCROSSFIELD allowCross) {
        System.out.println("GoToSwitch: startBox: " + startBox + "  allowCross: " + allowCross);
        mStartBox = startBox;
        mAllowCross = allowCross;
        mSide = side;
        switch (mStartBox) {
            case LEFT:
                if (mSide == GameChooser.ALLIANCE_SIDE.LEFT) {
                    goFromPosOnetoLeft();
                } else {
                    if (mAllowCross == GameChooser.ALLOWCROSSFIELD.Yes) {
                        goFromPosOneToRight();
                    }
                    else {
                        crossLine();
                    }
                }
            case CENTER:
                if (mSide == GameChooser.ALLIANCE_SIDE.LEFT) {
                    goFromPosTwoToLeft();
                } else {
                    goFromPosTwoToRight();
                }
            case RIGHT:
                if (mSide == GameChooser.ALLIANCE_SIDE.RIGHT) {
                    goFromPosThreeToRight();
                }
                else {
                    if (mAllowCross == GameChooser.ALLOWCROSSFIELD.Yes) {
                        goFromPosThreeToLeft();
                    }
                    else {
                        crossLine();
                    }
                }
        }
    }



    private void crossLine() {
        addSequential(new DriveDistCmd(15));
    }

    private void goFromPosThreeToRight() {
        addSequential(new DriveDistCmd(18), 10);
        addSequential(new PositionSwitchShooter());
    }

    // go straight, 90 degree turn to right, straight, 90 degree turn left, straight to finish at switch
    private void goFromPosTwoToRight() {
        addSequential(new DriveDistCmd(2));
        addSequential(new DriveTurnYawCmd(45), 2); // need a timeout on the turn in case the pid gets close and can't actually drive the motor
        addSequential(new DriveDistCmd(5));
        addSequential(new DriveTurnYawCmd(0),2);
        addSequential(new DriveDistCmd(2), 1);
        addSequential(new PositionSwitchShooter(), 3);
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
    }

    private void goFromPosOneToRight() {
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(90));
        addSequential(new DriveDistCmd((float)10.5));
        addSequential(new DriveTurnYawCmd(-90));
        //addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd((float)2.7));
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
        addSequential(new DriveDistCmd(2));
        addSequential(new DriveTurnYawCmd(-50),2);
        addSequential(new DriveDistCmd(6));
        addSequential(new DriveTurnYawCmd(0),2);
        addSequential(new DriveDistCmd(this.inchesToFeet(30)), 1);
        addSequential(new PositionSwitchShooter(), 3);
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
        //addSequential(new DriveDistCmd(this.inchesToFeet(10)));
        System.out.println("end of goFromPosTwoToLeft **************");
    }

    private void goFromPosOnetoLeft() {
        addSequential(new DriveDistCmd(18), 10);
        addSequential(new PositionSwitchShooter());
    }

    public void testSwitch() {
        DriveDistCmd cmd = new DriveDistCmd(12);
        addSequential(new DriveDistCmd(1));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new PositionSwitchShooter());
        addSequential(new DriveDistCmd(1));
    }

    public float inchesToFeet(int inches) {
        System.out.println("inches to feet: " + (float)(inches/12));
        return (float)(inches/12);
    }
}
