package vekta.benchmarking;

import java.io.Serializable;
import java.util.ArrayList;

public class FrameTimer implements Serializable {
    private Long frameStart;
    private ArrayList<Timing> timings;

    public FrameTimer() {
        clearTimings();
    }

    public void addTimeStamp(String descriptor) {
        timings.add(new Timing(descriptor, System.currentTimeMillis()));
    }

    public ArrayList<Timing> getTimings() {
        return timings;
    }

    public void clearTimings() {
        timings = new ArrayList<>();
        frameStart = 0L;
    }
}
