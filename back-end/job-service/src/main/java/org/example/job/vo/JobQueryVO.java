package org.example.job.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.job.model.Job;
import org.springframework.beans.BeanUtils;

/**
 * @author zhuxj
 * @since 2021/1/5
 */
@Data
@NoArgsConstructor
public class JobQueryVO {


    public JobQueryVO(Job job) {
        BeanUtils.copyProperties(job, this);
    }

    /**
     * 任务名称
     */
    private String name;

    /**
     * 源端数据源类型
     */
    private Integer fromDatasourceType;

    /**
     * 源端数据源ID
     */
    private Integer fromDatasource;

    /**
     * 目的端数据源类型
     */
    private Integer toDatasourceType;

    /**
     * 目的端数据源ID
     */
    private Integer toDatasource;

    /**
     * 调度表达式ID
     */
    private Integer cron;

    /**
     * datax配置
     */
    private String pluginConfig;


}
