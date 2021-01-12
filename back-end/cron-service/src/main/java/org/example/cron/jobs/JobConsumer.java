package org.example.cron.jobs;

import lombok.extern.slf4j.Slf4j;
import org.example.cron.rpc.JobService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author zhuxj
 * @since 2021/1/6
 */
@Slf4j
public class JobConsumer extends QuartzJobBean {

    private JobService jobService;
    private int jobId;

    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
            log.info("任务到达触发时间，开始消费任务，jobKey = {}", jobKey);

            jobService.createInstance(jobId);
        } catch (Exception e) {
            log.error("消费任务异常!!!", e);
        }
    }
}
