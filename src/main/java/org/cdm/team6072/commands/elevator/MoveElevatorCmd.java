package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;



public class MoveElevatorCmd extends Command {

    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mTarget;

    private ElevatorSys mElevatorSys;


    public MoveElevatorCmd(ElevatorSys.Direction dir, double target) {
        CrashTracker.logMessage("MoveElevatorCmd: direction: " + dir);
        requires(ElevatorSys.getInstance());
        mDirection = dir;
        mTarget = target;
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
        mElevatorSys.initForMove();
    }

    @Override
    protected void execute() {
        mElevatorSys.move(mDirection, mSpeed);
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.targetReached();
    }


    protected void end() {
        CrashTracker.logMessage("MoveElevatorCmd.end");
        mElevatorSys.stop();
    }

    protected void interrupted() {
        CrashTracker.logMessage("MoveElevatorCmd.interrupted");
        mElevatorSys.stop();
    }

}
