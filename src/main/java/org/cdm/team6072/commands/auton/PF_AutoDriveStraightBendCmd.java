package org.cdm.team6072.commands.auton;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.PathFinderDriveSys;
import org.cdm.team6072.subsystems.AutoTrajectoryMgr;


public class PF_AutoDriveStraightBendCmd extends Command {

    private PathFinderDriveSys mPathFinderDriveSys;



    public PF_AutoDriveStraightBendCmd() {
        requires(PathFinderDriveSys.getInstance());
        mPathFinderDriveSys = PathFinderDriveSys.getInstance();
        mPathFinderDriveSys.setTrajectory(AutoTrajectoryMgr.getTrajectory(AutoTrajectoryMgr.STRAIGHTBEND));
    }


    protected void execute() {
        mPathFinderDriveSys.advanceTrajectory();
    }


    /**
     * @return Return true when command is completed
     */
    @Override
    protected boolean isFinished() {

        return mPathFinderDriveSys.trajectoryComplete();
    }


}
