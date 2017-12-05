package org.cdm.team6072.subsystems;
import org.cdm.team6072.RobotConfig;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Spark;



public class Climber {
    private static Climber mInstance;
    double climb_speed = 50;

    public static Climber getInstance() {

        if (mInstance == null) {
            mInstance = new Climber();
        }
        return mInstance;
    }

    private Spark motor;

    private Climber() {
        motor = new Spark(RobotConfig.CLIMBER_MOTOR);
    }

    /**
     * Create a command to run the climber.
     * Called from the ControlBoard:
     *          Command climb = Climber.getInstance().climbUp();
     *          gamepadButtons[ControlMappings.CLIMB_BTN].whileHeld(climb);
     * TODO: the command should indicate that it requires the Climber subsystem
     * @return  Command
     */
    public Command climbUp() {
        System.out.println("6072 initiating climb");
        return new Command() {
            @Override
            protected void initialize() {

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
