package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;


public class ElvMoveToScaleCmd extends Command {


    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mTarget;

    private ElevatorSys mElevatorSys;


    public ElvMoveToScaleCmd(ElevatorSys.Direction dir, double target) {
        CrashTracker.logMessage("ElvMoveToScaleCmd: direction: " + dir);
        requires(ElevatorSys.getInstance());;
    }

    @Override
    protected void initialize() {
        mElevatorSys = ElevatorSys.getInstance();
    }

    @Override
    protected void execute() {
        CrashTracker.logMessage("ElvMoveToScaleCmd.execute");
        mElevatorSys.move(mDirection, mSpeed);
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.moveDeltaComplete();
    }

}
