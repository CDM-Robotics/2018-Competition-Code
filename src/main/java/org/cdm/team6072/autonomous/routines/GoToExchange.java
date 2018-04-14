package org.cdm.team6072.autonomous.routines;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.GameChooser;
import org.cdm.team6072.commands.arm.ArmMoveToIntake;
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

    public GoToExchange(GameChooser.STARTBOX startBox) {
        switch (startBox) {
            case LEFT:
                return;
            case CENTER:
                this.placeStartingCubeFromPosTwo();
            case RIGHT:
                return;
        }

    }

    // currently runs 1 cube (assumes no starting cubes, this is just a test and will be moded)
    private void twoCubeExchangeFromPosTwo() {
        addSequential(new DriveTurnYawCmd(-19));
        addSequential(new DriveDistCmd((float) 95/12));
        addSequential(new DriveTurnYawCmd(19));
        addSequential(new DriveDistCmd((float) 7/12));
        addParallel(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.In, 1.0));
        addSequential(new CloseIntakeHiCmd());
        addSequential(new DriveDistCmd(3, DriveDistCmd.DIR.REVERSE));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd((float) 35/12));
        addSequential(new DriveTurnYawCmd(-90));
        addSequential(new DriveDistCmd((float)65/12));
        addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
    }


    private void placeStartingCubeFromPosTwo() {
        addSequential(new DriveDistCmd(1));
        addSequential(new DriveTurnYawCmd(-90), 2);
        addSequential(new DriveDistCmd((float)45/12));
        //addParallel(new ArmMoveToIntake(), 3);
        addSequential(new DriveTurnYawCmd(-180), 2);
        addSequential(new DriveDistCmd(1));
        addSequential(new DriveDistCmd(1, DriveDistCmd.DIR.REVERSE));
        //addSequential(new RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn.Out, 1.0));
        this.processSecondCubeFromExchangePos();
    }

    // -145 turn, forward 55
    // called after initial exchange trip and robot is backed off the wall 1 foot
    private void processSecondCubeFromExchangePos() {
        addSequential(new DriveTurnYawCmd(38), 2);
        addSequential(new DriveDistCmd((float)55/12));
        addSequential(new DriveDistCmd((float)38/12, DriveDistCmd.DIR.REVERSE));
        addSequential(new DriveTurnYawCmd(180), 2);
        addSequential(new DriveDistCmd(1), 2);
    }


}
