package org.example.job.vo;

import lombok.Data;
import org.example.job.model.Job;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuxj
 * @since 2020/12/24
 */
@Data
public class JobSaveVO {

    @NotBlank(message = "任务名称不可以为空")
    private String name;

    @NotNull(message = "源头数据源编号必选")
    private Integer fromDatasource;

    @NotNull(message = "目的数据源编号必选")
    private Integer toDatasource;

    @NotNull(message = "调度规则必选")
    private Integer cron;

    @NotBlank(message = "调度任务配置不可以为空")
    private String pluginConfig;

    public Job toJob() {
        Job job = new Job();
        BeanUtils.copyProperties(this, job);
        return job;
    }
}
