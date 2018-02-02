package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.autonomous.profiles.DrivetrainProfile;
import org.cdm.team6072.subsystems.Elevator;
import util.CrashTracker;

/**
 * Run the motion profile on the elevator
 */
public class RunMPCmd  extends Command {


    private Elevator mElevator;


    public RunMPCmd() {
        requires(Elevator.getInstance());
    }


    @Override
    protected void initialize() {
        CrashTracker.logMessage("RunMPCmd.initialize");
        mElevator = Elevator.getInstance();
        mElevator.setMPProfile(DrivetrainProfile.getInstance());
        mElevator.startMotionProfile();
    }


    @Override
    protected void execute() {
        //CrashTracker.logMessage("RunMPCmd.execute");
        mElevator.updateTalonRequiredMPState();
        mElevator.runProfile();
    }


    @Override
    protected boolean isFinished() {
        return mElevator.isProfileComplete();
    }



}
