package vekta.profiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Profiler implements Serializable {
	private final List<Timing> timings = new ArrayList<>();

	public Profiler() {
	}

	public void addTimeStamp(String descriptor) {
		timings.add(new Timing(descriptor, System.currentTimeMillis()));
	}

	public List<Timing> getTimings() {
		return timings;
	}

	public void reset() {
		timings.clear();
	}
}
