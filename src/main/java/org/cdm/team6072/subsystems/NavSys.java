package org.cdm.team6072.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;

public class NavSys implements PIDOutput {

    /* The following PID Controller coefficients will need to be tuned */
    /* to match the dynamics of your drive system.  Note that the      */
    /* SmartDashboard in Test mode has support for helping you tune    */
    /* controllers by displaying a form where you can enter new P, I,  */
    /* and D constants and test the mechanism.                         */
    private double kP = 0.03f;
    private double kI = 0.00f;
    private double kD = 0.00f;
    private double kF = 0.00f;

    /* This tuning parameter indicates how close to "on target" the    */
    /* PID Controller will attempt to get.                             */
    private double kToleranceDegrees = 2.0f;

    private AHRS navX;
    private PIDController turnController;

    public static NavSys mInstance;


    public static NavSys getInstance() {
        if (mInstance == null) {
            mInstance = new NavSys();
        }
        return mInstance;
    }

    private NavSys() {
        // communicative with the naxX MXP
        try {
            this.navX = new AHRS(SPI.Port.kMXP);
            this.navX.zeroYaw();


            this.turnController = new PIDController(kP, kI, kD, kF, this.navX, (PIDOutput) this);

        } catch (Exception ex ) {
            System.out.println("NavSys: error in constructor");
        }

        this.turnController.setInputRange(-180.0f,  180.0f);
        this.turnController.setOutputRange(-1.0, 1.0);
        this.turnController.setAbsoluteTolerance(kToleranceDegrees);
        this.turnController.setContinuous(true);
        this.turnController.disable();
    }

    @Override
    public void pidWrite(double output) {

    }

    // getting the navX direction in degrees
    public double getHeading() {
        return this.navX.getYaw();
    }
}
