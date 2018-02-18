package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;

public class ElvMoveToBottomCmd extends Command {

    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.2;
    private double mTarget;

    private ElevatorSys mElevatorSys;


    public ElvMoveToBottomCmd() {
        CrashTracker.logMessage("ElvMoveToBottomCmd: ");
        requires(ElevatorSys.getInstance());
        mDirection = ElevatorSys.Direction.Down;
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
        mElevatorSys.initBotSwitch();
        mElevatorSys.initForMove();
    }

    @Override
    protected void execute() {
        mElevatorSys.move(mDirection, mSpeed);
    }

    @Override
    protected boolean isFinished() {
        if (mElevatorSys.botSwitchSet()) {
            mElevatorSys.stop();
            mElevatorSys.setAtBottom();
        }
        return false;
    }


    protected void end() {
        CrashTracker.logMessage("ElvMoveToBottomCmd.end");
        mElevatorSys.stop();
    }

    protected void interrupted() {
        CrashTracker.logMessage("ElvMoveToBottomCmd.interrupted");
        mElevatorSys.stop();
    }


}
