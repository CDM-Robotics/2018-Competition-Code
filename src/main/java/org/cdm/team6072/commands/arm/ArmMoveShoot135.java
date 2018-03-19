package org.cdm.team6072.commands.arm;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.ArmSys;

public class ArmMoveShoot135   extends Command {


    private ArmSys mArmSys;


    public ArmMoveShoot135() {
        System.out.println("ArmMoveShoot135: ");
        requires(ArmSys.getInstance());
        mArmSys = ArmSys.getInstance();
    }

    @Override
    protected void initialize() {
        mArmSys.moveToShoot135();
    }

    @Override
    protected void execute() {
        // move is Shoot135ed in init
        mArmSys.moveStatus();
    }

    @Override
    protected boolean isFinished() {
        return mArmSys.moveToShoot135Complete();
    }
    
}
