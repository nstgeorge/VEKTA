package vekta.benchmarking;

import java.io.Serializable;

public class Timing implements Serializable {
    private String descriptor;
    private Long timestamp;
    public Timing(String descriptor, Long timestamp) {
        this.descriptor = descriptor;
        this.timestamp = timestamp;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
