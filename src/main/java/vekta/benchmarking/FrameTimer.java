package vekta.benchmarking;

import java.io.Serializable;
import java.util.ArrayList;

public class FrameTimer implements Serializable {
    private Long frameStart;
    private ArrayList<Timing> timings;

    public FrameTimer() {
        clearTimings();
    }

    public void addTimeStamp(String descriptor, Long timestamp) {
        timings.add(new Timing(descriptor, timestamp));
    }

    public ArrayList<Timing> getTimings() {
        return timings;
    }

    public void clearTimings() {
        timings = new ArrayList<>();
        frameStart = 0L;
    }
}
