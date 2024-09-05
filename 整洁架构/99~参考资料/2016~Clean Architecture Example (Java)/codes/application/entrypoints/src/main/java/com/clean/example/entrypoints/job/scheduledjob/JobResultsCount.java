package com.clean.example.entrypoints.job.scheduledjob;

public class JobResultsCount {

    private int numberOfSuccesses;
    private int numberOfFailures;

    public void success() {
        numberOfSuccesses++;
    }

    public void failure() {
        numberOfFailures++;
    }

    public int getNumberOfSuccesses() {
        return numberOfSuccesses;
    }

    public int getNumberOfFailures() {
        return numberOfFailures;
    }
}
