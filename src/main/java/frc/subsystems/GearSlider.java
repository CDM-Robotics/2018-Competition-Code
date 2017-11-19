//package frc.subsystems;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
//import com.ctre.CANTalon;
import frc.ControlBoard;
import frc.RobotConfig;
import frc.loops.Loop;
import java.lang.Math;
/**
 * Created by Cole on 9/29/17.
 */
/*public class GearSlider {
    // singleton instance
    public static GearSlider instance = new GearSlider();
    public static GearSlider getInstance() {
        return instance;
    }

    // motor controller
    private CANTalon talon;

    private GearSlider() {

        talon = new CANTalon(RobotConfig.SLIDER_TALON);
    }

    private enum State {
        IDLE,
        MOVE_LEFT,
        MOVE_RIGHT
    }

    private State mState = State.IDLE;


    private Loop mLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            stop();
            synchronized (GearSlider.this) {
                mState = State.IDLE;
            }
        }

        @Override
        public void onLoop(double timestamp) {
            synchronized (GearSlider.this) {
                switch (mState) {
                    case IDLE:
                        return;
                    case MOVE_LEFT:

                        return;
                    case MOVE_RIGHT:

                        return;
                    default:
                        return;
                }
            }
        }

        @Override
        public void onStop(double timestamp) {
            stop();
        }
    };


    public void stop() {

    }

    public State handleIdle() {
        return State.IDLE;
    }

    public void slideLeft() {

    }

    public void slideRight() {

    }

    public void setSpeed(double speed) {
        this.talon.changeControlMode(CANTalon.TalonControlMode.Speed);
        this.talon.set(speed);
    }

    public Command slide() {
        return new Command() {

            @Override
            protected void initialize() {
                super.initialize();
            }

            @Override
            protected void execute() {
                double speed = ControlBoard.getInstance().gamepad.getRawAxis(0);
                if (Math.abs(speed) > 85) { //limit the speed
                    speed = 85;
                }
                setSpeed(speed);
            }

            @Override
            protected boolean isFinished() {
                return false;
            }
        };
    }

}
*/