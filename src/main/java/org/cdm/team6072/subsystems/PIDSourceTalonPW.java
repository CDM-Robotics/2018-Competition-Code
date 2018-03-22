package org.cdm.team6072.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;



public class PIDSourceTalonPW implements PIDSource{


    private WPI_TalonSRX mTalon;

    public PIDSourceTalonPW(WPI_TalonSRX talon) {
        mTalon = talon;
    }


    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {

    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    /**
     * Return the PulseWidth posn of the selected talon
     * @return
     */
    @Override
    public double pidGet() {
        return mTalon.getSensorCollection().getPulseWidthPosition();
    }


}
