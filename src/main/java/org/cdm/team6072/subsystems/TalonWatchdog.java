package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import java.util.Timer;
import java.util.TimerTask;

public class TalonWatchdog {


    /**
     * Set a watch dog to check that the talon in position hold mode is not driving the motor hard forever
     * @param talon Talon to be checked
     * @param secs Number of seconds to wait before checking talon
     */
    private TalonWatchdog(WPI_TalonSRX talon, int secs, int pidIdx, int allowedError) {

        int delay = 1000 * secs;  // iterate every sec.
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                ControlMode mode = talon.getControlMode();
                int error = Math.abs(talon.getClosedLoopError(pidIdx));
                int posn = talon.getSelectedSensorPosition(pidIdx);
                if ((mode == ControlMode.Position) && error > allowedError) {
                    talon.set(ControlMode.PercentOutput, 0);
                    System.out.printf("WATCHDOG:  ********  Talon %s in mode: %s  with posn: %d   LARGE error: %d  *****************\r\n*",
                            talon.getName(), mode, posn, error);
                }
                else {
                    System.out.printf("WATCHDOG:  Talon %s in mode: %s  posn: %d   error: %d     OK\r\n", talon.getName(), mode, posn, error);
                }
                System.out.println();
            }
        }, delay);
    }


    public static TalonWatchdog SetWatchdog(WPI_TalonSRX talon, int secs, int pidIdx, int allowedError) {
        return new TalonWatchdog(talon, secs, pidIdx, allowedError);
    }
}
