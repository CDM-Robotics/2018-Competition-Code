package org.cdm.team6072.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Elevator;
import util.CrashTracker;



public class MoveElevatorCmd extends Command {

    private Elevator.Direction mDirection;
    private double mSpeed = 0.5;
    private double mTarget;

    private Elevator mElevator;


    public MoveElevatorCmd(Elevator.Direction dir, double target) {
        CrashTracker.logMessage("MoveElevatorCmd: direction: " + dir);
        requires(Elevator.getInstance());
        mDirection = dir;
        mTarget = target;
    }

    @Override
    protected void initialize() {
        mElevator = Elevator.getInstance();
    }

    @Override
    protected void execute() {
        CrashTracker.logMessage("MoveElevatorCmd.execute");
        mElevator.move(mDirection, mSpeed);
    }

    @Override
    protected boolean isFinished() {
        return mElevator.targetReached();
    }


    protected void end() {
        CrashTracker.logMessage("MoveElevatorCmd.end");
        mElevator.stop();
    }

    protected void interrupted() {
        CrashTracker.logMessage("MoveElevatorCmd.interrupted");
        mElevator.stop();
    }

}
