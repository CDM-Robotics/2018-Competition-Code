package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.cdm.team6072.RobotConfig;
import org.cdm.team6072.commands.elevator.MoveElevatorCmd;
import util.CrashTracker;

public class Elevator extends Subsystem {
    private static Elevator mInstance;

    private WPI_TalonSRX mainTalon;

    public static Elevator getInstance() {
        if (mInstance == null) {
            mInstance = new Elevator();
        }
        return mInstance;
    }

    private Elevator() {
        CrashTracker.logMessage("Elevator Subsystem initializing");
        try {
            this.mainTalon = new WPI_TalonSRX(RobotConfig.ELEVATOR_TALON);
            this.mainTalon.set(ControlMode.MotionProfile, ControlMode.MotionProfile.value);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
    }

    @Override
    protected void initDefaultCommand() {

    }

    public void resetSystemState() {
        this.mainTalon.setInverted(false);
    }

    /*public Command moveUp() {
        this.mainTalon.setInverted(false);
        Command cmd = new MoveElevatorCmd();
        return cmd;
    }

    public Command moveDown() {
        this.mainTalon.setInverted(true);
        Command cmd = new MoveElevatorCmd();
        return cmd;
    }*/

    //public Command stop() {
        //this.mainTalon.set(0);
    //}

    public TalonSRX getMotorController() {
        return this.mainTalon;
    }
}
