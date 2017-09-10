package frc;

public class Robot {

    public static void main(String[] args) {
        Core core = Core.getInstance();
        core.log("Testing logging capability.");
        core.warn("Testing warning capability.");
    }

}