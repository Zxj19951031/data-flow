package org.example.plugins.common.plugins;


import org.example.plugins.common.collectors.JobPluginCollector;

public abstract class AbstractJobPlugin extends AbstractPlugin {

    private JobPluginCollector jobPluginCollector;

    public AbstractJobPlugin() {
    }

    public JobPluginCollector getJobPluginCollector() {
        return jobPluginCollector;
    }

    public void setJobPluginCollector(JobPluginCollector jobPluginCollector) {
        this.jobPluginCollector = jobPluginCollector;
    }

}