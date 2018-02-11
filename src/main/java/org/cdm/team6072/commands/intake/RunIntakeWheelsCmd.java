package org.cdm.team6072.commands.intake;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import util.CrashTracker;


public class RunIntakeWheelsCmd extends Command {


    private IntakeMotorSys mGrabber;
    private IntakeMotorSys.WheelDirn mRunDirn;


    public RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn runDirn) {
        CrashTracker.logMessage("RunIntakeWheelsCmd: direction: " + runDirn);
        requires(IntakeMotorSys.getInstance());
        mRunDirn = runDirn;
    }


    @Override
    protected void initialize() {
        mGrabber = IntakeMotorSys.getInstance();
    }


    @Override
    protected void execute() {
        CrashTracker.logMessage("RunIntakeWheelsCmd.execute");
        mGrabber.runWheels(mRunDirn);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


    protected void interrupted() {
        CrashTracker.logMessage("RunIntakeWheelsCmd.interrupted");
        mGrabber.stopWheels();
    }


}
