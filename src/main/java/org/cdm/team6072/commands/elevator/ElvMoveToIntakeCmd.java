package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;



public class ElvMoveToIntakeCmd extends Command {


    private ElevatorSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mTarget;

    private ElevatorSys mElevatorSys;


    public ElvMoveToIntakeCmd() {
        CrashTracker.logMessage("ElvMoveToIntakeCmd  -------------");
        requires(ElevatorSys.getInstance());
        mElevatorSys = ElevatorSys.getInstance();
    }

    @Override
    protected void initialize() {
        // actually start the move here
        mElevatorSys.moveToIntake();
    }

    @Override
    protected void execute() {
        // dont do anything in exec because move has been started
        mElevatorSys.magicMoveStatus();
    }

    @Override
    protected boolean isFinished() {
        return mElevatorSys.moveToIntakeComplete();
    }


}
