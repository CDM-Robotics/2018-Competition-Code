package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.routines.subroutines.PositionScaleShooter;
import org.cdm.team6072.commands.drive.DriveDistCmd;
import org.cdm.team6072.commands.drive.DriveTurnYawCmd;
import org.cdm.team6072.commands.elevator.ElvMoveToScaleHiCmd;
import org.cdm.team6072.commands.intake.RunIntakeWheelsCmd;
import org.cdm.team6072.subsystems.IntakeMotorSys;

public class GoToScale extends CommandGroup {

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    public GoToScale(int startBox, ALLIANCE_SIDE side) {
        switch (startBox) {
            case 1:
                if (side == ALLIANCE_SIDE.LEFT) {
                    goFromPosOneToLeft();
                } else {
                    crossLine();
                }
            case 2:
                goFromPosTwoToRight();
            case 3:
                if (side == ALLIANCE_SIDE.RIGHT) {
                    goFromPosThreeToRight();
                } else {
                    crossLine();
                }
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
        addSequential(new DriveTurnYawCmd(90), 3);
        addSequential(new PositionScaleShooter());
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));

    }

    private void goFromPosThreeToRight() {
        addSequential(new DriveDistCmd(20));
        addSequential(new DriveDistCmd(5));
        addSequential(new DriveTurnYawCmd(-90), 3);
        addSequential(new PositionScaleShooter());
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
    }

    private void crossLine() {
        addSequential(new DriveDistCmd(15));
    }

    public float inchesToFeet(int inches) {
        System.out.println("inches to feet: " + (float)(inches/12));
        return (float)(inches/12);
    }

}
