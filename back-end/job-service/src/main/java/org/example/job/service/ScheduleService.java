package org.example.job.service;

import java.util.Date;

/**
 * 调度相关服务
 *
 * @author zhuxj
 * @since 2021/1/7
 */
public interface ScheduleService {

    /**
     * 注册任务至调度中心
     *
     * @param id 任务编号
     * @return 调度下次触发时间
     */
    Date registerJob(Integer id);

    /**
     * 从调度中心注销任务
     *
     * @param id 任务编号
     * @return 是否成功
     */
    Boolean cancelJob(Integer id);
}
