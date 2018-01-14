package org.cdm.team6072.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Elevator;

public class StopElevatorCmd extends Command {


    /**
     * If StopElevatorCmd is called when MoveElevatorCmd is running, it will cause an interrupt
     * on move which will cause it to stop anyway
     */
    public StopElevatorCmd() {
        requires(Elevator.getInstance());
    }

    protected void execute() {
        Elevator.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
