package com.clean.example.entrypoints.job.scheduledjob;

import java.util.concurrent.TimeUnit;

public interface ScheduledJob extends Runnable {

    String getName();

    long getInitialDelay();

    long getPeriod();

    TimeUnit getTimeUnit();

}
