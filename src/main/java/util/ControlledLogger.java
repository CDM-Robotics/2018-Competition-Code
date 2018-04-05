package util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class ControlledLogger {

    int count = 0;
    public ControlledLogger() {

    }

    public void print(int strike, String message) {
        count++;
        if (count % strike == 0) {
            System.out.println(message);
        }
    }

    public void print(int strike, Runnable func) {
        count++;
        if (count % strike == 0) {
            try {
                func.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // UTILS  ----------------------------------------------------------------------------------------


    private void Logging() {
        Path logFile;

        try {

            double elvCurrent = 0; //mPDP.getCurrent(RobotConfig.ELEVATOR_TALON_PDP);
            double armCurrent = 0; //mPDP.getCurrent(RobotConfig.ARM_TALON_PDP);
            double driveLeftCurrent = 0; //mPDP.getCurrent(RobotConfig.DRIVE_LEFT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_LEFT_SLAVE0_PDP);
            double driveRightCurrent = 0; //mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_MASTER_PDP) + mPDP.getCurrent(RobotConfig.DRIVE_RIGHT_SLAVE0_PDP);

            SmartDashboard.putNumber("PDP.ElevCurrent", elvCurrent);
            SmartDashboard.putNumber("PDP.ArmElevCurrent", armCurrent);
            SmartDashboard.putNumber("PDP.DriveLeftCurrent", driveLeftCurrent);
            SmartDashboard.putNumber("PDP.DriveRightCurrent", driveRightCurrent);

            String logmsg = String.format("lCur: %.3f, rCur: %.3f, elvCur: %.3f, armCur: %.3f", driveLeftCurrent, driveRightCurrent, elvCurrent, armCurrent);

        }
        catch (Exception ex) {
            System.out.println( "*******  Logging ex: "+ ex.getClass().getName() + "   msg: " + ex.getMessage() + " ");
        }
    }


}

