package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.profiles.test.Rot5_Vel2_Dur10;
import org.cdm.team6072.subsystems.ElevatorSys;
import util.CrashTracker;

/**
 * Run the motion profile on the elevator
 */
public class RunMPCmd  extends Command {


    private ElevatorSys mElevatorSys;


    public RunMPCmd() {
        requires(ElevatorSys.getInstance());
    }


    @Override
    protected void initialize() {
        CrashTracker.logMessage("RunMPCmd.initialize");
        mElevatorSys = ElevatorSys.getInstance();
        mElevatorSys.setMPProfile(Rot5_Vel2_Dur10.getInstance());
        mElevatorSys.resetSystemState();
        mElevatorSys.startMotionProfile();
        CrashTracker.logMessage(mElevatorSys.getSubsystem().toString());
    }


    @Override
    protected void execute() {
        //CrashTracker.logMessage("RunMPCmd.execute");
//        mElevatorSys.updateTalonRequiredMPState();
        mElevatorSys.runProfile();

        if (mElevatorSys.isProfileComplete()) {
            mElevatorSys.stop();
            mElevatorSys.resetSystemState();
        }
       // CrashTracker.logMessage(String.valueOf(mElevatorSys.isProfileComplete()));
    }


    @Override
    protected boolean isFinished() {
        return mElevatorSys.isProfileComplete();
    }



}
