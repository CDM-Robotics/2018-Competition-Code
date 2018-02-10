package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;

public class StopElevatorCmd extends Command {


    /**
     * If StopElevatorCmd is called when MoveElevatorCmd is running, it will cause an interrupt
     * on move which will cause it to stop anyway
     */
    public StopElevatorCmd() {
        requires(ElevatorSys.getInstance());
    }

    protected void execute() {
        ElevatorSys.getInstance().stop();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
