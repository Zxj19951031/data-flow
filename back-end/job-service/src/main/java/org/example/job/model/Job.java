package org.example.job.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tb_job
 *
 * @author zhuxj
 * @since 2020/12/23
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

    /**
     * 调度状态0-未运行，1-调度中，2-成功完成，3-异常结束，4-存在告警
     */
    private Integer scheduleStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Byte status;

    private static final long serialVersionUID = 1L;
}