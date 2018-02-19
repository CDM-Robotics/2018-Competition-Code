package org.cdm.team6072.commands.elevator;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;

public class ElvMotionMagicCmd extends Command {

    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mRotations;

    private ElevatorSys mElevatorSys;


    public ElvMotionMagicCmd(ElevatorSys.Direction dir, double rotations) {
        CrashTracker.logMessage("ElvMotionMagicCmd: direction: " + dir);
        requires(ElevatorSys.getInstance());
        mDirection = dir;
        mRotations = rotations;
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
        mElevatorSys.initForMagicMove();
        // only want to send the move command to elevator once
        mElevatorSys.magicMove(mDirection, mRotations * 4096);
    }


    @Override
    protected void execute() {
        // only want to send the move command to elevator once
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.magicMoveComplete();
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
