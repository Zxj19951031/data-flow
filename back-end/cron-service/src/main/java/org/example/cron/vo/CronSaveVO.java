package org.example.cron.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhuxj
 * @since 2020/12/16
 */
@Data
public class CronSaveVO {

    @NotBlank(message = "规则名称不可以为空")
    private String name;

    @NotBlank(message = "调度表达式不可以为空")
    private String expression;

    @NotBlank(message = "调度表达式元数据不可以为空")
    private String metadata;
}
