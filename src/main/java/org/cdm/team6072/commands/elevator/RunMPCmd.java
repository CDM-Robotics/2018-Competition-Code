package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.profiles.test.Rot5_Vel1_Dur50;
import org.cdm.team6072.profiles.test.Rot5_Vel2_Dur10;
import org.cdm.team6072.profiles.test.Rot_5_Vel_1;
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
        mElevator.setMPProfile(Rot5_Vel2_Dur10.getInstance());
        mElevator.resetSystemState();
        mElevator.startMotionProfile();
        CrashTracker.logMessage(mElevator.getSubsystem().toString());
    }


    @Override
    protected void execute() {
        //CrashTracker.logMessage("RunMPCmd.execute");
//        mElevator.updateTalonRequiredMPState();
        mElevator.runProfile();

        if (mElevator.isProfileComplete()) {
            mElevator.stop();
            mElevator.resetSystemState();
        }
       // CrashTracker.logMessage(String.valueOf(mElevator.isProfileComplete()));
    }


    @Override
    protected boolean isFinished() {
        return mElevator.isProfileComplete();
    }



}
