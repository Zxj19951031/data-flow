package org.example.plugins.core;

import org.apache.commons.lang3.StringUtils;
import org.example.plugins.common.collectors.PluginCollector;
import org.example.plugins.common.columns.ColumnCast;
import org.example.plugins.common.commons.CoreConstant;
import org.example.plugins.common.commons.JsonObject;
import org.example.plugins.common.commons.VMInfo;
import org.example.plugins.core.container.AbstractContainer;
import org.example.plugins.core.container.JobContainer;
import org.example.plugins.core.utils.LoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Engine {

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);
    protected final JsonObject allConfig;

    public Engine(JsonObject allConfig) {
        this.allConfig = allConfig;
    }

    /**
     * 交换入口
     *
     * @return
     */
    public PluginCollector entry() throws IOException {
        String jobId = allConfig.getString(CoreConstant.JOB_ID, "undefined");
        allConfig.set(CoreConstant.JOB_ID, jobId);


        Thread.currentThread().setName("job-" + jobId);
        //打印vmInfo
        VMInfo vmInfo = VMInfo.getVmInfo();
        if (vmInfo != null) {
            logger.info(vmInfo.toString());
        }

        logger.info("\n" + Engine.filterJobConfiguration(allConfig) + "\n");

        logger.debug(allConfig.toJSON());

        return start();
    }

    // 注意屏蔽敏感信息
    public static String filterJobConfiguration(final JsonObject configuration) {
        JsonObject jobConfWithSetting = configuration.getJsonObject("job").clone();

        JsonObject jobContent = jobConfWithSetting.getJsonObject("content");

        filterSensitiveConfiguration(jobContent);

        jobConfWithSetting.set("content", jobContent);

        return jobConfWithSetting.beautify();
    }

    public static void filterSensitiveConfiguration(JsonObject configuration) {
        Set<String> keys = configuration.getKeys();
        for (final String key : keys) {
            boolean isSensitive = StringUtils.endsWithIgnoreCase(key, "password")
                    || StringUtils.endsWithIgnoreCase(key, "accessKey");
            if (isSensitive && configuration.get(key) instanceof String) {
                configuration.set(key, configuration.getString(key).replaceAll(".", "*"));
            }
        }
    }

    public PluginCollector start() throws IOException {

        ColumnCast.bind(allConfig);

        LoadUtil.bind();

        AbstractContainer container = new JobContainer(allConfig);

        container.start();

        return container.getPluginCollector();
    }

    public static void main(String[] args) {

        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("job.json");
            InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            //将字符流以缓存的形式一行一行输出
            BufferedReader bf = new BufferedReader(isr);
            StringBuilder results = new StringBuilder();
            String newLine = "";
            while ((newLine = bf.readLine()) != null) {
                results.append(newLine).append("\n");

            }

            Engine engine = new Engine(JsonObject.from(results.toString()));
            engine.entry();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

    }
}
