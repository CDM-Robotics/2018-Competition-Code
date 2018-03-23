package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;

public class StopElevatorCmd extends Command {

    ElevatorSys mElvSys;

    /**
     * If StopElevatorCmd is called when MoveElevatorCmd is running, it will cause an interrupt
     * on MoveElevatorCmd which will cause it to stop anyway
     */
    public StopElevatorCmd() {
        requires(ElevatorSys.getInstance());
        mElvSys = ElevatorSys.getInstance();
    }


    @Override
    protected void initialize() {
        System.out.println("StopElevatorCmd:  ------------");
        mElvSys.initStop();
    }


    /**
     * The elevator move command does a stop on interrupt, which does a hold
     * So this stop command is a no-op at the moment, to avoid sending two hold commands
     */
    @Override
    protected void execute() {
        mElvSys.stopping();
    }

    @Override
    protected boolean isFinished() {
        return mElvSys.stopComplete();
    }


}
