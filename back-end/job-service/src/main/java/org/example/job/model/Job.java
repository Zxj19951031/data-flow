package org.example.job.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * tb_job
 * @author 
 */
@Data
public class Job implements Serializable {
    private Integer id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 源头数据源编号
     */
    private Integer fromDatasource;

    /**
     * 目的数据源编号
     */
    private Integer toDatasource;

    /**
     * datax插件任务的配置
     */
    private String pluginConfig;

    /**
     * 调度规则编号
     */
    private Integer cron;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Byte status;

    private static final long serialVersionUID = 1L;
}