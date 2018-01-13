package org.cdm.team6072.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Elevator;
import util.CrashTracker;

public class MoveElevatorCmd extends Command {
    private boolean down;
    public MoveElevatorCmd(boolean down) {
        CrashTracker.logMessage("Move Elevator Cmd");
        requires(Elevator.getInstance());

        this.down = down;
    }

    @Override
    protected void execute() {
        if (this.down) {
            Elevator.getInstance().getMotorController().setInverted(true);
        } else {
            Elevator.getInstance().getMotorController().setInverted(false);
        }
        CrashTracker.logMessage("execute move elevator cmd");
        Elevator.getInstance().getMotorController().set(ControlMode.PercentOutput, 0.5);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


    protected void end() {
        CrashTracker.logMessage("ended");
        Elevator.getInstance().getMotorController().setNeutralMode(NeutralMode.Brake);
        Elevator.getInstance().getMotorController().set(ControlMode.PercentOutput, 0);
        Elevator.getInstance().getMotorController().setInverted(false);
    }

    protected void interrupted() {
        CrashTracker.logMessage("interrupted");
    }
}
