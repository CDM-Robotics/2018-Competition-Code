package org.cdm.team6072.commands.auton;

import edu.wpi.first.wpilibj.command.Command;

import org.cdm.team6072.subsystems.AutoDriveSys;
import org.cdm.team6072.subsystems.AutoTrajectoryMgr;


public class AutoDriveStraightCmd extends Command {

    private AutoDriveSys mAutoDriveSys;



    public AutoDriveStraightCmd() {
        requires(AutoDriveSys.getInstance());
        mAutoDriveSys = AutoDriveSys.getInstance();
        mAutoDriveSys.setTrajectory(AutoTrajectoryMgr.getTrajectory(AutoTrajectoryMgr.STRAIGHT));
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
