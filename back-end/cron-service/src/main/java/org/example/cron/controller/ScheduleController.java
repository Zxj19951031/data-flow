package org.example.cron.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.cron.commons.SystemResponse;
import org.example.cron.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 调度管理
 *
 * @author zhuxj
 * @since 2021/1/6
 */
@RestController
@RequestMapping(value = "scheduler")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 注册任务
     *
     * @param cronId 调度编号
     * @param jobId  任务编号
     * @return 下一次调度触发时间
     */
    @PostMapping(value = "/register/cron/{cronId}/job/{jobId}")
    public SystemResponse<Date> registerJobWithCron(@PathVariable Integer cronId, @PathVariable Integer jobId) {
        Date nextFireTime = this.scheduleService.register(cronId, jobId);
        return SystemResponse.success(nextFireTime);
    }

    /**
     * 删除任务
     *
     * @param id 任务编号
     * @return 是否成功删除
     */
    @PostMapping(value = "/cancel/job/{id}")
    public SystemResponse<Boolean> cancelJob(@PathVariable Integer id) {
        boolean jobDeleted = this.scheduleService.cancel(id);
        return SystemResponse.success(jobDeleted);
    }
}
