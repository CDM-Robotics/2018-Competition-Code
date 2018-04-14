package org.cdm.team6072.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.cdm.team6072.autonomous.routines.GoToScale;
import org.cdm.team6072.autonomous.routines.GoToSwitch;
import org.cdm.team6072.autonomous.routines.tests.TestSwitchRoutine;
import util.Logger;

// used to select autonomous routine at the beginning of the match based
// on the game data returned from the field management system
public class GameChooser {

    private DriverStation.Alliance allianceColor;

    private char switchSideChar;
    private ALLIANCE_SIDE switchSide;

    private char scaleSideChar;
    private ALLIANCE_SIDE scaleSide;

    private char farSwitchSideChar;
    private ALLIANCE_SIDE farSwitchSide;


    public static enum CHOOSER {
        RUN_TEST,
        RUN_SWITCH,
        RUN_SCALE,
        RUN_EXCHANGE
    }

    public static enum STARTBOX {
        LEFT,
        CENTER,
        RIGHT
    }

    public static enum ALLOWCROSSFIELD {
        Yes,
        No
    }


    private static CHOOSER optionRun = CHOOSER.RUN_SWITCH;
    private static STARTBOX optionStartBox = STARTBOX.CENTER;
    private static ALLOWCROSSFIELD allowCross = ALLOWCROSSFIELD.No;



    public GameChooser() {
    }


    private void parseGameData() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        try {
            this.switchSideChar = gameData.charAt(0);
            if (switchSideChar == 'L') {
                switchSide = ALLIANCE_SIDE.LEFT;
            }
            else {
                switchSide = ALLIANCE_SIDE.RIGHT;
            }
            this.scaleSideChar = gameData.charAt(1);
            if (scaleSideChar == 'L') {
                scaleSide = ALLIANCE_SIDE.LEFT;
            }
            else {
                scaleSide = ALLIANCE_SIDE.RIGHT;
            }
            this.farSwitchSideChar = gameData.charAt(2);
            if (farSwitchSideChar == 'L') {
                farSwitchSide = ALLIANCE_SIDE.LEFT;
            }
            else {
                farSwitchSide = ALLIANCE_SIDE.RIGHT;
            }
        } catch (IndexOutOfBoundsException ex) {
            Logger.getInstance().printError("Game Chooser cannot parse game data");
        }
    }

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }



    public CommandGroup chooseCmdGrp(CHOOSER run, STARTBOX startBox, ALLOWCROSSFIELD allowCrossField) {

        this.optionRun = run;
        this.optionStartBox = startBox;
        this.parseGameData();

        Logger.getInstance().printBanner("GAME DATA SWITCH: " + this.switchSideChar + "  SCALE: " + this.scaleSideChar);

        switch (optionRun) {

            case RUN_TEST:
                System.out.println("EXECUTING TEST SWITCH ROUTINE " + this.switchSide);
//                TestSwitchRoutine test = new TestSwitchRoutine();
//                test.start();
//                break;
                return new TestSwitchRoutine();

            case RUN_SWITCH:
                System.out.println("EXECUTING SWITCH ROUTINE BOX TO SIDE " + this.switchSide);
//                GoToSwitch switchRoutine = new GoToSwitch(STARTBOX.CENTER, this.switchSide, allowCross);
//                switchRoutine.start();
//                break;
                return new GoToSwitch(STARTBOX.CENTER, this.switchSide, allowCross);

            case RUN_SCALE:
                System.out.println("EXECUTING SCALE ROUTINE  side " + this.scaleSide);
//                GoToScale scaleRoutine = new GoToScale(optionStartBox, this.scaleSide, allowCross);
//                scaleRoutine.start();
//                break;
                return new GoToScale(optionStartBox, this.scaleSide, allowCross);
        }
        return null;
    }




}
