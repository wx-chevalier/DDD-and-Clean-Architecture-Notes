package com.clean.example.entrypoints.job.scheduledjob;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JobResultsTest {

    JobResults jobResults = new JobResults();

    @Test
    public void createsNewJobResultsCount() throws Exception {
        JobResultsCount jobResultsCount = jobResults.createJobResultsCount();

        assertThat(jobResultsCount).isNotNull();
    }

}