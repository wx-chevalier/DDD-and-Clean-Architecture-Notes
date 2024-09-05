package com.clean.example.entrypoints.job.scheduledjob;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JobResultsCountTest {

    @Test
    public void recordsSuccesses() throws Exception {
        JobResultsCount jobResultsCount = new JobResultsCount();

        jobResultsCount.success();
        jobResultsCount.success();

        assertThat(jobResultsCount.getNumberOfSuccesses()).isEqualTo(2);
    }

    @Test
    public void recordsFailures() throws Exception {
        JobResultsCount jobResultsCount = new JobResultsCount();

        jobResultsCount.failure();
        jobResultsCount.failure();

        assertThat(jobResultsCount.getNumberOfFailures()).isEqualTo(2);
    }

}