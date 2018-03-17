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

    /**
     * https://www.chiefdelphi.com/forums/showthread.php?p=1691279
     *
     * The coordinate system is just standard x, y, and theta. Note that theta must be in radians.
     * The units of x and y don’t matter as long as you are consistent.
     * If you provide coordinates in feet, you must provide velocity and acceleration in ft/s and ft/s^2,
     * and your output will be in feet and ft/s.
     *
     * The way it currently works is like so:
     X+ is forward from where your robot starts.
     X- is backward from where your robot starts.
     Y+ is to the right of your robot where it starts.
     Y- is to the left of your robot where it starts.
     Angle (theta) is your desired robot heading in radians, which you can convert to/from degrees with the r2d and d2r functions provided by Pathfinder.

     Positive headings are going from X+ towards Y+,
     Negative Headings from X+ to Y-.
     As for the actual following of the heading, that depends on where your gyroscope is zero'd to.

     As with anything, these coordinates are useless unless you have the follower code to interpret them.
     X and Y coordinate directions can be flipped by just sending them to different motor outputs, for example

     You can orient the coordinate plane any way you want, as long as you are consistent.
     Since, as I mentioned above, angle is measured counterclockwise from the +x axis,
     a starting waypoint of (0,0,0) for (x,y,theta) will correspond to a robot at the origin facing in the +x direction.

     If you do not like this, and would rather have the robot face in the +y direction,
     all you need to do is start with a waypoint of (0,0,pi/2) instead.
     */

    // waypoint positions are in meters
    // angles in degrees but d2r method converts to radians
    private Waypoint[] points = new Waypoint[] {
            new Waypoint(-4, -1, Pathfinder.d2r(-45)),
            new Waypoint(-2 ,-2, 0),
            new Waypoint(0,0,0)
    };

    private Waypoint[] sinewave = new Waypoint[] {
            new Waypoint(2, 2, Pathfinder.d2r(0)),
            new Waypoint(4, -2, Pathfinder.d2r(0)),
            new Waypoint(6, 2, Pathfinder.d2r(0)),
            new Waypoint(8, -2, Pathfinder.d2r(0)),
    };


    private static Waypoint[] TRAJ_STRAIGHT = new Waypoint[] {
            new Waypoint(0, 0, 0),
            new Waypoint(1.0, 0, 0)
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
        System.out.println("TrajMgr.get: " + trajName);
        Trajectory trajectory = computedTrajs.get(trajName); //Pathfinder.readFromFile(myFile);
        if (trajectory == null) {
            Waypoint[] waypoints = TRAJ_STRAIGHT; //.get(trajName);
            Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_LOW, 0.02, PathFinderDriveSys.MAX_VELOCITY, PathFinderDriveSys.MAX_ACCEL, 60);
            trajectory = Pathfinder.generate(waypoints, config);
            //computedTrajs.put(trajName, trajectory);
        }
        return trajectory;
    }



    // filename extension should be .trah
    private void saveTrajectory(String filename, Trajectory traj) {
        File file = new File(filename);
        Pathfinder.writeToFile(file, traj);
    }



}
