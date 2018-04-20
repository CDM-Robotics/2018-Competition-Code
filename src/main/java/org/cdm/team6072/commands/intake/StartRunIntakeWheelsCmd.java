package org.cdm.team6072.commands.intake;


import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.IntakeMotorSys;
import util.CrashTracker;


public class StartRunIntakeWheelsCmd extends Command {


    private IntakeMotorSys mGrabber;
    private IntakeMotorSys.WheelDirn mRunDirn;
    private double mSpeed;


    public StartRunIntakeWheelsCmd(IntakeMotorSys.WheelDirn runDirn, double speed) {
        CrashTracker.logMessage("StartRunIntakeWheelsCmd: direction: " + runDirn);
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
        return true;
    }


    protected void interrupted() {
        CrashTracker.logMessage("RunIntakeWheelsCmd.interrupted");
        mGrabber.runWheelsInLo();
    }


}
