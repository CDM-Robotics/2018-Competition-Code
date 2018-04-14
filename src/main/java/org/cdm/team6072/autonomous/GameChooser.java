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

    private char switchSideChar;
    public static ALLIANCE_SIDE switchSide;

    private char scaleSideChar;
    public static ALLIANCE_SIDE scaleSide;

    private char farSwitchSideChar;
    public static ALLIANCE_SIDE farSwitchSide;

    public static enum ALLIANCE_SIDE {
        LEFT, RIGHT
    }

    /**
     * Get the game set up and make available for commands.
     * Must be run in auto init
     */
    public static void parseGameData() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        try {
            if (gameData.charAt(0) == 'L') {
                switchSide = ALLIANCE_SIDE.LEFT;
            }
            else {
                switchSide = ALLIANCE_SIDE.RIGHT;
            }
            if (gameData.charAt(1) == 'L') {
                scaleSide = ALLIANCE_SIDE.LEFT;
            }
            else {
                scaleSide = ALLIANCE_SIDE.RIGHT;
            }
            if (gameData.charAt(2) == 'L') {
                farSwitchSide = ALLIANCE_SIDE.LEFT;
            }
            else {
                farSwitchSide = ALLIANCE_SIDE.RIGHT;
            }
        } catch (IndexOutOfBoundsException ex) {
            Logger.getInstance().printError("Game Chooser cannot parse game data");
        }
    }



    public CommandGroup chooseCmdGrp(CHOOSER run, STARTBOX startBox, ALLOWCROSSFIELD allowCrossField) {

        this.optionRun = run;
        this.optionStartBox = startBox;
        this.parseGameData();

        Logger.getInstance().printBanner("GAME DATA SWITCH: " + switchSide + "  SCALE: " + scaleSide);

        switch (optionRun) {

            case RUN_TEST:
                System.out.println("SELECTED TEST SWITCH ROUTINE " + this.switchSide);
                return new TestSwitchRoutine();

            case RUN_SWITCH:
                System.out.println("SELECTED SWITCH ROUTINE:  BOX: " + STARTBOX.CENTER + "  TO SIDE " + switchSide);
                return new GoToSwitch(STARTBOX.CENTER, this.scaleSide, allowCross);

            case RUN_SCALE:
                System.out.println("SELECTED SCALE ROUTINE  BOX: " + optionStartBox + "  TO SIDE " + scaleSide);
                return new GoToScale(optionStartBox, this.scaleSide, allowCross);
        }
        return null;
    }




}
