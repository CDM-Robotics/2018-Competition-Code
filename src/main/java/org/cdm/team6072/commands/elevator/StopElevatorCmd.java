package org.cdm.team6072.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Elevator;

public class StopElevatorCmd extends Command {

    public StopElevatorCmd() {
        requires(Elevator.getInstance());
    }

    protected void execute() {
        Elevator.getInstance().getMotorController().set(ControlMode.PercentOutput, 0);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
