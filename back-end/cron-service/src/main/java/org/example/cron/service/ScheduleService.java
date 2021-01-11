package org.example.cron.service;

import java.util.Date;

/**
 * 调度服务，主要提供的对Quartz实例的维护
 *
 * @author zhuxj
 * @since 2021/1/6
 */
public interface ScheduleService {
    Date register(Integer cronId, Integer jobId);

    boolean cancel(Integer JobId);
}
