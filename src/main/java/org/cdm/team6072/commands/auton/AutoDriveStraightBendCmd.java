package org.cdm.team6072.commands.auton;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.AutoDriveSys;
import org.cdm.team6072.subsystems.AutoTrajectoryMgr;



public class AutoDriveStraightBendCmd extends Command {

    private AutoDriveSys mAutoDriveSys;



    public AutoDriveStraightBendCmd() {
        requires(AutoDriveSys.getInstance());
        mAutoDriveSys = AutoDriveSys.getInstance();
        mAutoDriveSys.setTrajectory(AutoTrajectoryMgr.getTrajectory(AutoTrajectoryMgr.STRAIGHTBEND));
    }


    protected void execute() {
        mAutoDriveSys.advanceTrajectory();
    }


    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {

        return mAutoDriveSys.trajectoryComplete();
    }


}
