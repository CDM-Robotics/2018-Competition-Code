package org.cdm.team6072.autonomous;

import edu.wpi.first.wpilibj.DriverStation;
import org.cdm.team6072.autonomous.routines.GoToScale;
import org.cdm.team6072.autonomous.routines.GoToSwitch;
import org.cdm.team6072.autonomous.routines.tests.TestSwitchRoutine;

// used to select autonomous routine at the beginning of the match based
// on the game data returned from the field management system
public class GameChooser {

    private DriverStation.Alliance allianceColor;
    private char switchSide;
    private char scaleSide;
    private char farSwitchSide;
    private int stationNum;


    public GameChooser() {

    }

    private void parseGameData() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();

        this.stationNum = DriverStation.getInstance().getLocation();
        try {
            this.switchSide = gameData.charAt(0);
            this.scaleSide = gameData.charAt(1);
            this.farSwitchSide = gameData.charAt(2);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("GAME CHOOSER -> CANNOT GET GAME DATA");
        }
    }

    public void runChooser() {
        this.parseGameData();

        int option = 2;
        switch (option) {
            case 1:
                TestSwitchRoutine test = new TestSwitchRoutine();
                test.start();
                break;
            case 2:
                //initSwitchRoutine(this.stationNum, switchSide);
                System.out.println("TEST: EXECUTING INIT SWITCH ROUTINE BOX TO SIDE RIGHT");
                initSwitchRoutine(2, 'R');
                break;
            case 3:
                initScaleRoutine(this.stationNum, scaleSide);
                break;
            default:
                break;
        }
    }

    public void initSwitchRoutine(int startBox, char switchSide) {
        GoToSwitch switchRoutine;
        GoToSwitch.ALLIANCE_SIDE side = null;

        if (switchSide == 'L') {
            side = GoToSwitch.ALLIANCE_SIDE.LEFT;
        } else if (switchSide == 'R') {
            side = GoToSwitch.ALLIANCE_SIDE.RIGHT;
        }
        switchRoutine = new GoToSwitch(startBox, side);
        switchRoutine.start();
    }

    public void initScaleRoutine(int startBox, char scaleSide) {
        GoToScale scaleRoutine;
        GoToScale.ALLIANCE_SIDE side = null;

        if (scaleSide == 'L') {
            side = GoToScale.ALLIANCE_SIDE.LEFT;
        } else if (scaleSide == 'R') {
            side = GoToScale.ALLIANCE_SIDE.RIGHT;
        }
        scaleRoutine = new GoToScale(startBox, side);
        scaleRoutine.start();
    }
}
