package org.example.plugins.core.container;


import org.example.plugins.common.collectors.PluginCollector;
import org.example.plugins.common.commons.JsonObject;

/**
 * 抽象容器，子容器有job和task
 *
 * @author zhuxj
 */
public abstract class AbstractContainer {
    /**
     * 全局配置
     */
    protected JsonObject allConfig;
    /**
     * 消息收集器，实现有job和task两类
     */
    protected PluginCollector pluginCollector;

    public AbstractContainer(JsonObject allConfig) {
        this.allConfig = allConfig;
    }

    /**
     * 启动容器
     */
    public abstract void start();

    public PluginCollector getPluginCollector() {
        return pluginCollector;
    }

    public void setPluginCollector(PluginCollector pluginCollector) {
        this.pluginCollector = pluginCollector;
    }
}
