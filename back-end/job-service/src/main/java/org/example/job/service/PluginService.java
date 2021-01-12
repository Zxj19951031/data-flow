package org.example.job.service;

import org.example.job.model.Datasource;
import org.example.plugins.common.commons.JsonObject;

/**
 * @author zhuxj
 * @since 2021/1/11
 */
public interface PluginService {
    /**
     * 补充读取数据源的连接配置
     *
     * @param datasource
     * @param pluginConfig
     * @return
     */
    JsonObject fixReaderDatasourceParameters(Datasource datasource, JsonObject pluginConfig);

    /**
     * 补充写入数据源的连接配置
     *
     * @param datasource
     * @param pluginConfig
     * @return
     */
    JsonObject fixWriterDatasourceParameters(Datasource datasource, JsonObject pluginConfig);

    /**
     * 执行Datax插件
     *
     * @param pluginConfig 任务配置
     */
    void runDatax(JsonObject pluginConfig);
}
