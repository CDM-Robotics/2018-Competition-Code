package org.cdm.team6072.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Elevator;
import util.CrashTracker;

public class MoveElevatorCmd extends Command {

    public MoveElevatorCmd() {
        CrashTracker.logMessage("Move Elevator Cmd");
        requires(Elevator.getInstance());
    }

    @Override
    protected void execute() {
        super.execute();
        Elevator.getInstance().getMotorController().set(ControlMode.MotionProfile, 3);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
