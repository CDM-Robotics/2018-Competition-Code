package org.cdm.team6072.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.cdm.team6072.subsystems.DriveSys;


import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;



public class TestDriveForward extends Command {

    // 5 seconds - assumes execute is called every 20 mSec
    private static int KTIMETORUN = 50 * 8;


    public TestDriveForward() {
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
            byte updateHz = 64;
            mAhrs = new AHRS(SPI.Port.kMXP, 100000, updateHz);
            //mAhrs.reset();
            mAhrs.enableLogging(true);
        }
        catch (Exception ex) {
            System.out.println("*********************  TestDriveForward: Ex initializing: " + ex.getMessage());
        }
    }


    @Override
    protected void execute() {
        mCounter++;
        double angle = mAhrs.getAngle();
        double yaw = mAhrs.getYaw();
        boolean isMoving = mAhrs.isMoving();
        boolean isConnected = mAhrs.isConnected();
        if (mCounter % 5 == 0) {
            System.out.println("TestDriveFwd.execute: angle = " + angle + "  yaw: " + yaw + "  isMoving: " + isMoving + "  isConn: " + isConnected);
        }
        if (mCounter == 20) {
            mAhrs.enableLogging(false);
        }
        mDriveSys.arcadeDrive(-0.6,0);
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
