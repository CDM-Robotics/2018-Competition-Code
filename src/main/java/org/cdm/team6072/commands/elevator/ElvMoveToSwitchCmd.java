package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;


public class ElvMoveToSwitchCmd extends Command {


    private ElevatorSys mElevatorSys;


    public ElvMoveToSwitchCmd() {
        CrashTracker.logMessage("ElvMoveToSwitchCmd: ");
        requires(ElevatorSys.getInstance());
        mElevatorSys = ElevatorSys.getInstance();
    }

    @Override
    protected void initialize() {
        mElevatorSys.moveToSwitch();
    }

    @Override
    protected void execute() {
        // move is started in init
        mElevatorSys.magicMoveStatus();
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.moveToSwitchComplete();
    }

}
