package org.example.job.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tb_job_instance
 *
 * @author zhuxj
 * @since 2021/01/11
 */
@Data
public class JobInstance implements Serializable {
    /**
     * 实例编号
     */
    private Integer id;

    /**
     * 任务编号
     */
    private Integer jobId;

    /**
     * 实例状态0-运行中，1-成功结束，2-存在告警，3-异常结束
     */
    private Integer status;

    /**
     * 实例开始时间
     */
    private Date startTime;

    /**
     * 实例结束时间
     */
    private Date endTime;

    /**
     * 读取记录数
     */
    private Long readCnt;

    /**
     * 写入记录数
     */
    private Long writeCnt;

    /**
     * 源头数据源编号
     */
    private Integer fromDatasource;

    /**
     * 目的数据源编号
     */
    private Integer toDatasource;

    /**
     * 插件配置
     */
    private String pluginConfig;

    /**
     * 实例创建时间
     */
    private Date createTime;

    /**
     * 实例更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}