package org.example.job.rpc;

import org.example.job.commons.SystemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

/**
 * @author zhuxj
 * @since 2021/1/6
 */
@FeignClient(value = "cron-service")
public interface CronService {

    @PostMapping(value = "scheduler/register/cron/{cronId}/job/{jobId}")
    public SystemResponse<Date> registerJobWithCron(@PathVariable Integer cronId, @PathVariable Integer jobId);

    @PostMapping(value = "scheduler/cancel/job/{id}")
    public SystemResponse<Boolean> cancelJob(@PathVariable Integer id);
}
