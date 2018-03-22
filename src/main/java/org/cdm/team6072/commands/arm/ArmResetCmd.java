package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;


public class ArmResetCmd extends Command {


    public ArmResetCmd() {
        requires(ArmSys.getInstance());
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        ArmSys.getInstance().setSensorStartPosn();
    }

    @Override
    protected boolean isFinished() {
        //return mArmSys.moveDeltaComplete();
        return true;
    }


}
