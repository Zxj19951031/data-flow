package org.example.job.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.job.commons.SystemResponse;
import org.example.job.enums.ScheduleStatusEnum;
import org.example.job.exceptions.SystemError;
import org.example.job.exceptions.SystemException;
import org.example.job.model.Job;
import org.example.job.rpc.CronService;
import org.example.job.service.JobService;
import org.example.job.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author zhuxj
 * @since 2021/1/7
 */
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    private JobService jobService;

    @Autowired
    private CronService cronService;

    @Override
    @Transactional
    public Date registerJob(Integer id) {
        Job job = this.jobService.findById(id);
        if (job == null) {
            throw SystemException.newException(SystemError.PARAMETER_ERROR, "目标任务不存在");
        }

        SystemResponse<Date> resp = this.cronService.registerJobWithCron(job.getCron(), id);

        this.jobService.updateScheduleStatus(id, ScheduleStatusEnum.SCHEDULED);
        log.info("向调度中心注册任务成功，jobId={}", id);
        return resp.getData();
    }

    @Override
    @Transactional
    public Boolean cancelJob(Integer id) {

        SystemResponse<Boolean> resp = this.cronService.cancelJob(id);

        this.jobService.updateScheduleStatus(id, ScheduleStatusEnum.STOPPED);
        log.info("向调度中心注销任务成功，jobId={}", id);
        return resp.getData();
    }
}
