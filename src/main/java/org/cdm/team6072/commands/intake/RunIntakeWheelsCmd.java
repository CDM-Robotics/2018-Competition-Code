package org.cdm.team6072.commands.intake;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import util.CrashTracker;


public class RunIntakeWheelsCmd extends Command {


    private IntakeMotorSys mGrabber;
    private IntakeMotorSys.WheelDirn mRunDirn;
    private double mSpeed;


    public RunIntakeWheelsCmd(IntakeMotorSys.WheelDirn runDirn, double speed) {
        CrashTracker.logMessage("RunIntakeWheelsCmd: direction: " + runDirn);
        requires(IntakeMotorSys.getInstance());
        mRunDirn = runDirn;
        mSpeed = speed;
    }


    @Override
    protected void initialize() {
        mGrabber = IntakeMotorSys.getInstance();
    }


    @Override
    protected void execute() {
        mGrabber.runWheels(mRunDirn, mSpeed);
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
