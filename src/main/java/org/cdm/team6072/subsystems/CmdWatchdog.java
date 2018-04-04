package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;

import java.util.Timer;
import java.util.TimerTask;

public class CmdWatchdog {



    public static CmdWatchdog SetWatchdog(Command cmd, int milliSecs) {
        return new CmdWatchdog(cmd, milliSecs);
    }


    /**
     * This MUST be an instance variable to allow the cancel to hit the correct task
     */
    private TimerTask mTask;

    public void Cancel() {
        if (mTask != null) {
            mTask.cancel();
        }
    }


    /**
     * Set a watch dog to check that the talon in position hold mode is not driving the motor hard forever
     * @param cmd Command to be cancelled if time exceeded to be checked
     * @param milliSecs Number of seconds to wait before checking talon
     */
    private CmdWatchdog(Command cmd, int milliSecs) {

        int delay = milliSecs;  // iterate every sec.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                cmd.cancel();
                System.out.printf("WATCHDOG:  ********  cancelled command: %s  \r\n", cmd.getName());
            }
        }, delay);
    }


}
