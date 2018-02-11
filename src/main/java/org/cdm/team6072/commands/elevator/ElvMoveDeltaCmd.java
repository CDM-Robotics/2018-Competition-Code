package org.cdm.team6072.commands.elevator;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;

public class ElvMoveDeltaCmd extends Command {


    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mDelta;

    private ElevatorSys mElevatorSys;


    public ElvMoveDeltaCmd(ElevatorSys.Direction dir, double speed) {
        CrashTracker.logMessage("ElvMoveDeltaCmd: direction: " + dir);
        requires(ElevatorSys.getInstance());
        mDirection = dir;
        mDelta = 100;
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
        mElevatorSys.initForMove();
    }

    @Override
    protected void execute() {
        mElevatorSys.moveDelta(mDirection, mSpeed, mDelta);
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.moveDeltaComplete();
    }


    protected void end() {
        CrashTracker.logMessage("ElvMoveDeltaCmd.end");
        mElevatorSys.stop();
    }

    protected void interrupted() {
        CrashTracker.logMessage("ElvMoveDeltaCmd.interrupted");
        mElevatorSys.stop();
    }

}
