package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;


public class ElvMoveToSwitchLoCmd extends Command {


    private ElevatorSys mElevatorSys;


    public ElvMoveToSwitchLoCmd() {
        CrashTracker.logMessage("ElvMoveToSwitchLoCmd: ");
        requires(ElevatorSys.getInstance());
        mElevatorSys = ElevatorSys.getInstance();
    }

    @Override
    protected void initialize() {
        mElevatorSys.moveToSwitchLo();
    }

    @Override
    protected void execute() {
        // move is started in init
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.moveToSwitchLoComplete();
    }

}
