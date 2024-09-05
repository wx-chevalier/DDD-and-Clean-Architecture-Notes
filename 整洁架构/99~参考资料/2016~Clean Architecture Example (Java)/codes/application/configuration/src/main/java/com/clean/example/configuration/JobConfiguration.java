package com.clean.example.configuration;

import com.clean.example.core.usecase.broadbandaccessdevice.reconcile.ReconcileBroadbandAccessDevicesUseCase;
import com.clean.example.entrypoints.job.broadbandaccessdevice.ReconcileBroadbandAccessDeviceJob;
import com.clean.example.entrypoints.job.scheduledjob.JobResults;
import com.clean.example.entrypoints.job.scheduledjob.ScheduledJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfiguration {

    @Bean
    public JobResults jobResults() {
        return new JobResults();
    }

    @Bean
    public ScheduledJob reconcileBroadbandAccessDeviceJob(ReconcileBroadbandAccessDevicesUseCase reconcileBroadbandAccessDevicesUseCase, JobResults jobResults) {
        return new ReconcileBroadbandAccessDeviceJob(reconcileBroadbandAccessDevicesUseCase, jobResults);
    }

}
