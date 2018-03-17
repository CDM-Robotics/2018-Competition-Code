package org.cdm.team6072;
// manages information such as which side of the switch or scale our team is supposed to control

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Details on what is provided by field management system
 * https://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details
 *
 * Data is a 3 char string with the side for:
 *      switch
 *      scale
 *      far switch
 * For example:  LRL
 *      left switch
 *      right scale
 *      left far switch
 *
 *
 The DriverStation class can provide information on what alliance color the robot is.
 When connected to FMS this is the alliance color communicated to the DS by the field.
 When not connected, the alliance color is determined by the Team Station dropdown box on the Operation tab of the DS software.

 https://www.chiefdelphi.com/forums/showthread.php?t=163822 - problems with
 We had this problem our rookie year in 2016. We found by making sure we selected the auto mode after the robot
 was connected to the field solved our problem. Even if you have already selected the mode you want,
 click off of it and back on it after the robot was connected.
 Make sure you only have one instance of the SmartDashboard open.
 If there are multiple instances open, each with a different auton selected,
 you will get mixed results. Happened to us last weekend.

 */
public class GameData {

    private static GameData mInstance = null;
    private String gameData;

    public static enum DIRECTION {
        LEFT,
        RIGHT
    }

    public static GameData getInstance() {
        if (mInstance == null) {
            mInstance = new GameData();
        }
        return mInstance;
    }

    public void loadGameData() {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 3) {
            this.gameData = gameData;
        }
        this.gameData = null;
    }

    // example string: LRL (switch, scale, switch far)
    public DIRECTION getDirection(int index) {
        if (gameData.charAt(index) == 'L') {
            return DIRECTION.LEFT;
        } else if (gameData.charAt(index) == 'R') {
            return DIRECTION.RIGHT;
        } else {
            System.out.println("GameData.getDirection: ERROR GETTING DIRECTION!!!!");
        }
        return null;
    }

    public DIRECTION getSwitchSide() {
        return this.getDirection(0);
    }

    public DIRECTION getScaleSide() {
        return this.getDirection(1);
    }

    public DIRECTION getFarSwitchSide() {
        return this.getDirection(2);
    }

    public void logDirections() {
        String output = "switch side -> " + this.getSwitchSide().toString() + ", scale side -> " + this.getScaleSide().toString() + ", far switch -> " + this.getFarSwitchSide().toString();
        System.out.println(output);
    }

    // TODO decide how to route commands
    public void routeCommandGroups(CommandGroup group) {
        if (this.getSwitchSide() == DIRECTION.LEFT) {

        } else if (this.getSwitchSide() == DIRECTION.RIGHT) {

        }
    }

}
