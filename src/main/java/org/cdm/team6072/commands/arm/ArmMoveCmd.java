package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;
import util.CrashTracker;

public class ArmMoveCmd  extends Command {

    private ArmSys.Direction mDirection;
    private double mSpeed = 0.5;
    private double mTarget;

    private ArmSys mArmSys;


    public ArmMoveCmd(ArmSys.Direction dir, double speed) {
        CrashTracker.logMessage("ArmMoveCmd: direction: " + dir);
        requires(ArmSys.getInstance());
        mDirection = dir;
        mSpeed = speed;
    }

    @Override
    protected void initialize() {
        mArmSys = ArmSys.getInstance();
        mArmSys.initForMove();
    }

    @Override
    protected void execute() {
        mArmSys.move(mDirection, mSpeed);
    }

    @Override
    protected boolean isFinished() {
        //return mArmSys.moveDeltaComplete();
        return false;
    }


    protected void end() {
        CrashTracker.logMessage("ArmMoveCmd.end");
        //mArmSys.stop();
    }

    protected void interrupted() {
        CrashTracker.logMessage("ArmMoveCmd.interrupted");
        //mArmSys.stop();
    }

}
