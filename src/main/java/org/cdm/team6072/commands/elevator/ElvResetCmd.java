package org.cdm.team6072.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ElevatorSys;

public class ElvResetCmd extends Command {


    public ElvResetCmd() {
        requires(ElevatorSys.getInstance());
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ElevatorSys.getInstance().setSensorStartPosn();
    }

    @Override
    protected boolean isFinished() {
        //return mArmSys.moveDeltaComplete();
        return true;
    }

}
