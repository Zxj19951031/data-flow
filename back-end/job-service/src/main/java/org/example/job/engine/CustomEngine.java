package org.example.job.engine;

import lombok.extern.slf4j.Slf4j;
import org.example.job.service.JobInstanceService;
import org.example.plugins.common.collectors.PluginCollector;
import org.example.plugins.common.columns.ColumnCast;
import org.example.plugins.common.commons.CoreConstant;
import org.example.plugins.common.commons.JsonObject;
import org.example.plugins.common.commons.VMInfo;
import org.example.plugins.core.Engine;
import org.example.plugins.core.container.AbstractContainer;
import org.example.plugins.core.container.JobContainer;
import org.example.plugins.core.utils.LoadUtil;

import java.io.IOException;

/**
 * 这里重写Engine，是为了将Spring的bean作为任务的观察者Collector放置入JobContainer，从而做到任务数据的落库
 *
 * @author zhuxj
 * @since 2021/1/12
 */
@Slf4j
public class CustomEngine extends Engine {

    private JobInstanceService jobInstanceService;

    public CustomEngine(JsonObject allConfig) {
        super(allConfig);
    }

    @Override
    public PluginCollector entry() throws IOException {
        int jobId = allConfig.getInt(CoreConstant.JOB_ID, 0);
        allConfig.set(CoreConstant.JOB_ID, jobId);


        Thread.currentThread().setName("job-" + jobId);
        //打印vmInfo
        VMInfo vmInfo = VMInfo.getVmInfo();
        if (vmInfo != null) {
            log.info(vmInfo.toString());
        }

        log.info("\n" + Engine.filterJobConfiguration(allConfig) + "\n");

        return this.start();
    }

    @Override
    public PluginCollector start() throws IOException {
        ColumnCast.bind(allConfig);

        LoadUtil.bind();

        AbstractContainer container = new JobContainer(allConfig);

        //使用自定义的任务消息收集器用于数据落库
        CustomJobPluginCollector pluginCollector = new CustomJobPluginCollector();
        pluginCollector.setJobInstanceService(jobInstanceService);
        pluginCollector.setJobId(allConfig.getInt(CoreConstant.JOB_ID));

        container.setPluginCollector(pluginCollector);
        container.start();

        return container.getPluginCollector();
    }


    public void setJobInstanceService(JobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }
}
