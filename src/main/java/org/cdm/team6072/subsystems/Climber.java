package org.cdm.team6072.subsystems;
/*import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Spark;
import ControlBoard;
import RobotConfig;

public class Climber {
    public static Climber instance = new Climber();
    double climb_speed = 50;

    public static Climber getInstance() {
        return instance;
    }

    private Spark motor;

    private Climber() {
        motor = new Spark(RobotConfig.CLIMBER_MOTOR);

    }

    public Command initiateClimb() {
        return new Command() {
            @Override
            protected void initialize() {
                super.initialize();
            }

            @Override
            protected void execute() {
                motor.setSpeed(climb_speed);
            }

            @Override
            protected boolean isFinished() {
                return false;
            }

            @Override
            protected void end() {
                motor.stopMotor();
            }
        };
    }

}
*/