package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;


public class ElvMoveToScaleHiCmd extends Command {
    

    private ElevatorSys mElevatorSys;


    public ElvMoveToScaleHiCmd() {
        CrashTracker.logMessage("ElvMoveToScaleHiCmd: ");
        requires(ElevatorSys.getInstance());
        mElevatorSys = ElevatorSys.getInstance();
    }

    @Override
    protected void initialize() {
        mElevatorSys.moveToScaleHi();
    }

    @Override
    protected void execute() {
        // move is started in init
        mElevatorSys.magicMoveStatus();
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.moveToScaleHiComplete();
    }

}
