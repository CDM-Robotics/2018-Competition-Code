package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.DriveSys;


import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;


public class TestDriveGyro extends Command {

    // 5 seconds - assumes execute is called every 20 mSec
    private static int KTIMETORUN = 50 * 5;


    public TestDriveGyro() {
        requires(DriveSys.getInstance());
    }


    private int mCounter = 0;

    private DriveSys mDriveSys;
    private AHRS mAhrs;


    @Override
    protected void initialize() {
        try {
            mCounter = 0;
            mDriveSys = DriveSys.getInstance();
            mAhrs = new AHRS(SPI.Port.kMXP);
            mAhrs.reset();
        }
        catch (Exception ex) {
            System.out.println("*********************  TestDriveGyro: Ex initializing: " + ex.getMessage());
        }
    }


    @Override
    protected void execute() {
        mCounter++;
        double angle = mAhrs.getAngle();
        if (mCounter % 5 == 0) {
            System.out.println("TestDriveGyro.execute: angle = " + angle);
        }
        mDriveSys.arcadeDrive(0.2, -angle);
    }

    @Override
    protected boolean isFinished() {
        if (mCounter == KTIMETORUN) {
            mDriveSys.arcadeDrive(0,0);
            return true;
        }
        return false;
    }

}
