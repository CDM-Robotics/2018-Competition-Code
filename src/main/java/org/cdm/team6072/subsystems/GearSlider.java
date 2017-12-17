//package frc.subsystems;
package org.cdm.team6072.subsystems;
//import com.ctre.MotorControl.CANTalon;
//import com.ctre.MotorControl.CANTalon;
import com.ctre.CTR_Code;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.ControlBoard;
import org.cdm.team6072.RobotConfig;
//import com.ctre.CTR_Code.*;

//import com.ctre.CANTalon;
// NOTE: support for WPI CANTalon dropped
// need to get a jar from somewhere else


/**
 * Created by Cole on 9/29/17.
 */
public class GearSlider extends Subsystem {
    // singleton instance
    private static GearSlider instance;
    public static GearSlider getInstance() {
        return instance;
    }

    // motor controller
    //private CANTalon talon;

    private GearSlider() {

        //this.talon = new CANTalon(RobotConfig.SLIDER_TALON);
        //this.talon = new CANTalon(RobotConfig.SLIDER_TALON);
    }

    public void stop() {

    }

    public void setSpeed(double speed) {
        //this.talon.changeControlMode(CANTalon.TalonControlMode.Speed);
        //this.talon.set(speed);
        //talon.set(speed);
    }

    @Override
    public void initDefaultCommand() {
        manualSlide();
    }


    // slide according to the yaw of the xbox remote
    // a manual command doesn't need to return
    public void manualSlide() {
        Command cmd = new Command() {

            @Override
            protected void initialize() {
                super.initialize();
            }

            @Override
            protected void execute() {
                double speed = ControlBoard.getInstance().gamepad.getRawAxis(0);
                if (Math.abs(speed) > 85) { //limit the speed
                    speed = 85;
                }
                setSpeed(speed);
            }

            @Override
            protected boolean isFinished() {
                return false;
            }
        };
        Scheduler.getInstance().add(cmd);
    }

}


