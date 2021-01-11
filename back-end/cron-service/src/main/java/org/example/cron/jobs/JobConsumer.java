package org.example.cron.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

/**
 * @author zhuxj
 * @since 2021/1/6
 */
@Slf4j
public class JobConsumer implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        try {

            JobKey jobKey = context.getJobDetail().getKey();
            log.info("任务到达触发时间，开始消费任务，jobKey = {}", jobKey);
        } catch (Exception e) {
            log.error("error!!!", e);
        }
    }
}
