package org.example.job.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.job.commons.PluginKeys;
import org.example.job.engine.CustomEngine;
import org.example.job.engine.CustomJobPluginCollector;
import org.example.job.enums.DatasourceEnum;
import org.example.job.enums.InstanceStatusEnum;
import org.example.job.exceptions.SystemError;
import org.example.job.exceptions.SystemException;
import org.example.job.model.Datasource;
import org.example.job.model.JobInstance;
import org.example.job.service.JobInstanceService;
import org.example.job.service.PluginService;
import org.example.plugins.common.commons.CoreConstant;
import org.example.plugins.common.commons.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;


/**
 * datax 配置维护
 *
 * @author zhuxj
 * @since 2021/1/11
 */
@Slf4j
@Service
public class PluginServiceImpl implements PluginService {

    @Autowired
    private JobInstanceService jobInstanceService;


    @Override
    public JsonObject fixReaderDatasourceParameters(Datasource datasource, JsonObject pluginConfig) {
        Assert.notNull(datasource, "datasource must not be null");
        Assert.notNull(pluginConfig, "pluginConfig must not be null");

        switch (DatasourceEnum.getByValue(datasource.getType())) {
            case MYSQL:
                JsonObject connectionConfig = JsonObject.newDefault();
                connectionConfig.set(PluginKeys.USERNAME, datasource.getUsername());
                connectionConfig.set(PluginKeys.PASSWORD, datasource.getPassword());
                // TODO: 2021/1/11 jdbcUrl配置考虑开放在数据源管理维护，因为涉及到连接参数的维护
                connectionConfig.set(PluginKeys.JDBC_URL, String.format("jdbc:mysql://%s:%s", datasource.getHost(), datasource.getPort()));
                pluginConfig.set(PluginKeys.JOB_CONTENT_READER_PARAMETER, pluginConfig.getJsonObject(PluginKeys.JOB_CONTENT_READER_PARAMETER).merge(connectionConfig, true));
                pluginConfig.set(PluginKeys.JOB_CONTENT_READER_NAME, PluginKeys.MYSQL_READER);
                break;
            default:
                throw SystemException.newException(SystemError.ENUM_ERROR);
        }
        return pluginConfig;

    }

    @Override
    public JsonObject fixWriterDatasourceParameters(Datasource datasource, JsonObject pluginConfig) {
        Assert.notNull(datasource, "datasource must not be null");
        Assert.notNull(pluginConfig, "pluginConfig must not be null");

        switch (DatasourceEnum.getByValue(datasource.getType())) {
            case MYSQL:
                JsonObject connectionConfig = JsonObject.newDefault();
                connectionConfig.set(PluginKeys.USERNAME, datasource.getUsername());
                connectionConfig.set(PluginKeys.PASSWORD, datasource.getPassword());
                // TODO: 2021/1/11 jdbcUrl配置考虑开放在数据源管理维护，因为涉及到连接参数的维护
                connectionConfig.set(PluginKeys.JDBC_URL, String.format("jdbc:mysql://%s:%s", datasource.getHost(), datasource.getPort()));
                pluginConfig.set(PluginKeys.JOB_CONTENT_WRITER_PARAMETER, pluginConfig.getJsonObject(PluginKeys.JOB_CONTENT_WRITER_PARAMETER).merge(connectionConfig, true));
                pluginConfig.set(PluginKeys.JOB_CONTENT_WRITER_NAME, PluginKeys.MYSQL_WRITER);
                break;
            default:
                throw SystemException.newException(SystemError.ENUM_ERROR);
        }
        return pluginConfig;
    }

    /**
     * 运行任务
     * 正常运行期间的各个指标给到  {@link CustomJobPluginCollector} 进行更新
     * 异常发生时由 {@code try-catch} 对实例状态和结束时间进行更新
     *
     * @param pluginConfig 任务配置
     */
    @Async
    @Override
    public void runDatax(JsonObject pluginConfig) {

        try {
            CustomEngine engine = new CustomEngine(pluginConfig);
            engine.setJobInstanceService(jobInstanceService);
            engine.entry();
        } catch (Throwable e) {
            log.error("实例运行异常", e);
            JobInstance instance = new JobInstance();
            instance.setId(pluginConfig.getInt(CoreConstant.JOB_ID));
            instance.setStatus(InstanceStatusEnum.ERROR.getValue());
            instance.setEndTime(new Date());
            this.jobInstanceService.update(instance);
        }
    }
}
