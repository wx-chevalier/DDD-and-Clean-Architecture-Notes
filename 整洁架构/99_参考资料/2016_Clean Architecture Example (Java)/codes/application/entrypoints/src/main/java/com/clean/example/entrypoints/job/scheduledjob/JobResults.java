package com.clean.example.entrypoints.job.scheduledjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobResults {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobResults.class);

    public JobResultsCount createJobResultsCount() {
        return new JobResultsCount();
    }

    public void recordJobResults(ScheduledJob job, JobResultsCount jobResultsCount) {
        LOGGER.info("{} finished, recording results: {} successes, {} failures", job.getName(), jobResultsCount.getNumberOfSuccesses(), jobResultsCount.getNumberOfFailures());
        // do nothing for now; eventually this could save results into a database, or send them to another app, or anything really
    }
}
