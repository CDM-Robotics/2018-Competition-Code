package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;

public class ElvInitMotionMagicCmd extends Command {

    private ElevatorSys mElevatorSys;


    public ElvInitMotionMagicCmd() {
        CrashTracker.logMessage("ElvInitMotionMagicCmd: ");
        requires(ElevatorSys.getInstance());
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
    }

    @Override
    protected void execute() {
        mElevatorSys.initForMagicMove();
    }

    @Override
    protected boolean isFinished() {
        return true;
    }


    protected void end() {
        CrashTracker.logMessage("ElvInitMotionMagicCmd.end");
    }

    protected void interrupted() {
        CrashTracker.logMessage("ElvInitMotionMagicCmd.interrupted");
    }
}
