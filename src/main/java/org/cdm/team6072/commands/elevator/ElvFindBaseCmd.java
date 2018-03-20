package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;

public class ElvFindBaseCmd extends Command {

    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mTarget;

    private ElevatorSys mElevatorSys;


    public ElvFindBaseCmd() {
        CrashTracker.logMessage("ElvFindBaseCmd:");
        requires(ElevatorSys.getInstance());
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
        mElevatorSys.findBase();
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.findBaseComplete();
    }


    protected void end() {
        CrashTracker.logMessage("MoveElevatorCmd.end");
        //mElevatorSys.stop();
    }

    protected void interrupted() {
        CrashTracker.logMessage("MoveElevatorCmd.interrupted");
        // mElevatorSys.stop();
    }

}

