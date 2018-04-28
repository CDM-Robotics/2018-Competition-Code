package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.GameChooser;
import org.cdm.team6072.autonomous.routines.subroutines.PositionScaleShooter;
import org.cdm.team6072.commands.arm.ArmMoveToIntake;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToScaleHiCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToSwitchCmd;
import org.cdm.team6072.commands.intake.RunIntakeWheelsCmd;
import org.cdm.team6072.commands.intake.TimedRunIntakeWheelsCmd;
import org.cdm.team6072.subsystems.IntakeMotorSys;

public class GoToScale extends CommandGroup {


    // Field Layout
    // ------------------------              --------------------- \\
    // -    1        L                L                  1  -      \\
    // -    2     (switch)         (scale)               2  -      \\
    // -    3        R                R                  3  -      \\
    // -----------------------               --------------------- \\



    public GoToScale(GameChooser.STARTBOX startBox, GameChooser.ALLIANCE_SIDE side, GameChooser.ALLOWCROSSFIELD allowCross) {
        switch (startBox) {
            case LEFT:
                if (side == GameChooser.ALLIANCE_SIDE.LEFT) {
                    goFromPosOneToLeft();
                } else {
                    if (allowCross == GameChooser.ALLOWCROSSFIELD.Yes) {
                        goFromPosOneToRight();
                    } else {
                        crossLine();
                    }
                }
                break;

            case CENTER:
                goFromPosTwoToRight();
                break;

            case RIGHT:
                if (side == GameChooser.ALLIANCE_SIDE.RIGHT) {
                    goFromPosThreeToRight();
                }
                else {
                    if (allowCross == GameChooser.ALLOWCROSSFIELD.Yes) {
                        goFromPosThreeToLeft();
                    }
                    else {
                        crossLine();
                    }
                }
                break;
        }
    }

    // --------------  posn  1  ---------------------------------------------

    private void goFromPosOneToLeft() {
        addParallel(new ElvMoveToScaleHiCmd());
        addParallel(new ArmMoveToIntake());
        addSequential(new DriveDistCmd(24));
        //addSequential(new DriveDistCmd(5));
        addSequential(new DriveTurnYawCmd(90), 3);
        addSequential(new DriveDistCmd((float) 6/12));
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, 1.0));
    }


    private void goFromPosOneToRight() {
        addSequential(new DriveDistCmd((float)227/12));
        addSequential(new DriveTurnYawCmd(90), 2);
        addSequential(new DriveDistCmd((float) 214/12));
        addSequential(new DriveTurnYawCmd(-90), 2);
        addSequential(new DriveDistCmd((float)96/12));
        addSequential(new PositionScaleShooter());
        addSequential(new DriveTurnYawCmd(-90), 2);
        // fire
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
    }

    // ---------------  posn 2  -------------------------------------------------

    private void goFromPosTwoToRight() {
        addSequential(new DriveDistCmd((float)1.5));
        addSequential(new DriveTurnYawCmd(45), 2);
        addSequential(new DriveDistCmd(this.inchesToFeet(154)));
        addSequential(new DriveTurnYawCmd(0), 2);
        addSequential(new DriveDistCmd(this.inchesToFeet(208)));
        addSequential(new DriveTurnYawCmd(-90), 2);
        addSequential(new DriveDistCmd((float)0.1));
    }



    // ------------  posn 3  -----------------------------------------------


    // robot is 38 long by 34 wide
    //
    // full field width 264 + 29.69 * 2 = 323.38
    // field edge to side of switch:  85.25
    // field start to
    //          switch edge         140
    //          switch centerline:  168
    //          switch far edge:    196
    //                  need to be 196 + 19 (center robot) + 12 (clear cubes) = 227  (18.9 feet)
    //
    // field side to scale platform edge:  71.57
    //          scale width:        323.38 - (2*71.57) = 180.24
    // start robot 6" from angle => centerline is 29.69 + 6 + 19 = 54.69 (4.55 ft) from field side
    //      want to go to same centerline other side:  323.38 - 2 * 54.69  =  214  (17.83 ft)

    //  scale centerline is 299.65 + 48/2  =  323.65  48 is width of scale platform
    //      robot is at 227
    //       so need to go 323 - 227 = 96
    //
    private void goFromPosThreeToLeft() {
        addParallel(new ElvMoveToScaleHiCmd());
        addParallel(new ArmMoveToIntake());
        addSequential(new DriveDistCmd((float)227/12));            // 242 inches for centerline of robot
        addSequential(new DriveTurnYawCmd(-90), 2);
        addSequential(new DriveDistCmd((float) 214/12));
        addSequential(new DriveTurnYawCmd(90), 2);
        addSequential(new DriveDistCmd((float)96/12));
        addSequential(new DriveTurnYawCmd(90), 1);
        addSequential(new DriveDistCmd((float) 12/12));

        // fire
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, 1.0));
    }

    private void goFromPosThreeToRight() {
        System.out.println("GTS:  goFromPosThreeToRight");
        addParallel(new ElvMoveToScaleHiCmd());
        addParallel(new ArmMoveToIntake());
        addSequential(new DriveDistCmd(24));
        //addSequential(new DriveDistCmd(4));
        addSequential(new DriveTurnYawCmd(-90), 3);
        addSequential(new DriveDistCmd((float) 6/12));
        addSequential(new TimedRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0, 1.0));
    }

    private void crossLine() {
        addSequential(new DriveDistCmd(15));
    }

    public float inchesToFeet(int inches) {
        System.out.println("inches to feet: " + (float)(inches/12));
        return (float)(inches/12);
    }

}
