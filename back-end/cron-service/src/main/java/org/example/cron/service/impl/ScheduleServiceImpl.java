package org.example.cron.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.cron.exceptions.QuartzError;
import org.example.cron.exceptions.SystemError;
import org.example.cron.exceptions.SystemException;
import org.example.cron.jobs.JobConsumer;
import org.example.cron.model.Cron;
import org.example.cron.rpc.JobService;
import org.example.cron.service.CronService;
import org.example.cron.service.ScheduleService;
import org.quartz.*;
import org.quartz.spi.MutableTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhuxj
 * @since 2021/1/6
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private CronService cronService;
    @Autowired
    private JobService jobService;

    @Override
    public Date register(Integer cronId, Integer jobId) {
        Cron cron = this.cronService.getCron(cronId);
        if (cron == null) {
            log.error("所提供的调度规则编号不合法，对应规则不存在，cronId = {}", cronId);
            throw SystemException.newException(SystemError.RECORD_NOT_FOUND, "任务调度规则配置有误，请确认！");
        }
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("jobId", jobId);
            JobDetail job = JobBuilder
                    .newJob(JobConsumer.class)
                    .withIdentity(JobKey.jobKey("JOB-" + jobId))
                    .usingJobData(jobDataMap)
                    .build();
            MutableTrigger trigger = CronScheduleBuilder
                    .cronSchedule(cron.getExpression())
                    .withMisfireHandlingInstructionFireAndProceed().build();
            trigger.setKey(TriggerKey.triggerKey("Trigger-" + jobId));
            Date nextFireTime = this.scheduler.scheduleJob(job, trigger);
            log.info("向调度器注册任务完成，下次触发时间为{}", nextFireTime);
            return nextFireTime;
        } catch (SchedulerException e) {
            log.error("向调度器注册任务失败，cronId = {} ，jobId = {}", cronId, jobId, e);
            throw SystemException.newException(QuartzError.REGISTER_JOB_ERROR, "向调度器注册任务失败");
        }
    }

    @Override
    public boolean cancel(Integer jobId) {

        try {
            boolean jobDeleted = this.scheduler.deleteJob(JobKey.jobKey("Job-" + jobId));
            log.info("从调度器注销任务完成，jobId = {}", jobId);
            return jobDeleted;
        } catch (SchedulerException e) {
            log.error("从调度器中注销任务失败，jobId={}", jobId);
            throw SystemException.newException(QuartzError.CANCEL_JOB_ERROR, "从调度器中注销任务失败");
        }

    }
}
