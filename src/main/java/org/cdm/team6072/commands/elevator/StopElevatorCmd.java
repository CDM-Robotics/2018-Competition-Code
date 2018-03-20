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

    /**
     * The elevator move command does a stop on interrupt, which does a hold
     * So this stop command is a no-op at the moment, to avoid sending two hold commands
     */
    protected void execute() {
        System.out.println("StopElevatorCmd: ---------------------------------------");
        ElevatorSys.getInstance().stop();
        System.out.println("StopElevatorCmd end: ---------------------------------------");
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


}
