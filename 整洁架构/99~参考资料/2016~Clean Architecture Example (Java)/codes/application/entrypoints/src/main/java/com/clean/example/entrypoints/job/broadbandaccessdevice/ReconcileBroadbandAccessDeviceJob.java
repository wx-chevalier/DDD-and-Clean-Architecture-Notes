package com.clean.example.entrypoints.job.broadbandaccessdevice;

import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.ReconcileBroadbandAccessDevicesUseCase;
import com.clean.example.core.usecase.job.OnFailure;
import com.clean.example.core.usecase.job.OnSuccess;
import com.clean.example.entrypoints.job.scheduledjob.JobResults;
import com.clean.example.entrypoints.job.scheduledjob.JobResultsCount;
import com.clean.example.entrypoints.job.scheduledjob.ScheduledJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ReconcileBroadbandAccessDeviceJob implements ScheduledJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReconcileBroadbandAccessDeviceJob.class);

    private ReconcileBroadbandAccessDevicesUseCase reconcileBroadbandAccessDevicesUseCase;
    private final JobResults jobResults;

    public ReconcileBroadbandAccessDeviceJob(ReconcileBroadbandAccessDevicesUseCase reconcileBroadbandAccessDevicesUseCase, JobResults jobResults) {
        this.reconcileBroadbandAccessDevicesUseCase = reconcileBroadbandAccessDevicesUseCase;
        this.jobResults = jobResults;
    }

    @Override
    public String getName() {
        return "ReconcileBroadbandAccessDeviceJob";
    }

    @Override
    public long getInitialDelay() {
        return 0;
    }

    @Override
    public long getPeriod() {
        return 5;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public void run() {
        LOGGER.info("Job Starting: {}...", getName());
        JobResultsCount jobResultsCount = jobResults.createJobResultsCount();
        OnSuccess success = jobResultsCount::success;
        OnFailure failure = jobResultsCount::failure;
        reconcileBroadbandAccessDevicesUseCase.reconcile(success, failure);
        jobResults.recordJobResults(this, jobResultsCount);
        LOGGER.info("Job Completed: {}", getName());
    }

}
