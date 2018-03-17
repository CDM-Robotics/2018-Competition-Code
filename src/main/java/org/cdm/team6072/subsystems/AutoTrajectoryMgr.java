package org.cdm.team6072.subsystems;


import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.util.HashMap;

/**
 * Manage trajectories for autonomous
 */
public class AutoTrajectoryMgr {

    public static String STRAIGHT = "Straight";
    public static String STRAIGHTBEND = "StraightBend";


    private static Waypoint[] TRAJ_STRAIGHT = new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(1.0, 0, 45)
    };

    private static Waypoint[] TRAJ_STRAIGHTBEND = new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(1, 0, 0),
            new Waypoint(2, 1,90)
    };

    private static HashMap<String, Waypoint[]> baseTrajs = new HashMap<String, Waypoint[]>();

    private static HashMap<String, Trajectory> computedTrajs = new HashMap<String, Trajectory>();


    private AutoTrajectoryMgr mTrajMgr;

    public AutoTrajectoryMgr getInstance() {
        if (mTrajMgr == null) {
            mTrajMgr = new AutoTrajectoryMgr();
        }
        return mTrajMgr;
    }


    private AutoTrajectoryMgr() {
        baseTrajs.put(STRAIGHT, TRAJ_STRAIGHT);
        baseTrajs.put(STRAIGHTBEND, TRAJ_STRAIGHTBEND);
    }

    /**
     * Create a Trajectory Configuration
     *  fit                   The fit method to use
     *  samples               How many samples to use to refine the path (higher = smoother, lower = faster)
     *  dt                    The time delta between points (in seconds)
     *  max_velocity          The maximum velocity the body is capable of travelling at (in meters per second)
     *  max_acceleration      The maximum acceleration to use (in meters per second per second)
     *  max_jerk              The maximum jerk (acceleration per second) to use
     * @return
     */

    /**
     * If necessary, read trajectory from a file and store in the HashMap, then return
     * @param trajName
     * @return
     */
    public static Trajectory getTrajectory(String trajName) {

        //File myFile = new File("/home/lvuser/profiles/testTraj.traj");
        Trajectory trajectory = computedTrajs.get(trajName); //Pathfinder.readFromFile(myFile);
        if (trajectory == null) {
            Waypoint[] waypoints = baseTrajs.get(trajName);
            Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.02, PathFinderDriveSys.MAX_VELOCITY, PathFinderDriveSys.MAX_ACCEL, 60);
            trajectory = Pathfinder.generate(waypoints, config);
        }
        return trajectory;
    }



    // filename extension should be .trah
    private void saveTrajectory(String filename, Trajectory traj) {
        File file = new File(filename);
        Pathfinder.writeToFile(file, traj);
    }



}
