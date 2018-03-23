package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;

public class ArmMoveToStart extends Command {


    private ArmSys mArmSys;


    public ArmMoveToStart() {
        System.out.println("ArmMoveToStart: ");
        requires(ArmSys.getInstance());
        mArmSys = ArmSys.getInstance();
    }

    @Override
    protected void initialize() {
        mArmSys.moveToStart();
    }

    @Override
    protected void execute() {
        // move is started in init
        mArmSys.moveStatus();
    }

    @Override
    protected boolean isFinished() {
        return mArmSys.moveToStartComplete();
    }


}
