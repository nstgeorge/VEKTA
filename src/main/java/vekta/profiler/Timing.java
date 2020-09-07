package vekta.profiler;

import java.io.Serializable;

public class Timing implements Serializable {
	private final String descriptor;
	private final long timestamp;

	public Timing(String descriptor, long timestamp) {
		this.descriptor = descriptor;
		this.timestamp = timestamp;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
