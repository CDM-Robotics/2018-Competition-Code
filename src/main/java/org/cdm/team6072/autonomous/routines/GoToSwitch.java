package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.GameChooser;
import org.cdm.team6072.autonomous.routines.subroutines.PositionSwitchShooter;
import org.cdm.team6072.commands.arm.ArmMoveToIntake;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToIntakeCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToSwitchCmd;
import org.cdm.team6072.commands.intake.*;
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


    private static double RUN_WHEELS_TIME = 1.0;


    // startBox options are LEFT, CENTER, RIGHT
    // but currently hard wired to CENTER
    public GoToSwitch(GameChooser.STARTBOX startBox, GameChooser.ALLIANCE_SIDE side,
                        GameChooser.ALLOWCROSSFIELD allowCross, GameChooser.NUM_CUBES numCubes) {
        System.out.println("GoToSwitch: startBox: " + startBox + "  allowCross: " + allowCross + "   side: " + side);
        mStartBox = startBox;
        mAllowCross = allowCross;
        mSide = side;
        switch (mStartBox) {
            case LEFT:
                if (mSide == GameChooser.ALLIANCE_SIDE.LEFT) {
                    switch(numCubes) {
                        case ONE:
                            goFromPosOnetoLeft_OneCube();
                            break;
                        case TWO:
                            goFromPosOnetoLeft_TwoCube();
                    }
                } else {
                    if (mAllowCross == GameChooser.ALLOWCROSSFIELD.Yes) {
                        goFromPosOneToRight_OneCube();
                    }
                    else {
                        crossLine();
                    }
                }
                break;
            case CENTER:
                if (mSide == GameChooser.ALLIANCE_SIDE.LEFT) {
                    switch (numCubes) {
                        case ONE:
                            goFromPosTwoToLeft_OneCube();
                            break;
                        case TWO:
                            goPosnTwoToLeft_TwoCube();
                            break;
                    }
                } else {
                    switch (numCubes) {
                        case ONE:
                            goFromPosTwoToRight_OneCube();
                            break;
                        case TWO:
                            goPosnTwoToRight_TwoCube();
                            break;
                    }
                }
                break;
            case RIGHT:
                if (mSide == GameChooser.ALLIANCE_SIDE.RIGHT) {
                    switch(numCubes) {
                        case ONE:
                            goFromPosThreeToRight_OneCube();
                            break;
                        case TWO:
                            goFromPosThreetoRight_TwoCube();
                            break;
                        default:
                            crossLine();
                            break;
                    }
                }
                else {
                    if (mAllowCross == GameChooser.ALLOWCROSSFIELD.Yes) {
                        goFromPosThreeToLeft();
                    }
                    else {
                       crossLine();
                    }
                }
                break;
        }
    }


    private void crossLine() {
        addSequential(new DriveDistCmd(15));
    }


    // --------------  Posn  1  ------------------------------------------


    private void goFromPosOnetoLeft_OneCube() {
        System.out.println("GTSwitch.goFromPosOnetoLeft_OneCube");
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd(12/12), 5);
        addSequential(new DriveTurnYawCmd(35), 2);
        addSequential(new DriveDistCmd(80/12), 5);
        // straighten up
        addSequential(new DriveTurnYawCmd(0), 2);
        addSequential(new DriveDistCmd(6/12), 2);
        // fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
    }

    private void goFromPosOnetoLeft_TwoCube() {
        System.out.println("GTSwitch.goFromPosOnetoLeft_TwoCube");
        goFromPosOnetoLeft_OneCube();

        // now back up and get bottom left cube
        addSequential(new DriveDistCmd((float) 24/12, DriveDistCmd.DIR.REVERSE));
        addParallel((new ElvMoveToIntakeCmd()));
        addParallel((new CloseIntakeLoCmd()));

        addSequential(new DriveTurnYawCmd(45));
        addSequential(new StartRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
        addSequential(new DriveDistCmd(36/12), 2);
        // grab the cube
        addSequential(new TimedStopRunIntakeWheelsCmd(RUN_WHEELS_TIME));
        addSequential(new CloseIntakeHiCmd());

        // back up and approach switch
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd((float) 24/12, DriveDistCmd.DIR.REVERSE));
        addSequential(new DriveTurnYawCmd(0));
        addSequential(new DriveDistCmd(30/12), 2);
        // and fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
    }

    private void goFromPosOneToRight_OneCube() {
        System.out.println("GTSwitch.goFromPosOneToRight_OneCube");
        // move behind switch
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(90));
        addParallel(new ElvMoveToSwitchCmd());
        addParallel(new ArmMoveToIntake());
        // run along back
        addSequential(new DriveDistCmd((float)10.5));
        addSequential(new DriveTurnYawCmd(180));
        addSequential(new DriveDistCmd((float)2.7));
        // and fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
    }


    // --------------  Posn 2  ------------------------------------------


    // angle from center to get to switch side
    private void goFromPosTwoToRight_OneCube() {
        System.out.println("GTSwitch.goFromPosTwoToRight_OneCube");
        addSequential(new DriveDistCmd(2));
        addParallel(new ElvMoveToSwitchCmd());
        addParallel(new ArmMoveToIntake());
        addSequential(new DriveTurnYawCmd(45), 1);
        addSequential(new DriveDistCmd(5));
        addSequential(new DriveTurnYawCmd(0),1);
        addSequential(new DriveDistCmd(2), 1);

        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
    }

    private void goPosnTwoToRight_TwoCube() {
        System.out.println("GTSwitch.goPosnTwoToRight_TwoCube");
        this.goFromPosTwoToRight_OneCube();
        addParallel(new ElvMoveToIntakeCmd(), 2);
        addParallel(new CloseIntakeLoCmd());

        // back up and look for cube
        addSequential(new DriveDistCmd((float) 50/12, DriveDistCmd.DIR.REVERSE));
        addSequential(new DriveTurnYawCmd(-56),2);

        // grab the cube
        addSequential(new StartRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
        addSequential(new DriveDistCmd((float)64/12), 2);
        addSequential(new TimedStopRunIntakeWheelsCmd(1.0 ));
        addSequential(new CloseIntakeHiCmd(), 0.1);

        // move back to switch
        addSequential(new DriveDistCmd((float)50/12, DriveDistCmd.DIR.REVERSE), 2);
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveTurnYawCmd(0),2);
        addSequential(new DriveDistCmd((float) 45/12, DriveDistCmd.DIR.FORWARD),2);
        // shoot
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
    }

    private void goFromPosTwoToLeft_OneCube() {
        addSequential(new DriveDistCmd(2));
        addParallel(new ElvMoveToSwitchCmd());
        addParallel(new ArmMoveToIntake());
        addSequential(new DriveTurnYawCmd(-50),2);
        addSequential(new DriveDistCmd(6));
        addSequential(new DriveTurnYawCmd(0),2);
        addSequential(new DriveDistCmd(this.inchesToFeet(30)), 1);
        // fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
        System.out.println("end of goFromPosTwoToLeft_OneCube **************");
    }

    private void goPosnTwoToLeft_TwoCube() {
        System.out.println("GTSwitch.goPosnTwoToLeft_TwoCube");
        this.goFromPosTwoToLeft_OneCube();

        addParallel(new ElvMoveToIntakeCmd(), 1.5);
        addParallel(new CloseIntakeLoCmd(), 0.1);

        addSequential(new DriveDistCmd((float) 50/12, DriveDistCmd.DIR.REVERSE));
        addSequential(new DriveTurnYawCmd(50),1);

        addSequential(new StartRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
        addSequential(new DriveDistCmd((float)64/12));

        // grab the cube
        addSequential(new TimedStopRunIntakeWheelsCmd(1.0 ));
        addSequential(new CloseIntakeHiCmd(), 0.1);

        addParallel(new ElvMoveToSwitchCmd());

        addSequential(new DriveDistCmd((float)50/12, DriveDistCmd.DIR.REVERSE));
        addSequential(new DriveTurnYawCmd(0),1);
        addSequential(new DriveDistCmd((float) 45/12, DriveDistCmd.DIR.FORWARD),2);

        // shoot
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
    }



    //  ------------------  Posn 3  -------------------------------------------------------

    private void goFromPosThreeToRight_OneCube() {
        addParallel(new ElvMoveToSwitchCmd());
        addParallel(new ArmMoveToIntake());
        addSequential(new DriveDistCmd(12/12), 5);
        addSequential(new DriveTurnYawCmd(-35));
        addSequential(new DriveDistCmd(80/12), 5);
        addSequential(new DriveTurnYawCmd(0), 2);
        addSequential(new DriveDistCmd(6/12), 2);
        // fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0,RUN_WHEELS_TIME));
    }

    private void goFromPosThreetoRight_TwoCube() {
        System.out.println("GTSwitch.goFromThreeOnetoRight_TwoCube");
        goFromPosThreeToRight_OneCube();

        // now back up and try to cube from pyramid
        addSequential(new DriveDistCmd((float) 30/12, DriveDistCmd.DIR.REVERSE));
        addParallel(new ElvMoveToIntakeCmd(), 2);
        addParallel(new CloseIntakeLoCmd());
        addSequential(new DriveTurnYawCmd(-45), 2);
        addSequential(new StartRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
        addSequential(new DriveDistCmd(36/12), 2);
        // grab the cube
        addSequential(new TimedStopRunIntakeWheelsCmd(RUN_WHEELS_TIME ));
        addSequential(new CloseIntakeHiCmd());

        // move back to switch
        addParallel(new ElvMoveToSwitchCmd());
        addSequential(new DriveDistCmd((float) 24/12, DriveDistCmd.DIR.REVERSE));
        addSequential(new DriveTurnYawCmd(0));

        addSequential(new DriveDistCmd(30/12), 2);
        // fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
    }

    private void goFromPosThreeToLeft() {
        // move behind switch
        addSequential(new DriveDistCmd(midChannelDist));
        addSequential(new DriveTurnYawCmd(-90));
        addParallel(new ElvMoveToSwitchCmd());
        addParallel(new ArmMoveToIntake());
        // run along back
        addSequential(new DriveDistCmd((float)10.5));
        addSequential(new DriveTurnYawCmd(-180));
        addSequential(new DriveDistCmd((float)2.7));
        // and fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, RUN_WHEELS_TIME));
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

    // testing right

}
