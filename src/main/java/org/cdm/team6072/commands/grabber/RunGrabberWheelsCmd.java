package org.cdm.team6072.commands.grabber;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import util.CrashTracker;


public class RunGrabberWheelsCmd  extends Command {


    private IntakeMotorSys mGrabber;
    private IntakeMotorSys.WheelDirn mRunDirn;


    public RunGrabberWheelsCmd(IntakeMotorSys.WheelDirn runDirn) {
        CrashTracker.logMessage("RunGrabberWheelsCmd: direction: " + runDirn);
        requires(IntakeMotorSys.getInstance());
        mRunDirn = runDirn;
    }


    @Override
    protected void initialize() {
        mGrabber = IntakeMotorSys.getInstance();
    }


    @Override
    protected void execute() {
        CrashTracker.logMessage("RunGrabberWheelsCmd.execute");
        mGrabber.runWheels(mRunDirn);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }


    protected void interrupted() {
        CrashTracker.logMessage("RunGrabberWheelsCmd.interrupted");
        mGrabber.stopWheels();
    }


}
