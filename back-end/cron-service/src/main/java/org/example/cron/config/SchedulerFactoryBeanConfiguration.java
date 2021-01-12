package org.example.cron.config;

import org.example.cron.rpc.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhuxj
 * @since 2021/1/12
 */
@Configuration
public class SchedulerFactoryBeanConfiguration implements SchedulerFactoryBeanCustomizer {

    @Autowired
    private JobService jobService;

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("jobService", jobService);
        schedulerFactoryBean.setSchedulerContextAsMap(params);
    }
}
