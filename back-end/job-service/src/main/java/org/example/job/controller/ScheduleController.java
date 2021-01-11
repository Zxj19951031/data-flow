package org.example.job.controller;

import org.example.job.commons.SystemResponse;
import org.example.job.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 调度管理
 *
 * @author zhuxj
 * @since 2021/1/6
 */
@RestController
@RequestMapping(value = "/scheduler")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 注册任务至调度中心
     *
     * @param id 任务编号
     * @return 下次触发时间
     */
    @PostMapping(value = "/register/job/{id}")
    public SystemResponse<String> registerJob(@PathVariable Integer id) {
        Date nextFireTime = this.scheduleService.registerJob(id);
        String dateTimeStr = SimpleDateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG).format(nextFireTime);
        return SystemResponse.success(dateTimeStr);
    }


    /**
     * 从调度中心注销任务
     *
     * @param id 任务编号
     * @return boolean
     */
    @PostMapping(value = "/cancel/job/{id}")
    public SystemResponse<Boolean> cancelJob(@PathVariable Integer id) {
        Boolean jobDeleted = this.scheduleService.cancelJob(id);
        return SystemResponse.success(jobDeleted);
    }
}
