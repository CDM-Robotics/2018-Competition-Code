package org.cdm.team6072.commands.elevator;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;

public class ElvMotionMagicCmd extends Command {

    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mTarget;

    private ElevatorSys mElevatorSys;


    public ElvMotionMagicCmd(ElevatorSys.Direction dir, double target) {
        CrashTracker.logMessage("ElvMotionMagicCmd: direction: " + dir);
        requires(ElevatorSys.getInstance());
        mDirection = dir;
        mTarget = target;
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
        mElevatorSys.initForMagicMove();
    }


    @Override
    protected void execute() {
        // only want to send the move command to elevator once
        mElevatorSys.magicMove(mDirection, mTarget);
    }

    @Override
    protected boolean isFinished() {
        return false;
        //return mElevatorSys.magicMoveComplete();
    }


    protected void end() {
        CrashTracker.logMessage("ElvMotionMagicCmd.end");
        //mElevatorSys.stop();
    }

    protected void interrupted() {
        CrashTracker.logMessage("ElvMotionMagicCmd.interrupted");
        //mElevatorSys.stop();
    }

}
