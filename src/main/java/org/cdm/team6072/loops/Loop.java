package org.cdm.team6072.loops;

/**
 * Interface for a loop - routine that runs in the code
 * Drive train is an example of loop
 */
public interface Loop {

    public void onStart(double timestamp);
    public void onLoop(double timestamp);
    public void onStop(double timestamp);
}
