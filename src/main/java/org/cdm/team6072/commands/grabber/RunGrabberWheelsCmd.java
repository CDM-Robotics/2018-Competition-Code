package org.cdm.team6072.commands.grabber;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.Grabber;
import util.CrashTracker;


public class RunGrabberWheelsCmd  extends Command {


    private Grabber mGrabber;
    private Grabber.WheelDirn mRunDirn;


    public RunGrabberWheelsCmd(Grabber.WheelDirn runDirn) {
        CrashTracker.logMessage("RunGrabberWheelsCmd: direction: " + runDirn);
        requires(Grabber.getInstance());
        mRunDirn = runDirn;
    }


    @Override
    protected void initialize() {
        mGrabber = Grabber.getInstance();
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
